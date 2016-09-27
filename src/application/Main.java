package application;
	
import java.io.IOException;

import com.example.controllers.StartController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		
		Main.primaryStage = primaryStage;
		try {
			showLoginView();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void showLoginView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/LoginView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	public static void showCustomerStartView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CustomerStartView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		Stage customerStage = new Stage();
		customerStage.setTitle("Welcome customer");
		customerStage.setScene(scene);
		customerStage.show();
	}
	public static void showEmployeeStartView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/EmployeeStartView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Welcome employee");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showCustomerCreateView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CustomerCreateView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Create a new customer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showCustomerEditView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CustomerEditView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Customers");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showCarCreateView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CarCreateView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Create a new car");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showCarEditView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CarEditView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Cars");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showServiceCreateView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CarServiceCreateView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Create a new service");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void showServiceEditView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/CarServiceEditView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		primaryStage.setTitle("Services");
		primaryStage.setScene(scene);
		primaryStage.show();
	}	
	public static void showStatisticsView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../com/example/fxml/StatisticsView.fxml"));
		AnchorPane layout = loader.load();
		Scene scene = new Scene(layout);
		Stage statisticsStage = new Stage();
		statisticsStage.setTitle("Statistics");
		statisticsStage.setScene(scene);
		statisticsStage.show();
	}
	public static void main(String[] args) {
		StartController start = new StartController();
		launch(args);
	}
}
