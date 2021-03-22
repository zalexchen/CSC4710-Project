import java.sql.Date;
import java.sql.Timestamp;

public class Image {
	protected int imageid;
	protected String url;
	protected String description;
	protected String postuser;
	protected Date postdate;
	protected Timestamp posttime;
	
	public Image(int imageid, String url, String description, String postuser, Date postdate, Timestamp posttime) {
		this(url, description, postuser, postdate, posttime);
		this.imageid = imageid;
	}
	
	public Image(String url, String description, String postuser, Date postdate, Timestamp posttime) {
		this.url = url;
		this.description = description;
		this.postuser = postuser;
		this.postdate = postdate;
		this.posttime = posttime;
	}
	
	public Image(int imageid, String url, String description, String postuser) {
		this.imageid = imageid;
		this.url = url;
		this.description = description;
		this.postuser = postuser;
	}
	
	public int getImageid() {
		return imageid;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPostuser() {
		return postuser;
	}
	
	public Date getPostdate() {
		return postdate;
	}
	
	public Timestamp getPosttime() {
		return posttime;
	}
}
