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
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private HttpSession session=null;
 
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
            case "/showFeedPage":
            	listImages(request, response);
            	break;
            case "/search":
            	listSelectedUsers(request, response);
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
            case "/showPostImageForm":
            	showPostImageForm(request, response);
            	break;
            case "/postImage":
            	postImage(request, response);
            	break;
            case "/showEditImageForm":
            	showEditImageForm(request, response);
            	break;
            case "/editImage":
            	editImage(request,response);
            	break;
            case "/logout":
            	logoutUser(request, response);
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
    
    private void listSelectedUsers(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException, ServletException {
    	System.out.println("Got to listSelectedUsers in ControlServlet");
    	String searchText = request.getParameter("searchText");
    	String searchParameter = request.getParameter("searchParameter");
    	List<User> listUser = UserDAO.listSelectedUsers(searchText, searchParameter);
    	request.setAttribute("listUser", listUser);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("UserList.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void listImages(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside listImages in ControlServlet");
    	System.out.println((String)session.getAttribute("currentUserEmail"));
    	List<Image> listImage = UserDAO.listSelectedImages((String)session.getAttribute("currentUserEmail")); //Get a list of all images for specific user
    	request.setAttribute("listImage", listImage); //attach the list to the request
    	RequestDispatcher dispatcher = request.getRequestDispatcher("FeedPage.jsp");
    	dispatcher.forward(request, response); //throw it to the FeedPage to be displayed
    }
    
    
    private void initializeDB(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException {
    	System.out.println("Got to initializeDB() in ControlServlet");
    	UserDAO.initializeDatabase();
    	System.out.println("Sending Redirect Link");
    	response.sendRedirect("home"); //Go to home
    	//ControlServlet in web.xml file is mapped to a general url /, it gets recieves the request
    	//Redirects to home (showsLoginForm) after initializing the DB
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
    		
    		//get user's information and save into session
    		System.out.println("User has been authenticated: Getting User information for Session");
    		User currentUser = UserDAO.getSingleUser(email, password);
    		session = request.getSession();
    		session.setAttribute("currentUserEmail", currentUser.getEmail());
    		session.setAttribute("currentUserPassword", currentUser.getPassword());
    		session.setAttribute("currentUserBirthday", currentUser.getBirthday());
    		session.setAttribute("currentUserFirstName", currentUser.getFirstName());
    		session.setAttribute("currentUserLastName", currentUser.getLastName());
    		session.setAttribute("currentUserGender", currentUser.getGender());
    		session.setAttribute("currentUserNumFollowers", currentUser.getNumFollowers());
    		session.setAttribute("currentUserNumFollowing", currentUser.getNumFollowing());
    		
    		response.sendRedirect("showFeedPage");
    	}
    	else {
    		//redirect to login page
    		RequestDispatcher dispatcher = request.getRequestDispatcher("LoginForm.jsp");
            dispatcher.forward(request, response);
    	}
    	System.out.println("End LoginUser()");
    }
    
    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
    	System.out.println("Inside logoutUser in ControlServlet");
    	session = request.getSession();
    	session.invalidate();
    	System.out.println("Successfully invalidated session!");
    	response.sendRedirect("LoginForm.jsp");
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
    
    private void postImage(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ParseException {
    	System.out.println("Got to postImage in ControlServlet");
    	String url = request.getParameter("url");
    	String description = request.getParameter("description");
    	Date currentDate = Date.valueOf(LocalDate.now()); // Magic?!
    	Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    	
    	Image newImage = new Image(url, description, (String)session.getAttribute("currentUserEmail"), currentDate, currentTimestamp);
    	UserDAO.postImage(newImage);
    	response.sendRedirect("showFeedPage");
    }
    
    private void editImage(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ParseException {
    	System.out.println("Got to editImage in ControlServlet");
    	int imageid = Integer.parseInt(request.getParameter("imageid"));
        
        System.out.println(imageid);
        String url = request.getParameter("url");
        String description = request.getParameter("description");
        System.out.println("Post Date Gotten: " + (String)request.getParameter("postdate"));
        /*
        Date postdate = Date.valueOf(request.getParameter("postdate"));
        Timestamp posttime = Timestamp.valueOf(request.getParameter("posttime"));
        */
        Image editedImage = new Image(imageid, url, description, (String)session.getAttribute("currentUserEmail"));
        UserDAO.editImage(editedImage);
        response.sendRedirect("showFeedPage");
    
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

    private void showPostImageForm(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	RequestDispatcher dispatcher = request.getRequestDispatcher("PostImageForm.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void showEditImageForm(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside showEditImageForm");
    	int imageid = Integer.parseInt(request.getParameter("imageid"));
        Image existingImage = UserDAO.getSingleImage(imageid);
        RequestDispatcher dispatcher = request.getRequestDispatcher("EditImageForm.jsp");
        request.setAttribute("image", existingImage);
        dispatcher.forward(request, response); // The forward() method works at server side, and It sends the same request and response objects to another servlet.
    }
}