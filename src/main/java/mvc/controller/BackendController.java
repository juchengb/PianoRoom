package mvc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.dao.UserDao;
import mvc.model.dto.EditRoom;
import mvc.model.po.BusinessHour;
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
	public String updateRoomPage(@PathVariable("id") Integer id,
								 HttpSession session, Model model){
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		model.addAttribute("room", roomDao.getRoomById(id).get());
		model.addAttribute("bhListOrg", roomDao.getBusinessHoursByRoomId(id));
		
		return "backend/room";
	}
	
	@PostMapping("/update-room/{id}")
	public String updateRoom(@ModelAttribute @Valid EditRoom editRoom, BindingResult result,
						     @PathVariable("id") Integer id, Model model) throws IOException{
		
		Room roomOrg = roomDao.getRoomById(id).get();
		
		model.addAttribute("room", roomOrg);
		
		if(result.hasErrors()) {
			System.out.println(result.toString());
			return "backend/room";
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
		
		roomDao.updateRoomById(id, roomEntity);
		return "backend/room";
	}
	
	@PostMapping("/update-room/businesshour{id}")
	public String updateRoomBusinessHour(@ModelAttribute ("businessHour") @Valid BusinessHour businessHour, BindingResult result,
										 @PathVariable("id") Integer id, Model model) {
		
		if(result.hasErrors()) {
			return "backend/room";
		}
		
		return "backend/room";
	}
	
	@GetMapping("/add-room")
	public String addRoomPage() {
		return "backend/room";
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
	
	@GetMapping(value = "/get-reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Reservation> getReservations() {
		List<Reservation> reservations = reservationDao.findAllReservations();
		System.out.println(reservations);
		return reservations;
	}
	
	
	public class book {
		
	}
	

}
