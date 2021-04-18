import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 * Servlet implementation class Connect
 */
@WebServlet("/UserDAO")
public class UserDAO {     
	private static final long serialVersionUID = 1L; //not used
	private Connection connect = null; //jdbc Connection
	private Statement statement = null; 
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	public UserDAO() {

    }
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    protected void connect_func() throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
  			      .getConnection("jdbc:mysql://127.0.0.1:3306/testdb?"
  			          + "useSSL=false&user=john&password=pass1234");
            System.out.println(connect);
        }
    }
    
    //Utility function to disconnect from the database server
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
    public List<User> listAllUsers() throws SQLException {
        List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM Users";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
            System.out.println("Added a user to listUsers");
        }
        statement.close();
        System.out.println("Returning listUsers");
        return listUsers;
    }
    
    public User getSingleUser(String email, String password) throws SQLException {
    	System.out.println("Inside getSingleUser in UserDAO");
    	connect_func();
    	String sql1 = "SELECT * FROM Users U " +
    				"WHERE email = ? AND password = ?";
    	
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setString(2, password);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Statement Executed");
    	resultSet.next();	
    	
    	Date birthday = resultSet.getDate("birthday");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String gender = resultSet.getString("gender");
        int numFollowers = resultSet.getInt("numFollowers");
        int numFollowing = resultSet.getInt("numFollowing");
        System.out.println("Returning the single user with email: " + email);
        return new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
    }
    
    public List<User> listSelectedUsers(String text, String parameter) throws SQLException {
    	System.out.println("Inside listSelectedUsers in UserDAO");
    	List<User> listUsers = new ArrayList<User>();
    	String sql1;
    	connect_func();
    	
    	if(parameter.equals("First Name")) {
    		System.out.println("Selecting by firstName");
    		sql1 = "SELECT * FROM Users U " +
    				"WHERE firstName = ? ";
    		preparedStatement = connect.prepareStatement(sql1);
        	preparedStatement.setString(1, text);
    	}
    	else if(parameter.equals("Last Name")) {
    		System.out.println("Selecting by lastName");
    		sql1 = "SELECT * FROM Users U " +
    				"WHERE lastName = ?";
    		preparedStatement = connect.prepareStatement(sql1);
        	preparedStatement.setString(1, text);
    	}
    	else {
    		System.out.println("Selecting by both first and last name");
    		sql1 = "SELECT * FROM Users U " +
    				"Where firstName = ? OR lastName = ?";
    		preparedStatement = connect.prepareStatement(sql1);
        	preparedStatement.setString(1, text);
        	preparedStatement.setString(2, text);
    	}
    	System.out.println("Statement prepared");
    	ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Statement executed");
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
            System.out.println("Added a user to listUsers");
        }        
        System.out.println("End listSelectedUsers in UserDAO");
    	return listUsers;
    }
    
    public Image getSingleImage(int imageid) throws SQLException {
    	System.out.println("Inside getSingleImage in UserDAO");
    	connect_func();
    	String sql1 = "SELECT * FROM Images I " +
    				"WHERE imageid = ?";
    	
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setInt(1, imageid);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Statement Executed");
    	resultSet.next();	
    	
    	String url = resultSet.getString("url");
    	String description = resultSet.getString("description");
    	String postuser = resultSet.getString("postuser");
    	Date postdate = resultSet.getDate("postdate");
    	Timestamp posttime = resultSet.getTimestamp("posttime");
    	
        System.out.println("Returning the single user with email: " + postuser);
        return new Image(imageid, url, description, postuser, postdate, posttime);
    }
    
    public List<Image> listSelectedImages(String email) throws SQLException {
    	System.out.println("Inside listSelectedImages in UserDAO");
    	List<Image> listImage = new ArrayList<Image>();
    	connect_func();
    	String sql1 = "SELECT * FROM Images " +
    			"WHERE postuser = ? OR postuser IN " +
    			"(SELECT followingemail FROM follow " +
    			"WHERE followeremail = ?)";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setString(2, email);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Execute Query");
    	
    	while(resultSet.next()) {
    		int imageid = resultSet.getInt("imageid");
    		String url = resultSet.getString("url");
    		String description = resultSet.getString("description");
    		String postuser = resultSet.getString("postuser");
    		Date postdate = resultSet.getDate("postdate");
    		Timestamp posttime = resultSet.getTimestamp("posttime");
    		int numLikes = resultSet.getInt("numLikes");
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime, numLikes);
    		listImage.add(newImage);
    		System.out.println("Added an image to listImage");
    	}
    	System.out.println("End listSelectedImages in UserDAO");
    	return listImage;
    	
    }
    
    public void initializeDatabase() throws SQLException {
    	System.out.println("initDB Start");
    	connect_func();
    	dropDatabase();
    	initializeTenUsers();
    	initializeImages();
    	initializeLikes();
    	initializeComments();
    	initializeTag();
    	initializeFollow();
    	updateFollowerCount();
    	updateLikeCount();
    	System.out.println("initDB End");
    }
    
    public void dropDatabase() throws SQLException {
    	System.out.println("dropDatabase Start");
    	String sql1 = "DROP TABLE IF EXISTS Tags";
    	String sql2 = "DROP TABLE IF EXISTS Comments";
    	String sql3 = "DROP TABLE IF EXISTS Follow";
    	String sql4 = "DROP TABLE IF EXISTS Likes";
    	String sql5 = "DROP TABLE IF EXISTS Images";
    	String sql6 = "DROP TABLE IF EXISTS Users";
    	
    	statement = connect.createStatement();
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);
        statement.executeUpdate(sql3);
        statement.executeUpdate(sql4);
        statement.executeUpdate(sql5);
        statement.executeUpdate(sql6);
        
        System.out.println("dropDatabase End");
    }
    
    public void initializeImages() throws SQLException {
    	System.out.println("initImages Start");
    	String sql1 = "DROP TABLE IF EXISTS Images";
        String sql2 = "CREATE TABLE IF NOT EXISTS Images(" +
                " imageid MEDIUMINT NOT NULL AUTO_INCREMENT, " + 
                " url VARCHAR(150), " + 
                " description VARCHAR(100), " + 
                " postuser VARCHAR(100) NOT NULL, " +
                " postdate DATE, " +
                " posttime DATETIME, " +
                " numLikes INTEGER, " +
                " PRIMARY KEY(imageid), " +
                " FOREIGN KEY(postuser) references Users(email))";
        
        String sql3 = "INSERT INTO Images(imageid, url, description, postuser, postdate, posttime, numLikes) VALUES"
        		+ " (1, 'https://picsum.photos/id/1/200/300', '1st Picture', 'test@wayne', STR_TO_DATE('17-04-2001','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(2, 'https://picsum.photos/id/2/200/300', '2nd Picture', 'guy@wayne', STR_TO_DATE('17-04-2021','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(3, 'https://picsum.photos/id/3/200/300', '3rd Picture', 'something@gmail', STR_TO_DATE('18-04-2021','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(4, 'https://picsum.photos/id/4/200/300', '4th Picture', 'test@wayne', STR_TO_DATE('17-04-2001','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(5, 'https://picsum.photos/id/5/200/300', '5th Picture', 'guy@wayne', STR_TO_DATE('17-04-2021','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(6, 'https://picsum.photos/id/6/200/300', '6th Picture', 'something@gmail', STR_TO_DATE('17-04-2001','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(7, 'https://picsum.photos/id/7/200/300', '7th Picture', 'alex@outlook', STR_TO_DATE('18-04-2021','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(8, 'https://picsum.photos/id/8/200/300', '8th Picture', 'person@protect', STR_TO_DATE('17-04-2001','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(9, 'https://picsum.photos/id/9/200/300', '9th Picture', 'programmer@gmail', STR_TO_DATE('18-04-2021','%d-%m-%Y'), CURDATE(), 0),"
        		+ "(10, 'https://picsum.photos/id/10/200/300', '10th Picture', 'random@gmail', STR_TO_DATE('17-04-2001','%d-%m-%Y'), CURDATE(), 0)";

        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        System.out.println("Drop table");
        statement.executeUpdate(sql2);
        System.out.println("Created table");
        statement.executeUpdate(sql3);
        System.out.println("Inserted table");
        
        System.out.println("initImages End");
    }
    
    public void initializeLikes() throws SQLException {
    	System.out.println("initLikes Start");
    	String sql1 = "DROP TABLE IF EXISTS Likes";
        String sql2 = "CREATE TABLE IF NOT EXISTS Likes(" +
                " email VARCHAR(100) NOT NULL," + 
                " imageid MEDIUMINT NOT NULL, " + 
                " likedate DATE, " +
                " PRIMARY KEY(email, imageid), " +
                " FOREIGN KEY(email) references Users(email), " +
                " FOREIGN KEY(imageid) references Images(imageid))";
        String sql3 = "INSERT INTO Likes(email, imageid, likedate) VALUES"
        		+ " ('test@wayne', 1, CURDATE()),"
        		+ " ('guy@wayne', 1, CURDATE()),"
        		+ " ('something@gmail', 1, CURDATE()),"
        		+ " ('random@gmail', 1, CURDATE()),"
        		+ " ('alex@outlook', 1, CURDATE()),"
        		+ " ('aliveguy@wayne', 1, CURDATE()),"
        		+ " ('person@protect', 1, CURDATE()),"
        		+ " ('bodyguard@protect', 1, CURDATE()),"
        		+ " ('progamer@gmail', 1, CURDATE()),"
        		+ " ('programmer@gmail', 1, CURDATE())";
        
        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        System.out.println("Dropped Table");
        statement.executeUpdate(sql2);
        System.out.println("Create Likes");
        statement.executeUpdate(sql3);
        System.out.println("Insert Likes");
        
        System.out.println("initLikes End");
    }
    
    public void updateLikeCount() throws SQLException {
    	System.out.println("Inside updateLikeCount in UserDAO");
    	//connect_func();
    	System.out.println("Passed connect_func()");
    	String sql1 = "SELECT imageid, count(*) FROM likes GROUP BY imageid";
    	statement = connect.createStatement();
    	ResultSet resultSet = statement.executeQuery(sql1);
    	while(resultSet.next()) {
    		String sql = "UPDATE images set numLikes = ? WHERE imageid = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setInt(1, resultSet.getInt("count(*)"));
    		preparedStatement.setInt(2, resultSet.getInt("imageid"));
    		preparedStatement.executeUpdate();
    		preparedStatement.close();
    	}
    	
    	String sql2 = "SELECT imageid FROM images WHERE imageid NOT IN (SELECT imageid FROM likes)";
    	statement = connect.createStatement();
    	resultSet = statement.executeQuery(sql2);
    	while(resultSet.next()) {
    		String sql = "UPDATE images set numLikes = 0 WHERE imageid = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setInt(1, resultSet.getInt("imageid"));
    		preparedStatement.executeUpdate();
    		preparedStatement.close();
    	}
    	
    	System.out.println("Update Like Count Complete");
    }
    
    public void initializeComments() throws SQLException {
    	System.out.println("initComments Start");
    	String sql1 = "DROP TABLE IF EXISTS Comments";
        String sql2 = "CREATE TABLE IF NOT EXISTS Comments(" +
                " email VARCHAR(100) NOT NULL," + 
                " imageid MEDIUMINT NOT NULL, " + 
                " description VARCHAR(500), " +
                " PRIMARY KEY(email, imageid), " +
                " FOREIGN KEY(email) references Users(email), " +
                " FOREIGN KEY(imageid) references Images(imageid))";
        String sql3 = "INSERT INTO Comments(email, imageid, description) VALUES"
        		+ " ('test@wayne', 1, 'Hello'),"
        		+ " ('guy@wayne', 1, 'World'),"
        		+ " ('something@gmail', 1, 'Pizza'),"
        		+ " ('random@gmail', 1, 'Is'),"
        		+ " ('alex@outlook', 1, 'Pretty'),"
        		+ " ('aliveguy@wayne', 1, 'Good'),"
        		+ " ('person@protect', 1, 'Unless'),"
        		+ " ('bodyguard@protect', 1, 'Its'),"
        		+ " ('progamer@gmail', 1, 'Pineapple'),"
        		+ " ('programmer@gmail', 1, 'Pizza')";
        
        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);
        statement.executeUpdate(sql3);
        
        System.out.println("initComments End");
    }
    
    public void initializeTag() throws SQLException {
    	System.out.println("initTag Start");
    	String sql1 = "DROP TABLE IF EXISTS Tags";
        String sql2 = "CREATE TABLE IF NOT EXISTS Tags(" +
                " imageid MEDIUMINT NOT NULL, " + 
                " tag VARCHAR(20) NOT NULL, " + 
                " PRIMARY KEY(imageid, tag), " +
                " FOREIGN KEY(imageid) references Images(imageid))";
        String sql3 = "INSERT INTO Tags(imageid, tag) VALUES"
        		+ " (1, 'Coffee'),"
        		+ " (2, 'Book'),"
        		+ " (3, 'Awesome'),"
        		+ " (4, 'Sick'),"
        		+ " (5, 'Chill'),"
        		+ " (6, 'Something'),"
        		+ " (7, 'Tagged'),"
        		+ " (8, 'Famous'),"
        		+ " (9, 'Cool'),"
        		+ " (10, 'People')";
        
        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);
        statement.executeUpdate(sql3);
        
        System.out.println("initTag End");
    }
    
    public void initializeFollow() throws SQLException {
    	System.out.println("initFollow Start");
    	String sql1 = "DROP TABLE IF EXISTS Follow";
        String sql2 = "CREATE TABLE IF NOT EXISTS Follow(" +
                " followingemail VARCHAR(100) NOT NULL," + 
                " followeremail VARCHAR(100) NOT NULL, " +
                " PRIMARY KEY(followingemail, followeremail), " +
                " FOREIGN KEY(followingemail) references Users(email), " +
                " FOREIGN KEY(followeremail) references Users(email))";
        String sql3 = "INSERT INTO Follow(followingemail, followeremail) VALUES"
        		+ " ('test@wayne', 'guy@wayne'),"
        		+ " ('test@wayne', 'something@gmail'),"
        		+ " ('test@wayne', 'random@gmail'),"
        		+ " ('test@wayne', 'alex@outlook'),"
        		+ " ('test@wayne', 'aliveguy@wayne'),"
        		+ " ('test@wayne', 'person@protect'),"
        		+ " ('test@wayne', 'bodyguard@protect'),"
        		+ " ('test@wayne', 'progamer@gmail'),"
        		+ " ('test@wayne', 'programmer@gmail'),"
        		+ " ('guy@wayne', 'test@wayne')";
        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);
        statement.executeUpdate(sql3);
        
        System.out.println("Initializing Follow End");
    }
    
    public void updateFollowerCount() throws SQLException {
    	System.out.println("Inside updateFollowerCount in UserDAO");
    	//connect_func();
    	String sql1 = "SELECT followingemail, count(followeremail) FROM follow GROUP BY followingemail";
    	statement = connect.createStatement();
    	ResultSet resultSet = statement.executeQuery(sql1);
    	while(resultSet.next()) {
    		//result will be email : count(followeremail)
    		//update follower count for the emails listed
    		String sql = "UPDATE users set numFollowers = ? WHERE email = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setInt(1, resultSet.getInt("count(followeremail)"));
    		preparedStatement.setString(2, resultSet.getString("followingemail"));
    		preparedStatement.executeUpdate();
    		preparedStatement.close();
    	}
    	
    	String sql2 = "SELECT followeremail, count(followingemail) FROM follow GROUP BY followeremail";
    	resultSet = statement.executeQuery(sql2);
    	while(resultSet.next()) {
    		//update following count for the emails listed
    		String sql = "UPDATE users set numFollowing = ? WHERE email = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setInt(1, resultSet.getInt("count(followingemail)"));
    		preparedStatement.setString(2, resultSet.getString("followeremail"));
    		preparedStatement.executeUpdate();
    		preparedStatement.close();
    	}
    	System.out.println("Update Following and Follower count complete");
    }
    
    public void initializeTenUsers() throws SQLException {
    	System.out.println("Initializing Users Start");
    	//connect_func();
    	String sql1 = "DROP TABLE IF EXISTS Users";
        String sql2 = "CREATE TABLE IF NOT EXISTS Users(" +
                " email VARCHAR(100) NOT NULL, " + 
                " password VARCHAR(20), " + 
                " birthday date, " + 
                " firstName VARCHAR(50), " +
                " lastName VARCHAR(50), " +
                " gender CHAR(1), " +
                " numFollowers INTEGER, " +
                " numFollowing INTEGER, " +
                " PRIMARY KEY ( email ))";
        String sql3 = "INSERT INTO Users(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing) VALUES"
        		+ " ('test@wayne', 'test1234', CURDATE(), 'Test', 'Guy', 'M', 0, 0),"
        		+ " ('guy@wayne', 'password', '2012-12-12', 'Guy', 'Person', 'F', 0, 0),"
        		+ "('something@gmail', 'password1234', '2011-11-11', 'Something', 'Person', 'F', 0, 0),"
        		+ "('random@gmail', 'password5678', '2000-10-10', 'Random', 'Person', 'M', 0, 0),"
        		+ "('alex@outlook', 'alex12321', '2001-09-14', 'Alex', 'Chen', 'M', 0, 0),"
        		+ "('aliveguy@wayne', 'alive12321', CURDATE(), 'Alive', 'Guy', 'O', 0, 0),"
        		+ "('person@protect', 'password333', CURDATE(), 'Person', 'Protect', 'O', 0, 0),"
        		+ "('bodyguard@protect', 'bodyguard', CURDATE(), 'Body', 'Guard', 'O', 0, 0),"
        		+ "('progamer@gmail', 'easypassword', CURDATE(), 'Pro', 'Gamer', 'M', 0, 0),"
        		+ "('programmer@gmail', 'hardpassword', CURDATE(), 'Pro', 'Grammer', 'F', 0, 0)";
        statement = connect.createStatement();
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);
        statement.executeUpdate(sql3);
        
        System.out.println("Initializing Users End");
    }
    
    public boolean loginAuthentication(String email, String password) throws SQLException {
    	System.out.println("LoginAuthentication() in UserDAO");
    	//if email and password combination is in database, then return true
    	connect_func();
    	
    	System.out.println("Got email: " + email + " and password: " + password);
    	String sql1 = "SELECT * FROM Users U " +
    				"WHERE email = ? AND password = ?";
    	
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setString(2, password);
    	System.out.println("Statement prepared");
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Executed statement");
    	if(resultSet.next()) {
    		System.out.println("Email: " + resultSet.getString("email"));
    		System.out.println("loginAuthentication Returned True");
    		return true;
    	}
    	System.out.println("loginAuthentication Returned False");
    	return false;
    }
    
    public boolean registerUser(User user) throws SQLException {
    	System.out.println("Got to registerUser() in UserDAO");
    	connect_func();
		String sql1 = "INSERT into Users(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing) values (?, ?, ?, ?, ?, ?, 0, 0)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
		preparedStatement.setString(1, user.email);
		preparedStatement.setString(2, user.password);
		preparedStatement.setDate(3, user.birthday);
		preparedStatement.setString(4, user.firstName);
		preparedStatement.setString(5, user.lastName);
		if(user.gender.equals("Male")) {
			preparedStatement.setString(6, "M");
		} else if (user.gender.equals("Female")) {
			preparedStatement.setString(6, "F");
		} else {
			preparedStatement.setString(6, "O");
		}
		try {
			System.out.println("Setup complete");
	        boolean userRegistered = preparedStatement.executeUpdate() > 0;
	        System.out.println("register SQL Executed");
	        preparedStatement.close();
	        System.out.println("End registerUser() in UserDAO");
	        return userRegistered;
		}
		catch (SQLIntegrityConstraintViolationException e){
			System.out.println("Account information already exists");
			return false;
		}
    }
    
    public boolean postImage(Image image, List<String> tags) throws SQLException {
    	System.out.println("Inside postImage in UserDAO");
    	connect_func();
    	// Before insert, check if user has posted 5 already today
    	String sql = "SELECT postuser FROM images WHERE postdate = CURDATE() GROUP BY postuser HAVING count(*) >= 5";
    	statement = connect.createStatement();
    	ResultSet resultSet = statement.executeQuery(sql);
    	List<String> maxedUsers = new ArrayList<String>();
    	while(resultSet.next()) {
    		maxedUsers.add(resultSet.getString("postuser"));
    	}
    	if (maxedUsers.contains(image.postuser)) {
    		System.out.println("CANNOT INSERT NEW IMAGE: POST LIMIT REACHED");
    		return false;
    	}
    	else {
			String sql1 = "INSERT INTO images(url, description, postuser, postdate, posttime, numLikes) values (?, ?, ?, ?, ?, ?)";
			preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
			preparedStatement.setString(1, image.url);
			preparedStatement.setString(2, image.description);
			preparedStatement.setString(3, image.postuser);
			preparedStatement.setDate(4, image.postdate);
			preparedStatement.setTimestamp(5, image.posttime);
			preparedStatement.setInt(6, image.numLikes);
	//		preparedStatement.executeUpdate();
	        boolean rowInserted = preparedStatement.executeUpdate() > 0;
	        System.out.println("Statement Executed");
	        preparedStatement.close();
	        
	        System.out.println("Now inserting tags for image... (First getting imageid of new image)");
	        for(int i = 0; i < tags.size(); i++) {
	            String sql3 = "SELECT last_insert_id()";
	            statement = connect.createStatement();
	            resultSet = statement.executeQuery(sql3);
	            if(resultSet.next()) {
	            	insertTag(resultSet.getInt(1), tags.get(i));
	            }
	        }
	//        disconnect();
	        return rowInserted;
    	}
    }
    
    public boolean editImage(Image image, List<String> tags) throws SQLException {
    	System.out.println("Inside editImage in UserDAO");
        String sql1 = "UPDATE images set url= ?, description= ? where imageid = ?";
        connect_func();
        System.out.println("Image ID gotten: " + image.imageid);
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
        preparedStatement.setString(1, image.url);
        preparedStatement.setString(2, image.description);
        preparedStatement.setInt(3, image.imageid);
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        System.out.println("Statement Executed: " + rowUpdated);
        preparedStatement.close();
        
        System.out.println("Updating tags...");
        //First Delete then Insert new
        String sql2 = "DELETE FROM tags WHERE imageid = ?";
        preparedStatement = connect.prepareStatement(sql2);
        preparedStatement.setInt(1, image.imageid);
        preparedStatement.executeUpdate();
        
        System.out.println("Now inserting tags for image...");
        for(int i = 0; i < tags.size(); i++) {
            insertTag(image.imageid, tags.get(i));
        }
        
//        disconnect();
        return rowUpdated;     
    }
    
    public List<String> getTagsFromSingleImage(int imageid) throws SQLException {
    	System.out.println("Inside getTagsFromSingleImage");
    	List<String> listTags = new ArrayList<String>();
    	connect_func();
    	String sql1 = "SELECT tag FROM tags WHERE imageid = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setInt(1, imageid);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	while(resultSet.next()) {
    		System.out.println("Got tag, adding to list");
    		listTags.add(resultSet.getString("tag"));
    	}
    	return listTags;
    }
    
    public List<String> listCurrentUserFollowing(String currentUserEmail) throws SQLException {
    	System.out.println("Inside listCurrentUserFollowing in UserDAO");
    	List<String> listFollowing = new ArrayList<String>();
    	connect_func();
    	String sql1 = "SELECT followingemail FROM follow " +
    			"WHERE followeremail = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, currentUserEmail);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Statement Executed");
    	while(resultSet.next()) {
    		listFollowing.add(resultSet.getString("followingemail"));
    	}
    	return listFollowing;
    }
    
    public boolean followUser(String followingemail, String followeremail) throws SQLException {
    	System.out.println("Inside followUser in UserDAO");
    	connect_func();
    	String sql1 = "INSERT INTO follow(followingemail, followeremail) values (?, ?)";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, followingemail);
    	preparedStatement.setString(2, followeremail);
    	
    	boolean newEntry = preparedStatement.executeUpdate() > 0;
    	System.out.println("Statement Executed: " + newEntry);
    	preparedStatement.close();
    	updateFollowerCount();
    	return newEntry;
    }
    
    public boolean unfollowUser(String followingemail, String followeremail) throws SQLException {
    	System.out.println("Inside deleteFollowUser in UserDAO");
    	connect_func();
    	String sql1 = "DELETE FROM follow WHERE followingemail = ? AND followeremail = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, followingemail);
    	preparedStatement.setString(2, followeremail);
    	
    	boolean newEntry = preparedStatement.executeUpdate() > 0;
    	System.out.println("Statement Executed: " + newEntry);
    	preparedStatement.close();
    	updateFollowerCount();
    	return newEntry;
    }
    
    public List<Integer> listCurrentUserLikedImageIds(String currentUserEmail) throws SQLException{
    	System.out.println("Inside listCurrentUserLikedImageIds in UserDAO");
    	List<Integer> listLikedImageIds = new ArrayList<Integer>();
    	connect_func();
    	String sql1 = "SELECT imageid FROM likes " +
    			"WHERE email = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, currentUserEmail);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Statement Executed");
    	while(resultSet.next()) {
    		listLikedImageIds.add(resultSet.getInt("imageid"));
    	}
    	return listLikedImageIds;
    }
    
    public boolean likeImage(String email, int imageid) throws SQLException {
    	System.out.println("Inside likeImage in UserDAO");
    	connect_func();
    	// Before insert into likes check if like limit is reached
    	String sql = "SELECT email FROM likes WHERE likedate = CURDATE() GROUP BY email HAVING count(imageid) >= 3";
    	statement = connect.createStatement();
    	ResultSet resultSet = statement.executeQuery(sql);
    	List<String> maxedUsers = new ArrayList<String>();
    	while(resultSet.next()) {
    		maxedUsers.add(resultSet.getString("email"));
    	}
    	if (maxedUsers.contains(email)) {
    		System.out.println("CANNOT LIKE IMAGE: LIKE LIMIT REACHED");
    		return false;
    	}
    	String sql1 = "INSERT INTO likes(email, imageid, likedate) values (?, ?, ?)";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setInt(2, imageid);
    	Date currentDate = Date.valueOf(LocalDate.now());
    	preparedStatement.setDate(3, currentDate);
    	
    	boolean newEntry = preparedStatement.executeUpdate() > 0;
    	System.out.println("Statement Executed: " + newEntry);
    	preparedStatement.close();
    	updateLikeCount();
    	return newEntry;
    }
    
    public boolean unlikeImage(String email, int imageid) throws SQLException {
    	System.out.println("Inside unlikeImage in UserDAO");
    	connect_func();
    	String sql1 = "DELETE FROM likes WHERE email = ? AND imageid = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setInt(2, imageid);
    	
    	boolean newEntry = preparedStatement.executeUpdate() > 0;
    	System.out.println("Statement Executed: " + newEntry);
    	preparedStatement.close();
    	updateLikeCount();
    	return newEntry;
    }
    
    public boolean deleteImage(int imageid) throws SQLException {
    	System.out.println("Inside deleteImage in UserDAO");
    	connect_func();
    	
    	//Need to first remove likes from image before removing the image!
    	String sql1 = "DELETE FROM likes WHERE imageid = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setInt(1, imageid);
    	preparedStatement.executeUpdate();
    	System.out.println("Deleted Likes");
    	
    	String sql2 = "DELETE FROM tags WHERE imageid = ?";
    	preparedStatement = connect.prepareStatement(sql2);
    	preparedStatement.setInt(1, imageid);
    	preparedStatement.executeUpdate();
    	System.out.println("Deleted Tags");
    	
    	String sql3 = "DELETE FROM comments WHERE imageid = ?";
    	preparedStatement = connect.prepareStatement(sql3);
    	preparedStatement.setInt(1, imageid);
    	preparedStatement.executeUpdate();
    	System.out.println("Deleted Comments");
    	
        String sql4 = "DELETE FROM images WHERE imageid = ?";
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql4);
        preparedStatement.setInt(1, imageid);
        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        System.out.println("Deleted!: " + rowDeleted);
        preparedStatement.close();
