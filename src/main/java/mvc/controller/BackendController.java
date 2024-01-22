package mvc.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.dao.UserDao;
import mvc.model.dto.EditRoom;
import mvc.model.dto.EditUser;
import mvc.model.po.Major;
import mvc.model.po.Reservation;
import mvc.model.po.Room;
import mvc.model.po.User;
import mvc.service.AuthService;
import mvc.service.BackendService;

/**
 * BackendController 處理後台管理相關請求，包含琴房管理、使用者管理、主修管理、預約管理等。
 */
@Controller
@RequestMapping("/backend")
public class BackendController {
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private BackendService backendService;

	// ----------------------------------------------------------------------
	// room
	
	// 定義時間格式
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	/**
	 * GET 請求，顯示所有琴房的管理頁面。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return rooms 頁面
	 */
	@GetMapping("/rooms")
	public String roomsPage(HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Room> rooms = roomDao.findAllRooms();
		model.addAttribute("rooms", rooms);
		return "backend/rooms";
	}
	
	/**
	 * GET 請求，顯示修改指定琴房頁面。
	 * 
	 * @param updateRoom EditRoom 接收修改琴房表單數據
	 * @param id Integer 欲修改的琴房 ID
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return updateRoom 頁面
	 */
	@GetMapping("/update-room/{id}")
	public String updateRoomPage(@ModelAttribute("updateRoom") EditRoom updateRoom, @PathVariable("id") Integer id,
								 HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		model.addAttribute("room", roomDao.getRoomById(id).get());
		model.addAttribute("businessHoursList", roomDao.getBusinessHoursByRoomId(id));
		return "backend/updateRoom";
	}
	
	/**
	 * POST 請求，更新指定琴房資料。
	 * 
	 * @param updateRoom EditRoom 接收修改琴房表單數據
	 * @param result BindingResult 驗證修改琴房表單
	 * @param id Integer 欲修改的琴房 ID
	 * @param model Spring MVC 模型
	 * @return 修改結果：成功/失敗
	 * @throws IOException
	 */
	@PostMapping("/update-room/{id}")
	public String updateRoom(@ModelAttribute("updateRoom") @Valid EditRoom updateRoom, BindingResult result,
						     @PathVariable("id") Integer id, Model model) throws IOException{
		
		if(result.hasErrors()) {
			model.addAttribute("validationErrors", result.getAllErrors());
			System.out.println(result.toString());
			return "backend/updateRoom";
		}
		
		Room roomEntity = backendService.convertToRoomEntity(updateRoom);
		
		int rowcount = roomDao.updateRoomById(id, roomEntity);
		
		if (rowcount > 0) {
			System.out.println("add User rowcount = " + rowcount);
		}
		model.addAttribute("message", "修改成功");
		model.addAttribute("togobtn", "返回頁面");
		model.addAttribute("togourl", "/backend/update-room/" + id);
	    return "dialog";
	}
	
	/**
	 * POST 請求，更新指定琴房營業時間。
	 * 
	 * @param openingTime List<String> 接收表單的琴房營業開始時間
	 * @param closingTime List<String> 接收表單的琴房營業結束時間
	 * @param id Integer 欲修改的琴房 ID
	 * @param model Spring MVC 模型
	 * @return 修改結果：成功/失敗
	 */
	@PostMapping("/update-room/businesshour/{id}")
	public String updateRoomBusinessHour(@RequestParam List<String> openingTime,
										 @RequestParam List<String> closingTime,
										 @PathVariable("id") Integer id, Model model) {
		int rowcount = 0;
		for (int i = 0; i < 7; i++) {
			LocalTime opening = LocalTime.parse(openingTime.get(i), formatter);
			LocalTime closing = LocalTime.parse(closingTime.get(i), formatter);
			rowcount += roomDao.updateBusinessHourByIdAndDayOfWeek(id, backendService.getDayOfWeek(i), opening, closing);
		}
		
		if (rowcount >= 7) {
			System.out.println("update BusinessHour rowcount = " + rowcount);
		}
		model.addAttribute("message", "修改成功");
		model.addAttribute("togobtn", "返回琴房頁面");
		model.addAttribute("togourl", "/backend/update-room/" + id);
	    return "dialog";
	}
	
