package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.RankingUser;
import mvc.bean.UserMonthlyDatas;
import mvc.entity.Major;
import mvc.entity.User;

public interface UserDao {
//	使用者-User
//	新增
	int addUser(User user);

//	查詢
	Optional<User> getUserById(Integer id);
	Optional<User> getUserByEmail(String email);
	Optional<UserMonthlyDatas> getUserMonthlyDatasByUserId(Integer userId);
	List<User> findAllUsers();
	List<RankingUser> findAllUsersMonthlyDatas();

//	修改
	int updateUserById(Integer id, User newUser);
	int updateUserPasswordById(Integer id, String newPassword);
//	int updateUserPasswordByEmail(String email, String newPassword);


//	主修-Major
//	查詢
	Optional<Major> getMajorById(Integer majorId);
	List<Major> findAllMajors();
	
	
//	後台
	
}