//        disconnect();
        return rowDeleted;
    }

    public boolean insertTag(int imageid, String tag) throws SQLException {
    	System.out.println("Inside insertTag in UserDAO");
    	connect_func();         
		String sql1 = "INSERT INTO tags(imageid, tag) values (?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
		preparedStatement.setInt(1, imageid);
		preparedStatement.setString(2, tag);
		
        boolean rowInserted = preparedStatement.executeUpdate() > 0;
        System.out.println("Statement Executed");
        preparedStatement.close();
//        disconnect();
        return rowInserted;
    }
    
    /*
     * Project Part 3 Start:
     */
    
    public List<Image> coolImages() throws SQLException {
    	System.out.println("Inside coolImages() in UserDAO");
    	List<Image> listImage = new ArrayList<Image>();
    	connect_func();
    	String sql1 = "SELECT * FROM images WHERE imageid in (SELECT imageid FROM likes GROUP BY imageid HAVING count(email) >= 5)";
    	statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
    	
    	while(resultSet.next()) {
    		int imageid = resultSet.getInt("imageid");
    		String url = resultSet.getString("url");
    		String description = resultSet.getString("description");
    		String postuser = resultSet.getString("postuser");
    		Date postdate = resultSet.getDate("postdate");
    		Timestamp posttime = resultSet.getTimestamp("posttime");
    		int numLikes = resultSet.getInt("numLikes");
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime, numLikes);
    		listImage.add(newImage);
    		System.out.println("Added an image to listImage in coolImages()");
    	}
    	System.out.println("End coolImages() in UserDAO");
    	return listImage;
    }
    
    public List<Image> newImages() throws SQLException {
    	System.out.println("Inside newImages() in UserDAO");
    	List<Image> listImage = new ArrayList<Image>();
    	connect_func();
    	String sql1 = "SELECT * FROM images WHERE postdate = CURDATE()";
    	statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
    	
    	while(resultSet.next()) {
    		int imageid = resultSet.getInt("imageid");
    		String url = resultSet.getString("url");
    		String description = resultSet.getString("description");
    		String postuser = resultSet.getString("postuser");
    		Date postdate = resultSet.getDate("postdate");
    		Timestamp posttime = resultSet.getTimestamp("posttime");
    		int numLikes = resultSet.getInt("numLikes");
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime, numLikes);
    		listImage.add(newImage);
    		System.out.println("Added an image to listImage in newImages()");
    	}
    	System.out.println("End newImages() in UserDAO");
    	return listImage;
    }
    
    public List<Image> viralImages() throws SQLException {
    	System.out.println("Inside viralImages() in UserDAO");
    	List<Image> listImage = new ArrayList<Image>();
    	connect_func();
    	String sql1 = "SELECT * FROM images ORDER BY numLikes DESC LIMIT 3";
    	statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
    	
    	while(resultSet.next()) {
    		int imageid = resultSet.getInt("imageid");
    		String url = resultSet.getString("url");
    		String description = resultSet.getString("description");
    		String postuser = resultSet.getString("postuser");
    		Date postdate = resultSet.getDate("postdate");
    		Timestamp posttime = resultSet.getTimestamp("posttime");
    		int numLikes = resultSet.getInt("numLikes");
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime, numLikes);
    		listImage.add(newImage);
    		System.out.println("Added an image to listImage in viralImages()");
    	}
    	System.out.println("End viralImages() in UserDAO");
    	return listImage;
    }
    
    public List<User> topUsers() throws SQLException {
    	List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM users WHERE email in ("
        		+ "SELECT postuser FROM images GROUP BY postuser HAVING count(imageid) in ("
        		+ "SELECT MAX(Total) FROM (SELECT count(*) AS Total FROM images GROUP BY postuser) AS Results"
        		+ ")"
        		+ ")";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listUsers;
    }
    
    public List<User> popularUsers() throws SQLException {
    	List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM users WHERE email in ("
        		+ "SELECT followeremail FROM follow GROUP BY followeremail HAVING count(followingemail) >= 5"
        		+ ")";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listUsers;
    }
    
    public List<User> commonUsers(String user1, String user2) throws SQLException {
    	List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM users WHERE email in ("
        		+ "SELECT DISTINCT followingemail FROM follow WHERE followeremail = ? OR followeremail = ?"
        		+ ")";      
        connect_func();      
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, user1);
		preparedStatement.setString(2, user2);
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listUsers;
    }
    
    public List<String> topTags() throws SQLException {
    	List<String> listTags = new ArrayList<String>();        
        String sql = "SELECT tag FROM tags GROUP BY tag HAVING count(imageid) >= 3";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            listTags.add(resultSet.getString("tag"));
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listTags;
    }
    
    public List<User> positiveUsers() throws SQLException {
    	List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM users WHERE email in ("
        		+ "SELECT followeremail FROM follow GROUP BY followeremail HAVING count(followingemail) >= 5"
        		+ ")";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listUsers;
    }
    
    public List<Image> poorImages() throws SQLException {
    	System.out.println("Inside poorImages() in UserDAO");
    	List<Image> listImage = new ArrayList<Image>();
    	connect_func();
    	String sql1 = "SELECT * FROM images I "
    			+ "WHERE I.imageid NOT IN ("
    			+ "(SELECT L.imageid "
    			+ "FROM likes L, images I "
    			+ "WHERE L.imageid = I.imageid) "
    			+ "UNION "
    			+ "(SELECT C.imageid "
    			+ "FROM comments C, images I "
    			+ "WHERE C.imageid = I.imageid))";
    	statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
    	
    	while(resultSet.next()) {
    		int imageid = resultSet.getInt("imageid");
    		String url = resultSet.getString("url");
    		String description = resultSet.getString("description");
    		String postuser = resultSet.getString("postuser");
    		Date postdate = resultSet.getDate("postdate");
    		Timestamp posttime = resultSet.getTimestamp("posttime");
    		int numLikes = resultSet.getInt("numLikes");
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime, numLikes);
    		listImage.add(newImage);
    		System.out.println("Added an image to listImage in poorImages()");
    	}
    	System.out.println("End poorImages() in UserDAO");
    	return listImage;
    }
    
    public List<User> inactiveUsers() throws SQLException {
    	List<User> listUsers = new ArrayList<User>();        
        String sql = "SELECT * FROM users WHERE email not in ("
        		+ "SELECT distinct(postuser) FROM images "
        		+ "UNION distinct "
        		+ "SELECT distinct(email) FROM likes "
        		+ "UNION distinct "
        		+ "SELECT distinct(followeremail) FROM follow "
        		+ "UNION distinct "
        		+ "SELECT distinct(email) FROM comments)";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            Date birthday = resultSet.getDate("birthday");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String gender = resultSet.getString("gender");
            int numFollowers = resultSet.getInt("numFollowers");
            int numFollowing = resultSet.getInt("numFollowing");
             
            User newUser = new User(email, password, birthday, firstName, lastName, gender, numFollowers, numFollowing);
            listUsers.add(newUser);
        }     
        resultSet.close();
        statement.close();         
        disconnect();
        return listUsers;
    }
    
    public List<Comment> getCommentsFromSingleImage(int imageid) throws SQLException {
    	System.out.println("Inside getCommentsFromSingleImage");
    	List<Comment> listComments = new ArrayList<Comment>();
    	connect_func();
    	String sql1 = "SELECT * FROM comments WHERE imageid = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setInt(1, imageid);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	while(resultSet.next()) {
    		System.out.println("Got comment, adding to list");
    		String email = resultSet.getString("email");
    		int imgid = resultSet.getInt("imageid");
    		String description = resultSet.getString("description");
    		Comment newComment = new Comment(email, imgid, description);
    		listComments.add(newComment);
    	}
    	return listComments;
    }
    
    public List<Integer> listCurrentUserCommentedImageIds(String currentUserEmail) throws SQLException{
    	System.out.println("Inside listCurrentUserCommentedImageIds in UserDAO");
    	List<Integer> listCommentedImageIds = new ArrayList<Integer>();
    	connect_func();
    	String sql1 = "SELECT imageid FROM comments " +
    			"WHERE email = ?";
    	preparedStatement = connect.prepareStatement(sql1);
    	preparedStatement.setString(1, currentUserEmail);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	System.out.println("Statement Executed");
    	while(resultSet.next()) {
    		listCommentedImageIds.add(resultSet.getInt("imageid"));
    	}
    	return listCommentedImageIds;
    }
    
    public boolean commentOnImage(Comment newComment) throws SQLException {
    	System.out.println("Inside commentOnImage in UserDAO");
    	connect_func();
    	String sql1 = "INSERT INTO comments(email, imageid, description) values (?, ?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
		preparedStatement.setString(1, newComment.email);
		preparedStatement.setInt(2, newComment.imageid);
		preparedStatement.setString(3, newComment.description);
//		preparedStatement.executeUpdate();
        boolean rowInserted = preparedStatement.executeUpdate() > 0;
        System.out.println("Statement Executed");
        preparedStatement.close();
        return rowInserted;
    }
    
    public Comment getExistingCommentFromImage(String currentuser, int imageid) throws SQLException {
    	System.out.println("Inside getExistingCommentFromImage in UserDAO");
    	connect_func();
    	String sql1 = "SELECT * FROM comments WHERE email = ? AND imageid = ?";
    	preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
    	preparedStatement.setString(1, currentuser);
    	preparedStatement.setInt(2, imageid);
    	ResultSet resultSet = preparedStatement.executeQuery();
    	if(resultSet.next()) {
    		String email = resultSet.getString("email");
    		int imgid = resultSet.getInt("imageid");
    		String description = resultSet.getString("description");
    		return new Comment(email, imgid, description);
    	}
    	return null;
    }
    
    public boolean editComment(Comment editedComment) throws SQLException {
    	System.out.println("editComment in UserDAO");
    	connect_func();
    	String sql1 = "UPDATE comments set description = ? WHERE email = ? AND imageid = ?";
    	preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
    	preparedStatement.setString(1, editedComment.description);
    	preparedStatement.setString(2, editedComment.email);
    	preparedStatement.setInt(3, editedComment.imageid);
    	boolean update = preparedStatement.executeUpdate() > 0;
    	preparedStatement.close();
    	return update;
    }
    
    public boolean deleteComment(String email, int imageid) throws SQLException {
    	System.out.println("deleteComment in UserDAO");
    	String sql1 = "DELETE FROM comments WHERE email = ? AND imageid = ?";
    	preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
    	preparedStatement.setString(1, email);
    	preparedStatement.setInt(2, imageid);
    	boolean update = preparedStatement.executeUpdate() > 0;
    	preparedStatement.close();
    	return update;
    }
}

