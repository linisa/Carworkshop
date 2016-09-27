package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.controllers.CarController;
import com.example.controllers.CarServiceController;
import com.example.models.CarModel;
import com.example.models.CarServiceModel;

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

public class CarEditViewController implements Initializable {

	@FXML
	private Button btnSaveChanges, btnDeleteCar, btnGoToCarCreateView, btnGoToStartView, btnGoToServiceView, btnGoToCustomerView;
	@FXML
	private TextField tfBrand, tfRegistrationNumber, tfModel, tfYearOfManufactury, tfFuelType, tfColour;
	@FXML
	private ListView<String> lvCarListView, lvCarServiceListView;
	@FXML
	private Label lbInfoLabel, lbHeader;

	private CarController carController;
	private CarModel carModel;
	private CarServiceController carServiceController;

	@FXML
	private void updateCarInformation(ActionEvent event) {
		if (carModel == null) {

		} else {
			carController.updateCarInDatabase(carModel, tfBrand.getText(), tfRegistrationNumber.getText(),
					tfYearOfManufactury.getText(), tfModel.getText(), tfFuelType.getText(), tfColour.getText());

			lvCarListView.setItems(populateCarListView());
			lbInfoLabel.setText("Car updated in database!");
		}
	}

	/*
	 * Denna metod hämtar alla bilar från databasen och skriver över deras information till en ObservableList som används för att 
	 * skriva ut bilarna till listview
	 */
	private ObservableList<String> populateCarListView() {
		List<CarModel> localListOfCars = carController.getAllCarsFromDatabase();
		ObservableList<String> observableListOfCars = FXCollections.observableArrayList();
		for (CarModel car : localListOfCars) {
			observableListOfCars.add("Id: " + car.getId() + " " + car.getBrand() + " " + car.getModel() + " "
					+ car.getRegistrationNumber());

		}
		return observableListOfCars;
	}

	/*
	 * Denna metod sätter den valda bilens servicetillfällen till en observablelist som skrivs ut i listview
	 */
	private ObservableList<String> populateServiceListView() {
		List<CarServiceModel> localListOfServices = carModel.getServiceList();
		ObservableList<String> observableListOfServices = FXCollections.observableArrayList();
		for (CarServiceModel services : localListOfServices) {
			if (localListOfServices != null) {
				observableListOfServices.add(services.getDescription() + " " + services.getDate().toString());
			}
		}
		return observableListOfServices;
	}

	/*
	 * Denna metod sätter carModel beroende på vad som väljs i listview. Denna carmodel används sen i de andra metoderna
	 */
	@FXML
	private void carListViewListener() {
		lvCarListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				carModel = carController.getSelectedCarFromDatabase(new_val);
				if (carModel.getBrand() == null) {
					lbHeader.setText("");
					lbHeader.setText("");
					tfBrand.setText("");
					tfRegistrationNumber.setText("");
					tfModel.setText("");
					tfYearOfManufactury.setText("");
					tfFuelType.setText("");
					tfColour.setText("");

					tfBrand.setEditable(false);
					tfRegistrationNumber.setEditable(false);
					tfModel.setEditable(false);
					tfYearOfManufactury.setEditable(false);
					tfFuelType.setEditable(false);
					tfColour.setEditable(false);
				} else {
					lbHeader.setText("Edit " + carModel.getOwner().getName() + "'s " + carModel.getBrand());
					tfBrand.setText(carModel.getBrand());
					tfRegistrationNumber.setText(carModel.getRegistrationNumber());
					tfModel.setText(carModel.getModel());
					tfYearOfManufactury.setText(carModel.getYearOfManufactury());
					tfFuelType.setText(carModel.getTypeOfFuel());
					tfColour.setText(carModel.getColour());

					getCarServicesToListView();

					tfBrand.setEditable(true);
					tfRegistrationNumber.setEditable(true);
					tfModel.setEditable(true);
					tfYearOfManufactury.setEditable(true);
					tfFuelType.setEditable(true);
					tfColour.setEditable(true);
					lbInfoLabel.setText("");
				}
			}
		});
	}

	@FXML
	private void deleteCar() {
		if (carModel == null) {

		} else {
			carController.deleteCarFromDatabase(carModel);
			lbInfoLabel.setText("Car deleted from database!");
			lvCarListView.setItems(populateCarListView());
		}
	}
	/*
	 * Denna metod hämtar vald bils servicetillfällen från databasen och skriver ut dem i listview
	 */
	@FXML
	private void getCarServicesToListView() {
		List<CarServiceModel> serviceList = carServiceController.getSelectedCarsServices(carModel);
		carModel.setServiceList(serviceList);
		lvCarServiceListView.setItems(populateServiceListView());
	}

	@FXML
	public void goToServiceEditView() {
		try {
			Main.showServiceEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToStartView(){
		try {
			Main.showEmployeeStartView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToCarCreateView(){
		try {
			Main.showCarCreateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToCustomerEditView(){
		try {
			Main.showCustomerEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		carController = new CarController();
		carServiceController = new CarServiceController();
		lvCarListView.setItems(populateCarListView());
		carListViewListener();
		lvCarServiceListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				goToServiceEditView();

			}

		});

	}
}
