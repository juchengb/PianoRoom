package mvc.bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {
	
	@NotEmpty(message = "{signupUser.email.notempty}")
	@Email(message = "{signupUser.email.email}")
	private String email;
	
	@NotEmpty(message = "{signupUser.name.notempty}")
	private String name;

	private Integer majorId;
	
	private MultipartFile avator;
}
