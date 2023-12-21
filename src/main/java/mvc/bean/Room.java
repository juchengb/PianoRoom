package mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
	private Integer id;
	private String dist;
	private String name;
	private String type;
	private Double latitude;
	private Double longitude;
}
