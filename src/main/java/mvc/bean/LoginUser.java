package mvc.bean;


import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
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
	
	@NotEmpty(message = "{loginUser.code.notempty}")
	private String code;
}