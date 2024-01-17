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

/**
 * MainController 處理使用者首頁相關功能，包含頁面內容、獲得下一個預約及位置、打卡簽到/退等。
 */
@Controller
@RequestMapping("/main")
public class MainController {

	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private MainService mainService;

	// 定義前端顯示的日期格式
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");

	/**
	 * GET 請求，顯示主頁面內容，包括下一個預約、使用者月數據、琴房當下狀態。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return main 頁面
	 */
	@GetMapping("")
	public String mainPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");

		// 渲染 下一個預約資訊，包含預約內容和按鈕狀態
		List<Object> next = mainService.nextReservation(user);
		if (next.size() > 0) {
			model.addAttribute("nextReservation", next.get(0));
			model.addAttribute("btnStatus", next.get(1));
			model.addAttribute("btnWord", next.get(2));
		} else {
			model.addAttribute("nextReservation", "查無預約，趕快去預約琴房");
		}

		// 渲染 使用者月數據
		model.addAttribute("monthly", mainService.userMonthlyDatas(user));

		// 渲染 頁面更新時間及琴房當下狀態
		model.addAttribute("updateTime", sdf.format(new Date()));
		model.addAttribute("roomStatusList", roomDao.findRoomsCurrentStatus());
		return "frontend/main";
	}

	/**
	 * GET 請求，以 JSON 格式回傳下一個預約的位置資訊，包括目標經緯度。
	 * 
	 * @param session HttpSession
	 * @return 包含位置資訊的 Map<String, Object>
	 */
	@GetMapping("/location")
	@ResponseBody
	public Map<String, Object> getNextReservationLocation(HttpSession session) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> location = new HashMap<>();

		Optional<Reservation> reservationOpt = reservationDao.getNextReservationByUserId(user.getId());

		if (reservationOpt.isPresent()) {
			Reservation reservation = reservationOpt.get();
			;
			Room room = roomDao.getRoomById(reservation.getRoomId()).get();

			location.put("reservationId", reservation.getId());
			location.put("targetLat", room.getLatitude());
			location.put("targetLng", room.getLongitude());
		}
		return location;
	}

	/**
	 * GET 請求，預約簽到。
	 * 
	 * @param reservationId Integer 預約 ID
	 * @return main 頁面
	 */
	@GetMapping("/checkin/{id}")
	public String checkin(@PathVariable("id") Integer reservationId) {
		Date checkinTime = new Date();
		reservationDao.updateCheckinById(reservationId, checkinTime);
		System.out.println("check in sucess! " + checkinTime.toString());
		return "redirect:/mvc/main";
	}

	/**
	 * GET 請求，預約簽退。
	 * 
	 * @param reservationId Integer 預約 ID
	 * @return main 頁面
	 */
	@GetMapping("/checkout/{id}")
	public String checkout(@PathVariable("id") Integer reservationId) {
		Date checkoutTime = new Date();
		reservationDao.updateCheckoutById(reservationId, checkoutTime);
		System.out.println("check out sucess! " + checkoutTime.toString());
		return "redirect:/mvc/main";
	}
	
}
