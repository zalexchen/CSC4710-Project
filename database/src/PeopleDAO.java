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
	private ResultSet resultSet = null; //not used
	
	
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
    
    //Returns the list of all "people" from the database
    public List<People> listAllPeople() throws SQLException {
        List<People> listPeople = new ArrayList<People>();        
        String sql = "SELECT * FROM student";      
        connect_func();      
        statement =  (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            String status = resultSet.getString("status");
             
            People people = new People(id,name, address, status);
            listPeople.add(people);
        }        
        resultSet.close();
        statement.close();         
        disconnect();        
        return listPeople;
    }
    
    //Utility function to disconnect from the database server
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
        
    //Inserting a User
    public boolean insert(People people) throws SQLException {
    	connect_func();         
		String sql = "insert into  student(Name, Address, Status) values (?, ?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, people.name);
		preparedStatement.setString(2, people.address);
		preparedStatement.setString(3, people.status);
//		preparedStatement.executeUpdate();
		
        boolean rowInserted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
//        disconnect();
        return rowInserted;
    }     
    
    //
    public boolean delete(int peopleid) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";        
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, peopleid);
         
        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
//        disconnect();
        return rowDeleted;     
    }
     
    public boolean update(People people) throws SQLException {
        String sql = "update student set Name=?, Address =?,Status = ? where id = ?";
        connect_func();
        
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setString(1, people.name);
        preparedStatement.setString(2, people.address);
        preparedStatement.setString(3, people.status);
        preparedStatement.setInt(4, people.id);
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
//        disconnect();
        return rowUpdated;     
    }
	
    //
    public People getPeople(int id) throws SQLException {
    	People people = null;
        String sql = "SELECT * FROM student WHERE id = ?";
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, id);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            String status = resultSet.getString("status");
             
            people = new People(id, name, address, status);
        }
        
        resultSet.close();
        statement.close();
         
        return people;
    }
    
    public void intializeTenUsers() throws SQLException {
    	System.out.println("Initializing Users Start");
    	
    	connect_func();
    	String sql1 = "DROP TABLE IF EXISTS testUsers";
        String sql2 = "CREATE TABLE IF NOT EXISTS testUsers(" +
                " email VARCHAR(50), " + 
                " password VARCHAR(50), " + 
                " bday date, " + 
                " firstName VARCHAR(50), " +
                " lastName VARCHAR(50), " +
                " gender CHAR(1), " +
                " PRIMARY KEY ( email ))";
        String sql3 = "INSERT INTO testUsers(email, password, bday, firstName, lastName, gender) VALUES ('test@1234', 'test1234', CURDATE(), 'test', 'est', 'M')";
        String sql4 = "INSERT INTO testUsers(email, password, bday, firstName, lastName, gender) VALUES ('guy@wayne', 'password', CURDATE(), 'testz', 'eszt', 'F')";
        String sql5 = "INSERT INTO testUsers(email, password, bday, firstName, lastName, gender) VALUES ('something@12345', 'password1234', CURDATE(), 'something', 'est', 'F')";
        String sql6 = "INSERT INTO testUsers(email, password, bday, firstName, lastName, gender) VALUES ('random@gmail', 'asdfjkljfdsa', CURDATE(), 'asdjkfl;', ';jfdas', 'M')";
        String sql7 = "INSERT INTO testUsers(email, password, bday, firstName, lastName, gender) VALUES ('person@protect', 'qweruipuirewq', CURDATE(), 'asdf', 'asdf', 'O')";
        statement = connect.createStatement();
        System.out.println("Executing Statements");
        statement.executeUpdate(sql1);
        System.out.println("Executed SQL 1");
        statement.executeUpdate(sql2);
        System.out.println("Executed SQL 2");
        statement.executeUpdate(sql3);
        System.out.println("Executed SQL 3");
        
        statement.executeUpdate(sql4);
        statement.executeUpdate(sql5);
        statement.executeUpdate(sql6);
        statement.executeUpdate(sql7);
        
        
        System.out.println("Initializing Users End");
    }
    
    public boolean loginAuthentication(String email, String password) throws SQLException {
    	System.out.println("LoginAuthentication() in PeopleDAO");
    	//if email and password combination is in database, then return true
    	connect_func();
    	
    	System.out.println("Got email: " + email + " and password: " + password);
    	String sql1 = "SELECT * FROM testUsers U " +
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
		String sql1 = "INSERT into testusers(email, password, bday, firstName, lastName, gender) values (?, ?, ?, ?, ?, ?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql1);
		preparedStatement.setString(1, user.email);
		preparedStatement.setString(2, user.password);
		preparedStatement.setDate(3, (Date) user.bday);
		preparedStatement.setString(4, user.firstName);
		preparedStatement.setString(5, user.lastName);
		preparedStatement.setString(6, user.gender);
		
        boolean userRegistered = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        System.out.println("End registerUser() in peopleDAO");
        return userRegistered;
    }
}
