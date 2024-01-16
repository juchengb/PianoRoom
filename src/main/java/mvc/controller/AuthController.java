package mvc.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.dao.UserDao;
import mvc.model.dto.LoginUser;
import mvc.model.dto.SignupUser;
import mvc.model.po.User;
import mvc.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthService authService;
	

	@GetMapping("/getcaptcha")
	public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {

		// get captcha
		String captcha = authService.getCaptcha();
		BufferedImage img = authService.getCaptchaImage(captcha);
		
		// set session
		session.setAttribute("captcha", captcha);
	
		// set response
		response.setContentType("image/png");

		// image to client
		ImageIO.write(img, "PNG", response.getOutputStream());
	}
	

	@GetMapping("/refreshcaptcha")
	@ResponseBody
	public void refreshCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
		getCaptchaImage(session, response);
	}
	

	@GetMapping("/login")
	public String loginPage(@ModelAttribute("loginUser") LoginUser loginUser,
							@ModelAttribute("signupUser") SignupUser signupUser, Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	

	@PostMapping("/login")
	public String login(@ModelAttribute("loginUser") @Valid LoginUser loginUser, BindingResult result,
			@ModelAttribute("signupUser") SignupUser signupUser, HttpSession session, Model model) throws Exception {

		// login form data validation
		if (result.hasErrors()) {
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}

		// compare verification code
		if (!loginUser.getCaptcha().equalsIgnoreCase(session.getAttribute("captcha") + "")) {
			session.invalidate(); // session invalid
			model.addAttribute("loginMessage", "驗證碼錯誤");
			return "login";
		}

		// login
		Optional<User> userOpt = userDao.getUserByEmail(loginUser.getEmail());

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// Encrypt password with AES			
			String encryptedPasswordECBBase64 = authService.encryptPassword(loginUser.getPassword());
			
			// compare password
			if (user.getPassword().equals(encryptedPasswordECBBase64)) {
				session.setAttribute("user", user);
				return "redirect:/mvc/main";
			}
			session.invalidate();
			model.addAttribute("loginMessage", "密碼錯誤");
			model.addAttribute("user", new User());
			return "login";

		} else {
			session.invalidate();
			model.addAttribute("loginMessage", "查無此帳號");
			model.addAttribute("user", new User());
			return "login";
		}
	}
	

	@RequestMapping("/signup")
	public String signupForm(@ModelAttribute("loginUser") LoginUser loginUser,
			@ModelAttribute("signupUser") SignupUser signupUser, Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	

	// sign up
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signupUser") @Valid SignupUser signupUser, BindingResult result,
			@ModelAttribute("loginUser") LoginUser loginUser, Model model) throws Exception {

		Optional<User> userOpt = userDao.getUserByEmail(signupUser.getEmail());
		if (userOpt.isPresent()) {
			// error message
			model.addAttribute("signupMessage", "帳號已存在");
			System.out.println("add User fail!");

			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}

		// Signup form valid
		if (result.hasErrors()) {
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}

		int rowcount = userDao.addUser(authService.signupUserConvertToUser(signupUser));
		if (rowcount > 0) {
			System.out.println("add User rowcount = " + rowcount);
		}
		model.addAttribute("message", "註冊成功");
		model.addAttribute("togobtn", "返回登入");
		model.addAttribute("togourl", "/auth/login");

		return "dialog";
	}

	@PostMapping("/password")
	public String forgottenPassword(@RequestParam("email") String email, Model model) {
		// find User by email
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			authService.sentEamil(email);
			return "redirect:/mvc/auth/login";
		}
		model.addAttribute("message", "查無此信箱");
		model.addAttribute("togobtn", "返回登入");
		model.addAttribute("togourl", "/auth/login");

		return "dialogFail";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mvc/auth/login";
	}

}