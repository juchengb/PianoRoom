package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.Room;


public interface RoomDao {
	int addRoom(Room room);
	int updateRoomById(Integer id,Room room);
	int deleteRoomById(Integer id);
	Optional<Room> getRoomById(Integer id);
	List<Room> findAllRooms();
}
