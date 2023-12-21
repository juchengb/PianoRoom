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

import mvc.bean.User;

@Repository
public class UserImplMySQL implements UserDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	@Override
	public int addUser(User user) {
		String sql = "insert into pianoroom.user(name, email, password, major_id, level) "
				+ "values(:name, :email, :password, :majorId, :level)";
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("majorId", user.getMajorId());
		params.put("level", user.getLevel());
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public int updateUserById(Integer id, User newUser) {
		String sql = "update pianoroom.user set name = :name, email = :email," 
				+ "password = :password, major_id = :majorId where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("name", newUser.getName());
		params.put("email", newUser.getEmail());
		params.put("password", newUser.getPassword());
		params.put("majorId", newUser.getMajorId());
		params.put("level", newUser.getLevel());
		return namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public Optional<User> getUserById(Integer id) {
		String sql = "select id, name, email, password, major_id, level "
				+ "from pianoroom.user where id= :id";
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		
		try {
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(User.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
		
		
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		String sql = "select id, name, email, password, major_id, level "
				+ "from pianoroom.user where email= :email";
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		
		try {
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(User.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
		
	}


	@Override
	public List<User> findAllUsers() {
		String sql = "select id, name, email, password, major_id, level "
				+ "from pianoroom.user order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

}
