package mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import mvc.model.po.Reservation;
import mvc.model.vo.DailyMinutes;


public interface ReservationDao {
	
//	預約-Reservation
//	新增
	int addReservation(Reservation reservation);
	
	
//	修改
	// 根據預約ID更新預約 (後臺用)
	int updateReservationById(Integer id, Reservation reservation);
	// 根據預約ID更新簽到 (前臺簽到打卡用)
	int updateCheckinById(Integer id, Date checkin);
	// 根據預約ID更新簽退 (前臺簽退打卡用)
	int updateCheckoutById(Integer id, Date checkout);

	
//	刪除
	int deleteReservationById(Integer id);
	
	
//	查詢
	// 根據預約 ID 查找預約
	Optional<Reservation> getReservationById(Integer id);
	// 根據 room id 和 start time 查詢預約
	Optional<Reservation> getReservationByRoomIdAndStartTime(Integer roomId, Date startTime);
	// 根據 user id 和 start time 查詢預約
	Optional<Reservation> getReservationByUserIdAndStartTime(Integer userId, Date startTime);
	// 查詢使用者下一個預約 (前臺打卡用)
	Optional<Reservation> getNextReservationByUserId(Integer userId);
	
	int getCurrentDayCheckinByUserId(Integer userId); // AndByCurrentTime
	// 查詢使用者每天練習分鐘數 (前臺 MyPractice 圖表使用)
	List<DailyMinutes> getDailyMinutesByUserId(Integer userId);
	// 查詢使用者未來預約 (前臺 MyReservation 用)
	List<Reservation> findFutureResrvationsByUserId(Integer userId);
	// 查詢使用者歷史預約 (前臺 MyReservation 用)
	List<Reservation> findPastResrvationsByUserId(Integer userId);
	// 查詢所有預約 (後臺用)
	List<Reservation> findAllReservations();

}
