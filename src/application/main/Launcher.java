package application.main;
	
import application.mqtt.MQTTClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Launcher extends Application {
	
public Launcher() {
	// TODO Auto-generated constructor stub
}
	
	public static MQTTClient mqtt;
	
	
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
		 mqtt = new MQTTClient();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
		
			
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(getClass().getResource("mainView.fxml"));
			Scene scene = new Scene(root,400,400);	
			MainViewController controller = (MainViewController) fxmlLoader.getController();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("MQTT Explorer");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}
}
