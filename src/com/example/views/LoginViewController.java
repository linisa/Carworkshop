package com.example.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController implements Initializable{

	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Label lbLoginLabel;
	
	@FXML
	public void handleLogin(ActionEvent event){
		
		String username = tfUsername.getText();
		String password = pfPassword.getText();
		
		if(username.equals("customer") && password.equals("password")){
			try {
				Main.showCustomerStartView();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(username.equals("admin") && password.equals("admin")){
			try {
				Main.showEmployeeStartView();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			lbLoginLabel.setText("Wrong login, try again.");
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}

}
