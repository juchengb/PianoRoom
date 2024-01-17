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

/**
 * AuthController 處理使用者身份驗證相關功能，包含登入、註冊、忘記密碼等。
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthService authService;
	
	
	/**
	 * GET 請求，生成驗證碼與驗證碼圖片，並將驗證碼存入 Session。
	 * 
	 * @param session HTTP Session
	 * @param response  HttpServletResponse
	 * @throws IOException 處理圖片輸出時可能的 IO 異常
	 */
	@GetMapping("/getcaptcha")
	public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {

		// 獲取驗證碼
		String captcha = authService.getCaptcha();
		BufferedImage img = authService.getCaptchaImage(captcha);
		
		// 將驗證碼存入 Session
		session.setAttribute("captcha", captcha);
	
		// 設置 Response
		response.setContentType("image/png");

		// 將圖片輸出給 client 端
		ImageIO.write(img, "PNG", response.getOutputStream());
	}
	
	/**
	 * GET 請求，刷新驗證碼。
	 * 
	 * @param session HTTP Session
	 * @param response  HttpServletResponse
	 * @throws IOException 處理圖片輸出時可能的 IO 異常
	 */
	@GetMapping("/refreshcaptcha")
	@ResponseBody
	public void refreshCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
		getCaptchaImage(session, response);
	}
	
	/**
	 * GET 請求，顯示登入頁面。
	 * 
	 * @param loginUser LoginUser 接收登入表單數據
	 * @param signupUser SignupUser 接收註冊表單數據
	 * @param model Spring MVC 模型
	 * @return login 頁面
	 */
	@GetMapping("/login")
	public String loginPage(@ModelAttribute("loginUser") LoginUser loginUser,
							@ModelAttribute("signupUser") SignupUser signupUser, Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	
	/**
	 * POST 請求，驗證並執行登入。
	 * 
	 * @param loginUser LoginUser 接收登入表單數據
	 * @param result 登入表單驗證結果
	 * @param signupUser SignupUser 接收註冊表單數據
	 * @param session HTTP Session
	 * @param model Spring MVC 模型
	 * @return 成功：使用者首頁；失敗：返回原頁面
	 * @throws Exception
	 */
	@PostMapping("/login")
	public String login(@ModelAttribute("loginUser") @Valid LoginUser loginUser, BindingResult result,
						@ModelAttribute("signupUser") SignupUser signupUser,
						HttpSession session, Model model) throws Exception {

		// 登入表單數據驗證
		if (result.hasErrors()) {
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}

		// 比對驗證碼
		if (!loginUser.getCaptcha().equalsIgnoreCase(session.getAttribute("captcha") + "")) {
			session.invalidate(); // session invalid
			model.addAttribute("loginMessage", "驗證碼錯誤");
			return "login";
		}

		// 登入
		Optional<User> userOpt = userDao.getUserByEmail(loginUser.getEmail());

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// 使用 AES 加密密碼		
			String encryptedPasswordECBBase64 = authService.encryptPassword(loginUser.getPassword());
			
			// 比對密碼
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
	
	/**
	 * GET 請求，載入註冊頁面所需資源。
	 * 
	 * @param loginUser LoginUser 接收登入表單數據
	 * @param signupUser SignupUser 接收註冊表單數據
	 * @param model Spring MVC 模型
	 * @return login 頁面
	 */
	@GetMapping("/signup")
	public String signupForm(@ModelAttribute("loginUser") LoginUser loginUser,
							 @ModelAttribute("signupUser") SignupUser signupUser, Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	

	/**
	 * POST 請求，驗證並執行註冊。
	 * 
	 * @param signupUser SignupUser 接收註冊表單數據
	 * @param result 註冊表單驗證結果
	 * @param loginUser LoginUser 接收登入表單數據
	 * @param model Spring MVC 模型
	 * @return login 頁面
	 * @throws Exception
	 */
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signupUser") @Valid SignupUser signupUser, BindingResult result,
						 @ModelAttribute("loginUser") LoginUser loginUser, Model model) throws Exception {

		Optional<User> userOpt = userDao.getUserByEmail(signupUser.getEmail());
		if (userOpt.isPresent()) {
			// 帳號已存在的錯誤訊息
			model.addAttribute("signupMessage", "帳號已存在");
			System.out.println("add User fail!");

			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}

		// 註冊表單驗證通過
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
	
	/**
	 * POST 請求，忘記密碼發送電子信件。
	 * 
	 * @param email  使用者提供的電子信箱
	 * @param model Spring MVC 模型
	 * @return 成功：發送電子信件；失敗：錯誤頁面
	 */
	@PostMapping("/password")
	public String forgottenPassword(@RequestParam("email") String email, Model model) {
		// 根據電子信箱查詢使用者
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			authService.sentEamil(email);
			return "redirect:/mvc/auth/otp/rest";
		}
		model.addAttribute("message", "查無此信箱");
		model.addAttribute("togobtn", "返回登入");
		model.addAttribute("togourl", "/auth/login");

		return "dialogFail";
	}
	
	@GetMapping("/opt/reset")
	public String optAndReset() {
		return null;
	}
	
	/**
	 * GET 請求，登出 (讓 Session 失效並重導到登入頁面)。
	 * 
	 * @param session HttpSession
	 * @return login 頁面
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mvc/auth/login";
	}
	
}