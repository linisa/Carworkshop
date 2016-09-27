package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.controllers.CarController;
import com.example.controllers.CustomerController;
import com.example.models.CarModel;
import com.example.models.CustomerModel;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import static javafx.scene.paint.Color.*;

public class CarCreateViewController implements Initializable {

	@FXML
	private Button btnCreateCar, btnGoToCarEditView;
	@FXML
	private TextField tfBrand, tfRegistrationNumber, tfModel, tfManufacturyYear, tfFuelType, tfColour;
	@FXML
	private Label lbCarCreated;
	@FXML
	private ListView<String> lvCustomerListView;

	private CustomerController customerController;
	private CustomerModel customerModel;
	private CarController carController;
	private CarModel carModel;

	public CarCreateViewController() {

	}

	@FXML
	private void createNewCar(ActionEvent event) {
		if (customerModel == null) {
			lbCarCreated.setTextFill(RED);
			lbCarCreated.setText("Select a customer from the list first!");
		} else {
			carModel = new CarModel();
			carModel.setBrand(tfBrand.getText());
			carModel.setRegistrationNumber(tfRegistrationNumber.getText());
			carModel.setModel(tfModel.getText());
			carModel.setYearOfManufactury(tfManufacturyYear.getText());
			carModel.setTypeOfFuel(tfFuelType.getText());
			carModel.setColour(tfColour.getText());
			carModel.setOwner(customerModel);
			carController.addCarToDatabase(carModel);
			lbCarCreated.setTextFill(BLACK);
			lbCarCreated.setText(carModel.getBrand() + " " + carModel.getModel() + " added to database!");
		}
	}

	/*
	 * Denna metod hämtar alla kunder och skriver över deras information till en ObservableList som används för att skriva ut kunderna till listview
	 */
	private ObservableList<String> populateCustomerListView() {
		List<CustomerModel> localListOfCustomers = customerController.getAllCustomersFromDatabase();
		ObservableList<String> observableListOfCustomers = FXCollections.observableArrayList();
		for (CustomerModel customer : localListOfCustomers) {
			observableListOfCustomers.add("Id: " + customer.getId() + " " + customer.getName());

		}
		return observableListOfCustomers;
	}

	/*
	 * Denna ListView listener ändrar customerModel som används när man ska lägga till en ny bil i databasen
	 */
	@FXML
	private void customerListViewListener() {
		lvCustomerListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				lbCarCreated.setText("");
				customerModel = customerController.getSelectedCustomerFromDatabase(new_val);
			}
		});
	}
	@FXML
	public void goToCarEditView(){
		try {
			Main.showCarEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		carController = new CarController();
		customerController = new CustomerController();
		lvCustomerListView.setItems(populateCustomerListView());
		customerListViewListener();

	}

}
