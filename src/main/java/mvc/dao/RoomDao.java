package mvc.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import mvc.model.dto.ReserveRoom;
import mvc.model.dto.RoomStatus;
import mvc.model.po.BusinessHour;
import mvc.model.po.Room;


public interface RoomDao {
	
//	琴房-Room
//	新增 (後臺用)
	int addRoom(Room room);
	
	
//	修改 (後臺用)
	int updateRoomById(Integer id,Room room);

	
//	刪除 (後臺用)
//	int deleteRoomById(Integer id);
	
	
//	查詢
	// 根據ID查詢琴房
	Optional<Room> getRoomById(Integer id);
	// 根據ID查詢琴房現在使用狀況
//	String getCurrentUsageById(Integer id);
	// 根據 dist 查詢琴房
	List<Room> findRoomsByDist(String dist);
	// 根據 type 查詢琴房
	List<Room> findRoomsByType(String type);
	// 查詢所有琴房現在使用狀況
	List<RoomStatus> findRoomsCurrentStatus();
	// 查詢所有琴房 (前臺預約用)
	List<ReserveRoom> findAllRoomsToReserve();
	// 查詢所有琴房
	List<Room> findAllRooms();
	

	
//  -----------------------------------------------------------------------------------------------------
//	營業時間-BusinessHour
//	修改 (後臺用)
	int updateBusinessHourByIdAndDayOfWeek(Integer roomId, String dayOfWeek, LocalTime openingTime, LocalTime closingTime);
	
//	查詢
	// 根據ID及星期查詢營業時間
//	Optional<BusinessHour> getBusinessHourByRoomIdAndDayOfWeek(Integer roomId, String dayOfWeek);
	// 根據ID查詢當天營業時間
	Optional<BusinessHour> getCurdateBusinessHourById(Integer roomId); // AndByCurrentDay
	// 根據ID查詢每天營業時間
	List<BusinessHour> getBusinessHoursByRoomId(Integer roomId);
	// 查詢所有營業時間
//	List<BusinessHour> findAllBusinessHours();

	
}
