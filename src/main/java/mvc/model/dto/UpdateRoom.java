package mvc.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoom {
	private Integer id;
	private String name;
	private String dist;
	private String type;
	private Double latitude;
	private Double longitude;
	private MultipartFile image;
}
