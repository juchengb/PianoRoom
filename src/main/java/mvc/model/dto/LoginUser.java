package mvc.model.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
	
	@NotEmpty(message = "{loginUser.email.notempty}")
	@Email(message = "{loginUser.email.email}")
	private String email;

	@NotEmpty(message = "{loginUser.password.notempty}")
	private String password;
	
	@NotEmpty(message = "{loginUser.captcha.notempty}")
	private String captcha;
}