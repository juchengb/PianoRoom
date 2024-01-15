package mvc.model.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddRoom {

	@NotNull
	private String name;
	
	@NotNull
	private String dist;

	@NotNull
	private String type;
	
	@NotNull
	private Double latitude;
	
	@NotNull
	private Double longitude;
	
	private MultipartFile image;
	
	private List<String> openingTime;
	private List<String> closingTime;
}
