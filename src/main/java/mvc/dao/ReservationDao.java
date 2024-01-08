package mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import mvc.bean.DailyMinutes;
import mvc.entity.Reservation;


public interface ReservationDao {
//	預約-Reservation
//	新增
	int addReservation(Reservation reservation);
	
//	查詢
	// 根據預約ID查找預約
	Optional<Reservation> getReservationById(Integer id);
	// 根據room id 和 start time 查找預約
	Optional<Reservation> getReservationByRoomIdAndStartTime(Integer roomId, Date startTime);
	
	// 尋找使用者的下一個預約 (前臺打卡用)
	Optional<Reservation> getNextReservationByUserId(Integer userId);
	
	int getCurrentDayCheckinByUserId(Integer userId); // AndByCurrentTime
	List<DailyMinutes> getDailyMinutesByUserId(Integer userId);
	List<Reservation> findFutureResrvationsByUserId(Integer userId);
	List<Reservation> findPastResrvationsByUserId(Integer userId);
	
	
//	修改
	// 根據預約ID更新預約 (給後臺用)
	int updateReservationById(Integer id, Reservation reservation);
	int updateCheckinById(Integer id, Date checkin);
	int updateCheckoutById(Integer id, Date checkout);

//	刪除
	int deleteReservationById(Integer id);


}
