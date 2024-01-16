package mvc.service;

import java.util.List;

import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

public interface MainService {
	
	public UserMonthlyDatas userMonthlyDatas(User user);
	public List<Object> nextReservation(User user);
}
