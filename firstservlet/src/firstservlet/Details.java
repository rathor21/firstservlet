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
@WebServlet("/Details")
public class Details extends HttpServlet {
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
		 String custId = request.getParameter("customerID");
		 ArrayList<String> addressList = new ArrayList<String>();
		 ArrayList<String> cityList = new ArrayList<String>();
		 ArrayList<String> stateIDList = new ArrayList<String>();
		 ArrayList<String> postalList = new ArrayList<String>();
		 ArrayList<String> phoneIDList = new ArrayList<String>();
		try {
			String sql = "select CUST_STREET_ADDRESS1,CUST_CITY,CUST_STATE,CUST_POSTAL_CODE,PHONE_NUMBER1 from DEMO_CUSTOMERS";
			ResultSet result;
			result = getFromDB(sql);
			String address = "";
			String city = "";
			String state = "";
			String postal = "";
			String phone = "";
			while(result.next()){
				address = result.getString("CUST_STREET_ADDRESS1");
				city = result.getString("CUST_CITY");
				state = result.getString("CUST_STATE");
				postal = result.getString("CUST_POSTAL_CODE");
				phone = result.getString("PHONE_NUMBER1");
			addressList.add(address);
			cityList.add(city);
			stateIDList.add(state);
			postalList.add(postal);
			phoneIDList.add(phone);			}
			message += "<div class=\"container\"><h2>Details</h2><p>Customer Details</p> "
					+ "<table class= \"table\"><thead><tr><th>Address</th><th>City</th><th>State</th><th>Postal Code</th><th>Phone #</th></tr></thead><tbody>";
			int i = Integer.parseInt(custId)-1;
			address =addressList.get(i);
			city = cityList.get(i);
			state = stateIDList.get(i);
			postal = postalList.get(i);
			phone = phoneIDList.get(i);
		
					message += "<tr><td>"+ address +"</td><td>"+ city + "</td><td>"+state +"</td><td>" + postal + "</td><td>"+ phone +"</td></tr>";
			
			message += "</tbody></table></div>";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute("message", message);
		getServletContext().getRequestDispatcher("/details.jsp").forward(
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