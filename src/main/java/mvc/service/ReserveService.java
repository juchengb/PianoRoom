package mvc.service;


import java.util.List;

import mvc.model.dto.ReserveRoom;
import mvc.model.po.Reservation;
import mvc.model.po.User;

public interface ReserveService {
	
	public List<ReserveRoom> showRoomsWithButtons();
	
	public Reservation getReservationInfo(User user, Integer roomId, String start);

}
