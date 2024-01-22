package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.model.dto.RankingUser;
import mvc.model.po.Major;
import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

/**
 * UserDao 定義使用者相關數據的 SQL 。
 */
public interface UserDao {
	
//	使用者-User
//	新增
	/**
	 * 新增使用者 (註冊用)。
	 *
	 * @param user User 欲新增的使用者
	 * @return int 新增使用者的筆數
	 */
	int addUser(User user);

	/**
	 * 新增使用者並包含頭像資訊 (後臺用)。
	 *
	 * @param user User 欲新增的使用者
	 * @return int 新增使用者的筆數
	 */
	int addUserWithAvator(User user);

	
//	修改
	/**
	 * 根據使用者 ID 修改使用者。
	 *
	 * @param id Integer 使用者 ID
	 * @param user User 修改後的使用者
	 * @return int 修改使用者的筆數
	 */
	int updateUserById(Integer id, User user);

	/**
	 * 根據使用者 ID 更新使用者密碼。
	 *
	 * @param id Integer 使用者 ID
	 * @param newPassword String 修改後的密碼
	 * @return int 修改使用者密碼的筆數
	 */
	int updateUserPasswordById(Integer id, String newPassword);

	/**
	 * 根據使用者 Email 修改密碼 (使用者忘記密碼用)
	 *
	 * @param email String 使用者 Email
	 * @param newPassword String 修改後的密碼
	 * @return int 修改使用者密碼的筆數
	 */
	int updateUserPasswordByEmail(String email, String newPassword);

	
//	查詢 (單筆)
	/**
	 * 根據使用者 ID 查詢使用者。
	 *
	 * @param id Integer 使用者 ID
	 * @return Optional<User> 使用者 (Optional)
	 */
	Optional<User> getUserById(Integer id);

	/**
	 * 根據Email查詢使用者。
	 *
	 * @param email String 使用者 Email
	 * @return Optional<User> 使用者 (Optional)
	 */
	Optional<User> getUserByEmail(String email);

	/**
	 * 根據使用者 ID 查詢使用者當月的數據，包含預約次數、練習分鐘數、排名 (前臺用)。
	 *
	 * @param userId Integer 使用者 ID
	 * @return Optional<UserMonthlyDatas> 使用者當月數據 (Optional)
	 */
	Optional<UserMonthlyDatas> getUserMonthlyDatasByUserId(Integer userId);


//	查詢 (多筆)
	/**
	 * 查詢所有使用者當月的數據 (前臺排行榜用)。
	 *
	 * @return List<RankingUser> 所有使用者當月的數據列表
	 */
	List<RankingUser> findAllUsersMonthlyDatas();

	/**
	 * 查詢所有使用者。
	 *
	 * @return List<User> 所有使用者列表
	 */
	List<User> findAllUsers();

	
//  -----------------------------------------------------------------------------------------------------
//	主修-Major
//	新增
	/**
	 * 新增主修 (後臺用)。
	 *
	 * @param major Major 欲新增的主修
	 * @return int 新增主修的筆數
	 */
	int addMajor(Major major);
	
	
//	修改
	/**
	 * 根據主修 ID 修改主修 (後臺用)。
	 *
	 * @param id Integer 主修ID
	 * @param major Major 新的主修信息
	 * @return int 更新操作影響的行數
	 */
	int updateMajorById(Integer id, Major major);
	
	
//	查詢 (單筆)
	/**
	 * 根據主修ID查詢主修。
	 *
	 * @param majorId Integer 主修 ID
	 * @return Optional<Major> 主修 (Optional)
	 */
	Optional<Major> getMajorById(Integer majorId);
	

//	查詢 (多筆)
	/**
	 * 查詢所有主修。
	 *
	 * @return List<Major> 所有主修列表
	 */
	List<Major> findAllMajors();
	
}
