package mvc.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mvc.dao.RoomDao;
import mvc.model.dto.EditRoom;
import mvc.model.po.Room;

@Service
public class BackendServiceImpl implements BackendService {
	
	@Autowired
	RoomDao roomDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	private static final Path upPath = Paths.get("C:/Javaclass/uploads/room-img");
	
	static {
		try {
			Files.createDirectories(upPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Room convertToRoomEntity(EditRoom addRoom) {
		MultipartFile multipartFile = addRoom.getImage();
		String imageString;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			imageString = "room" + addRoom.getName() + "-" + addRoom.getDist() + multipartFile.getOriginalFilename();
			Path picPath = upPath.resolve(imageString);
			try {
				Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			imageString = "default.png";
		}
		
		Room roomEntity = new Room().builder().name(addRoom.getName())
											  .dist(addRoom.getDist())
											  .type(addRoom.getType())
											  .latitude(addRoom.getLatitude())
											  .longitude(addRoom.getLongitude())
											  .image(imageString)
											  .build();
		return roomEntity;
	}

	@Override
	public int addBusinessHours(EditRoom addRoom, Room roomEntity) {
		System.out.println("add Room sucess! next to add business hour");
		System.out.println("room = " + roomEntity.toString());
		Integer id = roomDao.getRoomIdByNameAndDist(roomEntity.getName(), roomEntity.getDist());
		
		int count = 0;
		
		for (int i = 0; i < 7; i++) {
			LocalTime opening = null;
		    LocalTime closing = null;
		    if (addRoom.getOpeningTime().get(i) != null && !addRoom.getOpeningTime().get(i).trim().isEmpty()) {
		        opening = LocalTime.parse(addRoom.getOpeningTime().get(i), formatter);
		    }
		    if (addRoom.getClosingTime().get(i) != null && !addRoom.getClosingTime().get(i).trim().isEmpty()) {
		        closing = LocalTime.parse(addRoom.getClosingTime().get(i), formatter);
		    }
		    count += roomDao.addBusinessHourByIdAndDayOfWeek(id, getDayOfWeek(i), opening, closing);
		}    
		return count;
	}
	
	@Override
	public String getDayOfWeek(int dayIndex) {
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

	
}
