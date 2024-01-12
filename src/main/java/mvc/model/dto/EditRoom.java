package mvc.model.dto;

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
public class EditRoom {
	private Integer id;
	
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
}
