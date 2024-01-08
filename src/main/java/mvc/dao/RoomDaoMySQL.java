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

import mvc.bean.ReserveRoom;
import mvc.bean.RoomStatus;
import mvc.entity.BusinessHour;
import mvc.entity.Room;

@Repository
public class RoomDaoMySQL implements RoomDao{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Optional<Room> getRoomById(Integer id) {
		try {
			String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			Room room = namedParameterJdbcTemplate.queryForObject
						(sql, params, new BeanPropertyRowMapper<>(Room.class));
			List<BusinessHour> businessHours = getBusinessHoursByRoomId(room.getId());
	        room.setBusinessHour(businessHours);
	        return Optional.ofNullable(room);
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
	public List<Room> findRoomsByDist(String dist) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("dist", dist);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

	@Override
	public List<Room> findRoomsByType(String type) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where type = :type";
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
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room order by id";
		List<Room> rooms = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Room.class));
		for (Room room : rooms) {
	        List<BusinessHour> businessHours = getBusinessHoursByRoomId(room.getId());
	        room.setBusinessHour(businessHours);
	    }
		
		return rooms;
	}
	
	@Override
	public List<ReserveRoom> findAllRoomsToReserve() {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room order by id";
		List<ReserveRoom> reserveRooms = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ReserveRoom.class));
		for (ReserveRoom room : reserveRooms) {
	        List<BusinessHour> businessHours = getBusinessHoursByRoomId(room.getId());
	        room.setBusinessHour(businessHours);
	    }
		
		return reserveRooms;
	}
	
	// 後台
	@Override
	public int updateRoomById(Integer id, Room room) {
		String sql = "update pianoroom.room set name = :name, dist = :dist, type = :type, latitude = :latitude, "
				+ "longitude = :longitude, image = :image where id = :id";
		return 0;
	}
	
	
//	營業時間-BusinessHour
//	@Override
//	public Optional<BusinessHour> getBusinessHourByRoomIdAndDayOfWeek(Integer roomId, String dayOfWeek){
//		try {
//			String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour "
//					+ "where room_id= :roomId and day_of_week = :dayofWeek";
//			Map<String, Object> params = new HashMap<>();
//			params.put("roomId", roomId);
//			params.put("dayofWeek", dayOfWeek);			
//			return Optional.ofNullable(
//					namedParameterJdbcTemplate.queryForObject
//					(sql, params, new BeanPropertyRowMapper<>(BusinessHour.class)));
//		} catch (EmptyResultDataAccessException e) {
//			return Optional.empty();
//		}
//	}
	
	@Override
	public Optional<BusinessHour> getCurdateBusinessHourById(Integer roomId) {
		try {
			String sql = "select id, day_of_week, opening_time, closing_time from pianoroom.curdatebusinesshoursview where id = :roomId";
			Map<String, Object> params = new HashMap<>();
			params.put("roomId", roomId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(BusinessHour.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
		
	}
	
	@Override
	public List<BusinessHour> getBusinessHoursByRoomId(Integer roomId){
		String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour "
				+ "where room_id= :roomId";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", roomId);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(BusinessHour.class));
	}

	@Override
	public int updateBusinessHourById(Integer roomId, List<BusinessHour> businessHours) {
		String sql = "update pianoroom.business_hour set opening_time = :openingTime, closing_time = :closingTime "
				+ "where room_id = :roomId and day_of_week = :dayOfWeek";
		Map<String, Object> params = new HashMap<>();
		params.put("closingTime", roomId);
		
		int rowcount = 0;
		
		for (BusinessHour businessHour : businessHours) {
			params.put("dayOfWeek", businessHour.getDayOfWeek());
			params.put("openingTime", businessHour.getOpeningTime());
			params.put("closingTime", businessHour.getClosingTime());
			namedParameterJdbcTemplate.update(sql, params);
			rowcount += 1;
		}
		return rowcount;
	}
	
	
//	@Override
//	public List<BusinessHour> findAllBusinessHours(){
//		String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour";
//		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BusinessHour.class));
//	}
	
	



}
