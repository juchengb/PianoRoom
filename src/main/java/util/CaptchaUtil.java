package util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class CaptchaUtil {
	
	public void generateCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {
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
		response.setContentType("image/jpeg");
		
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
}