	/**
	 * GET 請求，顯示新增琴房頁面。
	 * 
	 * @param addRoom EditRoom 接收新增琴房表單數據
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return addRoom 頁面
	 */
	@GetMapping("/add-room")
	public String addRoomPage(@ModelAttribute("addRoom") EditRoom addRoom, HttpSession session, Model model) {
		return "backend/addRoom";
	}
	
	/**
	 * POST 請求，新增琴房。
	 * 
	 * @param addRoom EditRoom 接收新增琴房表單數據
	 * @param model Spring MVC 模型
	 * @return 新增結果：成功/失敗
	 * @throws IOException
	 */
	@PostMapping("/add-room")
	public String addRoom(@ModelAttribute("addRoom") EditRoom addRoom, Model model) throws IOException {
		
		Room roomEntity = backendService.convertToRoomEntity(addRoom);
		int rowcount = roomDao.addRoom(roomEntity);
		
		if (rowcount > 0) {
			rowcount += backendService.addBusinessHours(addRoom, roomEntity);

			if (rowcount > 1) {
				model.addAttribute("message", "修改成功");
				model.addAttribute("togobtn", "返回琴房管理頁面");
				model.addAttribute("togourl", "/backend/rooms");
			    return "dialog";
			}
		}
		return  "redirect:/mvc/backend/rooms";
	}
	
	
	// ----------------------------------------------------------------------
	// user
	
	/**
	 * GET 請求，顯示使用者管理頁面。
	 * 
	 * @param addUser User 接收新增使用者表單數據
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return users 頁面
	 */
	@GetMapping("/users")
	public String usersPage(@ModelAttribute("addUser") User addUser,
							HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("majors", userDao.findAllMajors());
		return "backend/users";
	}
	
	/**
	 * POST 請求，新增用戶。
	 * 
	 * @param addUser EditUser 接收新增使用者表單數據
	 * @param model Spring MVC 模型
	 * @return 新增結果：成功/失敗
	 * @throws IOException
	 */
	@PostMapping("/add-user")
	public String addUser(@ModelAttribute("addUser") EditUser addUser, Model model) throws IOException {
		User userEntity = authService.addUserConvertToUser(addUser);
		
		int rowcount = userDao.addUserWithAvator(userEntity);
		if (rowcount > 0) {
			System.out.println("add User rowcount = " + rowcount);
		}
		model.addAttribute("message", "新增成功");
		model.addAttribute("togobtn", "返回使用者管理頁面");
		model.addAttribute("togourl", "/backend/users");
	    return "dialog";
	}
	
