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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.model.dto.RoomStatus;
import mvc.model.po.Reservation;
import mvc.model.po.Room;
import mvc.model.po.User;
import mvc.service.MainService;

@Controller
@RequestMapping("/main")
public class MainController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private MainService mainService;
	
	// Define date format
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
	
	@GetMapping("")
    public String mainPage(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        
        List<Object> next = mainService.nextReservation(user);
        if (next.size() > 0) {
        	model.addAttribute("nextReservation", next.get(0));
        	model.addAttribute("btnStatus", next.get(1));
        	model.addAttribute("btnWord", next.get(2));
        } else {
        	model.addAttribute("nextReservation", "查無預約，趕快去預約琴房");
        }
        
        model.addAttribute("monthly", mainService.userMonthlyDatas(user));
        model.addAttribute("updateTime", sdf.format(new Date()));
        model.addAttribute("roomStatusList", roomDao.findRoomsCurrentStatus());

        return "frontend/main";
    }
    
    
    @GetMapping("/location")
    @ResponseBody
    public Map<String, Object> getNextReservationLocation(HttpSession session) {
    	User user = (User)session.getAttribute("user");
        Map<String, Object> location = new HashMap<>();
        
        Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
        
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();;
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            
            location.put("reservationId", reservation.getId());
            location.put("targetLat", room.getLatitude());
            location.put("targetLng", room.getLongitude());
        } 
        return location;
    }
    
  //--------------------------------------------------------------------------------------------
  // checkin
  @GetMapping("/checkin/{id}")
  public String checkin(@PathVariable("id")Integer reservationId){
	  Date checkinTime = new Date();
	  reservationDao.updateCheckinById(reservationId, checkinTime);
	  System.out.println("check in sucess! " + checkinTime.toString());
	  return "redirect:/mvc/main";
  }
  
  // checkout
  @GetMapping("/checkout/{id}")
  public String checkout(@PathVariable("id")Integer reservationId){
	  Date checkoutTime = new Date();
	  reservationDao.updateCheckoutById(reservationId, checkoutTime);
	  System.out.println("check out sucess! " + checkoutTime.toString());
	  return "redirect:/mvc/main";
  }
    
}
