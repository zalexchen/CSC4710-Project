import java.util.Date;

public class User {
	protected String email;
	protected String password;
	protected Date bday;
	protected String firstName;
	protected String lastName;
	protected String gender;
	
	public User(String email,String password, Date bday, String firstName, String lastName, String gender) {
		this.email = email;
		this.password = password;
		this.bday = bday;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
}
