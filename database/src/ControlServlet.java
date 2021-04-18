import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;

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
            case "/follow":
            	followUser(request, response);
            	break;
            case "/unfollow":
            	unfollowUser(request, response);
            	break;
            case "/like":
            	likeImage(request, response);
            	break;
            case "/unlike":
            	unlikeImage(request, response);
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
            case "/showCommentForm":
            	showCommentForm(request, response);
            	break;
            case "/comment":
            	commentOnImage(request, response);
            	break;
            case "/showEditCommentForm":
            	showEditCommentForm(request, response);
            	break;
            case "/deleteComment":
            	deleteComment(request, response);
            	break;
            case "/showEditImageForm":
            	showEditImageForm(request, response);
            	break;
            case "/editComment":
            	editComment(request, response);
            	break;
            case "/editImage":
            	editImage(request,response);
            	break;
            case "/deleteImage":
            	deleteImage(request, response);
            	break;
            case "/logout":
            	logoutUser(request, response);
            	break;
            // Project Part 3:
            case "/coolImages":
            	coolImages(request, response);
            	break;
            case "/newImages":
            	newImages(request, response);
            	break;
            case "/viralImages":
            	viralImages(request, response);
            	break;
            case "/topUsers":
            	topUsers(request, response);
            	break;
            case "/popularUsers":
            	popularUsers(request, response);
            	break;
            case "/commonUsers":
            	commonUsers(request, response);
            	break;
            case "/topTags":
            	topTags(request, response);
            	break;
            case "/positiveUsers":
            	positiveUsers(request, response);
            	break;
            case "/poorImages":
            	poorImages(request, response);
            	break;
            case "/inactiveUsers":
            	inactiveUsers(request, response);
            	break;
            default:          	
            	//listImages(request, response);           	
                break;
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void followUser(HttpServletRequest request, HttpServletResponse response) 
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside followUser in ControlServlet");
    	System.out.println("1st: " + request.getParameter("useremail") + " 2nd: " + (String)session.getAttribute("currentUserEmail"));
    	boolean followed = UserDAO.followUser((String)request.getParameter("useremail"), (String)session.getAttribute("currentUserEmail"));
    	response.sendRedirect("showFeedPage");
    }
    
    private void unfollowUser(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside unfollowUser in ControlServlet");
    	System.out.println("1st: " + (String)request.getParameter("useremail") + " 2nd: " + (String)session.getAttribute("currentUserEmail"));
    	boolean unfollowed = UserDAO.unfollowUser((String)request.getParameter("useremail"), (String)session.getAttribute("currentUserEmail"));
    	response.sendRedirect("showFeedPage");
    }
    
    private void likeImage(HttpServletRequest request, HttpServletResponse response) 
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside likeImage in ControlServlet");
    	System.out.println("1st: " + request.getParameter("imageid") + " 2nd: " + (String)session.getAttribute("currentUserEmail"));
    	boolean followed = UserDAO.likeImage((String)session.getAttribute("currentUserEmail"), Integer.parseInt(request.getParameter("imageid")));
    	response.sendRedirect("showFeedPage");
    }
    
    private void unlikeImage(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside unlikeImage in ControlServlet");
    	System.out.println("1st: " + request.getParameter("imageid") + " 2nd: " + (String)session.getAttribute("currentUserEmail"));
    	boolean followed = UserDAO.unlikeImage((String)session.getAttribute("currentUserEmail"), Integer.parseInt(request.getParameter("imageid")));
    	response.sendRedirect("showFeedPage");
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
    	
    	//we attach the list of currentFollowers to the request as well!
    	System.out.println("Attaching list of currentFollowers to the request as well");
    	List<String> listCurrentUserFollowing = UserDAO.listCurrentUserFollowing((String)session.getAttribute("currentUserEmail"));
    	request.setAttribute("listCurrentUserFollowing", listCurrentUserFollowing);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("UserList.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void listImages(HttpServletRequest request, HttpServletResponse response)
    		throws SQLException, IOException, ServletException {
    	System.out.println("Inside listImages in ControlServlet");
    	System.out.println((String)session.getAttribute("currentUserEmail"));
    	List<Image> listImage = UserDAO.listSelectedImages((String)session.getAttribute("currentUserEmail")); //Get a list of all images for specific user
    	//Create Treemap pairing the image data with the tag data
    	TreeMap<Image, String> imageAndTag = new TreeMap<Image, String>(Collections.reverseOrder());
    	for(int i = 0; i < listImage.size(); i++) {
    		List<String> existingTagsList = UserDAO.getTagsFromSingleImage(listImage.get(i).imageid);
            StringBuilder existingTags = new StringBuilder();
            for(String tag : existingTagsList) {
                existingTags.append(tag);
                existingTags.append(",");
            }
            //Remove trailing comma
            String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
            imageAndTag.put(listImage.get(i), existingTagsString);
    	}
    	//Sort the hashmap with stream
    	/*System.out.println("Sorting treemap by key...");
    	imageAndTag.entrySet()
    	  .stream()
    	  .sorted(Map.Entry.<Image, String>comparingByKey().reversed());*/
    	
    	request.setAttribute("treemapImageAndTag", imageAndTag);
    	//request.setAttribute("listImage", listImage); //attach the list to the request
    	
    	// Add another hashmap, this time pairing the image with its comment list
    	TreeMap<Integer, List<Comment>> imageidAndComments = new TreeMap<Integer, List<Comment>>();
    	for(int i = 0; i < listImage.size(); i++) {
    		List<Comment> existingCommentsList = UserDAO.getCommentsFromSingleImage(listImage.get(i).imageid);
    		imageidAndComments.put(listImage.get(i).getImageid(), existingCommentsList);
    	}
    	
    	request.setAttribute("treemapImageAndComment", imageidAndComments);
    	
    	//Attach a list with the imageids that the current user has liked
    	List<Integer> listLikes = UserDAO.listCurrentUserLikedImageIds((String)session.getAttribute("currentUserEmail"));
    	request.setAttribute("listLikes", listLikes);
    	
    	//Attach a list with the imageids that the current user has commented on
    	List<Integer> listComments = UserDAO.listCurrentUserCommentedImageIds((String)session.getAttribute("currentUserEmail"));
    	request.setAttribute("listComments", listComments);
    	
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
    		List<User> listUser = UserDAO.listAllUsers();
            request.setAttribute("listUser", listUser);
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
    	
    	String tags = request.getParameter("tags");
    	StringTokenizer st = new StringTokenizer(tags, ",");
    	ArrayList<String> tagsList = new ArrayList<String>();
    	while(st.hasMoreTokens()) {
    		//insert a tag one at a time
    		tagsList.add(st.nextToken());
    	}
    	
    	Image newImage = new Image(url, description, (String)session.getAttribute("currentUserEmail"), currentDate, currentTimestamp);
    	UserDAO.postImage(newImage, tagsList);
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
        
        String tags = request.getParameter("tags");
    	StringTokenizer st = new StringTokenizer(tags, ",");
    	ArrayList<String> tagsList = new ArrayList<String>();
    	while(st.hasMoreTokens()) {
    		//insert a tag one at a time
    		tagsList.add(st.nextToken());
    	}
    	
        
        Image editedImage = new Image(imageid, url, description, (String)session.getAttribute("currentUserEmail"));
        UserDAO.editImage(editedImage, tagsList);
        response.sendRedirect("showFeedPage");
    
    }
    
    private void deleteImage(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int imageid = Integer.parseInt(request.getParameter("imageid"));
        
        UserDAO.deleteImage(imageid);
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
        List<String> existingTagsList = UserDAO.getTagsFromSingleImage(imageid);
        StringBuilder existingTags = new StringBuilder();
        for(String tag : existingTagsList) {
            existingTags.append(tag);
            existingTags.append(",");
        }
        //Remove trailing comma
        String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
        System.out.println("existingTagsString: " + existingTagsString);
        RequestDispatcher dispatcher = request.getRequestDispatcher("EditImageForm.jsp");
        request.setAttribute("image", existingImage);
        request.setAttribute("existingTagsString", existingTagsString);
        dispatcher.forward(request, response); // The forward() method works at server side, and It sends the same request and response objects to another servlet.
    }
    
    private void commentOnImage(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside commentOnImage");
    	int imageid = Integer.parseInt(request.getParameter("imageid"));
        System.out.println(imageid);
        String description = request.getParameter("description");
        Comment newComment = new Comment((String)session.getAttribute("currentUserEmail"), imageid, description);
        UserDAO.commentOnImage(newComment);
        response.sendRedirect("showFeedPage");
    }
    
    private void showCommentForm(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside showCommentForm");
    	int imageid = Integer.parseInt(request.getParameter("imageid"));
    	request.setAttribute("imageid", imageid);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("CommentForm.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEditCommentForm(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	// get existing comment description
    	Comment existingComment = UserDAO.getExistingCommentFromImage((String)session.getAttribute("currentUserEmail"), Integer.parseInt(request.getParameter("imageid")));
    	request.setAttribute("existingComment", existingComment);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("EditCommentForm.jsp");
        dispatcher.forward(request, response);
    }

    private void editComment(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	String email = (String)session.getAttribute("currentUserEmail");
    	int imageid = Integer.parseInt(request.getParameter("imageid"));
    	String description = request.getParameter("description");
    	Comment editedComment = new Comment(email, imageid, description);
    	UserDAO.editComment(editedComment);
        response.sendRedirect("showFeedPage"); 
    }
    
    private void deleteComment(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	UserDAO.deleteComment((String)session.getAttribute("currentUserEmail"), Integer.parseInt(request.getParameter("imageid")));
        response.sendRedirect("showFeedPage"); 
    }
    
    /*
     * Project Part 3 Start
     */
    private void coolImages(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside coolImages in ControlServlet");
    	List<Image> listImage = UserDAO.coolImages();
    	//Create Treemap pairing the image data with the tag data
    	TreeMap<Image, String> imageAndTag = new TreeMap<Image, String>(Collections.reverseOrder());
    	for(int i = 0; i < listImage.size(); i++) {
    		List<String> existingTagsList = UserDAO.getTagsFromSingleImage(listImage.get(i).imageid);
            StringBuilder existingTags = new StringBuilder();
            for(String tag : existingTagsList) {
                existingTags.append(tag);
                existingTags.append(",");
            }
            // Remove trailing comma
            String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
            imageAndTag.put(listImage.get(i), existingTagsString);
    	}
    	
    	request.setAttribute("treemapImageAndTag", imageAndTag);
    	request.setAttribute("headerTag", "Images");
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void newImages(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside newImages in ControlServlet");
    	List<Image> listImage = UserDAO.newImages();
    	//Create Treemap pairing the image data with the tag data
    	TreeMap<Image, String> imageAndTag = new TreeMap<Image, String>(Collections.reverseOrder());
    	for(int i = 0; i < listImage.size(); i++) {
    		List<String> existingTagsList = UserDAO.getTagsFromSingleImage(listImage.get(i).imageid);
            StringBuilder existingTags = new StringBuilder();
            for(String tag : existingTagsList) {
                existingTags.append(tag);
                existingTags.append(",");
            }
            // Remove trailing comma
            String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
            imageAndTag.put(listImage.get(i), existingTagsString);
    	}
    	
    	request.setAttribute("treemapImageAndTag", imageAndTag);
    	request.setAttribute("headerTag", "Images");
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void viralImages(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside viralImages in ControlServlet");
    	List<Image> listImage = UserDAO.viralImages();
    	//Create Treemap pairing the image data with the tag data
    	TreeMap<Image, String> imageAndTag = new TreeMap<Image, String>(Collections.reverseOrder());
    	for(int i = 0; i < listImage.size(); i++) {
    		List<String> existingTagsList = UserDAO.getTagsFromSingleImage(listImage.get(i).imageid);
            StringBuilder existingTags = new StringBuilder();
            for(String tag : existingTagsList) {
                existingTags.append(tag);
                existingTags.append(",");
            }
            // Remove trailing comma
            String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
            imageAndTag.put(listImage.get(i), existingTagsString);
    	}
    	
    	request.setAttribute("treemapImageAndTag", imageAndTag);
    	request.setAttribute("headerTag", "Images");
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void topUsers(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside topUsers in ControlServlet");
    	request.setAttribute("headerTag", "Users");
    	
    	List<User> listUser = UserDAO.topUsers();
        request.setAttribute("listUser", listUser);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void popularUsers(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside popularUsers in ControlServlet");
    	request.setAttribute("headerTag", "Users");
    	
    	List<User> listUser = UserDAO.popularUsers();
        request.setAttribute("listUser", listUser);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void commonUsers(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside commonUsers in ControlServlet");
    	request.setAttribute("headerTag", "Users");
    	
    	List<User> listUser = UserDAO.commonUsers(request.getParameter("user1"), request.getParameter("user2"));
        request.setAttribute("listUser", listUser);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void topTags(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside topTags in ControlServlet");
    	request.setAttribute("headerTag", "Tags");
    	
    	List<String> listTags = UserDAO.topTags();
    	request.setAttribute("listTags", listTags);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void positiveUsers(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside positiveUsers in ControlServlet");
    	request.setAttribute("headerTag", "Users");
    	
    	List<User> listUser = UserDAO.positiveUsers();
        request.setAttribute("listUser", listUser);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void poorImages(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside poorImages in ControlServlet");
    	List<Image> listImage = UserDAO.poorImages();
    	//Create Treemap pairing the image data with the tag data
    	TreeMap<Image, String> imageAndTag = new TreeMap<Image, String>(Collections.reverseOrder());
    	for(int i = 0; i < listImage.size(); i++) {
    		List<String> existingTagsList = UserDAO.getTagsFromSingleImage(listImage.get(i).imageid);
            StringBuilder existingTags = new StringBuilder();
            for(String tag : existingTagsList) {
                existingTags.append(tag);
                existingTags.append(",");
            }
            // Remove trailing comma
            String existingTagsString = existingTags.length() > 0 ? existingTags.substring(0, existingTags.length() - 1) : "";
            imageAndTag.put(listImage.get(i), existingTagsString);
    	}
    	
    	request.setAttribute("treemapImageAndTag", imageAndTag);
    	request.setAttribute("headerTag", "Images");
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
    
    private void inactiveUsers(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException, SQLException {
    	System.out.println("Inside inactiveUsers in ControlServlet");
    	request.setAttribute("headerTag", "Users");
    	
    	List<User> listUser = UserDAO.inactiveUsers();
        request.setAttribute("listUser", listUser);
    	
    	RequestDispatcher dispatcher = request.getRequestDispatcher("RootUserPage.jsp");
    	dispatcher.forward(request, response);
    }
}