	/**
	 * POST 請求，修改使用者資料。
	 * 
	 * @param changedData User 接收修改的使用者數據
	 * @return 修改結果的實體資料
	 */
	@PostMapping("/update-user")
    public ResponseEntity<Map<String, Object>>  updateUser(@RequestBody User changedData) {
		Map<String, Object> response = new HashMap<>();
		String newMajor = changedData.getMajor().getMajor();
		List<Major> majors = userDao.findAllMajors();
		Integer newMajorId;
		
		for (Major major : majors) {
			if (major.getMajor().equals(newMajor)) {
				newMajorId = major.getId();
				changedData.setMajorId(newMajorId);
				changedData.setMajor(Major.builder().id(newMajorId).major(newMajor).build());
				break;
			}
		}
		System.out.println(changedData);
		int rowcount = userDao.updateUserById(changedData.getId(), changedData);
		if (rowcount > 0) {
			System.out.println("Received updated major data: " + changedData);
			System.out.println("update major rowcount = " + rowcount);
			
            response.put("success", true);
            return ResponseEntity.ok(response);
		} else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
	
	/**
	 * GET 請求，以 JSON 格式回傳所有使用者資料。
	 * 
	 * @return List<User> 所有使用者資料
	 */
	@GetMapping("/get-users")
    @ResponseBody
    public List<User> getUsers() {
		List<User> userList = userDao.findAllUsers();
		return userList;
	}

	// ----------------------------------------------------------------------
	// major
	
	/**
	 * GET 請求，顯示主修管理頁面。
	 * 
	 * @param major Major 接收新增主修表單數據
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return majors 頁面
	 */
	@GetMapping("/majors")
	public String majorsPage(@ModelAttribute Major major,
							 HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		return "backend/majors";
	}
	
	/**
	 * POST 請求，新增主修。
	 * 
	 * @param major Major 接收新增主修表單數據
	 * @param model Spring MVC 模型
	 * @return 新增結果：成功/失敗
	 */
	@PostMapping("/add-major")
	public String addMajor(@ModelAttribute Major major, Model model) {
		int rowcount = userDao.addMajor(major);
		if (rowcount > 0) {
			System.out.println("add Major rowcount = " + rowcount);
		}
		model.addAttribute("message", "新增成功");
		model.addAttribute("togobtn", "返回主修管理頁面");
		model.addAttribute("togourl", "/backend/majors");
	    return "dialog";
	}
	
	/**
	 * POST 請求，修改主修。
	 * 
	 * @param changedData Major 接收修改的主修數據
	 * @return 修改結果的實體資料
	 */
	@PostMapping("/update-major")
    public ResponseEntity<Map<String, Object>>  updateMajor(@RequestBody Major changedData) {
		Map<String, Object> response = new HashMap<>();
		int rowcount = userDao.updateMajorById(changedData.getId(), changedData);
		if (rowcount > 0) {
			System.out.println("Received updated major data: " + changedData);
			System.out.println("update major rowcount = " + rowcount);
            response.put("success", true);
            return ResponseEntity.ok(response);
		} else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
	
	/**
	 * GET 請求，以 JSON 格式回傳所有主修資料。
	 * 
	 * @return List<Major> 所有主修資料
	 */
	@GetMapping("/get-majors")
    @ResponseBody
    public List<Major> getMajors() {
		List<Major> majorList = userDao.findAllMajors();
		return majorList;
	}
	
	// ----------------------------------------------------------------------
	// reservation
	
	/**
	 * GET 請求，顯示預約管理頁面。
	 * 
	 * @param session HttpSession
	 * @param model Spring MVC 模型
	 * @return reservations 頁面
	 */
	@GetMapping("/reservations")
	public String reservationsPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		return "backend/reservations";
	}
	
	/**
	 * POST 請求，修改預約資料。
	 * 
	 * @param changedData Reservation 接收修改的預約數據
	 * @return 修改結果的實體資料
	 */
	@PostMapping("/update-reservation")
    public ResponseEntity<Map<String, Object>> updateReservation(@RequestBody Reservation changedData) {
		Map<String, Object> response = new HashMap<>();
		
		int rowcount = reservationDao.updateReservationById(changedData.getId(), changedData);
		if (rowcount > 0) {
			System.out.println("Received updated reservation data: " + changedData);
			System.out.println("update reservation rowcount = " + rowcount);
            response.put("success", true);
            return ResponseEntity.ok(response);
		} else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
	
	/**
	 * GET 請求，刪除指定 ID 的預約。
	 * 
	 * @param id Integer 欲刪除的預約 ID
	 * @param model Spring MVC 模型
	 * @return 刪除結果：成功/失敗
	 */
	@GetMapping("/delete-reservation/{id}")
	public String deleteReservationsPage(@PathVariable("id")Integer id, Model model) {
		int rowcount = reservationDao.deleteReservationById(id);
		if (rowcount > 0) {
			System.out.println("delete reservation (id = " + id + ") rowcount = " + rowcount);
			return "redirect:/mvc/backend/reservations";
		} else {
			model.addAttribute("message", "刪除失敗");
			model.addAttribute("togobtn", "返回預約管理頁面");
			model.addAttribute("togourl", "/backend/reservations");
		    return "dialogFail";
		}
	}
	
	/**
	 * GET 請求，以 JSON 格式回傳所有預約資料。
	 * 
	 * @return List<Reservation> 所有預約資料
	 */
	@GetMapping(value = "/get-reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Reservation> getReservations() {
		List<Reservation> reservations = reservationDao.findAllReservations();
		System.out.println(reservations);
		return reservations;
	}
	
}
