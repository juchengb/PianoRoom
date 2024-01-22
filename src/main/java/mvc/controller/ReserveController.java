package mvc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.model.dto.ReserveRoom;
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
	private RoomDao roomDao;
	
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
		model.addAttribute("dists", roomDao.findAllRoomDists());
		model.addAttribute("types", roomDao.findAllRoomTypes());
		
		// 顯示所有琴房資訊，包含預約按鈕
		List<ReserveRoom> rooms = roomDao.findAllRoomsToReserve();
		System.out.println("List<ReserveRoom>: " + rooms);
		model.addAttribute("rooms", reserveService.showRoomsWithButtons(rooms));
		model.addAttribute("distSelectItem", "");
		model.addAttribute("typeSelectItem", "");
		return "frontend/reserve";
	}
	
	/**
	 * POST 請求，篩選琴房
	 * 
	 * @param dist 選擇的校區 ("all" 表示所有校區)
	 * @param type 選擇的類型 ("all" 表示所有類型)
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return 顯示篩選後結果
	 */
	@PostMapping("")
	public String reserveSearch(@RequestParam("dist") String dist,
			                    @RequestParam("type") String type,
								HttpSession session, Model model) {
		System.err.println("dist: " + dist + "type: " + type);
		List<ReserveRoom> rooms;
		if (dist.equals("all") && type.equals("all")) {
			// 顯示所有校區和類型琴房資訊，包含預約按鈕
			rooms = roomDao.findAllRoomsToReserve();
		} else if (dist.equals("all")) {
			// 顯示指定類型琴房資訊，包含預約按鈕
			rooms = roomDao.findRoomsByTypeToReserve(type);
		} else if (type.equals("all")) {
			// 顯示指定校區琴房資訊，包含預約按鈕
			rooms = roomDao.findRoomsByDistToReserve(dist);
		} else {
			// 顯示指定校區和類型琴房資訊，包含預約按鈕
			rooms = roomDao.findRoomsByDistAndTypeToReserve(dist, type);
		}
		model.addAttribute("rooms", reserveService.showRoomsWithButtons(rooms));
		
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("currentDate", sdf.format(new Date()));
		model.addAttribute("dists", roomDao.findAllRoomDists());
		model.addAttribute("types", roomDao.findAllRoomTypes());
		model.addAttribute("distSelectItem", dist);
		model.addAttribute("typeSelectItem", type);
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
