package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.model.dto.RankingUser;
import mvc.model.po.Major;
import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

public interface UserDao {
	
//	使用者-User
//	新增
	// (註冊用)
	int addUser(User user);
	// (後臺用)
	int addUserWithAvator(User user);

//	修改
	// 根據ID更新使用者 (前臺用)
	int updateUserByIdFront(Integer id, User user);
	// 根據ID更新使用者 (後臺用)
	int updateUserByIdBack(Integer id, User user);
	// 根據ID更新密碼
	int updateUserPasswordById(Integer id, String newPassword);
	// 根據Email更新密碼 (使用者忘記密碼用)
	int updateUserPasswordByEmail(String email, String newPassword);
	// 根據ID更新使用者 level (後臺停用使用者)
	int updateUserLevelById(Integer id, Integer level);
	
//	查詢
	// 根據ID查詢使用者
	Optional<User> getUserById(Integer id);
	// 根據Email查詢使用者
	Optional<User> getUserByEmail(String email);
	// 根據ID查詢使用者該月數據-預約次數、練習分鐘數、排名 (前臺用)
	Optional<UserMonthlyDatas> getUserMonthlyDatasByUserId(Integer userId);
	// 查詢所有使用者該月數據 (前臺排行榜用)
	List<RankingUser> findAllUsersMonthlyDatas();
	// 查詢所有使用者
	List<User> findAllUsers();

//  -----------------------------------------------------------------------------------------------------
//	主修-Major
//	新增
	int addMajor(Major major);
//	修改 (後臺用)
	int updateMajorById(Integer id, Major major);
//	查詢
	// 根據ID查詢主修
	Optional<Major> getMajorById(Integer majorId);
	// 查詢所有主修
	List<Major> findAllMajors();
	
}
