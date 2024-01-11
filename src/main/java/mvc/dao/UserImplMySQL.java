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

import mvc.model.dto.RankingUser;
import mvc.model.po.Major;
import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

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

	
//	修改
//	根據ID更新使用者
	@Override
	public int updateUserById(Integer id, User user) {
		String sql = "update pianoroom.user set name = :name, email = :email, major_id = :majorId, "
				+ "avator = :avator where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("majorId", user.getMajorId());
		params.put("avator", user.getAvator());
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
//	根據ID更新密碼
	@Override
	public int updateUserPasswordById(Integer id, String newPassword) {
		String sql = "update pianoroom.user set password = :password where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("password", newPassword);
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

//	根據Email更新密碼 (使用者忘記密碼用，系統設定臨時密碼用)
	@Override
	public int updateUserPasswordByEmail(String email, String newPassword) {
		String sql = "update pianoroom.user set password = :password where email = :email";
		Map<String, Object> params = new HashMap<>();
		params.put("password", newPassword);
		params.put("email", email);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	//
	@Override
	public int updateUserLevelById(Integer id, Integer level) {
		String sql = "update pianoroom.user set level = :level where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("level", level);
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	查詢
//	根據ID查詢使用者
	@Override
	public Optional<User> getUserById(Integer id) {
		try {
			String sql = "select id, name, email, password, major_id, level, avator "
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
	
//	根據Email查詢使用者
	@Override
	public Optional<User> getUserByEmail(String email) {
		try {
			String sql = "select id, name, email, password, major_id, level, avator "
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
	
//	查詢所有使用者該月數據 (前臺排行榜用)
	@Override
	public List<RankingUser> findAllUsersMonthlyDatas() {
		String sql = "select user_id, name, major_id, minutes, ranking, avator from pianoroom.monthlydatasview order by ranking";
		List<RankingUser> rankingUsers = namedParameterJdbcTemplate.query
										 (sql, new BeanPropertyRowMapper<>(RankingUser.class));
		rankingUsers.forEach(
					 rankingUser -> getMajorById(rankingUser.getMajorId()).ifPresent(rankingUser::setMajor));
		return rankingUsers;
	}
	
//	根據ID查詢使用者該月數據-預約次數、練習分鐘數、排名 (前臺用)
	@Override
	public Optional<UserMonthlyDatas> getUserMonthlyDatasByUserId(Integer userId) {
		try {
			String sql = "select user_id, counts, minutes, ranking from pianoroom.monthlydatasview where user_id = :userId";
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(UserMonthlyDatas.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}
	
//	查詢所有使用者
	@Override
	public List<User> findAllUsers() {
		String sql = "select id, name, email, password, major_id, level, avator from pianoroom.user order by id";
		List<User> users = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
		users.forEach(
				user -> getMajorById(user.getMajorId()).ifPresent(user::setMajor));
		return users;
	}


//  -----------------------------------------------------------------------------------------------------
//	主修-Major
//	新增
	@Override
	public int addMajor(Major major) {
		String sql = "insert into pianoroom.major(major) values(:major)";
		Map<String, Object> params = new HashMap<>();
		params.put("major", major.getMajor());
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
//	查詢	
	// 根據ID查詢主修
	@Override
	public Optional<Major> getMajorById(Integer majorId) {
		try {
			String sql = "select id, major from pianoroom.major where id = :id";
			Map<String, Object> params = new HashMap<>();
			params.put("id", majorId);
			return Optional.ofNullable(
					namedParameterJdbcTemplate.queryForObject
					(sql, params, new BeanPropertyRowMapper<>(Major.class)));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
	
	// 查詢所有主修
	@Override
	public List<Major> findAllMajors() {
		String sql = "select id, major from pianoroom.major order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Major.class));
	}







}
