package mvc.test;

import java.net.URISyntaxException;
// import java.nio.file.Path;

import aweit.mail.GMail;

public class EmailDemo {
	
	public static void main(String[] args) throws URISyntaxException {
			
			GMail mail = new GMail("fjchengou@gmail.com", "aesj jqel tgrc uaez");
			
			mail.from("fjchengou@gmail.com")
			    .to("fjc.ncaa@gmail.com")
			    .personal("+Room 琴房預約系統")
			    .subject("+Room 琴房預約系統 重設密碼確認信")
			    .context(" ")
//			    .attachement(Path.of(EmailDemo.class.getClassLoader().getResource("123.txt").toURI()))
			    .send();
	
		}

}
