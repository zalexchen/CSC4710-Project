import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
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
@WebServlet("/PeopleDAO")
public class PeopleDAO {     
	private static final long serialVersionUID = 1L; //not used
	private Connection connect = null; //jdbc Connection
	private Statement statement = null; 
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	public PeopleDAO() {

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
        		+ " ('guy@wayne', 'password', CURDATE(), 'Guy', 'Person', 'F', 0, 0),"
        		+ "('something@gmail', 'password1234', CURDATE(), 'Something', 'Person', 'F', 0, 0),"
        		+ "('random@gmail', 'password5678', CURDATE(), 'Random', 'Person', 'M', 0, 0),"
        		+ "('alex@outlook', 'alex12321', CURDATE(), 'Alex', 'Chen', 'M', 0, 0),"
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
    	System.out.println("LoginAuthentication() in PeopleDAO");
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
    	System.out.println("Got to registerUser() in peopleDAO");
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
		
        boolean userRegistered = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        System.out.println("End registerUser() in peopleDAO");
        return userRegistered;
    }
}
