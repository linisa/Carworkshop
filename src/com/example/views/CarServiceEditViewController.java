package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

import com.example.controllers.CarServiceController;
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

public class CarServiceEditViewController implements Initializable {

	@FXML
	private Button btnEditService, btnDeleteService, btnGoToServiceCreateView, btnGoToCarEditView, btnGoToStartView;
	@FXML
	private ListView<String> lvServiceListView;
	@FXML
	private TextArea taEditDescription;
	@FXML
	private DatePicker dpEditDate;
	@FXML
	private TextField tfEditPrice;
	@FXML
	private Label lbHeader, lbInfoLabel;

	private CarServiceController carServiceController;
	private CarServiceModel carServiceModel;
	private SimpleDateFormat simpleDateFormat;

	public CarServiceEditViewController() {

	}
	/*
	 * Denna metod hämtar alla servicetillfällen från databasen och skriver över deras information till en 
	 * ObservableList som används för att skriva ut services till listview
	 */
	private ObservableList<String> populateServiceListView() {
		List<CarServiceModel> localListOfServices = carServiceController.getAllServicesFromDatabase();
		ObservableList<String> observableListOfServices = FXCollections.observableArrayList();
		for (CarServiceModel service : localListOfServices) {
			observableListOfServices.add("Id: " + service.getId() + " " + service.getDate());

		}
		return observableListOfServices;
	}

	/*
	 * Denna listener sätter carServiceModel till det man väljer i list view. Denna carServiceModel används sen i de andra metoderna
	 */
	@FXML
	private void serviceListViewListener() {
		lvServiceListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				carServiceModel = carServiceController.getSelectedServiceFromDatabase(new_val);
				if (carServiceModel.getDate() == null) {
					lbHeader.setText("");
					taEditDescription.setText("");
					dpEditDate.setValue(null);
					tfEditPrice.setText("");
					lbInfoLabel.setText("");
					taEditDescription.setEditable(false);
					dpEditDate.setVisible(false);
					tfEditPrice.setEditable(false);
				} else {
					
					lbHeader.setText("Edit service for " + carServiceModel.getCarModel().getBrand() + " "
							+ carServiceModel.getCarModel().getModel() + " "
							+ carServiceModel.getCarModel().getRegistrationNumber());
					
					taEditDescription.setText(carServiceModel.getDescription());

					try {
						java.util.Date utilDate = simpleDateFormat.parse(carServiceModel.getDate().toString());
						java.time.LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						dpEditDate.setValue(localDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					tfEditPrice.setText(String.valueOf(carServiceModel.getPrice()));

					lbInfoLabel.setText("");
					lbInfoLabel.setTextFill(BLACK);
					taEditDescription.setEditable(true);
					dpEditDate.setVisible(true);
					tfEditPrice.setEditable(true);
				}

			}
		});
	}

	@FXML
	private void updateServiceInformation(ActionEvent event) throws ParseException {
		if (dpEditDate.getValue() == null) {
			lbInfoLabel.setTextFill(RED);
			lbInfoLabel.setText("A date is required!");
		} else {
			java.util.Date utilDate = simpleDateFormat.parse(dpEditDate.getValue().toString());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			carServiceController.updateServiceInDatabase(carServiceModel, taEditDescription.getText(), sqlDate,
					Double.parseDouble(tfEditPrice.getText()));

			lvServiceListView.setItems(populateServiceListView());
			lbInfoLabel.setTextFill(BLACK);
			lbInfoLabel.setText("Service updated in database!");
		}
	}

	@FXML
	private void deleteService(ActionEvent event) {
		if (dpEditDate.getValue() == null) {
			lbInfoLabel.setTextFill(RED);
			lbInfoLabel.setText("A date is required!");
		} else {
			carServiceController.deleteServiceFromDatabase(carServiceModel);
			lvServiceListView.setItems(populateServiceListView());
			lbInfoLabel.setTextFill(BLACK);
			lbInfoLabel.setText("Service deleted from database!");
		}
	}
	@FXML
	public void goToServiceCreateView(ActionEvent event){
		try {
			Main.showServiceCreateView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToCarEditView(ActionEvent event){
		try {
			Main.showCarEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToStartView(ActionEvent event){
		try {
			Main.showEmployeeStartView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		carServiceController = new CarServiceController();
		lvServiceListView.setItems(populateServiceListView());
		serviceListViewListener();
	}

}
