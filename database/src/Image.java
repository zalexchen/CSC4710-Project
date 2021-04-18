import java.sql.Date;
import java.sql.Timestamp;

public class Image implements Comparable<Image> {
	protected int imageid;
	protected String url;
	protected String description;
	protected String postuser;
	protected Date postdate;
	protected Timestamp posttime;
	
	protected int numLikes;
	
	public Image(int imageid, String url, String description, String postuser, Date postdate, Timestamp posttime, int numLikes) {
		this(url, description, postuser, postdate, posttime);
		this.imageid = imageid;
		this.numLikes = numLikes;
	}
	
	public Image(int imageid, String url, String description, String postuser, Date postdate, Timestamp posttime) {
		this(url, description, postuser, postdate, posttime);
		this.imageid = imageid;
		this.numLikes = 0;
	}
	
	public Image(String url, String description, String postuser, Date postdate, Timestamp posttime) {
		this.url = url;
		this.description = description;
		this.postuser = postuser;
		this.postdate = postdate;
		this.posttime = posttime;
		this.numLikes = 0;
	}
	
	public Image(int imageid, String url, String description, String postuser) {
		this.imageid = imageid;
		this.url = url;
		this.description = description;
		this.postuser = postuser;
		this.numLikes = 0;
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
	
	public int getNumLikes() {
		return numLikes;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		int result = this.posttime.compareTo(((Image)o).posttime);
		return result == 0;
	}
	
	@Override
	public int compareTo(Image I) {
	    if (this.posttime == null || I.posttime == null) {
	      return 0;
	    }
	    //ensure no ambiguity in order of treemap
	    if(this.posttime.compareTo(I.posttime) == 0) {
	    	return -1;
	    }
	    return this.posttime.compareTo(I.posttime);
	}
}
