package mvc.controller;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.ResponseBody;

import aweit.mail.GMail;
import mvc.bean.LoginUser;
import mvc.bean.SignupUser;
import mvc.dao.UserDao;
import mvc.entity.User;
import mvc.util.KeyUtil;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserDao userDao;
	

	// Generate verification code
	@GetMapping("/getcaptcha")
	private void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {
		    
			Random random = new Random();
			String code1 = generateLetter(random);
	        String code2 = generateLetter(random);
	        String code3 = generateLetter(random);
	        String code4 = generateLetter(random);
			
			String captcha = code1+code2+code3+code4;
			session.setAttribute("captcha", captcha);
			
			// Java 2D 產生圖檔
			// 1. 建立圖像暫存區
			BufferedImage img = new BufferedImage(82, 45, BufferedImage.TYPE_INT_RGB);
			// 2. 建立畫布
			Graphics2D g = img.createGraphics();
			// 3. 設定顏色
			g.setColor(new Color(238,238,238));
			// 4. 塗滿背景
			g.fillRect(0, 0, 82, 45);
			// 5. 設定顏色
			g.setColor(new Color(111, 66, 193));
			// 6. 設定字型、繪字串
			int x = 5; // 起始 x 座標
		    int y = 30; // 起始 y 座標
		    for (int i = 0; i < captcha.length(); i++) {
		        int fontSize = random.nextInt(10) + 16; // 字體大小在 16-24 之間
		        g.setFont(new Font("Montserrat", Font.BOLD, fontSize));
		        g.drawString(String.valueOf(captcha.charAt(i)), x, y);
		        x += fontSize;
		        y += (random.nextInt(10) - 6);
		    }
			// 7. 繪製干擾線
			g.setColor(new Color(255,65,108));
			for(int i=0 ; i<20 ; i++) {
				int x1 = random.nextInt(82);
				int y1 = random.nextInt(45);
				int x2 = random.nextInt(82);
				int y2 = random.nextInt(45);
				g.drawLine(x1, y1, x2, y2);
				
				// set random linewidth
				float lineWidth = .1f + random.nextFloat() * (1f - .1f);
	            g.setStroke(new BasicStroke(lineWidth));
	            
	            // set random opacity
	            float alpha = random.nextFloat(); // 0.0 ~ 1.0 
	            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			}
			
			// 設定回應類型
			response.setContentType("image/png");
			
			// 將影像串流回寫給 client
			ImageIO.write(img, "PNG", response.getOutputStream());
    }
	
	// generate letters
	private static String generateLetter(Random random) {
        String captcha;
        do {
        	captcha = String.format("%c", (char) (random.nextInt(26 * 2) + (random.nextBoolean() ? 'A' : 'a')));
        } while (!Character.isLetter(captcha.charAt(0)));
        return captcha;
    }
	
	@GetMapping("/refreshcaptcha")
	@ResponseBody
	private void refreshCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
		System.out.println("refresh Captcha");
		getCaptchaImage(session, response);
	}
	
	
	@GetMapping("/login")
	public String loginPage(@ModelAttribute("loginUser") LoginUser loginUser,
								@ModelAttribute("signupUser") SignupUser signupUser,
								Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute("loginUser") @Valid LoginUser loginUser,BindingResult result,
						@ModelAttribute("signupUser") SignupUser signupUser, 
						HttpSession session, Model model) throws Exception {
		
		// login form data validation
		if(result.hasErrors()) {
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}
		
		// compare verification code
		if(!loginUser.getCaptcha().equalsIgnoreCase(session.getAttribute("captcha")+"")) {
			session.invalidate(); // session 過期失效
			model.addAttribute("loginMessage", "驗證碼錯誤");
			return "login";
		}
		
		// login
		Optional<User> userOpt = userDao.getUserByEmail(loginUser.getEmail());
		
		if (userOpt.isPresent()) {
			User user = userOpt.get();
		    
		    // Encrypt password with AES
			final String KEY = KeyUtil.getSecretKey();
			SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, loginUser.getPassword());
			String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		    
			// compare password
			if (user.getPassword().equals(encryptedPasswordECBBase64)) {
		        session.setAttribute("user", user);
		        		        return "redirect:/mvc/main";
		    }
		    session.invalidate();
		    model.addAttribute("loginMessage", "密碼錯誤");
		    model.addAttribute("user", new User());
		    return "login";
		} else {
		    session.invalidate();
		    model.addAttribute("loginMessage", "查無此帳號");
		    model.addAttribute("user", new User());
		    return "login";
		}
	}
	
	@RequestMapping("/signup")
    public String signupForm(@ModelAttribute("loginUser") LoginUser loginUser,
								 @ModelAttribute("signupUser") SignupUser signupUser,
								 Model model) {
		model.addAttribute("majors", userDao.findAllMajors());
		return "login";
	}
	
	
	// 建立帳號
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signupUser") @Valid SignupUser signupUser, BindingResult result,
						 @ModelAttribute("loginUser") LoginUser loginUser,
						 Model model) throws Exception {
		// Signup 表單數據驗證
		if(result.hasErrors()) {
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}
		
		// 根據 email 查找 user 物件
		Optional<User> userOpt = userDao.getUserByEmail(signupUser.getEmail());
		if (userOpt.isPresent()) {
			// 出現錯誤訊息
			model.addAttribute("signupMessage", "帳號已存在");
			System.out.println("add User fail!");
			
			model.addAttribute("majors", userDao.findAllMajors());
			return "login";
		}
		
		User user = new User();
		user.setName(signupUser.getName());
		user.setEmail(signupUser.getEmail());
		// Encrypt password with AES
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, signupUser.getPassword());
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		user.setPassword(encryptedPasswordECBBase64);
		user.setMajorId(signupUser.getMajorId());
		
		int rowcount = userDao.addUser(user);
		if (rowcount > 0) {
			System.out.println("add User rowcount = " + rowcount);
		}
		model.addAttribute("message", "註冊成功");
		model.addAttribute("togobtn", "返回登入");
		model.addAttribute("togourl", "/auth/login");
		
	    return "dialog";
    }
	
	
	@PostMapping("/password")
	public String forgottenPassword(@RequestParam("email") String email) {
		// 根據 email 查找 user 物件
		Optional<User> userOpt = userDao.getUserByEmail(email);
		if (userOpt.isPresent()) {
			// 設定一組亂數密碼
			
			
			// send reset email
			GMail mail = new GMail("fjchengou@gmail.com", "aesj jqel tgrc uaez");
			mail.from("fjchengou@gmail.com")
			    .to(email)
			    .personal("+Room 琴房預約系統")
			    .subject("+Room 琴房預約系統 重設密碼確認信")
			    .context("Dear +Room 琴房預約系統的使用者:<br>"
			    		+ "您於 重設密碼，"
			    		+ "新密碼為806BF0。\r\n"
			    		+ "\r\n"
			    		+ "郵件是由系統自動寄發，請勿直接回覆，如有任何問題，請致電相關承辦人，感謝您的配合。 ")
			    .send();
			return "redirect:/mvc/auth/login";
		}
		System.out.println("add User rowcount = ");
		
		return "redirect:/mvc/auth/login";
    }
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mvc/auth/login";
	}
	
	
}
