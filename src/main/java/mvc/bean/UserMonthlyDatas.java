package mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMonthlyDatas {
	private Integer userId;
	private Integer counts;
	private Integer hours;
	private Integer ranking;
	
}
