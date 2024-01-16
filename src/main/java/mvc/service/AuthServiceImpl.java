package mvc.service;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import aweit.mail.GMail;
import mvc.model.dto.SignupUser;
import mvc.model.po.User;
import mvc.util.KeyUtil;

@Service
public class AuthServiceImpl implements AuthService {

	public String generateLetter() {
		Random random = new Random();
		String captcha;
		do {
			captcha = String.format("%c", (char) (random.nextInt(26 * 2) + (random.nextBoolean() ? 'A' : 'a')));
		} while (!Character.isLetter(captcha.charAt(0)));
		return captcha;
	}
	
	@Override
	public String getCaptcha() {
		
		String code1 = generateLetter();
		String code2 = generateLetter();
		String code3 = generateLetter();
		String code4 = generateLetter();
		
		String captcha = code1 + code2 + code3 + code4;
		return captcha;
	}
	
	@Override
	public BufferedImage getCaptchaImage(String captcha) {
		Random random = new Random();
		// Java 2D create image
		// 1. 建立圖像暫存區
		BufferedImage img = new BufferedImage(84, 45, BufferedImage.TYPE_INT_RGB);
		// 2. 建立畫布
		Graphics2D g = img.createGraphics();
		// 3. 設定顏色
		g.setColor(new Color(238, 238, 238));
		// 4. 塗滿背景
		g.fillRect(0, 0, 84, 45);
		// 5. 設定顏色
		g.setColor(new Color(111, 66, 193));
		// 6. 設定字型、繪字串
		int x = 5; // 起始 x 座標
		int y = 30; // 起始 y 座標
		for (int i = 0; i < captcha.length(); i++) {
			int fontSize = random.nextInt(7) + 18; // 字體大小在 18-24 之間
			g.setFont(new Font("Montserrat", Font.BOLD, fontSize));
			g.drawString(String.valueOf(captcha.charAt(i)), x, y);
			x += (fontSize / 2 + 10);
			y += (random.nextInt(10) - 3);

		}
		// 7. 繪製干擾線
		g.setColor(new Color(255, 65, 108));
		for (int i = 0; i < 20; i++) {
			int x1 = random.nextInt(84);
			int y1 = random.nextInt(45);
			int x2 = random.nextInt(84);
			int y2 = random.nextInt(45);
			g.drawLine(x1, y1, x2, y2);

			// set random linewidth
			float lineWidth = .1f + random.nextFloat() * (1f - .1f);
			g.setStroke(new BasicStroke(lineWidth));

			// set random opacity
			float alpha = random.nextFloat(); // 0.0 ~ 1.0
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		}
		return img;
	}

	@Override
	public String encryptPassword(String password) {
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB;
		try {
			encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, password);
		} catch (Exception e) {
			encryptedPasswordECB = null;
			e.printStackTrace();
		}
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		return encryptedPasswordECBBase64;
	}

	@Override
	public User signupUserConvertToUser(SignupUser signupUser) {
		User user = new User().builder().name(signupUser.getName())
										.email(signupUser.getEmail())
										.password(encryptPassword(signupUser.getPassword()))
										.majorId(signupUser.getMajorId())
										.build();
		return user;
	}

	@Override
	public void sentEamil(String email) {
		// set a random code
		SecureRandom secureRandom = new SecureRandom();
		int code = secureRandom.nextInt(1000000);
		String otp = String.format("%06d", code);
		// send reset email
		GMail mail = new GMail("fjchengou@gmail.com", "aesj jqel tgrc uaez");

		mail.from("fjchengou@gmail.com").to(email).personal("+Room 琴房預約系統").subject("+Room 琴房預約系統 一次性驗證碼通知")
				.context("Dear +Room 琴房預約系統的使用者:<br>"
						 + "您的驗證碼為： " + otp + "<br>"
						 + "此驗證碼有效時間為10分鐘，請儘速回到頁面重設密碼，謝謝。")
				.send();
		
	}

	

}
