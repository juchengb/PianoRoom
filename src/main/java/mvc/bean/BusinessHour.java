package mvc.bean;

import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHour {
	private Integer roomId;
	private String dayofWeek;
	private LocalTime openingTime;
	private LocalTime closingTime;
}
