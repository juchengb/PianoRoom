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

import mvc.entity.Reservation;

@Repository
public class ReservationDaoMySQL implements ReservationDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public int addReservation(Reservation reservation) {
		String sql = "insert into pianoroom.reservation(user_id, room_id, start_time, end_time) "
				+ "values(:user_id, :room_id, :startTime, :endTime)";
		Map<String, Object> params = new HashMap<>();
		params.put("user_id", reservation.getUserId());
		params.put("room_id", reservation.getRoomId());
		params.put("startTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("endTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public Optional<Reservation> getLatestReservationByUserId(Integer userId) {
		try {
			String sql = "select id, user_id, room_id, start_time, end_time from pianoroom.reservation "
					+ "where user_id = :userId and end_time >= now() order by start_time limit 1";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
			
	}

	@Override
	public Optional<Reservation> getUserMonthlyCountsByUserId(Integer userId) {
		try {
			String sql = "select user_id, count(id) from pianoroom.reservation where user_id = :userId and "
					+ "year(start_time) = year(curdate()) and month(start_time) = month(curdate()) group by user_id";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}

	@Override
	public Optional<Reservation> getUserMonthlyHoursByUserId(Integer userId) {
		try {
			String sql = "select user_id, sum(timestampdiff(hour,checkin, checkout)) from pianoroom.reservation "
					+ "where user_id = :userId and year(checkin) = year(curdate()) and "
					+ "month(checkin) = month(curdate()) group by user_id";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Reservation.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}

	@Override
	public int getCurrentDayCheckinByUserId(Integer userId) {
		String sql = "select count(checkin) from pianoroom.reservation where user_id = :userId and date(checkin) = curdate()";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return namedParameterJdbcTemplate.queryForObject(sql, params, int.class);
	}
	
	public List<Reservation> findFutureResrvationsByUserId(Integer userId) {
		String sql = "select id, user_id, room_id, start_time, end_time from pianoroom.reservation "
				+ "where user_id = :userId and start_time > now()";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Reservation.class));
	}

	@Override
	public List<Reservation> findPastResrvationsByUserId(Integer userId) {
		String sql = "select id, user_id, room_id, start_time, end_time from pianoroom.reservation "
				+ "where user_id = :userId and start_time <= now()";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Reservation.class));
	}

	@Override
	public List<Reservation> findAllUserMonthlyhours() {
		String sql = "select user_id, sum(timestampdiff(hour,checkin, checkout)) from pianoroom.reservation "
				+ "where year(checkin) = year(curdate()) and month(checkin) = month(curdate()) group by user_id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Reservation.class));
	}

	@Override
	public int updateReservationById(Integer id, Reservation reservation) {
		String sql = "update pianoroom.reservation set room_id = :roomId, start_time = :startTime end_time = :endTime where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", reservation.getRoomId());
		params.put("startTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("endTime", new java.sql.Timestamp(reservation.getStartTime().getTime()));
		params.put("roomId", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public int updateCheckinById(Integer id, Date checkin) {
		String sql = "update pianoroom.reservation set checkin = :checkin where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkin", new java.sql.Timestamp(checkin.getTime()));
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public int updateCheckoutById(Integer id, Date checkout) {
		String sql = "update pianoroom.reservation set checkout = :checkout where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("checkout", new java.sql.Timestamp(checkout.getTime()));
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public int deleteReservationById(Integer id) {
		String sql = "delete from pianoroom.reservation where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

}
