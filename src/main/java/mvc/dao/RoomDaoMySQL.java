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

import mvc.entity.Room;

@Repository
public class RoomDaoMySQL implements RoomDao{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Optional<Room> getRoomById(Integer id) {
		try {
			String sql = "select id, name, dist, type, latitude, longtitude from pianoroom.room where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Room.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Room> getCurrentUsageById(Integer id) {
		
		return Optional.empty();
	}

	@Override
	public List<Room> findRoomsByDist(String dist) {
		String sql = "select id, name, dist, type, latitude, longtitude from pianoroom.room where dist = :dist";
		Map<String, Object> params = new HashMap<>();
		params.put("dist", dist);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

	@Override
	public List<Room> findRoomsByType(String type) {
		String sql = "select id, name, dist, type, latitude, longtitude from pianoroom.room where type = :type";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Room.class));
	}

	@Override
	public List<Room> findAllRooms() {
		String sql = "select id, name, dist, type, latitude, longtitude from pianoroom.room order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Room.class));
	}

}
