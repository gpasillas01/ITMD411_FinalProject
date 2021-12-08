package javaapplication1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations. what they had 
		final String createTicketsTable = "CREATE TABLE farpas_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date VARCHAR(30))";
		final String createUsersTable = "CREATE TABLE farpas_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";
		final String createReturnTable = "CREATE TABLE farpas_returned(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date VARCHAR(30), end_date VARCHAR(30))";
		

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createReturnTable);
			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);

			System.out.println("Created tables in given database...");
			


			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into farpas_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc, LocalDate initialdate) {
		int id = 0;
		
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into farpas_tickets" + "(ticket_issuer, ticket_description, start_date) values(" + " '"
					+ ticketName + "','" + ticketDesc + "','" + initialdate +"')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	//Admin View
	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM farpas_tickets");
			System.out.println("Hello, Administrative user!");
			//connect.close();
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return results;
	}
	//****
	// continue coding for updateRecords implementation

	// continue coding for deleteRecords implementation
	//User View
	//	public ResultSet readUserRecords() {

	//		ResultSet results = null;
	//		try {
	//			statement = connect.createStatement();
	//			results = statement.executeQuery("SELECT * FROM farpas_tickets WHERE admin= '" + Login.adminid + "';");
	//			System.out.println("Hello!");
	//			//connect.close();
	//		} catch (SQLException e) {
	//			e.printStackTrace(); 
	//		}
	//		return results;
	//	}
		

		// continue coding for deleteRecords implementation
		//We were able to find a tutorial to help us at this link: https://www.geeksforgeeks.org/files-delete-method-in-java-with-examples/
		public void deleteRecords(int id) throws SQLException {

			// Execute delete  query
		      System.out.println("Hold on... we are making a delete statement...");
		      statement = connect.createStatement();

		     String sql = "DELETE FROM farpas_tickets  " + "WHERE ticket_id = '" + id + "'" ;
		     String sqlOne="DELETE FROM farpas_returned  " + "WHERE ticket_id = '" + id + "'" ;

		    
		     int answer = JOptionPane.showConfirmDialog(null, "Would you like us to remove your ticket number " + id + "?", "Please confirm",  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		     if (answer == JOptionPane.NO_OPTION) {
		       System.out.println("There have been no records deleted");
		       JOptionPane.showMessageDialog(null, "None of the tickets have been deleted.");
		       
		    } else if (answer == JOptionPane.YES_OPTION) {
		      statement.executeUpdate(sql);
		      statement.executeUpdate(sqlOne);
		      JOptionPane.showMessageDialog(null, "The ticket has now been deleted.");
		      System.out.println("File discarded");
		      
		    } else if (answer == JOptionPane.CLOSED_OPTION) {
		      JOptionPane.showMessageDialog(null, "The ticket has now been cancelled.");
		      System.out.println("Cancelling Request");
		    }
			
		}
		
		// continue coding for updateRecords implementation
		public void updateRecords(int id, String ticketDesc) throws SQLException {
			 // Execute update  query
		      System.out.println("Hold on... we are making an update statement...");
		      statement = connect.createStatement();
		      
		      String sql = "UPDATE farpas_tickets " + "SET ticket_description = '" + ticketDesc + "' WHERE ticket_id = '" + id +"'";
		      
		      statement.executeUpdate(sql);
		      JOptionPane.showMessageDialog(null, "the following update is successful!.");
		}
		
		//close ticket
		public void endingRecords(int id, LocalDate initialdate) throws SQLException {
			// Execute close ticket  query
		      System.out.println("Hold on... we're making the final ticket statement to close...");
		      statement = connect.createStatement();
		      
		     String sql = "INSERT INTO farpas_returned SELECT * FROM farpas_tickets WHERE ticket_id = '" + id + "'";
		     String sqlOne="DELETE FROM farpas_tickets  " + "WHERE ticket_id = '" + id + "'" ;
		     String sqlTwo = "UPDATE farpas_returned " + "SET end_date = '" + initialdate + "' WHERE ticket_id = '" + id +"'";
		     
		     int answer = JOptionPane.showConfirmDialog(null, "Would you like us to close your ticket number " + id + "?", "Please confirm before proceeding ahead",  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		     if (answer == JOptionPane.NO_OPTION) {
		       System.out.println("There are no records closed");
		       JOptionPane.showMessageDialog(null, "No tickets have been closed yet.");
		       
		    } else if (answer == JOptionPane.YES_OPTION) {
		      statement.executeUpdate(sql);
		      statement.executeUpdate(sqlOne);
		      statement.executeUpdate(sqlTwo);
		      
		      
		      JOptionPane.showMessageDialog(null, "Closing the ticket");
		      System.out.println("The ticket has been closed successfully.");
		      
		    } else if (answer == JOptionPane.CLOSED_OPTION) {
		      JOptionPane.showMessageDialog(null, "Error: Oops! Unfortuantley your tickt didn't close.");
		      System.out.println("The request has now been cancelled...");
		    }
		}
		
		//Display returned tickets
		public ResultSet readReturned() {
			ResultSet results = null;
			try {
				statement = connect.createStatement();
				results = statement.executeQuery("SELECT * FROM farpas_returned");
				//connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return results;
		}
	}
	
	
	

