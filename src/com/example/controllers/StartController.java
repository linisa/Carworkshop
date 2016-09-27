package com.example.controllers;

import java.sql.*;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class StartController {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/";
	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "password";
	
	private static final String NEW_DATABASE_URL = "jdbc:mysql://localhost:3306/carworkshop";
	private JdbcRowSet jdbcRowSet = null;
	private Connection connection;
	private Statement statement;

	/*
	 * Denna klass körs vid start av programmet och kollar om det finns en databas med namnet carworkshop på jdbc:mysql://localhost:3306/
	 * om det inte finns kommer det skapas en databas och de tre tabeller som programmet behöver
	 */
	public StartController() {
		try {
			Class.forName(JDBC_DRIVER);
			jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
			jdbcRowSet.setUrl(DATABASE_URL);
			jdbcRowSet.setUsername(DATABASE_USER);
			jdbcRowSet.setPassword(DATABASE_PASSWORD);
			jdbcRowSet.setCommand("SHOW databases like 'carworkshop';");
			jdbcRowSet.execute();
			if (jdbcRowSet.next()) {
				System.out.println("Database found");
				System.out.println("Initializing Car workshop");
			} else {
				System.out.println("Database does not exist");
				System.out.println("Setting up new database");
				setUpNewDatabase();
				System.out.println("Setting up tables");
				setUpNewTablesInDatabase();
				System.out.println("Database created!");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void setUpNewDatabase() {
		connection = null;
		statement = null;
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
			statement = connection.createStatement();
			String sql = "CREATE DATABASE carworkshop";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	private void setUpNewTablesInDatabase(){
		
		   try{
		      Class.forName(JDBC_DRIVER);
		      connection = DriverManager.getConnection(NEW_DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
		      statement = connection.createStatement();
		      
		      statement.executeUpdate(createCustomerTable());
		      statement.executeUpdate(createCarTable());
		      statement.executeUpdate(createCarServiceTable());
		    
		   }catch(SQLException e){
			   System.out.println("Error: " + e.getMessage());
		   }catch(Exception e){
			   System.out.println("Error: " + e.getMessage());
		   }finally{
		      try{
		         if(statement!=null)
		            connection.close();
		      }catch(SQLException e){
		    	  System.out.println("Error: " + e.getMessage());
		      }
		      try{
		         if(connection!=null)
		        	 connection.close();
		      }catch(SQLException e){
		    	  System.out.println("Error: " + e.getMessage());
		      }
		   }
	}
	private String createCustomerTable(){
		String customerTable = "CREATE TABLE customertable " +
				"(owner_id int(99) NOT NULL AUTO_INCREMENT, " +
				"name varchar(255) NOT NULL, " +
				"address varchar(255) DEFAULT NULL, " +
				"phone_number varchar(255) DEFAULT NULL, " +
				"age varchar(255) DEFAULT NULL, " +
				"email varchar(255) NOT NULL, " +
				"PRIMARY KEY ( owner_id ))";
		return customerTable; 
	}
	private String createCarTable(){
		String carTable = "CREATE TABLE cartable " +
	    		  "(car_id int(99) NOT NULL AUTO_INCREMENT, " +
	    		  "brand varchar(45) DEFAULT NULL, "+
	    		  "registration_number varchar(45) DEFAULT NULL, " +
	    		  "manufactury_year varchar(45) DEFAULT NULL, " +
	    		  "model varchar(45) DEFAULT NULL, " +
	    		  "fuel varchar(45) DEFAULT NULL, " +
	    		  "colour varchar(45) DEFAULT NULL, " +
	    		  "owner_id int(99) NOT NULL, " +
	    		  "PRIMARY KEY ( car_id ), " +
	    		  "KEY owner_id_idx ( owner_id ), " +
	    		  "CONSTRAINT owner_id FOREIGN KEY ( owner_id ) REFERENCES customertable ( owner_id ) ON DELETE NO ACTION ON UPDATE NO ACTION)";
		return carTable;
	}
	private String createCarServiceTable(){
		String carServiceTable = "CREATE TABLE carservicetable " +
	    		  "(service_id int(99) NOT NULL AUTO_INCREMENT, " +
	    		  "description varchar(255) DEFAULT NULL, " +
	    		  "date date NOT NULL, " +
	    		  "price double DEFAULT NULL, " +
	    		  "car_id int(99) NOT NULL, " +
	    		  "PRIMARY KEY ( service_id ), " +
	    		  "KEY car_id_idx ( car_id ), " +
	    		  "CONSTRAINT car_id FOREIGN KEY ( car_id ) REFERENCES cartable ( car_id ) ON DELETE NO ACTION ON UPDATE NO ACTION)";
		return carServiceTable;
	}
}
