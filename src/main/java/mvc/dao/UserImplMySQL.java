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

import mvc.bean.RankingUser;
import mvc.bean.UserMonthlyDatas;
import mvc.entity.Major;
import mvc.entity.User;

@Repository
public class UserImplMySQL implements UserDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
//	使用者-User
//	新增
	@Override
	public int addUser(User user) {
		String sql = "insert into pianoroom.user(name, email, password, major_id, level) "
				+ "values(:name, :email, :password, :majorId, 2)";
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("majorId", user.getMajorId());
		return namedParameterJdbcTemplate.update(sql, params);
	}

//	查詢
	@Override
	public Optional<User> getUserById(Integer id) {
		try {
			String sql = "select id, name, email, password, major_id, level, imgurl "
					+ "from pianoroom.user where id= :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(User.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<User> getUserByEmail(String email) {
		try {
			String sql = "select id, name, email, password, major_id, level "
					+ "from pianoroom.user where email= :email";
			Map<String, Object> params = new HashMap<>();
			params.put("email", email);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(User.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<UserMonthlyDatas> getUserMonthlyDatasByUserId(Integer userId) {
		try {
			String sql = "select user_id, counts, hours, ranking from pianoroom.monthlydatasview where user_id = :userId";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(UserMonthlyDatas.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}
	
	@Override
	public List<User> findAllUsers() {
		String sql = "select id, name, email, password, major_id, level from pianoroom.user order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}
	
	@Override
	public List<RankingUser> findAllUsersMonthlyDatas() {
		String sql = "select user_id, name, major_id, hours, ranking from pianoroom.monthlydatasview order by ranking";
		List<RankingUser> rankingUsers = namedParameterJdbcTemplate.query
										 (sql, new BeanPropertyRowMapper<>(RankingUser.class));
		rankingUsers.forEach(
					 rankingUser -> getMajorById(rankingUser.getMajorId()).ifPresent(rankingUser::setMajor));
		return rankingUsers;
	}
	
//	修改
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
	public int updateUserPasswordById(Integer id, String newPassword) {
		String sql = "update pianoroom.user set password = :password where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("password", newPassword);
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

//	@Override
//	public int updateUserPasswordByEmail(String email, String newPassword) {
//		String sql = "update pianoroom.user set password = :password where email = :email";
//		Map<String, Object> params = new HashMap<>();
//		params.put("password", newPassword);
//		params.put("email", email);
//		return namedParameterJdbcTemplate.update(sql, params);
//	}


//	主修-Major
//	查詢	
	@Override
	public Optional<Major> getMajorById(Integer majorId) {
		try {
			String sql = "select id major_name from pianoroom.major where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", majorId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Major.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Major> findAllMajors() {
		String sql = "select id, major_name from pianoroom.major order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Major.class));
	}
	

}
