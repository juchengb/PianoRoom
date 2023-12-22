package mvc.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import aweit.mail.GMail;
import mvc.bean.User;
import mvc.dao.UserDao;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserDao userDao;
	
	
	// 呈現 login 頁面
	@GetMapping("/login")
	public String showLoginPage(@ModelAttribute("user")User user, Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	
	
	// 登入
	@PostMapping("/login")
	public String login(@RequestParam("email") String email,
						@RequestParam("password") String password,
						HttpSession session, Model model) {
		// 登入邏輯
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			// 登入成功，重導到 user 的 home 
		    User user = userOpt.get();
		    if (user.getPassword().equals(password)) {
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
		    return "redirect:./login";
		}
	}
	
	
	// 建立帳號
	@PostMapping("/signup")
	public String signup(@ModelAttribute("user")User user, Model model) {
		// 根據 email 查找 user 物件
		Optional<User> userOpt = userDao.getUserByEmail(user.getEmail());
		if (userOpt.isPresent()) {
			// 出現錯誤訊息
			model.addAttribute("signupMessage", "密碼錯誤");
			System.out.println("add User fail!");
		}else {
			int rowcount = userDao.addUser(user);
			if (rowcount > 0) {
				System.out.println("add User rowcount = " + rowcount);
			}
		}
	    return "redirect:/mvc/auth/login";
    }
	
	
	// 忘記密碼
	@PostMapping("/password")
	public String forgottenPassword(@RequestParam("email") String email) {
		// 根據 email 查找 user 物件
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			// 設定一組亂數密碼
			
			
			// 寄發重設密碼信件
			GMail mail = new GMail("fjchengou@gmail.com", "aesj jqel tgrc uaez");
			mail.from("fjchengou@gmail.com")
			    .to(email)
			    .personal("+Room 琴房預約系統")
			    .subject("+Room 琴房預約系統 重設密碼確認信")
			    .context("Dear +Room 琴房預約系統的使用者:<br>"
			    		+ "您於 重設密碼，"
			    		+ "新密碼為806BF0。\r\n"
			    		+ "\r\n"
			    		+ "郵件是由系統自動寄發，請勿直接回覆，如有任何問題，請致電相關承辦人，感謝您的配合。 ")
			    .send();
			return "redirect:/mvc/auth/login";
		}
		System.out.println("add User rowcount = ");

		return "redirect:/mvc/auth/login";
    }
	
	
}
