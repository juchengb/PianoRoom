package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.entity.Room;
import mvc.entity.User;


public interface RoomDao {
//	琴房-Room
//	查詢
	Optional<Room> getRoomById(Integer id);
	Optional<Room> getCurrentUsageById(Integer id);
	List<Room> findRoomsByDist(String dist);
	List<Room> findRoomsByType(String type);
	List<Room> findAllRooms();

	
//	後台
//	int addRoom(Room room);
//	int updateRoomById(Integer id,Room room);
//	int deleteRoomById(Integer id);
	
}
