package mvc.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mvc.dao.ReservationDao;
import mvc.model.po.Reservation;
import mvc.model.po.User;

/**
 * MyReservationController 處理我的預約頁面相關請求。
 */
@Controller
@RequestMapping("/myreservation")
public class MyReservationController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	/**
	 * GET 請求，顯示使用者的未來預約資訊。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return myreservation 頁面
	 */
	@GetMapping("")
	public String myreservationPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		List<Reservation> reservations = reservationDao.findFutureResrvationsByUserId(user.getId());
		if (!reservations.isEmpty()) { // 
	        model.addAttribute("reservations", reservations);
	        model.addAttribute("show", "future");
	    } else {
	    	model.addAttribute("show", "future-none");
	    }
		return "frontend/myreservation";
	}
	
	/**
	 * GET 請求，顯示使用者的歷史預約資訊。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return myreservation 頁面
	 */
	@GetMapping("/past")
	public String past(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		
		List<Reservation> pastReservations = reservationDao.findPastResrvationsByUserId(user.getId());
		if (!pastReservations.isEmpty()) { 
	        model.addAttribute("reservations", pastReservations);
	        model.addAttribute("show", "past");
	    } else {
	    	model.addAttribute("show", "past-none");
	    }
		return "frontend/myreservation";
	}
	
	/**
	 * GET 請求，取消指定預約。
	 * 
	 * @param id Integer 預約ID
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return myreservation 頁面
	 */
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
