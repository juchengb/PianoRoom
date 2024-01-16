package mvc.controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mvc.dao.ReservationDao;
import mvc.model.po.Reservation;
import mvc.model.po.User;
import mvc.service.ReserveService;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private ReserveService reserveService;
	
	// Define date format
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E)");
	
	@GetMapping("")
	public String reservePage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		// currentDate
		model.addAttribute("currentDate", sdf.format(new Date()));
		
		// show all rooms
		model.addAttribute("rooms", reserveService.showRoomsWithButtons());
		
		return "frontend/reserve";
	}
	
	@GetMapping("/{roomId}/{start}")
	public String reserveRoom(HttpSession session, Model model,
						      @PathVariable("roomId") Integer roomId,
						      @PathVariable("start")String start) {
		
		User user = (User)session.getAttribute("user");
		
		// start & end time + LocalDate -> LocalDateTime -> Date
		String startString = start;
        LocalTime localTime = LocalTime.parse(startString);
        LocalDateTime localStart = LocalDateTime.of(LocalDate.now(), localTime);
        
		// get reservation info
		Reservation reservation = new Reservation();
		reservation.setUserId(user.getId());
		reservation.setRoomId(roomId);
		reservation.setStartTime(reserveService.localDateTimeToDate(localStart));
		reservation.setEndTime(reserveService.localDateTimeToDate(localStart.plus(Duration.ofHours(1))));
		
		// add reservation
		if (reservationDao.getReservationByRoomIdAndStartTime(reservation.getRoomId(), reservation.getStartTime()).isEmpty()) {
			try {
				int rowcount = reservationDao.addReservation(reservation);
				if (rowcount > 0) {
					System.out.println("add reservation sucess!");
					model.addAttribute("message", "預約成功");
					model.addAttribute("togobtn", "返回預約頁面");
					model.addAttribute("togourl", "/reserve");
					System.out.println(reservation.toString());
					return "dialog";
				}
			    System.out.println("add reservation fail!");
			} catch (Exception e) {
			    System.out.println("add reservation fail! " + e);
			}
		}
		System.out.println("add reservation fail! There is another reservation." );
		model.addAttribute("message", "預約失敗");
		model.addAttribute("togobtn", "返回預約頁面");
		model.addAttribute("togourl", "/reserve");
		return "dialogFail";
	}
	
}
