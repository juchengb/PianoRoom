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

/**
 * AccountController 處理使用者帳戶相關功能。
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthService authService;
	
	/**
     * GET 請求，顯示使用者帳戶頁面。
     *
     * @param updateUser 綁定前端表單的使用者資料
     * @param session HTTP Session
     * @param model Spring MVC 模型
     * @return account 頁面
     */
	@GetMapping("")
	public String accountPage(@ModelAttribute("updateUser") EditUser updateUser, HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("majors", userDao.findAllMajors());
		model.addAttribute("user", user);
		return "frontend/account";
	}
	
	/**
     * POST 請求，修改使用者個人檔案。
     *
     * @param updateUser 綁定前端表單的使用者資料
     * @param result 表單驗證結果
     * @param session HTTP Session
     * @param model Spring MVC 模型
     * @return 成功：修改成功頁面；失敗：返回原頁面
     * @throws IntrospectionException
     * @throws IOException
     */
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
		
		// 更新使用者資訊
		int rowCount = userDao.updateUserById(user.getId(), userEntity);
		System.out.println(userEntity);
		if(rowCount == 0) {
			model.addAttribute("error","更新失敗");
			System.out.println("update User fail!");
			return "frontend/account";
		}
		
		// 重設使用者的權限
		userEntity.setLevel(user.getLevel());
		
		// 更新 HttpSession 中的使用者資訊
	    session.setAttribute("user", userEntity);
		System.out.println("update User sucess!");
		model.addAttribute("message", "修改成功");
		model.addAttribute("togobtn", "返回使用者首頁");
		model.addAttribute("togourl", "/main");
		return "dialog";
	}
	
	// --------------------------------------------------------------------------------------
	
	/**
     * GET 請求，顯示重設密碼頁面。
     *
     * @return resetPassword 頁面
     */
	@GetMapping("/password")
	public String restPasswordPage() {
		return "frontend/resetPassword";
	}
	
	/**
     * POST 請求，重設密碼。
     *
     * @param oldPassword 舊密碼
     * @param newPassword 新密碼
     * @param confirmPassword 確認新密碼
     * @param session HTTP Session
     * @param model Spring MVC 模型
     * @return 成功：修改成功頁面；失敗：返回原頁面
     * @throws Exception
     */
	@PostMapping("/password")
	public String restPassword(@RequestParam("oldPassword")String oldPassword,
							   @RequestParam("newPassword")String newPassword,
							   @RequestParam("confirmPassword")String confirmPassword,
			    	           HttpSession session, Model model) throws Exception {
		User user = (User)session.getAttribute("user");
		
		// 使用 AES 加密舊密碼
		String encryptedOldPasswordECBBase64 = authService.encryptPassword(oldPassword);
		
		if(!user.getPassword().equals(encryptedOldPasswordECBBase64)) {
			model.addAttribute("errorMessage", "原密碼錯誤");
			return "frontend/resetPassword";
		}
		if(!newPassword.equals(confirmPassword)) {
			model.addAttribute("errorMessage", "兩次新密碼不一致");
			return "frontend/resetPassword";
		}
		
		// 使用 AES 加密新密碼
		String encryptedNewPasswordECBBase64 = authService.encryptPassword(newPassword);
		
		userDao.updateUserPasswordById(user.getId(), encryptedNewPasswordECBBase64);
		System.out.println("update User password sucess!");
		model.addAttribute("message", "密碼修改成功");
		model.addAttribute("togobtn", "請重新登入");
		model.addAttribute("togourl", "/auth/login");
		return "dialog";
	}
	
}
