package mvc.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;
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

import aweit.mail.GMail;
import mvc.dao.UserDao;
import mvc.model.dto.LoginUser;
import mvc.model.dto.SignupUser;
import mvc.model.po.User;
import mvc.service.AuthService;
import mvc.util.KeyUtil;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserDao userDao;
	
	@Autowired
	AuthService authService;

	@GetMapping("/getcaptcha")
	public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {

		// 拿資料
		String captcha = authService.getCaptcha();
		BufferedImage img = authService.getCaptchaImage(captcha);
		
		// 設定 session
		session.setAttribute("captcha", captcha);
	
		// 設定 回應類型
		response.setContentType("image/png");

		// 將影像串流回寫給 client
		ImageIO.write(img, "PNG", response.getOutputStream());
	}

	@GetMapping("/refreshcaptcha")
	@ResponseBody
	public void refreshCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
		System.out.println("refresh Captcha");
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
			session.invalidate(); // session 過期失效
			model.addAttribute("loginMessage", "驗證碼錯誤");
			return "login";
		}

		// login
		Optional<User> userOpt = userDao.getUserByEmail(loginUser.getEmail());

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// Encrypt password with AES
			final String KEY = KeyUtil.getSecretKey();
			SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, loginUser.getPassword());
			String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);

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

	// 建立帳號
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signupUser") @Valid SignupUser signupUser, BindingResult result,
			@ModelAttribute("loginUser") LoginUser loginUser, Model model) throws Exception {

		// 根據 email 查找 user 物件
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

		User user = new User();
		user.setName(signupUser.getName());
		user.setEmail(signupUser.getEmail());
		// Encrypt password with AES
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, signupUser.getPassword());
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		user.setPassword(encryptedPasswordECBBase64);
		user.setMajorId(signupUser.getMajorId());

		int rowcount = userDao.addUser(user);
		if (rowcount > 0) {
			System.out.println("add User rowcount = " + rowcount);
		}
		model.addAttribute("message", "註冊成功");
		model.addAttribute("togobtn", "返回登入");
		model.addAttribute("togourl", "/auth/login");

		return "dialog";
	}

	@PostMapping("/password")
	public String forgottenPassword(@RequestParam("email") String email) {
		// find User by email
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			// set a random code

			// send reset email
			GMail mail = new GMail("fjchengou@gmail.com", "aesj jqel tgrc uaez");

			mail.from("fjchengou@gmail.com").to(email).personal("+Room 琴房預約系統").subject("+Room 琴房預約系統 重設密碼確認信")
					.context("Dear +Room 琴房預約系統的使用者:<br>" + "您於 重設密碼，" + "新密碼為806BF0。\r\n" + "\r\n"
							+ "郵件是由系統自動寄發，請勿直接回覆，如有任何問題，請致電相關承辦人，感謝您的配合。 ")
					.send();

			return "redirect:/mvc/auth/login";
		}
		System.out.println("add User rowcount = ");

		return "redirect:/mvc/auth/login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mvc/auth/login";
	}

}