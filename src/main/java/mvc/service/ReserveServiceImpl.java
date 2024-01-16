package mvc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.model.dto.ReserveButton;
import mvc.model.dto.ReserveRoom;
import mvc.model.po.BusinessHour;
import mvc.model.po.Reservation;

@Service
public class ReserveServiceImpl implements ReserveService{
	
	@Autowired
	RoomDao roomDao;
	
	@Autowired
	ReservationDao reservationDao;
	
	
	@Override
	public List<ReserveRoom> showRoomsWithButtons() {
		
		List<ReserveRoom> rooms = roomDao.findAllRoomsToReserve();
		
		// show reserve buttons
		for (ReserveRoom room : rooms) {
		    Optional<BusinessHour> businessHour = roomDao.getCurdateBusinessHourById(room.getId());
		    
		    if (businessHour.isPresent()) {
		    	List<ReserveButton> reserveButtonList = new ArrayList<>();
		    	
		    	LocalTime openingTime = businessHour.get().getOpeningTime();
	            LocalTime closingTime = businessHour.get().getClosingTime();
	            LocalDate nowDate = LocalDate.now();
	            LocalTime now = LocalTime.now();
	            
	            // 切分營業時間，每小時一個按鈕
	            LocalTime buttonTime = openingTime;
	            
	            while (buttonTime != null && buttonTime.isBefore(closingTime))  {
	            	if (buttonTime.isAfter(now)) {
	                    Date buttonDate = localDateTimeToDate(LocalDateTime.of(nowDate, buttonTime));
	                    Optional<Reservation> reservationOpt = reservationDao.getReservationByRoomIdAndStartTime(room.getId(), buttonDate);
	                    
	                    reserveButtonList.add(new ReserveButton().builder()
	                    					  					 .buttonString(buttonTime.toString())
	                    					  					 .isBooked(reservationOpt.isPresent())
	                    					  					 .build());
	                }
	            	buttonTime = buttonTime.plusHours(1); // 增加一小時
	            }
	            room.setReserveButtonList(reserveButtonList);
		    } else {
		        room.setReserveButtonList(Collections.emptyList());
		    }
	    }
		return rooms;
	}
	
	@Override
	public Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
