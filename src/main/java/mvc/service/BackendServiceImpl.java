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

/**
 * BackendServiceImpl 實作 BackendService 的後臺管理相關功能。
 */
@Service
public class BackendServiceImpl implements BackendService {
	
	@Autowired
	RoomDao roomDao;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	// 琴房照片的上傳路徑
	private static final Path upPath = Paths.get("C:/Javaclass/uploads/room-img");
	
	static {
		try {
			Files.createDirectories(upPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得檔案的副檔名。
	 * 
	 * @param filename 檔案名稱字串
	 * @return 檔案副檔名，如找不到則回傳空字串
	 */
	public String getExtension(String filename) {
		// 副檔名預設為空字串
		String extension = "";
		// 尋找最後一個.的位置，沒找到會回傳 -1
		int idx = filename.lastIndexOf(".");
		if(idx >= 0) {
		    // 使用substring取得副檔名
		    extension = filename.substring(idx);
		}
		return extension;
	}

	/**
	 * 將 EditRoom 轉換為 Room 
	 * 
	 * @param addRoom EditRoom
	 * @return Room
	 */
	@Override
	public Room convertToRoomEntity(EditRoom addRoom) {
		MultipartFile multipartFile = addRoom.getImage();
		String imageString;
		if (multipartFile != null && !multipartFile.isEmpty()) {
			// 儲存檔案名稱：room{name}-{dist}.{extension}
			imageString = "room" + addRoom.getName() + "-" + addRoom.getDist() + getExtension(multipartFile.getOriginalFilename());
			Path picPath = upPath.resolve(imageString);
			try {
				Files.copy(multipartFile.getInputStream(), picPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 若未上傳新檔案，使用預設照片
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
	
	/**
	 * 新增琴房營業時間
	 * 
	 * @param addRoom EditRoom
	 * @param roomEntity 新增的琴房實體
	 * @return 新增營業時間的筆數
	 */
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
	
	
	/**
	 * 取得星期字串
	 * 
	 * @param dayIndex 星期索引
	 * @return 星期字串
	 * @throws IllegalArgumentException 索引不在合法範圍內的例外
	 */
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
