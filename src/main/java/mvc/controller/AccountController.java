package mvc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mvc.dao.UserDao;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("")
	public String accountPage(HttpSession session, Model model) {
		return "frontend/account";
	}
}
