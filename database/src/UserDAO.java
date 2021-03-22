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
        resultSet.close();
        statement.close();         
        disconnect();
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
    		Image newImage = new Image(imageid, url, description, postuser, postdate, posttime);
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
                " PRIMARY KEY(imageid), " +
                " FOREIGN KEY(postuser) references Users(email))";
        String sql3 = "INSERT INTO Images(imageid, url, description, postuser, posttime) VALUES"
        		+ " (1, 'https://picsum.photos/id/1/200/300', '1st Picture', 'test@wayne', CURDATE()),"
        		+ "(2, 'https://picsum.photos/id/2/200/300', '2nd Picture', 'guy@wayne', CURDATE()),"
        		+ "(3, 'https://picsum.photos/id/3/200/300', '3rd Picture', 'something@gmail', CURDATE()),"
        		+ "(4, 'https://picsum.photos/id/4/200/300', '4th Picture', 'test@wayne', CURDATE()),"
        		+ "(5, 'https://picsum.photos/id/5/200/300', '5th Picture', 'guy@wayne', CURDATE()),"
        		+ "(6, 'https://picsum.photos/id/6/200/300', '6th Picture', 'something@gmail', CURDATE()),"
        		+ "(7, 'https://picsum.photos/id/7/200/300', '7th Picture', 'alex@outlook', CURDATE()),"
        		+ "(8, 'https://picsum.photos/id/8/200/300', '8th Picture', 'person@protect', CURDATE()),"
        		+ "(9, 'https://picsum.photos/id/9/200/300', '9th Picture', 'programmer@gmail', CURDATE()),"
        		+ "(10, 'https://picsum.photos/id/10/200/300', '10th Picture', 'random@gmail', CURDATE())";
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
    	connect_func();
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
    public boolean postImage(Image image) throws SQLException {
    	System.out.println("Inside postImage in UserDAO");
    	connect_func();         
		String sql1 = "INSERT INTO images(url, description, postuser, postdate, posttime) values (?, ?, ?, ?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
		preparedStatement.setString(1, image.url);
		preparedStatement.setString(2, image.description);
		preparedStatement.setString(3, image.postuser);
		preparedStatement.setDate(4, image.postdate);
		preparedStatement.setTimestamp(5, image.posttime);
//		preparedStatement.executeUpdate();
		
        boolean rowInserted = preparedStatement.executeUpdate() > 0;
        System.out.println("Statement Executed");
        preparedStatement.close();
//        disconnect();
        return rowInserted;
    }
    
    public boolean editImage(Image image) throws SQLException {
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
//        disconnect();
        return rowUpdated;     
    }
    

}
