package mvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.entity.Room;
import mvc.entity.User;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@GetMapping("")
	public String reservePage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		
		// show all rooms
		roomDao.findAllRooms();
		List<Room> rooms = roomDao.findAllRooms();
		model.addAttribute("rooms", rooms);
		
		
		return "frontend/reserve";
	}
	
	
}
