package mvc.entity;

import java.time.LocalTime;
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
	private String dayOfWeek;
	private LocalTime openingTime;
	private LocalTime closingTime;
}
