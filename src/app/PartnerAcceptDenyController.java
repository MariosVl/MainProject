package app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;

public class PartnerAcceptDenyController implements Initializable {
	@FXML 
	private ListView<Partner> AcceptDeny;
	private ObservableList<Partner> partners = FXCollections.observableArrayList();
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		AcceptDeny.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		AcceptDeny.setCellFactory(new Callback<ListView<Partner>, ListCell<Partner>>() {
		    @Override
		    public ListCell<Partner> call(ListView<Partner> listView) {
		        return new ListCell<Partner>() {
		            @Override
		            protected void updateItem(Partner partner, boolean empty) {
		                super.updateItem(partner, empty);
		                if (empty || partner == null) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellPartnerAccept.fxml"));
		                    try {
		                        Parent root = loader.load();
		                        CellPartnerAcceptController controller = loader.getController();
		                        controller.setData(partner);
		                        setGraphic(root);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		    }
		});
		
		getPartners();
		
AcceptDeny.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Partner>()
		
		{

			@Override
			public void changed(ObservableValue<? extends Partner>  observable , Partner oldValue , Partner newValue) {
				  
				  try
				  {
					  
					 Connection conn=DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
					 String sql="UPDATE tourist_office.partners SET ispartner = ? WHERE afm = ?";
					 PreparedStatement stmt=conn.prepareStatement(sql);
					 stmt.setBoolean(1, true);
					 if(newValue!=null)
					 {
					 stmt.setString(2, newValue.getAFM());
					 }
					 int rowInfected=stmt.executeUpdate();
					 stmt.close();    
				     conn.close();
					 if(rowInfected>0)
					 {    
						 
						 
                     
						 Alert alert = new Alert(AlertType.INFORMATION);
			                alert.setTitle("Information");
			                alert.setHeaderText("Important Information");
			                alert.setContentText("Successfully update \nPress OK to close.");

			                
			                alert.getButtonTypes().setAll(ButtonType.OK);

			               
			                alert.showAndWait().ifPresent(response -> {
			                    if (response == ButtonType.OK) {
			                       
			                    	
			                        alert.close();
			                        
			                        
			                        
			                    }
			                });
					 }
				  }
				  catch(SQLException e)
				  {
					  e.printStackTrace();
				  }
			     
			        
			}
	
		}
		
		
		
		);
	    
	    
}
	
	public void getPartners()
	{
		partners.clear();
        
	    try {
	        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 

	        String sql = "SELECT * FROM tourist_office.partners WHERE ispartner=?";
	       PreparedStatement preparedStatement = conn.prepareStatement(sql);
	       preparedStatement.setBoolean(1, false);
	       ResultSet resultSet = preparedStatement.executeQuery();

	      
	    

	        while (resultSet.next()) {
	            String companyNameP = resultSet.getString("company_name");
	            String afmP = resultSet.getString("afm");
	            String locationP = resultSet.getString("loc");
	            String typeServP=resultSet.getString("typeservice");
	            String phoneP=resultSet.getString("phone"); 
	            String emailP=resultSet.getString("email"); 
	            String financial_agreementP=resultSet.getString("financial_agreement");
	            String usernameP=resultSet.getString("username");
	            String passwordP=resultSet.getString("password");
	            Partner partner = new Partner(companyNameP,afmP,locationP,typeServP,emailP,phoneP,financial_agreementP,usernameP,passwordP);
	            partners.add(partner);
	        }
	       preparedStatement.close();
	       resultSet.close();
	       conn.close();
	       AcceptDeny.setItems(partners);
		
		   
		
		
	}catch(SQLException e)
	{
	  e.printStackTrace();
	  }
	    
	}
	

    @FXML
   public void onRestart(ActionEvent event) {
    	
    	
    	getPartners();

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
