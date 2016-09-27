package com.example.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import com.example.models.CustomerModel;

public class CustomerController {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/carworkshop";
	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "password";
	private JdbcRowSet jdbcRowSet = null;
	
	public CustomerController(){
		
		try{
			Class.forName(JDBC_DRIVER);
			jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
			jdbcRowSet.setUrl(DATABASE_URL);
			jdbcRowSet.setUsername(DATABASE_USER);
			jdbcRowSet.setPassword(DATABASE_PASSWORD);
			jdbcRowSet.setCommand("SELECT * FROM customertable");
			jdbcRowSet.execute();
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}
	/*
	 * Lägger till en ny kund i databasen. Skickar in en customerModel som sätts i CustomerCreateViewController
	 */
	public CustomerModel addCustomerToDatabase(CustomerModel customerModel){
		try{
			jdbcRowSet.moveToInsertRow();
			jdbcRowSet.updateString("name", customerModel.getName());
			jdbcRowSet.updateString("address", customerModel.getAddress());
			jdbcRowSet.updateString("phone_number", customerModel.getPhoneNumber());
			jdbcRowSet.updateString("age", customerModel.getAge());
			jdbcRowSet.updateString("email", customerModel.getEmail());
			jdbcRowSet.insertRow();
			jdbcRowSet.moveToCurrentRow();
		}catch(SQLException ex){
			System.out.println("Error: " + ex.getMessage());
			try{
				jdbcRowSet.rollback();
				customerModel = null;
			}catch(Exception e){}
		}catch(Exception exception){
			System.out.println("Error: " + exception.getMessage());
		}
		return customerModel;
		
	}
	/*
	 * Denna metod körs från olika listviews för att sätta en customermodel genom att söka i databasen på id och namn vilka man får från listvyn som String verifier
	 */
	public CustomerModel getSelectedCustomerFromDatabase(String verifier){
		CustomerModel customerModel = new CustomerModel();
		try{
			while(jdbcRowSet.next()){
				String checkForCustomer = "Id: " + (Integer.toString(jdbcRowSet.getInt("owner_id"))) + " " + jdbcRowSet.getString("name");
				if(checkForCustomer.equals(verifier)){
					customerModel.setId(jdbcRowSet.getInt("owner_id"));
					customerModel.setName(jdbcRowSet.getString("name"));
					customerModel.setAddress(jdbcRowSet.getString("address"));
					customerModel.setPhoneNumber(jdbcRowSet.getString("phone_Number"));
					customerModel.setAge(jdbcRowSet.getString("age"));
					customerModel.setEmail(jdbcRowSet.getString("email"));
				}
			}
			jdbcRowSet.beforeFirst();
		}catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return customerModel;
	}
	/*
	 * Denna metod körs från CarController getSelectedCarFromDatabase som körs när man klickar på en bil i listview 
	 * Metoden används för att sätta den aktuella bilens ägare.
	 */
	public CustomerModel getCarOwner(int ownerId){
		CustomerModel customerModel = new CustomerModel();
		try{
			jdbcRowSet.setCommand("SELECT * FROM customertable WHERE owner_id='" + ownerId + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				customerModel.setId(jdbcRowSet.getInt("owner_id"));
				customerModel.setName(jdbcRowSet.getString("name"));
			}
			jdbcRowSet.beforeFirst();
		}catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return customerModel;
	}
	
	/*
	 * Denna metod hämtar alla kunder från databasen till en lista för att skrivas ut i olika list views.
	 */
	public List<CustomerModel> getAllCustomersFromDatabase(){
		List<CustomerModel> customerList = new ArrayList<CustomerModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM customertable");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				CustomerModel customerModel = new CustomerModel();
				customerModel.setName(jdbcRowSet.getString("name"));
				customerModel.setId(jdbcRowSet.getInt("owner_id"));
				customerModel.setAddress(jdbcRowSet.getString("address"));
				customerModel.setPhoneNumber(jdbcRowSet.getString("phone_Number"));
				customerModel.setAge(jdbcRowSet.getString("age"));
				customerModel.setEmail(jdbcRowSet.getString("email"));
				customerList.add(customerModel);
			}
				jdbcRowSet.beforeFirst();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return customerList;
	}
	/*
	 * Denna metod raderar kund från databasen och även dennes bilar. Från deleteCarFromDatabaseWhenDeletingCustomer kommer en annan metod anropas
	 * som raderar kundens bilars servicetillfällen
	 */
	public void deleteCustomerFromDatabase(CustomerModel customerModel){
		
		CarController carController = new CarController();
		
		try {
			carController.deleteCarFromDatabaseWhenDeletingCustomer(customerModel);
			jdbcRowSet.setCommand("SELECT * FROM customertable WHERE owner_id='" + customerModel.getId() + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
					jdbcRowSet.deleteRow();
					
			}
			jdbcRowSet.beforeFirst();
				
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	/*
	 * Uppdaterar vald kund med nya värden som användaren skrivit in
	 */
	public void updateCustomerInDatabase(CustomerModel customerModel, String newCustomerName, String newCustomerAddress,
											String newCustomerPhonenr, String newCustomerAge, String newCustomerEmail){
		try{
			jdbcRowSet.setCommand("SELECT * FROM customertable WHERE owner_id='" + customerModel.getId() + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
					jdbcRowSet.updateString("name", newCustomerName);
					jdbcRowSet.updateString("address", newCustomerAddress);
					jdbcRowSet.updateString("phone_number", newCustomerPhonenr);
					jdbcRowSet.updateString("age", newCustomerAge);
					jdbcRowSet.updateString("email", newCustomerEmail);
					jdbcRowSet.updateRow();
				}
			jdbcRowSet.beforeFirst();	
			
		}catch(SQLException ex){
			System.out.println("Error: " + ex.getMessage());
			try{
				jdbcRowSet.rollback();
			}catch(Exception e){}
		
		}	
	}
}
