package mvc.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
	private Integer id;
	private Integer userId;
	private Integer roomId;
	private Date startTime;
	private Date endTime;
	private Date checkin;
	private Date checkout;
	
	private User user;
	private Room room;
}
