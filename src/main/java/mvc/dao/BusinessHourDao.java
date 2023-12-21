package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.BusinessHour;

public interface BusinessHourDao {
	int addBusinessHour(BusinessHour businessHour);
	int updateBusinessHourByRoomId(Integer roomId, BusinessHour businessHour);
	int deleteBusinessHourByRoomId(Integer roomId);
	Optional<BusinessHour> getBusinessHourByRoomId(Integer roomId);
//	List<BusinessHour> findAllBusinessHours();
}
