package com.example.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.controllers.CarController;
import com.example.controllers.CarServiceController;
import com.example.controllers.CustomerController;
import com.example.models.CarModel;
import com.example.models.CarServiceModel;
import com.example.models.CustomerModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
/*
 * Detta är vyn om man loggar in som customer. Visar alla kunder, bilar och services från databasen i listviews.
 * Listeners på listvyerna ändrar vilken kunds bilar och servicetillfällen som ska visas
 */
public class CustomerStartViewController implements Initializable{

	@FXML
	private ListView<String> lvCustomerListView, lvCarListView, lvServicesListView;
	
	private CarController carController;
	private CarModel carModel;
	private CustomerController customerController;
	private CustomerModel customerModel;
	private CarServiceController carServiceController;
	private List<CarModel> localListOfCars;
	private List<CarServiceModel> localListOfServices;
	
	private ObservableList<String> populateCustomerListView() {
		List<CustomerModel> localListOfCustomers = customerController.getAllCustomersFromDatabase();
		ObservableList<String> observableListOfCustomers = FXCollections.observableArrayList();
		for (CustomerModel customer : localListOfCustomers) {
			observableListOfCustomers.add("Id: " + customer.getId() + " " + customer.getName());

		}
		return observableListOfCustomers;
	}
	
	private ObservableList<String> populateCarListView() {
		ObservableList<String> observableListOfCars = FXCollections.observableArrayList();
		for (CarModel car : localListOfCars) {
			observableListOfCars.add("Id: " + car.getId() + " " + car.getBrand() + " " + car.getModel() + " "
					+ car.getRegistrationNumber());

		}
		return observableListOfCars;
	}
	
	private ObservableList<String> populateServiceListView() {
		ObservableList<String> observableListOfServices = FXCollections.observableArrayList();
		for (CarServiceModel service : localListOfServices) {
			observableListOfServices.add(service.getDate() + " " + service.getDescription());

		}
		return observableListOfServices;
	}
	@FXML
	private void carListViewListener() {
		lvCarListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				carModel = carController.getSelectedCarFromDatabase(new_val);
				localListOfServices = carServiceController.getSelectedCarsServices(carModel);
				lvServicesListView.setItems(populateServiceListView());
			}
		});
	}
	@FXML
	private void customerListViewListener() {
		lvCustomerListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				customerModel = customerController.getSelectedCustomerFromDatabase(new_val);
				localListOfCars = carController.getSelectedCustomersCars(customerModel);
				lvCarListView.setItems(populateCarListView());
			}
			
		});
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		carController = new CarController();
		customerController = new CustomerController();
		carServiceController = new CarServiceController();
		
		localListOfCars = carController.getAllCarsFromDatabase();
		lvCarListView.setItems(populateCarListView());
		
		lvCustomerListView.setItems(populateCustomerListView());

		localListOfServices = carServiceController.getAllServicesFromDatabase();
		lvServicesListView.setItems(populateServiceListView());
		
		customerListViewListener();
		carListViewListener();
		
	}

}
