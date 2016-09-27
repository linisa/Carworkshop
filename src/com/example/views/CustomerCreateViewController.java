package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.controllers.CustomerController;
import com.example.models.CustomerModel;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerCreateViewController implements Initializable{

	@FXML
	private Button btnCreateCustomer, btnGoToCustomerView;
	@FXML
	private TextField tfId, tfName, tfTitle, tfShowName, tfAddress, tfPhoneNumber, tfAge, tfEmail, tfDeleteName;
	@FXML
	private Label lbInfoLabel, lbNameLabel, lbEmailLabel;

	private CustomerController customerController = new CustomerController();

	@FXML
	public void createNewCustomer(ActionEvent event) {
		if (tfName.getText().equals("")||tfEmail.getText().equals("")) {
			lbNameLabel.setText("This field is required!");
			lbEmailLabel.setText("This field is required!");	
		} else {
			CustomerModel customerModel = new CustomerModel();
			customerModel.setName(tfName.getText());
			customerModel.setAddress(tfAddress.getText());
			customerModel.setPhoneNumber(tfPhoneNumber.getText());
			customerModel.setAge(tfAge.getText());
			customerModel.setEmail(tfEmail.getText());
			customerController.addCustomerToDatabase(customerModel);

			lbInfoLabel.setText("Customer " + customerModel.getName() + " added to database!");
			lbEmailLabel.setText("");
			lbNameLabel.setText("");
		}
	}

	@FXML
	public void goToCustomerEditView(ActionEvent event) {
		try {
			Main.showCustomerEditView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
