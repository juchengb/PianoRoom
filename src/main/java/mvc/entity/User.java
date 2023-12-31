package mvc.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Integer majorId;
	private Integer level;
	private String avator;
//	private String authType; // 授權來源
//	private String authId; // 授權ID
	
	private Major major;
}
