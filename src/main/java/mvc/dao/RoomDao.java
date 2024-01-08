package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.ReserveRoom;
import mvc.bean.RoomStatus;
import mvc.entity.BusinessHour;
import mvc.entity.Room;


public interface RoomDao {
//	琴房-Room
//	查詢
	Optional<Room> getRoomById(Integer id);
//	String getCurrentUsageById(Integer id);
	
	List<Room> findRoomsByDist(String dist);
	List<Room> findRoomsByType(String type);
	List<RoomStatus> findRoomsCurrentStatus();
	List<Room> findAllRooms();
	List<ReserveRoom> findAllRoomsToReserve();
	
//	後台
//	int addRoom(Room room);
	int updateRoomById(Integer id,Room room);
//	int deleteRoomById(Integer id);

//	營業時間-BusinessHour
//	Optional<BusinessHour> getBusinessHourByRoomIdAndDayOfWeek(Integer roomId, String dayOfWeek);
	Optional<BusinessHour> getCurdateBusinessHourById(Integer id); // AndByCurrentDay
	List<BusinessHour> getBusinessHoursByRoomId(Integer roomId);
//	List<BusinessHour> findAllBusinessHours();
	int updateBusinessHourById(Integer id, List<BusinessHour> businessHours);

	
}
