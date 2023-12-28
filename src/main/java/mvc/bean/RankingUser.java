package mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.entity.Major;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUser {
	private Integer userId;
	private String name;
	private Integer majorId;
	private Integer hours;
	private Integer ranking;
	private String imgurl;
	
	private Major major;
	
}
