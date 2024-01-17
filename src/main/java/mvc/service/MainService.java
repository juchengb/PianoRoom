package mvc.service;

import java.util.List;

import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

/**
 * MainService 提供使用者首頁相關功能的介面。
 */
public interface MainService {
	
	/**
	 * 獲得使用者下一筆預約資訊。
	 * 
	 * @param user 目標使用者
	 * @return 預約資訊的列表
	 */
	public UserMonthlyDatas userMonthlyDatas(User user);
	
	/**
	 * 獲得使用者的每月數據。
	 * 
	 * @param user 目標使用者
	 * @return 使用者每月數據的封裝物件
	 */
	public List<Object> nextReservation(User user);
	
}
