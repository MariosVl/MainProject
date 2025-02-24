package app;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

	Stage stage;
	Scene scene;
	Parent root;
	FXMLLoader loader;
	
	@FXML
	public void onCreateDIY(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			loader = new FXMLLoader(getClass().getResource("DIY.fxml"));
			root=loader.load();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 scene = new Scene(root);
		 stage.setScene(scene);
	}
	@FXML
	public void onAcceptPartner(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			loader = new FXMLLoader(getClass().getResource("PartnerAcceptDeny.fxml"));
			root=loader.load();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 scene = new Scene(root);
		 stage.setScene(scene);
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

