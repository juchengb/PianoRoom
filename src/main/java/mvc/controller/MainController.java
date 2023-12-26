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
import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
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
	
	// Define date format
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	
	@GetMapping("")
	public String mainPage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		
		// 
		Optional<Reservation> reservationOpt = reservationDao.getLatestReservationByUserId(user.getId());
		
		if (reservationOpt.isPresent()) {
			Reservation reservation = reservationOpt.get();
			Room room = roomDao.getRoomById(reservation.getRoomId()).get();
			String next = String.format("%s %s %s %n %s",
                    room.getDist(), room.getType(), room.getName(), sdf.format(reservation.getStartTime()));
			model.addAttribute("nextReservation", next);
			model.addAttribute("latitude", room.getLatitude());
			model.addAttribute("longitude", room.getLongitude());
		}else {
			model.addAttribute("nextReservation", "查無預約");
		}
		
		
		// 更新時間
		model.addAttribute("updateTime", sdf.format(new Date()));

		// show all rooms'status
		List<RoomStatus> roomStatusList = roomDao.findRoomsCurrentStatus();
		model.addAttribute("roomStatusList", roomStatusList);
		return "frontend/main";
		
		
	}
}
