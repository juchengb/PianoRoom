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

/**
 * RoomDaoMySQL 實作琴房相關數據的 SQL 。
 */
@Repository
public class RoomDaoMySQL implements RoomDao{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
//	琴房-Room
//	新增
	/**
     * 新增琴房 (後臺用)。
     *
     * @param room Room 欲新增的琴房
     * @return int 新增琴房的筆數
     */
	@Override
	public int addRoom(Room room) {
		String sql = "insert into pianoroom.room(name, dist, type, latitude, longitude, image) "
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
	
//	修改
	/**
     * 根據琴房 ID 修改琴房 (後臺用)。
     *
     * @param id Integer 欲修改的琴房 ID
     * @param room Room 修改後的琴房
     * @return int 修改琴房的筆數
     */
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
	
	
//	查詢 (單筆)
	/**
     * 根據琴房 ID 查詢琴房。
     *
     * @param id Integer 欲查詢的琴房 ID
     * @return Optional<Room> 琴房 (Optional)
     */
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
	
	/**
     * 根據琴房名稱和校區查詢琴房 ID。
     *
     * @param name String 琴房名稱
     * @param dist String 琴房校區
     * @return Integer 琴房 ID
     */
	@Override
	public Integer getRoomIdByNameAndDist(String name, String dist) {
		String sql = "select id from pianoroom.room where name = :name and dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("dist", dist);
		return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
	}

	
//	查詢 (多筆)
	/**
     * 根據校區查詢琴房列表。
     *
     * @param dist String 琴房校區
     * @return List<Room> 琴房列表
     */
	@Override
	public List<Room> findRoomsByDist(String dist) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("dist", dist);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

	/**
     * 根據琴房類型查詢琴房列表。
     *
     * @param type String 琴房類型
     * @return List<Room> 琴房列表
     */
	@Override
	public List<Room> findRoomsByType(String type) {
		String sql = "select id, name, dist, type, latitude, longitude, image from pianoroom.room where type = :type";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}
	
	/**
     * 查詢所有琴房現在的使用狀況。
     *
     * @return List<RoomStatus> 琴房使用狀況列表
     */
	@Override
	public List<RoomStatus> findRoomsCurrentStatus() {
		String sql = "select id, name, dist, type, latitude, longitude, status from pianoroom.currentroomstatusview";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RoomStatus.class));
	}
	
	/**
     * 查詢所有琴房 (前臺預約用)。
     *
     * @return List<ReserveRoom> 琴房預約資料列表
     */
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

	/**
     * 查詢所有琴房。
     *
     * @return List<Room> 所有琴房列表
     */
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
//	新增
	/**
     * 新增琴房的營業時間 (後臺用)。
     *
     * @param roomId Integer 欲新增營業時間的琴房 ID
     * @param dayOfWeek String 星期
     * @param openingTime LocalTime 營業開始時間
     * @param closingTime LocalTime 營業結束時間
     * @return int 新增營業時間的筆數
     */
	@Override
	public int addBusinessHourByIdAndDayOfWeek(Integer roomId, String dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
		String sql = "insert into pianoroom.business_hour(room_id, day_of_Week, opening_time, closing_time) "
				+ "values(:roomId, :dayOfWeek, :openingTime, :closingTime)";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", roomId);
		params.put("dayOfWeek", dayOfWeek);
		params.put("openingTime", openingTime);
		params.put("closingTime", closingTime);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	修改
	/**
	 * 修改琴房指定星期的營業時間 (後臺用)。
	 *
	 * @param roomId Integer 欲修改營業時間的琴房 ID
	 * @param dayOfWeek String 星期
	 * @param openingTime LocalTime 欲更新的營業開始時間
	 * @param closingTime LocalTime 欲更新的營業結束時間
	 * @return int 修改營業時間的筆數
	 */
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
	
	
//	查詢 (單筆)
	/**
	 * 根據琴房 ID 查詢該琴房當天的營業時間。
	 *
	 * @param roomId Integer 欲查詢的琴房 ID
	 * @return Optional<BusinessHour> 該琴房當天的營業時間 (Optional)
	 */
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
	
	
//	查詢 (多筆)
	/**
	 * 根據琴房 ID 查詢該琴房每天的營業時間列表。
	 *
	 * @param roomId Integer 欲查詢的琴房ID
	 * @return List<BusinessHour> 該琴房每天的營業時間列表
	 */
	@Override
	public List<BusinessHour> getBusinessHoursByRoomId(Integer roomId){
		String sql = "select room_id, day_of_week, opening_time, closing_time from pianoroom.business_hour "
				+ "where room_id= :roomId";
		Map<String, Object> params = new HashMap<>();
		params.put("roomId", roomId);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(BusinessHour.class));
	}
	
}
