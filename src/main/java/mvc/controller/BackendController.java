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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import mvc.bean.UpdateRoom;
import mvc.dao.RoomDao;
import mvc.entity.Room;
import mvc.entity.User;

@Controller
@RequestMapping("/backend")
public class BackendController {
	
	@Autowired
	private RoomDao roomDao;

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
		
		return "backend/room";
	}
	
	@PostMapping("/update-room/{id}")
	public String updateRoom(@ModelAttribute @Valid UpdateRoom room, BindingResult result,
						     @PathVariable("id") Integer id,
							 HttpSession session, Model model) throws IOException{
		User user = (User)session.getAttribute("user");
		model.addAttribute("user", user);
		
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
		
		
		return "backend/room";
	}
	
	
	// ----------------------------------------------------------------------
	// user

}
