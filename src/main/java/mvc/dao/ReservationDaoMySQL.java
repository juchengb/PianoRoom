package mvc.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mvc.model.po.Reservation;
import mvc.model.vo.DailyMinutes;

/**
 * ReservationDaoMySQL 實作預約相關數據的 SQL 。
 */
@Repository
public class ReservationDaoMySQL implements ReservationDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoomDao roomDao;

	
//	預約-Reservation
//	新增
	/**
	 * 新增預約。
	 * 
	 * @param reservation Reservation 欲新增的預約
	 * @return int 新增預約的筆數
	 */
	@Override
	public int addReservation(Reservation reservation) {
		String sql = "insert into pianoroom.reservation(user_id, room_id, start_time, end_time) "
				+ "values(:user_id, :room_id, :startTime, :endTime)";
		Map<String, Object> params = new HashMap<>();
		params.put("user_id", reservation.getUserId());
		params.put("room_id", reservation.getRoomId());
		params.put("startTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("endTime", new java.sql.Timestamp(reservation.getEndTime().getTime()));
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	修改
	/**
	 * 根據預約 ID 修改預約 (後臺用)。
	 * 
	 * @param id Integer 欲修改的預約 ID
	 * @param reservation Reservation 修改後的預約
	 * @return int 修改預約的筆數
	 */
	@Override
	public int updateReservationById(Integer id, Reservation reservation) {
		String sql = "update pianoroom.reservation set room_id = :roomId, start_time = :startTime, end_time = :endTime,"
				+ "checkin = :checkin, checkout =:checkout where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", reservation.getRoomId());
		params.put("startTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("endTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("checkin", new java.sql.Timestamp(reservation.getCheckin().getTime()));
		params.put("checkout", new java.sql.Timestamp(reservation.getCheckout().getTime()));
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	/**
	 * 根據預約 ID 更新簽到時間 (前臺簽到打卡用)。
	 * 
	 * @param id Integer 欲更新的預約 ID
	 * @param checkin Date 簽到時間
	 * @return int 更新簽到時間的筆數
	 */
	@Override
	public int updateCheckinById(Integer id, Date checkin) {
		String sql = "update pianoroom.reservation set checkin = :checkin where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkin", new java.sql.Timestamp(checkin.getTime()));
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	/**
	 * 根據預約 ID 更新簽退時間 (前臺簽退打卡用)。
	 * 
	 * @param id Integer 欲更新的預約的ID
	 * @param checkout Date 簽退時間
	 * @return int 更新簽退時間的筆數
	 */
	@Override
	public int updateCheckoutById(Integer id, Date checkout) {
		String sql = "update pianoroom.reservation set checkout = :checkout where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkout", new java.sql.Timestamp(checkout.getTime()));
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	刪除
	/**
	 * 根據預約 ID 刪除預約。
	 * 
	 * @param id Integer 欲刪除的預約 ID
	 * @return int 刪除預約的筆數
	 */
	@Override
	public int deleteReservationById(Integer id) {
		String sql = "delete from pianoroom.reservation where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	查詢 (單筆)
	/**
	 * 根據預約 ID 查詢預約。
	 * 
	 * @param id Integer 欲查詢的預約 ID
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	@Override
	public Optional<Reservation> getReservationById(Integer id) {
		try {
			String sql = "select id, user_id, room_id, start_time, end_time from pianoroom.reservation where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * 根據 琴房 ID 和 開始時間 查詢預約。
	 * 
	 * @param roomId Integer 琴房 ID
	 * @param startTime Date 開始時間
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	@Override
	public Optional<Reservation> getReservationByRoomIdAndStartTime(Integer roomId, Date startTime){
		try {
			String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from pianoroom.reservation "
					+ "where room_id = :roomId and start_time = :startTime";
			Map<String, Object> params = new HashMap<>();
			params.put("roomId", roomId);
			params.put("startTime", startTime);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * 根據 使用者 ID 和 開始時間 查詢預約。
	 * 
	 * @param userId Integer 使用者 ID
	 * @param startTime Date 開始時間
	 * @return Optional<Reservation> 預約 (Optional)
	 */
	@Override
	public Optional<Reservation> getReservationByUserIdAndStartTime(Integer userId, Date startTime){
		try {
			String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from pianoroom.reservation "
					+ "where user_id = :userId and start_time = :startTime";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("startTime", startTime);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	/**
	 * 查詢使用者的下一筆預約 (前臺打卡用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return Optional<Reservation> 下一筆預約 (Optional)
	 */
	@Override
	public Optional<Reservation> getNextReservationByUserId(Integer userId) {
		try {
			String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from "
					+ "pianoroom.reservation where user_id = :userId and checkout is null and end_time >= now() order by start_time limit 1";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			Reservation reservation = namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Reservation.class));
			enrichWithDetails(reservation);
			return Optional.of(reservation);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
			
	}

	
//	查詢 (多筆)
	/**
	 * 查詢使用者的每天練習分鐘數 (前臺 MyPractice 圖表使用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<DailyMinutes> 每天練習分鐘數列表
	 */
	@Override
	public List<DailyMinutes> getDailyMinutesByUserId(Integer userId) {
		String sql = "select day(tdt.date) as day, coalesce(sum(timestampdiff(minute, r.checkin, r.checkout)), 0) as "
				+ "minutes from pianoroom.temporarydaytable tdt "
				+ "left join pianoroom.reservation r on date(tdt.date) = date(r.checkin) and r.user_id = :userId "
				+ "where month(tdt.date) = month(curdate()) group by tdt.date";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);

	    return namedParameterJdbcTemplate.query
				  (sql, params, new BeanPropertyRowMapper<>(DailyMinutes.class));
	}
	
	/**
	 * 查詢使用者的未來預約 (前臺 MyReservation 用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<Reservation> 未來預約列表
	 */
	public List<Reservation> findFutureResrvationsByUserId(Integer userId) {
		String sql = "select id, user_id, room_id, start_time, end_time from pianoroom.reservation "
				+ "where user_id = :userId and start_time > now() order by start_time";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		List<Reservation> reservations = namedParameterJdbcTemplate.query
										 (sql, params, new BeanPropertyRowMapper<>(Reservation.class));
		reservations.forEach(this::enrichWithDetails);
		return reservations;
	}
	
	/**
	 * 查詢使用者的歷史預約 (前臺 MyReservation 用)。
	 * 
	 * @param userId Integer 使用者 ID
	 * @return List<Reservation> 歷史預約列表
	 */
	@Override
	public List<Reservation> findPastResrvationsByUserId(Integer userId) {
		String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from pianoroom.reservation "
				+ "where user_id = :userId and start_time <= now() order by start_time desc";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		
		List<Reservation> reservations = namedParameterJdbcTemplate.query
										 (sql, params, new BeanPropertyRowMapper<>(Reservation.class));
		reservations.forEach(this::enrichWithDetails);
		return reservations;
	}
	
	/**
	 * 查詢所有預約 (後臺用)。
	 * 
	 * @return List<Reservation> 所有預約列表
	 */
	@Override
	public List<Reservation> findAllReservations() {
		String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from pianoroom.reservation";
		List<Reservation> reservations = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Reservation.class));
		reservations.forEach(this::enrichWithDetails);
		return reservations;
	}

	
	/**
	 * 為預約注入詳細的使用者及琴房資料。
	 * 
	 * @param reservation Reservation 欲注入詳細資料的預約
	 */
	private void enrichWithDetails(Reservation reservation) {
		// 注入使用者資料
		userDao.getUserById(reservation.getUserId()).ifPresent(reservation::setUser);
		// 注入琴房資料
		roomDao.getRoomById(reservation.getRoomId()).ifPresent(reservation::setRoom);
	}
	
}
