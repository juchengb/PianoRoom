package mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;

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

import mvc.dao.UserDao;
import mvc.model.dto.EditUser;
import mvc.model.po.User;
import mvc.service.AuthService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthService authService;
	
	@GetMapping("")
	public String accountPage(@ModelAttribute("updateUser") EditUser updateUser, HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("majors", userDao.findAllMajors());
		model.addAttribute("user", user);
		return "frontend/account";
	}
	
	// 修改
	@PostMapping("/update")
	public String updateAccount(@ModelAttribute("updateUser") @Valid EditUser updateUser, BindingResult result,
							    HttpSession session, Model model) throws IntrospectionException, IOException {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("majors", userDao.findAllMajors());
		
		if(result.hasErrors()) {
			return "frontend/account";
		}
		
		User userEntity = authService.updateUserConvertToUser(user, updateUser);
		
		// update user
		int rowCount = userDao.updateUserByIdFront(user.getId(), userEntity);
		System.out.println(userEntity);
		if(rowCount == 0) {
			model.addAttribute("error","更新失敗");
			System.out.println("update User fail!");
			return "frontend/account";
		}
		
		userEntity.setLevel(user.getLevel());
		
		// update user in HttpSession
	    session.setAttribute("user", userEntity);
		System.out.println("update User sucess!");
		model.addAttribute("message", "修改成功");
		model.addAttribute("togobtn", "返回使用者首頁");
		model.addAttribute("togourl", "/main");
		return "dialog";
	}
	
	// --------------------------------------------------------------------------------------
	// password
	
	@GetMapping("/password")
	public String restPasswordPage() {
		return "frontend/resetPassword";
	}
	
	@PostMapping("/password")
	public String restPassword(@RequestParam("oldPassword")String oldPassword,
							   @RequestParam("newPassword")String newPassword,
							   @RequestParam("confirmPassword")String confirmPassword,
			    	            HttpSession session, Model model) throws Exception {
		User user = (User)session.getAttribute("user");
		
		// Encrypt old password with AES
		String encryptedOldPasswordECBBase64 = authService.encryptPassword(oldPassword);
		
		if(!user.getPassword().equals(encryptedOldPasswordECBBase64)) {
			model.addAttribute("errorMessage", "原密碼錯誤");
			return "frontend/resetPassword";
		}
		if(!newPassword.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "兩次新密碼不一致");
			return "frontend/resetPassword";
		}
		
		// Encrypt new password with AES	
		String encryptedNewPasswordECBBase64 = authService.encryptPassword(newPassword);
		
		userDao.updateUserPasswordById(user.getId(), encryptedNewPasswordECBBase64);
		System.out.println("update User password sucess!");
		model.addAttribute("message", "密碼修改成功");
		model.addAttribute("togobtn", "請重新登入");
		model.addAttribute("togourl", "/auth/login");
		return "dialog";
	}
}
