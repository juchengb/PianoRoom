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
import mvc.dao.UserDao;
import mvc.model.dto.RoomStatus;
import mvc.model.po.Reservation;
import mvc.model.po.Room;
import mvc.model.po.User;
import mvc.model.vo.UserMonthlyDatas;

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
        System.out.println(user.toString());

        return "frontend/main";
    }

    private void showNextReservation(User user, Model model) {
        Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            System.out.println(reservation);
            Room room = roomDao.getRoomById(reservation.getRoomId()).get();
            Date startTime = reservation.getStartTime();
            Date endTime = reservation.getEndTime();
            String next = String.format("%s %s %s %n-%n %s",
                    room.getDist(), room.getType(), room.getName(), sdf.format(startTime), sdf.format(endTime));            
            model.addAttribute("nextReservation", next);
            
            // check button status
            Date now = new Date();
            if (now.before(startTime)) {
            	model.addAttribute("btnStatus", 0);
            	model.addAttribute("btnWord", "簽到");
            } else if (now.after(startTime) && reservation.getCheckin() == null) {
            	model.addAttribute("btnStatus", 1);
            	model.addAttribute("btnWord", "簽到");
            } else if (now.after(startTime) && now.before(endTime) && reservation.getCheckin() != null) {
            	model.addAttribute("btnStatus", 2);
            	model.addAttribute("btnWord", "簽退");
            }
			
            
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
