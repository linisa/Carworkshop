package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
/*
 * Detta är startvyn om man loggar in som admin.
 */
public class EmployeeStartViewController implements Initializable {
	@FXML
	private Button btnGoToCustomerView, btnGoToCarView, btnGoToServiceView, btnGoToStatisticsView;

	@FXML
	public void goToCustomerView(ActionEvent event) {
		try {
			Main.showCustomerEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void goToCarView(ActionEvent event) {
		try {
			Main.showCarEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void goToServiceView(ActionEvent event) {
		try {
			Main.showServiceEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void goToStatisticsView(ActionEvent event) {
		try {
			Main.showStatisticsView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
