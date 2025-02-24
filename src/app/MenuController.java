package app;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
	
	Stage stage;
	Parent root;
	Scene scene;
	FXMLLoader loader;
	public void onPartner(ActionEvent event) throws IOException
	{
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		root=FXMLLoader.load(getClass().getResource("PartnerEntrance.fxml"));
		scene=new Scene(root);
		stage.setScene(scene);
		
	}
	
	
	
	public void onUser(ActionEvent event) throws IOException
	{
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		loader=new FXMLLoader(getClass().getResource("UserEntrance.fxml"));
		root=loader.load();
		scene=new Scene(root);
		stage.setScene(scene);
			       
    }
		
	
		

	
	

}
