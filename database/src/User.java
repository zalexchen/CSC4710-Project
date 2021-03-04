import java.sql.Date;

public class User {
	protected String email;
	protected String password;
	protected Date birthday;
	protected String firstName;
	protected String lastName;
	protected String gender;
	protected int numFollowers;
	protected int numFollowing;
	
	public User(String email,String password, Date birthday, String firstName, String lastName, String gender) {
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.numFollowers = 0;
		this.numFollowing = 0;
	}
	
	public User(String email,String password, Date birthday, String firstName, String lastName, String gender, int numFollowers, int numFollowing) {
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.numFollowers = numFollowers;
		this.numFollowing = numFollowing;
	}
	
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getGender() {
		return gender;
	}
	public int getNumFollowers() {
		return numFollowers;
	}
	public int getNumFollowing() {
		return numFollowing;
	}
}
