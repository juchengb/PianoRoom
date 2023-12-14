package mvc.bean;

import com.google.gson.Gson;

public class Room {
	private Integer id;
	private String name;
	private Integer typeId;
	private RoomTypeData type;
	private Integer capacity;
	private Double latitude;
	private Double longitude;
	
	public Room() {};
	
	public Room(Integer id, String name, Integer typeId, RoomTypeData type, Integer capacity, Double latitude,
			Double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.type = type;
		this.capacity = capacity;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public RoomTypeData getType() {
		return type;
	}

	public void setType(RoomTypeData type) {
		this.type = type;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	
	
	
}
