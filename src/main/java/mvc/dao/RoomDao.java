package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.RoomStatus;
import mvc.entity.Reservation;
import mvc.entity.Room;
import mvc.entity.User;


public interface RoomDao {
//	琴房-Room
//	查詢
	Optional<Room> getRoomById(Integer id);
//	String getCurrentUsageById(Integer id);
	Optional<Room> getBusinessHourById(Integer id); // AndByCurrentDay
	List<Room> findRoomsByDist(String dist);
	List<Room> findRoomsByType(String type);
	List<RoomStatus> findRoomsCurrentStatus();
	List<Room> findAllRooms();

	
//	後台
//	int addRoom(Room room);
//	int updateRoomById(Integer id,Room room);
//	int deleteRoomById(Integer id);
	
}
