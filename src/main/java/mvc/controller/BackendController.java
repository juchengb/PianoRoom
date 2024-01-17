package mvc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.multipart.MultipartFile;

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
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	@GetMapping("/rooms")
	public String roomsPage(HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Room> rooms = roomDao.findAllRooms();
		model.addAttribute("rooms", rooms);
		
		return "backend/rooms";
	}
	
	@GetMapping("/update-room/{id}")
	public String updateRoomPage(@ModelAttribute("updateRoom") EditRoom updateRoom, @PathVariable("id") Integer id,
								 HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		model.addAttribute("room", roomDao.getRoomById(id).get());
		model.addAttribute("businessHoursList", roomDao.getBusinessHoursByRoomId(id));
		
		return "backend/updateRoom";
	}
	
	@PostMapping("/update-room/{id}")
	public String updateRoom(@ModelAttribute("updateRoom") @Valid EditRoom updateRoom, BindingResult result,
						     @PathVariable("id") Integer id, Model model) throws IOException{
		
		Room roomOrg = roomDao.getRoomById(id).get();
		
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
	
	@GetMapping("/add-room")
	public String addRoomPage(@ModelAttribute("addRoom") EditRoom addRoom, HttpSession session, Model model) {
		return "backend/addRoom";
	}
	
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
	
	@GetMapping("/users")
	public String usersPage(@ModelAttribute("addUser") User addUser,
							HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("majors", userDao.findAllMajors());
		
		return "backend/users";
	}
	
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

		
		int rowcount = userDao.updateUserByIdBack(changedData.getId(), changedData);
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
	
	@GetMapping("/get-users")
    @ResponseBody
    public List<User> getUsers() {
		List<User> userList = userDao.findAllUsers();
		
		return userList;
	}

	// ----------------------------------------------------------------------
	// major
	@GetMapping("/majors")
	public String majorsPage(@ModelAttribute Major major,
							 HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "backend/majors";
	}
	
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
	
	@GetMapping("/get-majors")
    @ResponseBody
    public List<Major> getMajors() {
		List<Major> majorList = userDao.findAllMajors();
		return majorList;
	}
	
	
	// ----------------------------------------------------------------------
	// reservation
	@GetMapping("/reservations")
	public String reservationsPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		return "backend/reservations";
	}
	
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
	
	@GetMapping(value = "/get-reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Reservation> getReservations() {
		List<Reservation> reservations = reservationDao.findAllReservations();
		System.out.println(reservations);
		return reservations;
	}

	

}
