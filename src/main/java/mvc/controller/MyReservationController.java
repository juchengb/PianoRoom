package mvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mvc.dao.ReservationDao;
import mvc.entity.Reservation;
import mvc.entity.User;

@Controller
@RequestMapping("/myreservation")
public class MyReservationController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@GetMapping("")
	public String myreservationPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		List<Reservation> reservations = reservationDao.findFutureResrvationsByUserId(user.getId());
		if (!reservations.isEmpty()) { // 
	        model.addAttribute("reservations", reservations);
	        model.addAttribute("show", "future");
	    }
		return "frontend/myreservation";
	}
	
	// 歷史預約紀錄
	@GetMapping("/past")
	public String past(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		List<Reservation> pastReservations = reservationDao.findPastResrvationsByUserId(user.getId());
		if (!pastReservations.isEmpty()) { 
	        model.addAttribute("reservations", pastReservations);
	        model.addAttribute("show", "past");
	    }
		return "frontend/myreservation";
	}
	
	// 取消預約
	@GetMapping("/delete")
	public String deleteReservation(@RequestParam("id") Integer id,
								 HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		// 確認訂單是否為該使用者的
		Boolean validation = reservationDao.getReservationById(id).get().getUserId().equals(user.getId());
		if(validation) {
			reservationDao.deleteReservationById(id);
		}

		return "redirect:/mvc/myreservation";
		
	}
}
