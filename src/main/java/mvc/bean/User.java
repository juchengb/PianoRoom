package mvc.bean;

import com.google.gson.Gson;

public class User {
	private Integer id;
	private String name;
	private String password;
	private String email;
	private Integer majorId;
	private MajorData major;
	private Integer levelId;
	
	public User() {};
	
	public User(Integer id, String name, String password, String email, Integer majorId, MajorData major,
			Integer levelId) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.majorId = majorId;
		this.major = major;
		this.levelId = levelId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getMajorId() {
		return majorId;
	}

	public void setMajorId(Integer majorId) {
		this.majorId = majorId;
	}

	public MajorData getMajor() {
		return major;
	}

	public void setMajor(MajorData major) {
		this.major = major;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	
	
	
}
