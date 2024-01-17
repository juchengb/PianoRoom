package mvc.model.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
	
	@NotNull
	@Size(min=1, message="{loginUser.email.size}")
	@Email(message = "{loginUser.email.email}")
	private String email;

	@NotNull
	@Size(min=1, message="{loginUser.password.size}")
	private String password;
	
	@NotNull
	@Size(min=1, message="{loginUser.captcha.size}")
	private String captcha;
	
}