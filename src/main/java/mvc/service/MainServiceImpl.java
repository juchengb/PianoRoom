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

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ReservationDao reservationDao;
	
	@Autowired
	RoomDao roomDao;
	
	// Define date format
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	
	
	@Override
	public List<Object> nextReservation(User user) {
		 Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
		 
		 List<Object> list = new ArrayList<>();
		 if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            Date startTime = reservation.getStartTime();
            Date endTime = reservation.getEndTime();
            
            // check button status
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
