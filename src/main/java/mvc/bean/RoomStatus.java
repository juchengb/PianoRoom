package mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomStatus {
	private Integer id;
	private String name;
	private String dist;
	private String type;
	private Double latitude;
	private Double longitude;
	private String status;
}
