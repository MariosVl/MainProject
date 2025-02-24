package app;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsersFunct {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private FXMLLoader loader;
	private User user;
	private ReadyPackageController control;
	private DIYUsController control1;
	private PrintReservationController control2;
	@FXML
	public void onChoiceReady(ActionEvent event)
	{
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			loader = new FXMLLoader(getClass().getResource("ReadyPackage.fxml"));
			root=loader.load();
			control=loader.getController();
			control.setUser(user);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
		 scene = new Scene(root);
		 stage.setScene(scene);
		
	}
	@FXML
	public void onCreateDIYUs(ActionEvent event)
	{stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	 try {
		loader = new FXMLLoader(getClass().getResource("DIYUs.fxml"));
		root=loader.load();
		control1=loader.getController();
		control1.setUser(user);
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	 
	 scene = new Scene(root);
	 stage.setScene(scene);
	 
		
	}
	@FXML
	public void onChoicePrint(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			loader = new FXMLLoader(getClass().getResource("Print.fxml"));
			root=loader.load();
			control2=loader.getController();
			control2.setUser(user);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
		 scene = new Scene(root);
		 stage.setScene(scene);
		
	}
	
	public void setUser(User aUser)
	{
		this.user=aUser;
	}
	@FXML
	public void onBacktoMenu(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 scene = new Scene(root);
		stage.setScene(scene);
	}
	
}
