package mvc.service;

import java.awt.image.BufferedImage;

import mvc.model.dto.SignupUser;
import mvc.model.po.User;

public interface AuthService {

	public String getCaptcha();

	public BufferedImage getCaptchaImage(String captcha);
	
	public String encryptPassword(String password);
	
	public User signupUserConvertToUser(SignupUser signupUser);
	
	public void sentEamil(String email);
}
