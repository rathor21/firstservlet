package firstservlet;

// Import required java libraries

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// Extend HttpServlet class
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	static Connection conn = null;
	
	private String message = "";

	public void init() throws ServletException {
		// Do required initialization

		openConnection();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set response content type
		response.setContentType("text/html");
		message = "";
		// Actual logic goes here.
		// PrintWriter out = response.getWriter();
		 ArrayList<String> firstNameList = new ArrayList<String>();
		 ArrayList<String> lastNameList = new ArrayList<String>();
		 ArrayList<String> customerIDList = new ArrayList<String>();
		try {
			String sql = "select CUST_FIRST_NAME,CUST_LAST_NAME,CUSTOMER_ID from DEMO_CUSTOMERS";
			ResultSet result;
			result = getFromDB(sql);
			String firstName = "";
			String lastName = "";
			String customerID = "";
			while(result.next()){
			firstName = result.getString("CUST_FIRST_NAME");
			lastName = result.getString("CUST_LAST_NAME");
			customerID = result.getString("CUSTOMER_ID");
			firstNameList.add(firstName);
			lastNameList.add(lastName);
			customerIDList.add(customerID);
			}
			message += "<div class=\"container\"><h2>Customer Info</h2><p>Customer Names</p> "
					+ "<table class= \"table\"><thead><tr><th>First Name</th><th>Last Name</th></tr></thead><tbody>";
					for (int i = 0; i < firstNameList.size(); i++){
			firstName = firstNameList.get(i);
			lastName = lastNameList.get(i);
			customerID = customerIDList.get(i);
		
					message += "<tr><td><a href=\"Details?customerID="+ customerID + "\">"+firstName + "</a></td><td><a href=\"Details?customerID="+customerID+ "\">" + lastName + "</a></td></tr>";
			}
			message += "</tbody></table></div>";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute("message", message);
		getServletContext().getRequestDispatcher("/output.jsp").forward(
				request, response);
		
		
	}

	public void destroy() {
		// do nothing.
	}

	public static void openConnection() {
		String url = "jdbc:oracle:thin:testuser/password@localhost";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Properties props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");
		try {
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void updateDB(String sql) throws SQLException {

		PreparedStatement preStatement = conn.prepareStatement(sql);

		preStatement.setQueryTimeout(10);
		preStatement.executeUpdate();

	}

	public static ResultSet getFromDB(String sql) throws SQLException {

		PreparedStatement preStatement = conn.prepareStatement(sql);
		ResultSet result = preStatement.executeQuery();
		return result;
	}
}