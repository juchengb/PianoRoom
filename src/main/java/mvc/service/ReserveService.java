package mvc.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mvc.model.dto.ReserveRoom;

public interface ReserveService {
	
	public List<ReserveRoom> showRoomsWithButtons();
	
	public Date localDateTimeToDate(LocalDateTime localDateTime);

}
