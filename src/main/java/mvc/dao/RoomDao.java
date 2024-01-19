package mvc.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import mvc.model.dto.ReserveRoom;
import mvc.model.dto.RoomStatus;
import mvc.model.po.BusinessHour;
import mvc.model.po.Room;

/**
 * RoomDao 定義琴房相關數據的 SQL 。
 */
public interface RoomDao {
	
//	琴房-Room
//	新增
	/**
     * 新增琴房 (後臺用)。
     *
     * @param room Room 欲新增的琴房
     * @return int 新增琴房的筆數
     */
	int addRoom(Room room);
	
	
//	修改
	/**
     * 根據琴房 ID 修改琴房 (後臺用)。
     *
     * @param id Integer 欲修改的琴房 ID
     * @param room Room 修改後的琴房
     * @return int 修改琴房的筆數
     */
	int updateRoomById(Integer id,Room room);
	
	
//	查詢 (單筆)
	/**
     * 根據琴房 ID 查詢琴房。
     *
     * @param id Integer 欲查詢的琴房 ID
     * @return Optional<Room> 琴房 (Optional)
     */
	Optional<Room> getRoomById(Integer id);
	
	/**
     * 根據琴房名稱和校區查詢琴房 ID。
     *
     * @param name String 琴房名稱
     * @param dist String 琴房校區
     * @return Integer 琴房 ID
     */
	Integer getRoomIdByNameAndDist(String name, String dist);

	
//	查詢 (多筆)
	/**
     * 查詢所有琴房校區。
     *
     * @return <String> 不重複的校區列表
     */
	List<String> findAllRoomDists();
	
	/**
     * 查詢所有琴房類型。
     *
     * @return <String> 不重複的類型列表
     */
	List<String> findAllRoomTypes();
	
	/**
     * 根據校區查詢琴房列表。
     *
     * @param dist String 琴房校區
     * @return List<ReserveRoom> 琴房列表
     */
	List<ReserveRoom> findRoomsByDistToReserve(String dist);
	
	/**
     * 根據琴房類型查詢琴房列表。
     *
     * 
     * @param type String 琴房類型
     * @return List<ReserveRoom> 琴房列表
     */
	List<ReserveRoom> findRoomsByTypeToReserve(String type);
	
	/**
     * 根據琴房校區和類型查詢琴房列表。
     * 
     * @param dist String 琴房校區
     * @param type String 琴房類型
     * @return List<ReserveRoom> 琴房列表
     */
	List<ReserveRoom> findRoomsByDistAndTypeToReserve(String dist, String type);
	
	/**
     * 查詢所有琴房現在的使用狀況。
     *
     * @return List<RoomStatus> 琴房使用狀況列表
     */
	List<RoomStatus> findRoomsCurrentStatus();
	
	/**
     * 查詢所有琴房 (前臺預約用)。
     *
     * @return List<ReserveRoom> 琴房預約資料列表
     */
	List<ReserveRoom> findAllRoomsToReserve();
	
	/**
     * 查詢所有琴房。
     *
     * @return List<Room> 所有琴房列表
     */
	List<Room> findAllRooms();
	
	
//  -----------------------------------------------------------------------------------------------------
//	營業時間-BusinessHour
//	新增
	/**
     * 新增琴房的營業時間 (後臺用)。
     *
     * @param roomId Integer 欲新增營業時間的琴房 ID
     * @param dayOfWeek String 星期
     * @param openingTime LocalTime 營業開始時間
     * @param closingTime LocalTime 營業結束時間
     * @return int 新增營業時間的筆數
     */
	int addBusinessHourByIdAndDayOfWeek(Integer roomId, String dayOfWeek, LocalTime openingTime, LocalTime closingTime);
	
	
//	修改
	/**
	 * 修改琴房指定星期的營業時間 (後臺用)。
	 *
	 * @param roomId Integer 欲修改營業時間的琴房 ID
	 * @param dayOfWeek String 星期
	 * @param openingTime LocalTime 欲更新的營業開始時間
	 * @param closingTime LocalTime 欲更新的營業結束時間
	 * @return int 修改營業時間的筆數
	 */
	int updateBusinessHourByIdAndDayOfWeek(Integer roomId, String dayOfWeek, LocalTime openingTime, LocalTime closingTime);
	
	
//	查詢 (單筆)
	/**
	 * 根據琴房 ID 查詢該琴房當天的營業時間。
	 *
	 * @param roomId Integer 欲查詢的琴房 ID
	 * @return Optional<BusinessHour> 該琴房當天的營業時間 (Optional)
	 */
	Optional<BusinessHour> getCurdateBusinessHourById(Integer roomId);

	
//	查詢 (多筆)
	/**
	 * 根據琴房 ID 查詢該琴房每天的營業時間列表。
	 *
	 * @param roomId Integer 欲查詢的琴房ID
	 * @return List<BusinessHour> 該琴房每天的營業時間列表
	 */
	List<BusinessHour> getBusinessHoursByRoomId(Integer roomId);
	
}
