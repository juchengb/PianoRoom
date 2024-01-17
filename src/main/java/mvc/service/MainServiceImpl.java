package mvc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.dao.UserDao;
import mvc.model.po.Reservation;
import mvc.model.po.Room;
import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

/**
 * MainServiceImpl 實作 MainService 的使用者首頁相關功能。
 */
@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ReservationDao reservationDao;
	
	@Autowired
	RoomDao roomDao;
	
	// 定義日期格式
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	
	/**
	 * 獲得使用者下一筆預約資訊。
	 * 
	 * @param user 目標使用者
	 * @return 預約資訊的列表
	 */
	@Override
	public List<Object> nextReservation(User user) {
		 Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
		 
		 List<Object> list = new ArrayList<>();
		 if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            Date startTime = reservation.getStartTime();
            Date endTime = reservation.getEndTime();
            
            // 檢查按鈕狀態
            Date now = new Date();
            int btnStatus = 0;
            String btnWord = "";
            
            if (now.before(startTime)) {
                btnStatus = 0;
                btnWord = "未開放";
            } else if (now.after(startTime) && reservation.getCheckin() == null) {
                btnStatus = 1;
                btnWord = "簽到";
            } else if (now.after(startTime) && now.before(endTime) && reservation.getCheckin() != null) {
                btnStatus = 2;
                btnWord = "簽退";
            }
            String next = String.format("%s %s %s - %s",
                    room.getDist(), room.getType(), room.getName(), sdf.format(startTime), sdf.format(endTime));
            list.add(next);
            list.add(btnStatus);
	        list.add(btnWord);
		}
		return list;
	}
	
	/**
	 * 獲得使用者的每月數據。
	 * 
	 * @param user 目標使用者
	 * @return 使用者每月數據的封裝物件
	 */
	@Override
	public UserMonthlyDatas userMonthlyDatas(User user) {
		Optional<UserMonthlyDatas> monthlyOpt = userDao.getUserMonthlyDatasByUserId(user.getId());
		UserMonthlyDatas monthly = new UserMonthlyDatas().builder().counts(0).minutes(0).build();
        if (monthlyOpt.isPresent()) {
            monthly = monthlyOpt.get();
        }
		return monthly;
	}
	
}
