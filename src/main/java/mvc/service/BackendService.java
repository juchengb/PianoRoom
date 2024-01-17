package mvc.service;

import mvc.model.dto.EditRoom;
import mvc.model.po.Room;

public interface BackendService {

	public Room convertToRoomEntity(EditRoom addRoom);
	public int addBusinessHours(EditRoom addRoom, Room roomEntity);
	public String getDayOfWeek(int dayIndex);
}
