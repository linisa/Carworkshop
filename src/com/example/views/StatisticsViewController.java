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

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class StatisticsViewController implements Initializable {
	private CarController carController;
	private CustomerController customerController;
	private CarServiceController carServiceController;

	@FXML
	private TextField tfTotalCustomers, tfTotalCars, tfNumberOfServices, tfEarnings, tfCarBrand, tfCarColour, tfCarFuel, 
						tfTotalCarsByBrand, tfTotalCarsByColour, tfTotalCarsByFuel;
	@FXML
	private DatePicker dpDatePicker;
	@FXML
	private Button btnSearchBrand, btnSearchColour, btnSearchFuel;

	/*
	 * Denna metod körs från onclicklistener på DatePicker och hämtar antal servicetillfällen och totala priset för vald månad.
	 * String dateInput skickas in som t.ex. 2016-04
	 */
	private void getDateInput() {
		
		if (dpDatePicker.getValue() == null) {

		} else {
			String dateInput = dpDatePicker.getValue().toString();
			List<CarServiceModel> serviceList = carServiceController.getNumberOfServicesForMonth(dateInput.substring(0, 7));
			tfNumberOfServices.setText(Integer.toString(serviceList.size()));
			double earnings = 0;
			for (CarServiceModel service : serviceList) {
				earnings += service.getPrice();
			}
			tfEarnings.setText(Double.toString(earnings));
		}
	}
	/*
	 * Denna metod söker efter antal bilar i databasen efter märke med det som användaren skrivit in
	 */
	@FXML
	public void getCarsByBrand(){
		List<CarModel> carList = carController.getTotalCarsByAttribute("brand", tfCarBrand.getText());
		tfTotalCarsByBrand.setText("No. of " + tfCarBrand.getText() + "'s: " + Integer.toString(carList.size()));
	}
	/*
	 * Denna metod söker efter antal bilar i databasen efter färg med det som användaren skrivit in
	 */
	@FXML
	public void getCarsByColour(){
		List<CarModel> carList = carController.getTotalCarsByAttribute("colour", tfCarColour.getText());
		tfTotalCarsByColour.setText("No. of " + tfCarColour.getText() + " cars: " + Integer.toString(carList.size()));
	}
	/*
	 * Denna metod söker efter antal bilar i databasen efter bränsle med det som användaren skrivit in
	 */
	@FXML
	public void getCarsByFuel(){
		List<CarModel> carList = carController.getTotalCarsByAttribute("fuel", tfCarFuel.getText());
		tfTotalCarsByFuel.setText("No. of " + tfCarFuel.getText() + " cars: " + Integer.toString(carList.size()));
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		carController = new CarController();
		customerController = new CustomerController();
		carServiceController = new CarServiceController();

		List<CustomerModel> customerList = customerController.getAllCustomersFromDatabase();
		tfTotalCustomers.setText(Integer.toString(customerList.size()));
		List<CarModel> carList = carController.getAllCarsFromDatabase();
		tfTotalCars.setText(Integer.toString(carList.size()));

		dpDatePicker.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				getDateInput();

			}

		});
	}

}
