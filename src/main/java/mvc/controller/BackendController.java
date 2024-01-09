package mvc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import mvc.bean.UpdateRoom;
import mvc.dao.ReservationDao;
import mvc.dao.RoomDao;
import mvc.dao.UserDao;
import mvc.entity.BusinessHour;
import mvc.entity.Reservation;
import mvc.entity.Room;
import mvc.entity.User;

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
	public String updateRoom(@ModelAttribute @Valid UpdateRoom room, BindingResult result,
						     @PathVariable("id") Integer id, Model model) throws IOException{
		
		Room roomOrg = roomDao.getRoomById(id).get();
		
		if(result.hasErrors()) {
			return "backend/room";
		}
		
		MultipartFile multipartFile = room.getImage();
		String imageString;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			imageString = "room" + room.getId() + "-" + multipartFile.getOriginalFilename();
			Path picPath = upPath.resolve(imageString);
			Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			imageString = roomOrg.getImage();
		}
		
		Room roomEntity = new Room().builder()
									.id(roomOrg.getId())
									.name(room.getName())
									.dist(room.getDist())
									.type(room.getType())
									.latitude(room.getLatitude())
									.longitude(room.getLongitude())
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
	public String usersPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<User> users = userDao.findAllUsers();
		model.addAttribute("users", users);
		
		return "backend/users";
	}
	
	
	
	// ----------------------------------------------------------------------
	// reservation
	@GetMapping("/reservations")
	public String reservationsPage(HttpSession session, Model model) {
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
		List<Reservation> reservations = reservationDao.findAllReservations();
		model.addAttribute("reservations", reservations);
		
		return "backend/reservations";
	}

}
