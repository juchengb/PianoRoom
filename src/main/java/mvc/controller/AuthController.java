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

import mvc.bean.User;
import mvc.dao.UserDao;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("email") String email,
						@RequestParam("password") String password,
						HttpSession session, Model model) {
		
		// 登入邏輯
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			// 登入成功，重倒到 user 的 home 
		    User user = userOpt.get();
		    if (user.getPassword().equals(password)) {
		        session.setAttribute("user", user);
		        return "redirect:/mvc";
		    }
		    session.invalidate();
		    model.addAttribute("loginMessage", "密碼錯誤");
		} else {
		    session.invalidate();
		    model.addAttribute("loginMessage", "查無此帳號");
		}
		return "login";
	}
	
	@PostMapping("/signup")
	public String signup(Model model) {
//	    model.addAttribute("signupSuccess", true);
	    return "redirect:/mvc/auth/login";
    }
	
	@PostMapping("/password")
	public String forgottenPassword() {
		
		return "redirect:/mvc/auth/login";
    }
	
	
}
