package mvc.dao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mvc.model.dto.ReserveRoom;
import mvc.model.dto.RoomStatus;
import mvc.model.po.BusinessHour;
import mvc.model.po.Room;

@Repository
public class RoomDaoMySQL implements RoomDao{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
//	琴房-Room
//	新增 (後臺用)
	@Override
	public int addRoom(Room room) {
		String sql = "isnert into pianoroom.room(name, dist, type, latitude, longitude, image) "
				+ "values(:name, :dist, :type, :latitude, :longitude, :image)";
		Map<String, Object> params = new HashMap<>();
		params.put("name", room.getName());
		params.put("dist", room.getDist());
		params.put("type", room.getType());
		params.put("latitude", room.getLatitude());
		params.put("longitude", room.getLongitude());
		params.put("image", room.getImage());
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
//	修改 (後臺用)
	@Override
	public int updateRoomById(Integer id, Room room) {
		String sql = "update pianoroom.room set name = :name, dist = :dist, type = :type, latitude = :latitude, "
				+ "longitude = :longitude, image = :image where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("name", room.getName());
		params.put("dist", room.getDist());
		params.put("type", room.getType());
		params.put("latitude", room.getLatitude());
		params.put("longitude", room.getLongitude());
		params.put("image", room.getImage());
		params.put("id", id);
		
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	刪除 (後臺用) 關聯性問題
//	@Override
//	public int deleteRoomById(Integer id) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	
	
//	查詢
//	根據ID查詢琴房
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

//	根據ID查詢琴房現在使用狀況
//	@Override
//	public String getCurrentUsageById(Integer id) {
//		String sql = "select room_status from pianoroom.currentroomusagesview where id = :id";
//		Map<String, Object> params = new HashMap<>();
//		params.put("id", id);
//		return namedParameterJdbcTemplate.queryForObject
//				(sql, params, new BeanPropertyRowMapper<>(String.class));
//	}

//	根據 dist 查詢琴房
	@Override
	public List<Room> findRoomsByDist(String dist) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("dist", dist);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

//	根據 type 查詢琴房
	@Override
	public List<Room> findRoomsByType(String type) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where type = :type";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}
	
//	查詢所有琴房現在使用狀況
	@Override
	public List<RoomStatus> findRoomsCurrentStatus() {
		String sql = "select id, name, dist, type, latitude, longitude, status from pianoroom.currentroomstatusview";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RoomStatus.class));
	}
	
//	查詢所有琴房 (前臺預約用)
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

//	查詢所有琴房
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
		

	
//  -----------------------------------------------------------------------------------------------------
//	營業時間-BusinessHour
//	修改 (後臺用)
	@Override
	public int updateBusinessHourByIdAndDayOfWeek(Integer roomId, String dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
		String sql = "update pianoroom.business_hour set opening_time = :openingTime, closing_time = :closingTime "
				+ "where room_id = :roomId and day_of_week = :dayOfWeek";
		Map<String, Object> params = new HashMap<>();
		params.put("openingTime", openingTime);
		params.put("closingTime", closingTime);
		params.put("roomId", roomId);
		params.put("dayOfWeek", dayOfWeek);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	查詢
//	根據ID及星期查詢營業時間
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
	
//	根據ID查詢當天營業時間
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
	
//	根據ID查詢每天營業時間
	@Override
	public List<BusinessHour> getBusinessHoursByRoomId(Integer roomId){
		String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour "
				+ "where room_id= :roomId";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", roomId);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(BusinessHour.class));
	}

//	查詢所有營業時間
//	@Override
//	public List<BusinessHour> findAllBusinessHours(){
//		String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour";
//		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BusinessHour.class));
//	}
	
	



}
