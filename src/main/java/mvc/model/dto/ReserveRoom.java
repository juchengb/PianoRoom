package mvc.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.model.po.BusinessHour;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveRoom {

	private Integer id;
    private String name;
    private String dist;
    private String type;
    private String image;
    
    private BusinessHour businessHour;
    private List<ReserveButton> reserveButtonList;
    
}
