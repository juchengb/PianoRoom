package mvc.bean;


import java.time.LocalTime;
import java.util.Date;

public class Reservation {
	
	private Integer id;
	private Integer userId;
	private Integer roomId;
	private Date date;
	private LocalTime startTime;
	private LocalTime endTime;
}
