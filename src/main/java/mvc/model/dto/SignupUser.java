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
public class SignupUser {
	
	@NotEmpty(message = "{signupUser.email.notempty}")
	@Email(message = "{signupUser.email.email}")
	private String email;

	@NotEmpty(message = "{signupUser.password.notempty}")
	private String password;
	
	@NotEmpty(message = "{signupUser.name.notempty}")
	private String name;

	private Integer majorId;
}