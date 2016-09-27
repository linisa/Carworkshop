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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CustomerEditViewController implements Initializable {

	@FXML
	private ListView<String> lvCustomerListView;
	@FXML
	private ListView<String> lvCarListView;
	@FXML
	private TextField tfEditCustomerName, tfEditCustomerAddress, tfEditCustomerPhone, tfEditCustomerAge,
			tfEditCustomerEmail;
	@FXML
	private Button btnSaveChanges, btnDeleteCustomer, btnGoToStartView, btnGoToCustomerCreateView, btnGoToCarEditView;
	@FXML
	private Label lbInfoLabel, lbCarList;

	private CustomerController customerController;
	private CustomerModel customerModel;
	private CarController carController;

	public CustomerEditViewController() {

	}

	@FXML
	private void updateCustomerInformation() {
		if (customerModel == null) {

		} else {
			customerController.updateCustomerInDatabase(customerModel, tfEditCustomerName.getText(),
					tfEditCustomerAddress.getText(), tfEditCustomerPhone.getText(), tfEditCustomerAge.getText(),
					tfEditCustomerEmail.getText());
			lvCustomerListView.setItems(populateCustomerListView());
			lbInfoLabel.setText("Customer updated in database");
		}
	}

	@FXML
	private void deleteCustomer(ActionEvent event) {
		if (customerModel == null) {

		} else {
			customerController.deleteCustomerFromDatabase(customerModel);
			lvCustomerListView.setItems(populateCustomerListView());
			lbCarList.setText("");
		}
	}

	/*
	 * Denna metod hämtar alla kunder från databasen och skriver över deras information till en 
	 * ObservableList som används för att skriva ut kunderna till listview
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
	 * denna metod skriver kundens billista till en ObservableList som används för att skriva ut bilarna till listview
	 */
	private ObservableList<String> populateCarListView() {
		List<CarModel> localListOfCars = customerModel.getCarList();
		ObservableList<String> observableListOfCars = FXCollections.observableArrayList();
		for (CarModel cars : localListOfCars) {
			if (localListOfCars != null) {
				observableListOfCars.add(cars.getBrand() + " " + cars.getModel());
			}
		}
		return observableListOfCars;
	}

	/*
	 * Denna listener sätter customerModel till det man väljer i list view. Denna customerModel används sen i de andra metoderna
	 */
	@FXML
	private void customerListViewListener() {
		lvCustomerListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				customerModel = customerController.getSelectedCustomerFromDatabase(new_val);

				tfEditCustomerName.setText(customerModel.getName());
				tfEditCustomerAddress.setText(customerModel.getAddress());
				tfEditCustomerPhone.setText(customerModel.getPhoneNumber());
				tfEditCustomerAge.setText(customerModel.getAge());
				tfEditCustomerEmail.setText(customerModel.getEmail());

				getCustomerCarsToListView();

				tfEditCustomerName.setEditable(true);
				tfEditCustomerAddress.setEditable(true);
				tfEditCustomerPhone.setEditable(true);
				tfEditCustomerAge.setEditable(true);
				tfEditCustomerEmail.setEditable(true);
				lbInfoLabel.setText("");
			}
		});
	}

	/*
	 * Denna metod hämtar vald kunds bilar från databasen och skriver ut dem i listview
	 */
	@FXML
	private void getCustomerCarsToListView() {
		if (customerModel.getName() == null) {
			lbCarList.setText("");
		} else {
			lbCarList.setText(customerModel.getName() + "'s cars");
			List<CarModel> carList = carController.getSelectedCustomersCars(customerModel);
			customerModel.setCarList(carList);
			lvCarListView.setItems(populateCarListView());
		}

	}

	@FXML
	public void goToCarEditView() {
		try {
			Main.showCarEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void goToStartView(ActionEvent event) {
		try {
			Main.showEmployeeStartView();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@FXML
	public void goToCustomerCreateView(){
		try {
			Main.showCustomerCreateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		customerController = new CustomerController();
		carController = new CarController();
		lvCustomerListView.setItems(populateCustomerListView());
		customerListViewListener();
		lvCarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				goToCarEditView();

			}

		});

	}
}
