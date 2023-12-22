package mvc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.bean.Major;

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
	private String imageurl;
	
	private Major major;
}