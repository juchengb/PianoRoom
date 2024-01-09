package mvc.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mvc.entity.BusinessHour;

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
    
    private List<BusinessHour> businessHour;

    private List<ReserveButton> reserveButtonList;
}
