import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
 
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
    private PeopleDAO peopleDAO;
 
    public void init() {
        peopleDAO = new PeopleDAO(); 
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
            case "/new":
                showNewForm(request, response);
                break;
            case "/insert":
            	insertPeople(request, response);
                break;
            case "/delete":
            	deletePeople(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/update":
            	updatePeople(request, response);
                break;
            case "/test":
            	intializeTenUsers(request, response);
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
            	listPeople(request, response);           	
                break;
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listPeople(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<People> listPeople = peopleDAO.listAllPeople(); //Get list of people from database
        request.setAttribute("listPeople", listPeople);  //giving it a name / tag for data     
        RequestDispatcher dispatcher = request.getRequestDispatcher("PeopleList.jsp");       
        dispatcher.forward(request, response);
    }
 
    // to insert a people
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("InsertPeopleForm.jsp");
        dispatcher.forward(request, response);
    }
 
    // to present an update form to update an  existing Student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        People existingPeople = peopleDAO.getPeople(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("EditPeopleForm.jsp");
        request.setAttribute("people", existingPeople);
        dispatcher.forward(request, response); // The forward() method works at server side, and It sends the same request and response objects to another servlet.
 
    }
 
    // after the data of a people are inserted, this method will be called to insert the new people into the DB
    // 
    private void insertPeople(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String status = request.getParameter("status");
        People newPeople = new People(name, address, status);
        peopleDAO.insert(newPeople);
        response.sendRedirect("list");  // The sendRedirect() method works at client side and sends a new request
    }
 
    private void updatePeople(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        System.out.println(id);
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String status = request.getParameter("status");
        
        System.out.println(name);
        
        People people = new People(id,name, address, status);
        peopleDAO.update(people);
        response.sendRedirect("list");
    }
 
    private void deletePeople(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        //People people = new People(id);
        peopleDAO.delete(id);
        response.sendRedirect("list");  
    }
    
    private void intializeTenUsers(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException {
    	System.out.println("Got to intializeTenUsers() in ControlServlet");
    	peopleDAO.intializeTenUsers();
    	System.out.println("Sending Redirect Link");
    	response.sendRedirect("default"); //this just sends a redirect to /default which because
    	//ControlServlet in web.xml file is mapped to a general url /, it gets recieves the request
    	//Essentially a redirect back to itself
    	System.out.println("End intializeTenUsers()");
    }
    
    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
    	System.out.println("Got to loginUsers() in ControlServlet");
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	System.out.println("Starting login authentication");
    	if(peopleDAO.loginAuthentication(email, password)) {
    		//redirect to main page
    		response.sendRedirect("main");
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
        
        Date bday = Date.valueOf(request.getParameter("bday"));
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String gender = request.getParameter("gender");
        
        User newUser = new User(email, password, bday, firstName, lastName, gender);
        peopleDAO.registerUser(newUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("LoginForm.jsp");
        dispatcher.forward(request, response);
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