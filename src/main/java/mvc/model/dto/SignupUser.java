package mvc.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupUser {
	
	@NotNull
	@Size(min=1, message="{signupUser.email.size}")
	@Email(message = "{signupUser.email.email}")
	private String email;
	
	@NotNull
	@Size(min=6, message = "{signupUser.password.size}")
	private String password;
	
	@NotNull
	@Size(min=1, message = "{signupUser.name.size}")
	private String name;

	private Integer majorId;
	
}
