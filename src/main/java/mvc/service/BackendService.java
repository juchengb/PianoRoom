package mvc.service;

import mvc.model.dto.EditRoom;
import mvc.model.po.Room;

/**
 * BackendService 提供後臺管理相關功能的介面。
 */
public interface BackendService {
	
	/**
	 * 將 EditRoom 轉換為 Room 
	 * 
	 * @param addRoom EditRoom
	 * @return Room
	 */
	public Room convertToRoomEntity(EditRoom addRoom);
	
	/**
	 * 新增琴房營業時間
	 * 
	 * @param addRoom EditRoom
	 * @param roomEntity 新增的琴房實體
	 * @return 新增營業時間的筆數
	 */
	public int addBusinessHours(EditRoom addRoom, Room roomEntity);
	
	/**
	 * 取得星期字串
	 * 
	 * @param dayIndex 星期索引
	 * @return 星期字串
	 * @throws IllegalArgumentException 索引不在合法範圍內的例外
	 */
	public String getDayOfWeek(int dayIndex);
	
}
