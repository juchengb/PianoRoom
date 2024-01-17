package mvc.service;

import java.util.List;

import mvc.model.dto.ReserveRoom;
import mvc.model.po.Reservation;
import mvc.model.po.User;

/**
 * ReserveService 提供預約相關功能的介面。
 */
public interface ReserveService {
	
	/**
	 * 獲得包含預約按鈕的琴房列表。
	 * 
	 * @return 預約按鈕信息的琴房列表
	 */
	public List<ReserveRoom> showRoomsWithButtons();

	/**
	 * 獲得預約資訊。
	 * 
	 * @param user  目標使用者
	 * @param roomId 琴房 ID
	 * @param start  開始時間字串
	 * @return Reservation
	 */
	public Reservation getReservationInfo(User user, Integer roomId, String start);
	
}
