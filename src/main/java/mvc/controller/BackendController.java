package mvc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
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
import mvc.model.dto.UpdateRoom;
import mvc.model.dto.AddRoom;
import mvc.model.po.Major;
import mvc.model.po.Reservation;
import mvc.model.po.Room;
import mvc.model.po.User;
import mvc.util.KeyUtil;

@Controller
@RequestMapping("/backend")
public class BackendController {
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReservationDao reservationDao;

	// ----------------------------------------------------------------------
	// room
	private static final Path upPath = Paths.get("C:/Javaclass/uploads/room-img");
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	static {
		try {
			Files.createDirectories(upPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/rooms")
	public String roomsPage(HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Room> rooms = roomDao.findAllRooms();
		model.addAttribute("rooms", rooms);
		
		return "backend/rooms";
	}
	
	@GetMapping("/update-room/{id}")
	public String updateRoomPage(@ModelAttribute UpdateRoom editRoom, @PathVariable("id") Integer id,
								 HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		model.addAttribute("room", roomDao.getRoomById(id).get());
		model.addAttribute("businessHoursList", roomDao.getBusinessHoursByRoomId(id));
		
		return "backend/updateRoom";
	}
	
	@PostMapping("/update-room/{id}")
	public String updateRoom(@ModelAttribute("editRoom") @Valid UpdateRoom editRoom, BindingResult result,
						     @PathVariable("id") Integer id, Model model) throws IOException{
		
		Room roomOrg = roomDao.getRoomById(id).get();
		
		if(result.hasErrors()) {
			model.addAttribute("validationErrors", result.getAllErrors());
			System.out.println(result.toString());
			return "backend/updateRoom";
		}
		
		MultipartFile multipartFile = editRoom.getImage();
		String imageString;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			imageString = "room" + editRoom.getId() + "-" + multipartFile.getOriginalFilename();
			Path picPath = upPath.resolve(imageString);
			Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			imageString = roomOrg.getImage();
		}
		
		Room roomEntity = new Room().builder()
									.id(roomOrg.getId())
									.name(editRoom.getName())
									.dist(editRoom.getDist())
									.type(editRoom.getType())
									.latitude(editRoom.getLatitude())
									.longitude(editRoom.getLongitude())
									.image(imageString)
									.build(); 
		
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
			rowcount += roomDao.updateBusinessHourByIdAndDayOfWeek(id, getDayOfWeek(i), opening, closing);
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
	public String addRoomPage(@ModelAttribute AddRoom addRoom, HttpSession session, Model model) {
		return "backend/addRoom";
	}
	
	@PostMapping("/add-room")
	public String addRoom(@ModelAttribute AddRoom addRoom, Model model) throws IOException {
		System.out.println(addRoom.toString());
		
		MultipartFile multipartFile = addRoom.getImage();
		String imageString;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			imageString = "room" + addRoom.getName() + "-" + addRoom.getDist() + multipartFile.getOriginalFilename();
			Path picPath = upPath.resolve(imageString);
			Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			imageString = null;
		}
		
		Room roomEntity = new Room().builder().name(addRoom.getName())
											  .dist(addRoom.getDist())
											  .type(addRoom.getType())
											  .latitude(addRoom.getLatitude())
											  .longitude(addRoom.getLongitude())
											  .image(imageString)
											  .build();
		int rowcount = roomDao.addRoom(roomEntity);
		
		if (rowcount > 0) {
			System.out.println("add Room sucess! next to add business hour");
			System.out.println("room = " + roomEntity.toString());
			Integer id = roomDao.getRoomIdByNameAndDist(roomEntity.getName(), roomEntity.getDist());
			
			
			for (int i = 0; i < 7; i++) {
				LocalTime opening = null;
			    LocalTime closing = null;
			    if (addRoom.getOpeningTime().get(i) != null && !addRoom.getOpeningTime().get(i).trim().isEmpty()) {
			        opening = LocalTime.parse(addRoom.getOpeningTime().get(i), formatter);
			    }
			    if (addRoom.getClosingTime().get(i) != null && !addRoom.getClosingTime().get(i).trim().isEmpty()) {
			        closing = LocalTime.parse(addRoom.getClosingTime().get(i), formatter);
			    }
			    rowcount += roomDao.addBusinessHourByIdAndDayOfWeek(id, getDayOfWeek(i), opening, closing);
			    System.out.printf("count: %d id: %d %s %s %s %n", rowcount, id, getDayOfWeek(i), opening, closing);
			}

			if (rowcount > 1) {
				model.addAttribute("message", "修改成功");
				model.addAttribute("togobtn", "返回琴房管理頁面");
				model.addAttribute("togourl", "/backend/rooms");
				
			    return "dialog";
			}
			
		}
		
		return  "redirect:/mvc/backend/rooms";
	}
	
	private String getDayOfWeek(int dayIndex) {
	    switch (dayIndex) {
	        case 0: return "monday";
	        case 1: return "tuesday";  
	        case 2: return "wednesday";
	        case 3: return "thursday"; 
	        case 4: return "friday";   
	        case 5: return "saturday";  
	        case 6: return "sunday";
	        default: throw new IllegalArgumentException("Invalid dayIndex: " + dayIndex);
	    }
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
	public String addUser(@ModelAttribute User addUser, Model model) throws Exception {
		// Encrypt password with AES
		final String KEY = KeyUtil.getSecretKey();
		SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, addUser.getPassword());
		String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);
		addUser.setPassword(encryptedPasswordECBBase64);
		
		int rowcount = userDao.addUser(addUser);
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
