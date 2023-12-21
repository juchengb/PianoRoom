package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.User;

public interface UserDao {
	int addUser(User user);
	int updateUserById(Integer id,User user);
	Optional<User> getUserById(Integer id);
	Optional<User> getUserByEmail(String email);
	List<User> findAllUsers();

//	int deleteUserById(Integer id);
}
