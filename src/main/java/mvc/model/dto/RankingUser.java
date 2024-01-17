package mvc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.model.po.Major;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUser {
	
	private Integer userId;
	private String name;
	private Integer majorId;
	private Integer minutes;
	private Integer ranking;
	private String avator;
	
	private Major major;
	
}
