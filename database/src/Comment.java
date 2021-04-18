import java.sql.Date;
import java.sql.Timestamp;

public class Comment {
	protected String email;
	protected int imageid;
	protected String description;
	
	public Comment(String email, int imageid, String description) {
		this.email = email;
		this.imageid = imageid;
		this.description = description;
	}

	public int getImageid() {
		return imageid;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getEmail() {
		return email;
	}
}
