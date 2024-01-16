package mvc.service;

import java.awt.image.BufferedImage;

public interface AuthService {

	public String getCaptcha();

	public BufferedImage getCaptchaImage(String captcha);
}
