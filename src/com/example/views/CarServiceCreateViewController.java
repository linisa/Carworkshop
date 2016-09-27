package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static javafx.scene.paint.Color.*;

public class CarServiceCreateViewController implements Initializable {

	@FXML
	private ListView<String> lvCarListView;
	@FXML
	private TextArea taDescription;
	@FXML
	private TextField tfPrice;
	@FXML
	private DatePicker dpDatePicker;
	@FXML
	private Button btnCreateService, btnGoToServiceEditView;
	@FXML
	private Label lbInfoLabel, lbDateLabel;

	private CarController carController;
	private CarModel carModel;
	private CarServiceController carServiceController;
	private CarServiceModel carServiceModel;

	public CarServiceCreateViewController() {

	}

	@FXML
	private void createNewCarService(ActionEvent event) throws ParseException {
		lbInfoLabel.setTextFill(RED);
		if (carModel == null) {
			lbInfoLabel.setText("Select a car for this service!");
		}else if (dpDatePicker.getValue() == null) {
			lbDateLabel.setText("A date is required!");
		}else if (tfPrice.getText().contains(",")) {
			lbInfoLabel.setText("Decimal values need to be separated with a dot!");
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			carServiceModel = new CarServiceModel();
			carServiceModel.setDescription(taDescription.getText());
			java.util.Date utilDate = simpleDateFormat.parse(dpDatePicker.getValue().toString());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			carServiceModel.setDate(sqlDate);
			carServiceModel.setPrice(Double.parseDouble(tfPrice.getText()));
			carServiceModel.setCarModel(carModel);
			carServiceController.addServiceToDatabase(carServiceModel);
			lbInfoLabel.setTextFill(BLACK);
			lbInfoLabel.setText("Service added on " + carServiceModel.getDate().toString() + " for " + carServiceModel.getCarModel().getOwner().getName());
		}
	}

	/*
	 * Denna metod hämtar alla bilar och skriver över deras information till en ObservableList som används för att skriva ut bilarna till listview
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
	 * Denna listener sätter carModel till det som väljs i listview. carModel behövs för att skapa en ny service.
	 */
	@FXML
	private void carListViewListener() {
		lvCarListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				lbInfoLabel.setText("");
				lbDateLabel.setText("");
				carModel = carController.getSelectedCarFromDatabase(new_val);
			}
		});
	}
	@FXML
	public void goToServiceEdit() {
		try {
			Main.showServiceEditView();
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
	}
}
