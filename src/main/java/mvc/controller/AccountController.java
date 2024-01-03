package mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mvc.bean.UpdateUser;
import mvc.dao.UserDao;
import mvc.entity.User;
import util.KeyUtil;

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
	@PostMapping("/update")
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
		// System.out.println(multipartFile.getOriginalFilename());
		String avator;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			avator = user.getId() + updateUser.getName() + "-" + multipartFile.getOriginalFilename(); 
			Path picPath = upPath.resolve(avator);
			Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			// If no new file is uploaded, keep the original avator
			avator = user.getAvator();
		}
		
		// bean UpdateUser to entity User
		User userEntity = new User();
		userEntity.setId(user.getId());
		userEntity.setEmail(updateUser.getEmail());
		userEntity.setName(updateUser.getName());
		userEntity.setMajorId(updateUser.getMajorId());
		userEntity.setAvator(avator);
		System.out.println("userEntity: " + userEntity);
		
		// update user
		int rowCount = userDao.updateUserById(user.getId(), userEntity);
		System.out.println(userEntity);
		if(rowCount == 0) {
			model.addAttribute("error","更新失敗");
			System.out.println("update User fail!");
			return "frontend/account";
		}
		
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
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String restPassword(@RequestParam("oldPassword")String oldPassword,
							   @RequestParam("newPassword")String newPassword,
							   @RequestParam("confirmPassword")String confirmPassword,
			    	            HttpSession session, Model model) throws Exception {
		User user = (User)session.getAttribute("user");
		
		// Encrypt old password with AES
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedOldPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, oldPassword);
		String encryptedOldPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedOldPasswordECB);
		
		if(!user.getPassword().equals(encryptedOldPasswordECBBase64)) {
			model.addAttribute("errorMessage", "原密碼錯誤");
			return "frontend/resetPassword";
		}
		if(!newPassword.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "兩次新密碼不一致");
			return "frontend/resetPassword";
		}
		
		// Encrypt new password with AES
		byte[] encryptedNewPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, newPassword);
		String encryptedNewPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedNewPasswordECB);
		
		userDao.updateUserPasswordById(user.getId(), encryptedNewPasswordECBBase64);
		System.out.println("update User password sucess!");
		model.addAttribute("message", "密碼修改成功");
		model.addAttribute("togobtn", "請重新登入");
		model.addAttribute("togourl", "/auth/login");
		return "dialog";
	}
}
