package mvc.service;

import java.time.Duration;
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
import mvc.model.po.User;

/**
 * ReserveServiceImpl 實作 ReserveService 的預約相關功能。
 */
@Service
public class ReserveServiceImpl implements ReserveService{
	
	@Autowired
	RoomDao roomDao;
	
	@Autowired
	ReservationDao reservationDao;
	
	/**
	 * 獲得包含預約按鈕的琴房列表。
	 * 
	 * @return 預約按鈕信息的琴房列表
	 */
	@Override
	public List<ReserveRoom> showRoomsWithButtons(List<ReserveRoom> rooms) {

		// 顯示預約按鈕
		for (ReserveRoom room : rooms) {
		    Optional<BusinessHour> businessHour = roomDao.getCurdateBusinessHourById(room.getId());
		    
		    if (businessHour.isPresent()) {
		    	room.setBusinessHour(businessHour.get());
		    	List<ReserveButton> reserveButtonList = new ArrayList<>();
		    	
		    	LocalTime openingTime = businessHour.get().getOpeningTime();
	            LocalTime closingTime = businessHour.get().getClosingTime();
	            LocalDate nowDate = LocalDate.now();
	            LocalTime now = LocalTime.now().minusHours(1); // 獲得現在時間減1小時
	            
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
	
	/**
	 * 獲得預約資訊。
	 * 
	 * @param user  目標使用者
	 * @param roomId 琴房 ID
	 * @param start  開始時間字串
	 * @return Reservation
	 */
	@Override
	public Reservation getReservationInfo(User user, Integer roomId, String start) {
		// start & end time + LocalDate -> LocalDateTime -> Date
		// 將開始與結束時間添加日期
        LocalDateTime localStart = LocalDateTime.of(LocalDate.now(), LocalTime.parse(start));
        
		// 獲得預約資訊
		Reservation reservation = new Reservation();
		reservation.setUserId(user.getId());
		reservation.setRoomId(roomId);
		reservation.setStartTime(localDateTimeToDate(localStart));
		reservation.setEndTime(localDateTimeToDate(localStart.plus(Duration.ofHours(1))));
		return reservation;
	}
	
	// 將 LocalDateTime 轉換為 Date
	private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
	
}
