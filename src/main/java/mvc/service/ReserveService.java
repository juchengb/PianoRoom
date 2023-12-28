package mvc.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import mvc.bean.RoomBusinessHours;

@Service
public class ReserveService {
	
    public List<String> splitBusinessHoursIntoButtons(RoomBusinessHours roomBusinessHours) {
    	List<String> buttons = new ArrayList<>();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            LocalTime openingTime = roomBusinessHours.getOpeningTime();
            LocalTime closingTime = roomBusinessHours.getClosingTime();

            // 切分營業時間，每小時一個按鈕
            LocalTime currentTime = openingTime;
            while (currentTime.isBefore(closingTime)) {
                buttons.add(currentTime.format(timeFormatter));
                currentTime = currentTime.plusHours(1); // 增加一小時
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buttons;
    }

}
