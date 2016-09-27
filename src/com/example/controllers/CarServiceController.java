package com.example.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import com.example.models.CarModel;
import com.example.models.CarServiceModel;
import com.example.models.CustomerModel;

public class CarServiceController {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/carworkshop";
	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "password";
	private JdbcRowSet jdbcRowSet = null;

	public CarServiceController() {
		try {
			Class.forName(JDBC_DRIVER);
			jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
			jdbcRowSet.setUrl(DATABASE_URL);
			jdbcRowSet.setUsername(DATABASE_USER);
			jdbcRowSet.setPassword(DATABASE_PASSWORD);
			jdbcRowSet.setCommand("SELECT * FROM carservicetable");
			jdbcRowSet.execute();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/*
	 * Lägger till en ny carservice i databasen
	 */
	public CarServiceModel addServiceToDatabase(CarServiceModel carServiceModel) {
		try {
			jdbcRowSet.moveToInsertRow();
			jdbcRowSet.updateString("description", carServiceModel.getDescription());
			jdbcRowSet.updateDate("date", (Date) carServiceModel.getDate());
			jdbcRowSet.updateDouble("price", carServiceModel.getPrice());
			jdbcRowSet.updateInt("car_id", carServiceModel.getCarModel().getId());
			jdbcRowSet.insertRow();
			jdbcRowSet.moveToCurrentRow();

		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
			try {
				jdbcRowSet.rollback();
				carServiceModel = null;
			} catch (Exception e) {
			}
		} catch (Exception exception) {
			System.out.println("Error: " + exception.getMessage());
		}

		return carServiceModel;
	}

	/*
	 * Denna metod körs från olika listviews för att sätta vald carservicemodel genom att söka i databasen på id och datum vilka man får från listvyn
	 */
	public CarServiceModel getSelectedServiceFromDatabase(String verifier) {
		CarServiceModel carServiceModel = new CarServiceModel();
		CarController carController = new CarController();
		try {
			while (jdbcRowSet.next()) {
				String checkForService = "Id: " + jdbcRowSet.getInt("service_id") + " " + jdbcRowSet.getDate("date").toString();
				if (checkForService.equals(verifier)) {
					carServiceModel.setId(jdbcRowSet.getInt("service_id"));
					carServiceModel.setDescription(jdbcRowSet.getString("description"));
					carServiceModel.setDate(jdbcRowSet.getDate("date"));
					carServiceModel.setPrice(jdbcRowSet.getDouble("price"));
					int carId = jdbcRowSet.getInt("car_id");
					CarModel carModel = carController.getCurrentServicesCar(carId);
					carServiceModel.setCarModel(carModel);
				}
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return carServiceModel;
	}

	/*
	 * Hämtar alla carservices från databasen till en lista som skrivs ut i listviews
	 */
	public List<CarServiceModel> getAllServicesFromDatabase() {
		List<CarServiceModel> serviceList = new ArrayList<CarServiceModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				CarServiceModel carServiceModel = new CarServiceModel();
				carServiceModel.setId(jdbcRowSet.getInt("service_id"));
				carServiceModel.setDescription(jdbcRowSet.getString("description"));
				carServiceModel.setDate(jdbcRowSet.getDate("date"));
				carServiceModel.setPrice(jdbcRowSet.getDouble("price"));
				serviceList.add(carServiceModel);
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return serviceList;
	}
	/*
	 * Uppdaterar vald carservicemodel i databasen med nya värden
	 */
	public void updateServiceInDatabase(CarServiceModel carServiceModel, String newDescription, Date newDate,
			double newPrice) {
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE service_id='" + carServiceModel.getId() + "'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				jdbcRowSet.updateString("description", newDescription);
				jdbcRowSet.updateDate("date", newDate);
				jdbcRowSet.updateDouble("price", newPrice);
				jdbcRowSet.updateRow();
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
			try {
				jdbcRowSet.rollback();
			} catch (Exception e) {
			}
		}
	}

	/*
	 * Raderar vald carservicemodel från databasen
	 */
	public void deleteServiceFromDatabase(CarServiceModel carServiceModel) {
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE service_id='" + carServiceModel.getId() + "'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				jdbcRowSet.deleteRow();
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
		}

	}

	/*
	 * Denna metod körs från CarController deleteCarFromDatabase när man raderar en bil så även dess servicetillfällen raderas
	 */
	public void deleteServiceFromDatabaseWhenDeletingCar(CarModel carModel) {
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE car_id='" + carModel.getId() + "'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				jdbcRowSet.deleteRow();
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

	/*
	 * Denna metod körs från CarController deleteCarFromDatabaseWhenDeletingCustomer som körs när man raderar en kund i CustomerEditViewController
	 * för att kundens bilar och dess servicetillfällen ska raderas samtidigt som kunden raderas
	 */
	public void deleteServiceFromDatabaseWhenDeletingCustomer(CustomerModel customerModel) {
		try {
			jdbcRowSet.setCommand("SELECT * FROM cartable WHERE owner_id='" + customerModel.getId() + "'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				int carId = jdbcRowSet.getInt("car_id");
				try {
					jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE car_id='" + carId + "'");
					jdbcRowSet.execute();
					while (jdbcRowSet.next()) {
						jdbcRowSet.deleteRow();
					}
				} catch (SQLException ex) {
					System.out.println("Error: " + ex.getMessage());
				}
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException ex) {
			System.out.println("Error: " + ex.getMessage());
		}

	}

	/*
	 * Denna metod hämtar en bils servicetillfällen och skickar tillbaka dem i en lista för att skrivas ut i listviews
	 */
	public List<CarServiceModel> getSelectedCarsServices(CarModel carModel) {
		List<CarServiceModel> serviceList = new ArrayList<CarServiceModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE car_id='" + carModel.getId() + "'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				CarServiceModel carServiceModel = new CarServiceModel();
				carServiceModel.setDescription(jdbcRowSet.getString("description"));
				carServiceModel.setDate(jdbcRowSet.getDate("date"));
				serviceList.add(carServiceModel);
			}
			jdbcRowSet.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return serviceList;
	}

	/*
	 * Denna metod körs från StatisticsViewController för att hämta alla servicetillfällen från databasen på vald månad.
	 * String date som skickas in ser ut t.ex. 2016-04
	 */
	public List<CarServiceModel> getNumberOfServicesForMonth(String date) {
		List<CarServiceModel> serviceList = new ArrayList<CarServiceModel>();
		try {
			jdbcRowSet.setCommand("SELECT * FROM carservicetable WHERE date LIKE '%" + date + "%'");
			jdbcRowSet.execute();
			while (jdbcRowSet.next()) {
				CarServiceModel carServiceModel = new CarServiceModel();
				carServiceModel.setPrice(jdbcRowSet.getDouble("price"));
				serviceList.add(carServiceModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return serviceList;

	}

}
