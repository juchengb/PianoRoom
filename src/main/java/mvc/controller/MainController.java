package mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.bean.RoomStatus;
import mvc.bean.UserMonthlyDatas;
import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.dao.UserDao;
import mvc.entity.Reservation;
import mvc.entity.Room;
import mvc.entity.User;

@Controller
@RequestMapping("/main")
public class MainController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private UserDao userDao;
	
	// Define date format
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	
	@GetMapping("")
    public String mainPage(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");

        showNextReservation(user, model);
        showUserMonthlyDatas(user, model);
        showRoomStatus(model);

        return "frontend/main";
    }

    private void showNextReservation(User user, Model model) {
        Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            String next = String.format("%s %s %s %n %s",
                    room.getDist(), room.getType(), room.getName(), sdf.format(reservation.getStartTime()));
            model.addAttribute("nextReservation", next);
        } else {
            model.addAttribute("nextReservation", "查無預約，趕快去預約琴房");
        }
    }
       
    private void showUserMonthlyDatas(User user, Model model) {
        Optional<UserMonthlyDatas> monthlyOpt = userDao.getUserMonthlyDatasByUserId(user.getId());
        if (monthlyOpt.isPresent()) {
            UserMonthlyDatas monthly = monthlyOpt.get();
            model.addAttribute("monthly", monthly);
        } else {
            model.addAttribute("monthly.counts", 0);
            model.addAttribute("monthly.minutes", 0);
            model.addAttribute("monthly.ranking", 0);
        }
    }

    private void showRoomStatus(Model model) {
        model.addAttribute("updateTime", sdf.format(new Date()));
        List<RoomStatus> roomStatusList = roomDao.findRoomsCurrentStatus();
        model.addAttribute("roomStatusList", roomStatusList);
    }
    
    //--------------------------------------------------------------------------------------------
    
    @GetMapping("/location")
    @ResponseBody
    public Map<String, Double> getNextReservationLocation(HttpSession session) {
    	User user = (User)session.getAttribute("user");
        Map<String, Double> location = new HashMap<>();
        
        Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
        
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            System.out.println(reservation);
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            System.out.println(room);
            location.put("targetLat", room.getLatitude());
            location.put("targetLng", room.getLongitude());
        } 
        return location;
    }
    
}
