import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
 
/**
 * ControllerServlet.java
 * This servlet acts as a page controller for the application, handling all
 * requests from the user.
 * @author www.codejava.net
 */
public class ControlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO UserDAO;
 
    public void init() {
        UserDAO = new UserDAO(); 
    }
    
    //doPost == doGet
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println(action);
        try {
            switch (action) {
            case "/showListUsers":
            	listUsers(request, response);
            	break;
            case "/initDB":
            	initializeDB(request, response);
            	break;
            case "/home":
            	showLoginForm(request, response);
            	break;
            case "/login":
            	loginUser(request, response);
            	break;
            case "/newUser":
            	showRegisterForm(request, response);
            	break;
            case "/register":
            	registerUser(request, response);
            	break;
            default:          	
            	listUsers(request, response);           	
                break;
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = UserDAO.listAllUsers(); //Get list of users from database
        request.setAttribute("listUser", listUser);  //giving it a name / tag for data     
        RequestDispatcher dispatcher = request.getRequestDispatcher("UserList.jsp");       
        dispatcher.forward(request, response);
    }
    
    
    private void initializeDB(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException {
    	System.out.println("Got to initializeDB() in ControlServlet");
    	UserDAO.initializeDatabase();
    	System.out.println("Sending Redirect Link");
    	response.sendRedirect("showListUsers"); //this just sends a redirect to /default which because
    	//ControlServlet in web.xml file is mapped to a general url /, it gets recieves the request
    	//Essentially a redirect back to itself
    	System.out.println("End intializeTenUsers()");
    }
    
    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
    	System.out.println("Got to loginUsers() in ControlServlet");
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	System.out.println("Starting login authentication");
    	
    	//Get root user from Txt file
    	Properties props = new Properties();
    	InputStream is = ControlServlet.class.getResourceAsStream("root.properties");
    	props.load(is);
    	String rootUsername = props.getProperty("username");
    	String rootPassword = props.getProperty("password");
    	
    	if(email.equals(rootUsername) && password.equals(rootPassword)) {
    		RequestDispatcher dispatcher = request.getRequestDispatcher("InitDB.jsp");
    		dispatcher.forward(request, response);
    	}
    	else if(UserDAO.loginAuthentication(email, password)) {
    		//redirect to main page
    		//response.sendRedirect("main");
    		//redirect to listOfUsers
    		response.sendRedirect("showListUsers");
    	}
    	else {
    		//redirect to login page
    		RequestDispatcher dispatcher = request.getRequestDispatcher("LoginForm.jsp");
            dispatcher.forward(request, response);
    	}
    	System.out.println("End LoginUser()");
    }
    
    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ParseException {
    	System.out.println("Got to registerUsers() in ControlServlet");
    	String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Date birthday = Date.valueOf(request.getParameter("birthday"));
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String gender = request.getParameter("gender");
        
        User newUser = new User(email, password, birthday, firstName, lastName, gender);
        boolean success = UserDAO.registerUser(newUser);
        if (success) {
        	RequestDispatcher dispatcher = request.getRequestDispatcher("LoginForm.jsp");
            dispatcher.forward(request, response);
        }
        else {
        	//Display an error message for the user : Account Already Exists
        	request.setAttribute("failed", true);
        	RequestDispatcher dispatcher = request.getRequestDispatcher("RegisterForm.jsp");
        	dispatcher.forward(request, response);
        }
        System.out.println("End registerUser() in ControlServlet");
    }
    
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("LoginForm.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("RegisterForm.jsp");
        dispatcher.forward(request, response);
    }

}