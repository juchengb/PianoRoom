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
//	根據預約ID更新預約 (後臺用)
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
	
//	根據預約ID更新簽到 (前臺簽到打卡用)
	@Override
	public int updateCheckinById(Integer id, Date checkin) {
		String sql = "update pianoroom.reservation set checkin = :checkin where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkin", new java.sql.Timestamp(checkin.getTime()));
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
//	根據預約ID更新簽退 (前臺簽退打卡用)
	@Override
	public int updateCheckoutById(Integer id, Date checkout) {
		String sql = "update pianoroom.reservation set checkout = :checkout where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkout", new java.sql.Timestamp(checkout.getTime()));
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	刪除
	@Override
	public int deleteReservationById(Integer id) {
		String sql = "delete from pianoroom.reservation where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	查詢
//  根據預約ID查詢預約
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
	
//  根據room id 和 start time 查詢預約
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

//  查詢使用者下一個預約 (前臺打卡用)
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

//	
	@Override
	public int getCurrentDayCheckinByUserId(Integer userId) {
		String sql = "select count(checkin) from pianoroom.reservation where user_id = :userId and date(checkin) = curdate()";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return namedParameterJdbcTemplate.queryForObject(sql, params, int.class);
	}
	
//	查詢使用者每天練習分鐘數 (前臺 MyPractice 圖表使用)
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
	
//	查詢使用者未來預約 (前臺 MyReservation 用)
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
	
//	查詢使用者歷史預約 (前臺 MyReservation 用)
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
	
//	查詢所有預約 (後臺用)
	@Override
	public List<Reservation> findAllReservations() {
		String sql = "select id, user_id, room_id, start_time, end_time, checkin, checkout from pianoroom.reservation";
		List<Reservation> reservations = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Reservation.class));
		reservations.forEach(this::enrichWithDetails);
		return reservations;
	}

	
	
	private void enrichWithDetails(Reservation reservation) {
		// 注入 user
		userDao.getUserById(reservation.getUserId()).ifPresent(reservation::setUser);
		// 注入 room
		roomDao.getRoomById(reservation.getRoomId()).ifPresent(reservation::setRoom);
	}
	
}
