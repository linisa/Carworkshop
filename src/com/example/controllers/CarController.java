package com.example.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import com.example.models.CarModel;
import com.example.models.CustomerModel;

public class CarController {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/carworkshop";
	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "password";
	private JdbcRowSet jdbcRowSet = null;
	
	public CarController() {
		try {
			Class.forName(JDBC_DRIVER);
			jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
			jdbcRowSet.setUrl(DATABASE_URL);
			jdbcRowSet.setUsername(DATABASE_USER);
			jdbcRowSet.setPassword(DATABASE_PASSWORD);
			jdbcRowSet.setCommand("SELECT * FROM cartable");
			jdbcRowSet.execute();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	/*
	 * Lägger till en ny bil i databasen. Skickar in en carmodel som sätts i CarCreateViewController
	 */
	public CarModel addCarToDatabase(CarModel carModel) {
		try {
			jdbcRowSet.moveToInsertRow();
			jdbcRowSet.updateString("brand", carModel.getBrand());
			jdbcRowSet.updateString("registration_number", carModel.getRegistrationNumber());
			jdbcRowSet.updateString("manufactury_year", carModel.getYearOfManufactury());
			jdbcRowSet.updateString("model", carModel.getModel());
			jdbcRowSet.updateString("fuel", carModel.getTypeOfFuel());
			jdbcRowSet.updateString("colour", carModel.getColour());
			jdbcRowSet.updateInt("owner_id", carModel.getOwner().getId());
			jdbcRowSet.insertRow();
			jdbcRowSet.moveToCurrentRow();

		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
			try {
				jdbcRowSet.rollback();
				carModel = null;
			} catch (Exception e) {
			}
		} catch (Exception exception) {
			System.out.println("Error: " + exception.getMessage());
		}
		return carModel;

	}
	/*
	 * Denna metod körs från CustomerEditViewController för att visa den aktuella kundens bilar i en listview
	 */
	public List<CarModel> getSelectedCustomersCars(CustomerModel customerModel) {
		List<CarModel> carList = new ArrayList<CarModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE owner_id='" + customerModel.getId() +"'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
					CarModel carModel = new CarModel();
					carModel.setId(jdbcRowSet.getInt("car_id"));
					carModel.setBrand(jdbcRowSet.getString("brand"));
					carModel.setModel(jdbcRowSet.getString("model"));
					carModel.setRegistrationNumber(jdbcRowSet.getString("registration_number"));
					carModel.setYearOfManufactury(jdbcRowSet.getString("manufactury_year"));
					carModel.setTypeOfFuel(jdbcRowSet.getString("fuel"));
					carModel.setColour(jdbcRowSet.getString("colour"));
					carList.add(carModel);
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return carList;
	}
	/*
	 * Uppdaterar vald bil med nya värden från CarEditViewController
	 */
	public void updateCarInDatabase(CarModel carModel, String newCarBrand, String newCarRegistrationNumber, 
									String newCarYear, String newCarModel, String newCarFuel, String newCarColour){
		try{
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE car_id='" + carModel.getId() + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				
					jdbcRowSet.updateString("brand", newCarBrand);
					jdbcRowSet.updateString("registration_number", newCarRegistrationNumber);
					jdbcRowSet.updateString("manufactury_year", newCarYear);
					jdbcRowSet.updateString("model", newCarModel);
					jdbcRowSet.updateString("fuel", newCarFuel);
					jdbcRowSet.updateString("colour", newCarColour);
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
	/*
	 * Denna metod raderar aktuell bil och raderar även bilens servicetillfällen (om bilen har några inlagda)
	 */
	public void deleteCarFromDatabase(CarModel carModel){
		CarServiceController carServiceController = new CarServiceController();
		carServiceController.deleteServiceFromDatabaseWhenDeletingCar(carModel);
		try{
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE car_id='" + carModel.getId() + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				jdbcRowSet.deleteRow();
			}
			jdbcRowSet.beforeFirst();
		}catch(SQLException ex){
			System.out.println("Error: " + ex.getMessage());
		}
	}
	/*
	 * Denna metod körs när man raderar en kund så att även kundens bil/bilar raderas. Samtidigt körs en liknande metod från CarServiceController
	 * för att radera eventuella services kopplade till bilen
	 */
	public void deleteCarFromDatabaseWhenDeletingCustomer(CustomerModel customerModel){
		CarServiceController carServiceController = new CarServiceController();
		carServiceController.deleteServiceFromDatabaseWhenDeletingCustomer(customerModel);
		try{
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE owner_id='" + customerModel.getId() + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				jdbcRowSet.deleteRow();
			}
		}catch(SQLException ex){
			System.out.println("Error: " + ex.getMessage());
		}
	}
	/*
	 * Denna metod hämtar alla bilar från cartable och returnerar dem i en lista för att skrivas ut i de olika list views
	 */
	public List<CarModel> getAllCarsFromDatabase() {
		List<CarModel> carList = new ArrayList<CarModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM cartable");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				CarModel carModel = new CarModel();
				carModel.setId(jdbcRowSet.getInt("car_id"));
				carModel.setBrand(jdbcRowSet.getString("brand"));
				carModel.setModel(jdbcRowSet.getString("model"));
				carModel.setRegistrationNumber(jdbcRowSet.getString("registration_number"));
				carModel.setYearOfManufactury(jdbcRowSet.getString("manufactury_year"));
				carModel.setTypeOfFuel(jdbcRowSet.getString("fuel"));
				carModel.setColour(jdbcRowSet.getString("colour"));
				carList.add(carModel);
			}
				jdbcRowSet.beforeFirst();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return carList;
	}
	/*
	 * Denna metod körs från de olika listview listeners och returnerar den bil man valt i listan genom att söka på id, brand, modell och regnr
	 * vilka man får från listvyn som String verifier
	 */
	public CarModel getSelectedCarFromDatabase(String verifier) {
		CarModel carModel = new CarModel();
		CustomerController customerController = new CustomerController();
		try{
			while(jdbcRowSet.next()){
				String checkForCar = "Id: " + jdbcRowSet.getInt("car_id") + " " + jdbcRowSet.getString("brand") + " " + jdbcRowSet.getString("model") + " " + jdbcRowSet.getString("registration_number");
				if(checkForCar.equals(verifier)){
					carModel.setId(jdbcRowSet.getInt("car_id"));
					carModel.setBrand(jdbcRowSet.getString("brand"));
					carModel.setModel(jdbcRowSet.getString("model"));
					carModel.setRegistrationNumber(jdbcRowSet.getString("registration_number"));
					carModel.setYearOfManufactury(jdbcRowSet.getString("manufactury_year"));
					carModel.setTypeOfFuel(jdbcRowSet.getString("fuel"));
					carModel.setColour(jdbcRowSet.getString("colour"));
					int ownerId = jdbcRowSet.getInt("owner_id");
					CustomerModel customerModel = customerController.getCarOwner(ownerId);
					carModel.setOwner(customerModel);
				}
			}
			jdbcRowSet.beforeFirst();
		}catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return carModel;
	}

	/*
	 * Denna metod körs från CarServiceController för att hitta vilken bil i cartable som hör till det aktuella servicetillfället
	 * med servicetillfällets car_id
	 */
	public CarModel getCurrentServicesCar(int carId) {
		CarModel carModel = new CarModel();
		try{
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE car_id='" + carId + "'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				carModel.setId(jdbcRowSet.getInt("car_id"));
				carModel.setBrand(jdbcRowSet.getString("brand"));
				carModel.setModel(jdbcRowSet.getString("model"));
				carModel.setRegistrationNumber(jdbcRowSet.getString("registration_number"));
			}
			jdbcRowSet.beforeFirst();
		}catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return carModel;
	}
	/*
	 * Denna metod körs från statistics view för att söka genom cartable efter user input. Attribute kan vara brand, colour eller fuel
	 * beroende på vad man söker efter
	 */
	public List<CarModel> getTotalCarsByAttribute(String attribute, String input) {
		List<CarModel> carList = new ArrayList<CarModel>();
		try{
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE " + attribute +  " LIKE '%" + input + "%'");
			jdbcRowSet.execute();
			while(jdbcRowSet.next()){
				CarModel carModel = new CarModel();
				carList.add(carModel);
			}
		}catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return carList;
	}
	
}