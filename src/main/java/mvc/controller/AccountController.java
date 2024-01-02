package mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mvc.bean.UpdateUser;
import mvc.dao.UserDao;
import mvc.entity.User;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	private static final Path upPath = Paths.get("C:/Javaclass/uploads/profile");
			
	static {
		try {
			Files.createDirectories(upPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("")
	public String accountPage(@ModelAttribute("updateUser") UpdateUser updateUser, HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		model.addAttribute("majors", userDao.findAllMajors());
		model.addAttribute("user", user);
		return "frontend/account";
	}
	
	// 修改
	@PostMapping("update")
	public String updateAccount(@ModelAttribute @Valid UpdateUser updateUser, BindingResult result,
							    HttpSession session, Model model) throws IntrospectionException, IOException {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("majors", userDao.findAllMajors());
		
		if(result.hasErrors()) {
			return "frontend/account";
		}
		
		// profile avator
		MultipartFile multipartFile = updateUser.getAvator();
		System.out.println(multipartFile);
		String avator = user.getId() + "-" +updateUser.getName();  
		Path picPath = upPath.resolve(avator);
		Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
		
		// bean UpdateUser to entity User
		User userEntity = new User();
		userEntity.setEmail(updateUser.getEmail());
		userEntity.setName(updateUser.getName());
		userEntity.setMajorId(updateUser.getMajorId());
		userEntity.setAvator(avator);
		System.out.println(userEntity);
		
		// update user
		int rowCount = userDao.updateUserById(user.getId(), userEntity);
		if(rowCount == 0) {
			model.addAttribute("error","更新失敗");
			return "frontend/account";
		}
		return "redirect:/mvc/account";
	}
	
	// --------------------------------------------------------------------------------------
	// password
	
	@GetMapping("/password")
	public String restPasswordPage() {
		return "frontend/resetPassword";
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String restPassword(@RequestParam("oldPassword")String oldPassword,
							   @RequestParam("newPassword")String newPassword,
							   @RequestParam("confirmPassword")String confirmPassword,
			    	            HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		if(!user.getPassword().equals(oldPassword)) {
			model.addAttribute("errorMessage", "原密碼錯誤");
			return "frontend/resetPassword";
		}
		if(!newPassword.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "兩次新密碼不一致");
			return "frontend/resetPassword";
		}
		
		userDao.updateUserPasswordById(user.getId(), newPassword);
		return "redirect:/mvc/auth/login";
	}
}
