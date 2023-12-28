package mvc.entity;


import org.springframework.web.multipart.MultipartFile;

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
	private String imgurl;
	
	private Major major;
}
