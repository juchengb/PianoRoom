package mvc.bean;


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

	@NotEmpty(message = "email 不能為空")
	private String email;
	
	@NotEmpty(message = "password 不能為空")
	private String password;
}
