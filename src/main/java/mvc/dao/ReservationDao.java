package mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import mvc.model.po.Reservation;
import mvc.model.vo.DailyMinutes;

/**
 * ReservationDao 定義預約相關數據的 SQL 。
 */
public interface ReservationDao {
	
//	預約-Reservation
//	新增
	/**
	 * 新增預約。
	 * 
	 * @param reservation Reservation 欲新增的預約
	 * @return int 新增預約的筆數
	 */
	int addReservation(Reservation reservation);
	
	
//	修改
	/**
	 * 根據預約 ID 修改預約 (後臺用)。
	 * 
	 * @param id Integer 欲修改的預約 ID
	 * @param reservation Reservation 修改後的預約
	 * @return int 修改預約的筆數
	 */
	int updateReservationById(Integer id, Reservation reservation);
	
	/**
	 * 根據預約 ID 更新簽到時間 (前臺簽到打卡用)。
	 * 
	 * @param id Integer 欲更新的預約 ID
	 * @param checkin Date 簽到時間
	 * @return int 更新簽到時間的筆數
	 */
	int updateCheckinById(Integer id, Date checkin);

	/**
	 * 根據預約 ID 更新簽退時間 (前臺簽退打卡用)。
	 * 
	 * @param id Integer 欲更新的預約的ID
	 * @param checkout Date 簽退時間
	 * @return int 更新簽退時間的筆數
	 */
	int updateCheckoutById(Integer id, Date checkout);

	
//	刪除
	/**
	 * 根據預約 ID 刪除預約。
	 * 
	 * @param id Integer 欲刪除的預約 ID
	 * @return int 刪除預約的筆數
	 */
	int deleteReservationById(Integer id);
	
	
//	查詢 (單筆)
	/**
	 * 根據預約 ID 查詢預約。
	 * 
	 * @param id Integer 欲查詢的預約 ID
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	Optional<Reservation> getReservationById(Integer id);
	
	/**
	 * 根據 琴房 ID 和 開始時間 查詢預約。
	 * 
	 * @param roomId Integer 琴房 ID
	 * @param startTime Date 開始時間
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	Optional<Reservation> getReservationByRoomIdAndStartTime(Integer roomId, Date startTime);

	/**
	 * 根據 使用者 ID 和 開始時間 查詢預約。
	 * 
	 * @param userId Integer 使用者 ID
	 * @param startTime Date 開始時間
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	Optional<Reservation> getReservationByUserIdAndStartTime(Integer userId, Date startTime);
	
	/**
	 * 查詢使用者的下一筆預約 (前臺打卡用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return Optional<Reservation> 下一筆預約 (Optional)
	 */
	Optional<Reservation> getNextReservationByUserId(Integer userId);
	

//	查詢 (多筆)
	/**
	 * 查詢使用者的每天練習分鐘數 (前臺 MyPractice 圖表使用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<DailyMinutes> 每天練習分鐘數列表
	 */
	List<DailyMinutes> getDailyMinutesByUserId(Integer userId);
	
	/**
	 * 查詢使用者的未來預約 (前臺 MyReservation 用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<Reservation> 未來預約列表
	 */
	List<Reservation> findFutureResrvationsByUserId(Integer userId);

	/**
	 * 查詢使用者的歷史預約 (前臺 MyReservation 用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<Reservation> 歷史預約列表
	 */
	List<Reservation> findPastResrvationsByUserId(Integer userId);

	/**
	 * 查詢所有預約 (後臺用)。
	 * 
	 * @return List<Reservation> 所有預約列表
	 */
	List<Reservation> findAllReservations();
	
}
