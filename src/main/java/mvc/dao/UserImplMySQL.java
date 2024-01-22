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
	/**
	 * 新增使用者 (註冊用)。
	 *
	 * @param user User 欲新增的使用者
	 * @return int 新增使用者的筆數
	 */
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
	
	/**
	 * 新增使用者並包含頭像資訊 (後臺用)。
	 *
	 * @param user User 欲新增的使用者
	 * @return int 新增使用者的筆數
	 */
	@Override
	public int addUserWithAvator(User user) {
		String sql = "insert into pianoroom.user(name, email, password, major_id, level, avator) "
				+ "values(:name, :email, :password, :majorId, :level, :avator)";
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("majorId", user.getMajorId());
		params.put("level", user.getLevel());
		params.put("avator", user.getAvator());
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	修改
	/**
	 * 根據使用者 ID 修改使用者。
	 *
	 * @param id Integer 使用者 ID
	 * @param user User 修改後的使用者
	 * @return int 修改使用者的筆數
	 */
	@Override
	public int updateUserById(Integer id, User user) {
		String sql = "update pianoroom.user set name = :name, email = :email, password = :password, major_id = :majorId, "
				+ "level = :level, avator = :avator where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		params.put("password", user.getPassword());
		params.put("majorId", user.getMajorId());
		params.put("level", user.getLevel());
		params.put("avator", user.getAvator());
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	/**
	 * 根據使用者 ID 更新使用者密碼。
	 *
	 * @param id Integer 使用者 ID
	 * @param newPassword String 修改後的密碼
	 * @return int 修改使用者密碼的筆數
	 */
	@Override
	public int updateUserPasswordById(Integer id, String newPassword) {
		String sql = "update pianoroom.user set password = :password where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("password", newPassword);
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	/**
	 * 根據使用者 Email 修改密碼 (使用者忘記密碼用)
	 *
	 * @param email String 使用者 Email
	 * @param newPassword String 修改後的密碼
	 * @return int 修改使用者密碼的筆數
	 */
	@Override
	public int updateUserPasswordByEmail(String email, String newPassword) {
		String sql = "update pianoroom.user set password = :password where email = :email";
		Map<String, Object> params = new HashMap<>();
		params.put("password", newPassword);
		params.put("email", email);
		return namedParameterJdbcTemplate.update(sql, params);
	}

	
//	查詢 (單筆)
	/**
	 * 根據使用者 ID 查詢使用者。
	 *
	 * @param id Integer 使用者 ID
	 * @return Optional<User> 使用者 (Optional)
	 */
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
	
	/**
	 * 根據Email查詢使用者。
	 *
	 * @param email String 使用者 Email
	 * @return Optional<User> 使用者 (Optional)
	 */
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
	
	/**
	 * 根據使用者 ID 查詢使用者當月的數據，包含預約次數、練習分鐘數、排名 (前臺用)。
	 *
	 * @param userId Integer 使用者 ID
	 * @return Optional<UserMonthlyDatas> 使用者當月數據 (Optional)
	 */
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
	

//	查詢 (多筆)
	/**
	 * 查詢所有使用者當月的數據 (前臺排行榜用)。
	 *
	 * @return List<RankingUser> 所有使用者當月的數據列表
	 */
	@Override
	public List<RankingUser> findAllUsersMonthlyDatas() {
		String sql = "select user_id, name, major_id, minutes, ranking, avator from pianoroom.monthlydatasview order by ranking";
		List<RankingUser> rankingUsers = namedParameterJdbcTemplate.query
										 (sql, new BeanPropertyRowMapper<>(RankingUser.class));
		rankingUsers.forEach(
					 rankingUser -> getMajorById(rankingUser.getMajorId()).ifPresent(rankingUser::setMajor));
		return rankingUsers;
	}
	
	/**
	 * 查詢所有使用者。
	 *
	 * @return List<User> 所有使用者列表
	 */
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
	/**
	 * 新增主修 (後臺用)。
	 *
	 * @param major Major 欲新增的主修
	 * @return int 新增主修的筆數
	 */
	@Override
	public int addMajor(Major major) {
		String sql = "insert into pianoroom.major(major) values(:major)";
		Map<String, Object> params = new HashMap<>();
		params.put("major", major.getMajor());
		return namedParameterJdbcTemplate.update(sql, params);
	}
	
	
//	修改
	/**
	 * 根據主修 ID 修改主修 (後臺用)。
	 *
	 * @param id Integer 主修ID
	 * @param major Major 新的主修信息
	 * @return int 更新操作影響的行數
	 */
	@Override
	public int updateMajorById(Integer id, Major major) {
		String sql = "update pianoroom.major set major = :major where id = :id";
		Map<String, Object> params = new HashMap<>();
		params.put("major", major.getMajor());
		params.put("id", id);
		return namedParameterJdbcTemplate.update(sql, params);
	}	
	
	
//	查詢 (單筆)
	/**
	 * 根據主修ID查詢主修。
	 *
	 * @param majorId Integer 主修 ID
	 * @return Optional<Major> 主修 (Optional)
	 */
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
	
	
//	查詢 (多筆)
	/**
	 * 查詢所有主修。
	 *
	 * @return List<Major> 所有主修列表
	 */
	@Override
	public List<Major> findAllMajors() {
		String sql = "select id, major from pianoroom.major order by id";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Major.class));
	}
	
}
