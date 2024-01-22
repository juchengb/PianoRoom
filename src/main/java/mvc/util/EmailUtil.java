package mvc.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class EmailUtil {
	
	public void sendVerificationCode(String toEmail, String totp) throws MessagingException {
    	
    	final String mail = "plusroomsystem@gmail.com";
        final String key = "jmds feuy owve iywz";

        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your email host
        properties.put("mail.smtp.port", "587"); // Replace with your email port 587

        // Set up a Session object
        // 要再設定把 credential 存入安全地方
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, key); // Replace with your email credentials
            }
        });

        // Create a MimeMessage object
        Message message = new MimeMessage(session);

        // Set the sender and recipient addresses
        message.setFrom(new InternetAddress(mail)); // Replace with your email address
        if (toEmail != null) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        } else {
            // Handle the case where toEmail is null
            System.out.println("toEmail is null");
           
        }

        // Set the email subject and content
        message.setSubject("+Room 琴房預約系統 一次性驗證碼通知");
        message.setText("Dear +Room 琴房預約系統的使用者:<br>"
				 + "您的驗證碼為： " + totp + "<br>"
				 + "此驗證碼有效時間為10分鐘，請儘速回到頁面重設密碼，謝謝。");

        // Send the email
        Transport.send(message);
    }
    
	
}
