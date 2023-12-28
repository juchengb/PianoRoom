package mvc.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mvc.bean.RoomBusinessHours;
import mvc.bean.RoomStatus;
import mvc.entity.Room;

@Repository
public class RoomDaoMySQL implements RoomDao{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Optional<Room> getRoomById(Integer id) {
		try {
			String sql = "select id, name, dist, type, latitude, longitude from pianoroom.room where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Room.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

//	@Override
//	public String getCurrentUsageById(Integer id) {
//		String sql = "select room_status from pianoroom.currentroomusagesview where id = :id";
//		Map<String, Object> params = new HashMap<>();
//		params.put("id", id);
//		return namedParameterJdbcTemplate.queryForObject
//				(sql, params, new BeanPropertyRowMapper<>(String.class));
//	}
	
	@Override
	public Optional<RoomBusinessHours> getCurdateBusinessHourById(Integer roomId) {
		try {
			String sql = "select id, day_of_week, opening_time, closing_time from pianoroom.curdatebusinesshoursview where id = :roomId";
			Map<String, Object> params = new HashMap<>();
			params.put("roomId", roomId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(RoomBusinessHours.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
		
	}

	@Override
	public List<Room> findRoomsByDist(String dist) {
		String sql = "select id, name, dist, type, latitude, longitude from pianoroom.room where dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("dist", dist);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

	@Override
	public List<Room> findRoomsByType(String type) {
		String sql = "select id, name, dist, type, latitude, longitude from pianoroom.room where type = :type";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}
	
	@Override
	public List<RoomStatus> findRoomsCurrentStatus() {
		String sql = "select id, name, dist, type, latitude, longitude, status from pianoroom.currentroomstatusview";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RoomStatus.class));
	}

	@Override
	public List<Room> findAllRooms() {
		String sql = "select id, name, dist, type, latitude, longitude from pianoroom.room order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Room.class));
	}



}
