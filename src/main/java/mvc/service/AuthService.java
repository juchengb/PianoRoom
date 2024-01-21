package mvc.service;

import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import mvc.model.dto.EditUser;
import mvc.model.dto.SignupUser;
import mvc.model.po.User;

/**
 * AuthService 提供驗證、加密、使用者註冊等相關功能的介面。
 */
public interface AuthService {
	
	/**
	 * 隨機生成四位數驗證碼。
	 * 
	 * @return 四位數驗證碼
	 */
	public String getCaptcha();
	
	/**
	 * 根據驗證碼生成相應的圖片。
	 * 
	 * @param captcha 驗證碼字串
	 * @return BufferedImage 驗證碼圖片
	 */
	public BufferedImage getCaptchaImage(String captcha);
	
	/**
	 * 使用 AES 對密碼進行加密。
	 * 
	 * @param password 原始密碼
	 * @return 加密後密碼 ( Base64 編碼)
	 */
	public String encryptPassword(String password);
	
	/**
	 * 將 SignupUser 轉換為 User。
	 * 
	 * @param signupUser 註冊使用者的 DTO
	 * @return User
	 */
	public User signupUserConvertToUser(SignupUser signupUser);
	
	/**
     * 生成一次性密碼（TOTP）的 SecretBase64。
     *
     * @return SecretBase64
     */
	public String getSecretBase64();
	
	/**
     * 生成10分鐘內有效的一次性密碼（TOTP）。
     *
     * @return TOTP 密碼
     * @throws InvalidKeyException 密鑰無效時的例外
     * @throws NoSuchAlgorithmException  指定的加密算法無效的例外
     */
	public String getTotp() throws InvalidKeyException, NoSuchAlgorithmException;
	
	/**
	 * 發送有 OPT 驗證碼的重設密碼信件。
	 * 
	 * @param email 使用者電子信箱
	 */
	public void sentEamil(String email, String totp);
	
//	--------------------------------------------------------------
//	account, backend user
	
	/**
	 * 將 updateUser 轉換為 User，用於更新使用者。
	 * 
	 * @param userOrg 原始 User
	 * @param updateUser 從前端接收的 EditUser
	 * @return 更新後的 User
	 */
	public User updateUserConvertToUser(User userOrg, EditUser updateUser);
	
	/**
	 * 將 addUser 轉換為 User ，用於新增使用者。
	 * 
	 * @param addUser  從前端接收的 EditUser 對象
	 * @return 新增後的 User
	 */
	public User addUserConvertToUser(EditUser addUser);
	
}
