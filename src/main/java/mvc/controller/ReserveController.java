package mvc.controller;

import java.text.SimpleDateFormat;
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

/**
 * ReserveController 處理預約琴房相關請求。
 */
@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private ReserveService reserveService;
	
	// 定義前端顯示日期格式
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E)");
	
	/**
	 * GET 請求，顯示預約頁面，包含當前日期和所有琴房資訊。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return reserve 頁面
	 */
	@GetMapping("")
	public String reservePage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("currentDate", sdf.format(new Date()));
		
		// 顯示所有琴房資訊，包含預約按鈕
		model.addAttribute("rooms", reserveService.showRoomsWithButtons());
		return "frontend/reserve";
	}
	
	/**
	 * GET 請求，預約指定琴房的指定時段。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @param roomId Integer 琴房ID
	 * @param start String 預約開始時間
	 * @return 預約結果：成功/失敗
	 */
	@GetMapping("/{roomId}/{start}")
	public String reserveRoom(HttpSession session, Model model,
						      @PathVariable("roomId") Integer roomId,
						      @PathVariable("start")String start) {
		
		User user = (User)session.getAttribute("user");
		
		// 獲得預約資訊
		Reservation reservation = reserveService.getReservationInfo(user, roomId, start);
		
		// 新增預約
		if (reservationDao.getReservationByRoomIdAndStartTime(reservation.getRoomId(), reservation.getStartTime()).isEmpty()) {
			if (reservationDao.getReservationByUserIdAndStartTime(user.getId(), reservation.getStartTime()).isEmpty()) {
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
			model.addAttribute("message", "預約失敗：您該時段已有其他預約");
			model.addAttribute("togobtn", "返回預約頁面");
			model.addAttribute("togourl", "/reserve");
			return "dialogFail";
		}
		System.out.println("add reservation fail! There is another reservation." );
		model.addAttribute("message", "預約失敗：該琴房該時段已被預約");
		model.addAttribute("togobtn", "返回預約頁面");
		model.addAttribute("togourl", "/reserve");
		return "dialogFail";
	}
	
}
