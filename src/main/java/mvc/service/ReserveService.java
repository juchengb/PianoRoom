package mvc.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import mvc.entity.BusinessHour;

@Service
public class ReserveService {
	
    public List<String> splitBusinessHoursIntoButtons(BusinessHour businessHour) {
    	List<String> buttons = new ArrayList<>();

        try {
            LocalTime openingTime = businessHour.getOpeningTime();
            LocalTime closingTime = businessHour.getClosingTime();
            LocalTime now = LocalTime.now();

            // 切分營業時間，每小時一個按鈕
            LocalTime currentTime = openingTime;
            while (currentTime.isBefore(closingTime)) {
            	if (currentTime.isAfter(now)) {
                    buttons.add(currentTime.toString());
                }
                currentTime = currentTime.plusHours(1); // 增加一小時
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buttons;
    }

}
