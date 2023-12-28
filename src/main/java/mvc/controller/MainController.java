package mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        displayNextReservation(user, model);
        displayUserMonthlyDatas(user, model);
        displayRoomStatus(model);

        return "frontend/main";
    }

    private void displayNextReservation(User user, Model model) {
        Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            String next = String.format("%s %s %s %n %s",
                    room.getDist(), room.getType(), room.getName(), sdf.format(reservation.getStartTime()));
            model.addAttribute("nextReservation", next);
        } else {
            model.addAttribute("nextReservation", "查無預約");
        }
    }

    private void displayUserMonthlyDatas(User user, Model model) {
        Optional<UserMonthlyDatas> monthlyOpt = userDao.getUserMonthlyDatasByUserId(user.getId());
        if (monthlyOpt.isPresent()) {
            UserMonthlyDatas monthly = monthlyOpt.get();
            model.addAttribute("monthly", monthly);
        } else {
            model.addAttribute("monthly.counts", 0);
            model.addAttribute("monthly.hours", 0);
            model.addAttribute("monthly.ranking", 0);
        }
    }

    private void displayRoomStatus(Model model) {
        model.addAttribute("updateTime", sdf.format(new Date()));
        List<RoomStatus> roomStatusList = roomDao.findRoomsCurrentStatus();
        model.addAttribute("roomStatusList", roomStatusList);
    }
}
