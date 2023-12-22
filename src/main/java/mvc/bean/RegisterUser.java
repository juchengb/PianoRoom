package mvc.bean;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUser {

	@NotEmpty(message = "請輸入Name")
	private String name;
	
	@NotEmpty(message = "請輸入Email")
	private String email;
	
	@NotEmpty(message = "請輸入Password")
	private String password;
	
	@Range(message = "請選擇一個主修")
	private Integer majorId;
}
