package mvc.bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

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