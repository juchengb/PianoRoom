package mvc.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;
import mvc.bean.RoomBusinessHours;
import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.entity.Reservation;
import mvc.entity.Room;
import mvc.entity.User;
import mvc.service.ReserveService;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private ReserveService reserveService;
	
	@GetMapping("")
	public String reservePage(HttpSession session, Model model) {
		// find user
		User user = (User)session.getAttribute("user");
		
		
		List<Room> rooms = roomDao.findAllRooms();
		
		// show reserve buttons
		for (Room room : rooms) {
	        Integer roomId = room.getId();
	        Optional<RoomBusinessHours> roomBusinessHours = roomDao.getCurdateBusinessHourById(room.getId());
	        
	        
	        if (roomBusinessHours.isPresent()) {
	            List<String> businessHourButtons = reserveService.splitBusinessHoursIntoButtons(roomBusinessHours.get());
	            model.addAttribute("businessHourButtons", businessHourButtons);
	            System.out.println(businessHourButtons);
	        } else {
	        	model.addAttribute("businessHourButtons", Collections.emptyList());
	        }
	        
	    }
		
		// show all rooms
		model.addAttribute("rooms", rooms);
		
		return "frontend/reserve";
	}
	
	@GetMapping("/reserveroom")
	public String reserveRoom(@RequestParam("roomId")Integer roomId,
							  @RequestParam("userId")Integer userId,
							  @RequestParam("startTime")Date startTime,
							  @RequestParam("endTime")Date endTime) {
		
		
		// get reservation info
		Reservation reservation = new Reservation();
		reservation.setUserId(userId);
		reservation.setRoomId(roomId);
		reservation.setStartTime(startTime);
		reservation.setEndTime(endTime);
		
		// add reservation
		try {
			int rowcount = reservationDao.addReservation(reservation);
			if (rowcount == 0) {
				System.out.println("預約失敗");
			} else {
				System.out.println("預約成功");
			}
				
		} catch (Exception e) {
			System.out.println("預約失敗");
		}

		return "redirect:/mvc/reserve";
	}

	
}
