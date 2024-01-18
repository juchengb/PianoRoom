package mvc.service;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aweit.mail.GMail;
import mvc.model.dto.EditUser;
import mvc.model.dto.SignupUser;
import mvc.model.po.User;
import mvc.util.KeyUtil;

/**
 * AuthServiceImpl 實作 AuthService 的驗證、加密、使用者註冊等相關功能。
 */
@Service
public class AuthServiceImpl implements AuthService {
	
	/**
	 * 隨機生成字母。
	 * 
	 * @return 一個隨機字母
	 */
	public String generateLetter() {
		Random random = new Random();
		String captcha;
		do {
			captcha = String.format("%c", (char) (random.nextInt(26 * 2) + (random.nextBoolean() ? 'A' : 'a')));
		} while (!Character.isLetter(captcha.charAt(0)));
		return captcha;
	}
	
	/**
	 * 隨機生成四位數驗證碼。
	 * 
	 * @return 四位數驗證碼
	 */
	@Override
	public String getCaptcha() {
		
		String code1 = generateLetter();
		String code2 = generateLetter();
		String code3 = generateLetter();
		String code4 = generateLetter();
		
		String captcha = code1 + code2 + code3 + code4;
		return captcha;
	}
	
	/**
	 * 根據驗證碼生成相應的圖片。
	 * 
	 * @param captcha 驗證碼字串
	 * @return BufferedImage 驗證碼圖片
	 */
	@Override
	public BufferedImage getCaptchaImage(String captcha) {
		Random random = new Random();
		// Java 2D 建立圖像
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
			int fontSize = random.nextInt(7) + 18; // 字體大小於 18-24 之間
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

			// 設定隨機線寬
			float lineWidth = .1f + random.nextFloat() * (1f - .1f);
			g.setStroke(new BasicStroke(lineWidth));

			// 設定隨機透明度
			float alpha = random.nextFloat(); // 0.0 ~ 1.0
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		}
		return img;
	}
	
	/**
	 * 使用 AES 對密碼進行加密。
	 * 
	 * @param password 原始密碼
	 * @return 加密後密碼 ( Base64 編碼)
	 */
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
	
	/**
	 * 將 SignupUser 轉換為 User。
	 * 
	 * @param signupUser 註冊使用者的 DTO
	 * @return User
	 */
	@Override
	public User signupUserConvertToUser(SignupUser signupUser) {
		User user = new User().builder().name(signupUser.getName())
										.email(signupUser.getEmail())
										.password(encryptPassword(signupUser.getPassword()))
										.majorId(signupUser.getMajorId())
										.level(2)
										.build();
		return user;
	}
	
	/**
     * 生成 10 分鐘內有效的一次性密碼（TOTP）。
     *
     * @return TOTP 密碼
     * @throws InvalidKeyException 密鑰無效時的例外
     * @throws NoSuchAlgorithmException  指定的加密算法無效的例外
     */
	@Override
	public String getTotp() throws InvalidKeyException, NoSuchAlgorithmException {
		int secretLength = 16;
	    StringBuilder secretBuilder = new StringBuilder();
	    for (int i = 0; i < secretLength; i++) {
	    	secretBuilder.append(generateLetter());
	    }
		String secret = Base64.getEncoder().encodeToString(secretBuilder.toString().getBytes()); // 當作金鑰
		System.out.println(secret);
		long timeInterval = System.currentTimeMillis() / 1000L / 60L / 10L; // 10分鐘
		
		// 得到 TOTP 密碼 (使用 HMACSHA256 作為加密算法)
		String totp;
			totp = KeyUtil.generateTOTP(secret, timeInterval, "HMACSHA256");
			return totp;
	}
	
	/**
	 * 發送有 TOPT 驗證碼的重設密碼信件。
	 * 
	 * @param email 使用者電子信箱
	 */
	@Override
	public void sentEamil(String email, String totp) {
		String sender = "plusroomsystem@gmail.com";
		GMail mail = new GMail(sender, "jmds feuy owve iywz");

		mail.from(sender).to(email).personal("+Room 琴房預約系統").subject("+Room 琴房預約系統 一次性驗證碼通知")
				.context("Dear +Room 琴房預約系統的使用者:<br>"
						 + "您的驗證碼為： " + totp + "<br>"
						 + "此驗證碼有效時間為10分鐘，請儘速回到頁面重設密碼，謝謝。")
				.send();
	}
	

//	------------------------------------------------------------------
//	account, backend
	
	// 使用者頭像的上傳路徑
	private static final Path upPath = Paths.get("C:/Javaclass/uploads/profile");
	
	static {
		try {
			Files.createDirectories(upPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 將 updateUser 轉換為 User，用於更新使用者。
	 * 
	 * @param userOrg 原始 User
	 * @param updateUser 從前端接收的 EditUser
	 * @return 更新後的 User
	 */
	@Override
	public User updateUserConvertToUser(User userOrg, EditUser updateUser) {
		// 使用者頭像處理
		MultipartFile multipartFile = updateUser.getAvator();
		String avator;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			avator = userOrg.getId() + updateUser.getName() + "-" + multipartFile.getOriginalFilename(); 
			Path picPath = upPath.resolve(avator);
			try {
				Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 如果沒有上傳新檔案，則使用預設頭像
			avator = userOrg.getAvator();
		}
		
		// 將 EditUser 轉換為 User
		User userEntity = new User();
		userEntity.setId(userOrg.getId());
		userEntity.setEmail(updateUser.getEmail());
		userEntity.setName(updateUser.getName());
		userEntity.setMajorId(updateUser.getMajorId());
		userEntity.setAvator(avator);
		System.out.println("userEntity: " + userEntity);
		return userEntity;
	}
	
	/**
	 * 將 addUser 轉換為 User ，用於新增使用者。
	 * 
	 * @param addUser  從前端接收的 EditUser 對象
	 * @return 新增後的 User
	 */
	@Override
	public User addUserConvertToUser(EditUser addUser) {
		// 使用 AES 加密密碼
		String encryptedPasswordECBBase64 = encryptPassword(addUser.getPassword());
		addUser.setPassword(encryptedPasswordECBBase64);
		
		// 使用者頭像處理
		MultipartFile multipartFile = addUser.getAvator();
		String avator;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			avator = addUser.getName() + "-" + multipartFile.getOriginalFilename(); 
			Path picPath = upPath.resolve(avator);
			try {
				Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 如果沒有上傳新檔案，使用預設頭像
			avator = "default.png";
		}

		// 將 EditUser 轉換為 User
		User userEntity = new User();
		userEntity.setEmail(addUser.getEmail());
		userEntity.setPassword(encryptedPasswordECBBase64);
		userEntity.setName(addUser.getName());
		userEntity.setMajorId(addUser.getMajorId());
		userEntity.setLevel(addUser.getLevel());
		userEntity.setAvator(avator);
		System.out.println("userEntity: " + userEntity);
		System.out.println("userEntity: " + userEntity.toString());
		return userEntity;
	}
	
}
