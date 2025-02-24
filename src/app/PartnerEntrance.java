package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PartnerEntrance {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Partner partner;
	private boolean find;
	private FXMLLoader loader;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Label empty_password_Validate,empty_username_Validate;
	
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
	
	
	@FXML
	public void onSignUp(ActionEvent event) {

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			root = FXMLLoader.load(getClass().getResource("PartnerCreation.fxml"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 scene = new Scene(root);
		stage.setScene(scene);
		
		
	}
	
	@FXML
	public void onLogIn(ActionEvent event) {
		find = false;
	    validate();
	    if (find) {
	        FXMLLoader loader;
	        Parent root = null;
	        try {
	            if (partner.getTypeServ().equals("Accomondation")) {
	                loader = new FXMLLoader(getClass().getResource("accomService.fxml"));
	                root = loader.load();
	                addServiceAccom control = loader.getController();
	                control.setPartner(partner);
	            } else if (partner.getTypeServ().equals("Ship")) {
	                loader = new FXMLLoader(getClass().getResource("shipService.fxml"));
	                root = loader.load();
	                addServiceShip control = loader.getController();
	                control.setPartner(partner);
	            } else {
	                loader = new FXMLLoader(getClass().getResource("airplaneService.fxml"));
	                root = loader.load();
	                addServiceAirPlane control = loader.getController();
	                control.setPartner(partner);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	    }
	}

	public void validate() {
	    int i = 2;
	    if (username.getText().isEmpty() ||  !username.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{10,}$" )) {
	        i--;
	        empty_username_Validate.setText("Fill username \n At least one lower,one upper and one digit (0-9)\n Not shorter than 10 chars\n Latin alphabet");
	        empty_username_Validate.setOpacity(1.0);
	        username.clear();
	    }
	    if (password.getText().isEmpty() || !password.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-_+=|{}[\\\\]:;\\\"'<>,.?/]).{8,}$")) {
	        i--;
	        empty_password_Validate.setText("Fill password\n At least one lower, one upper, one digit(0-9),one symbol\n Not shorter than 8 chars\nLatin alphabet");
	        empty_password_Validate.setOpacity(1.0);
	        password.clear();
	    }
	    if (i == 2) {
	        
	        try {
	            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	            String sql = "SELECT * FROM tourist_office.partners p WHERE p.username=? AND p.\"password\"=? AND ispartner=?";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, username.getText());
	            stmt.setString(2, password.getText());
	            stmt.setBoolean(3, true);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String companyName = rs.getString("company_name");
	                String afm = rs.getString("afm");
	                String loc = rs.getString("loc");
	                String type = rs.getString("typeservice");
	                String email = rs.getString("email");
	                String phone = rs.getString("phone");
	                String financial_agreement = rs.getString("financial_agreement");
	                String username = rs.getString("username");
	                String password = rs.getString("password");
	                partner = new Partner(companyName, afm, loc, type, email, phone, financial_agreement, username, password);
	                find = true;
	            } else {
	                
	               
	                empty_password_Validate.setText("User not found");
	                empty_password_Validate.setOpacity(1.0);
	                empty_username_Validate.setText("");
	                username.clear();
	                password.clear();
	            }

	            rs.close();
	            stmt.close();
	            conn.close();

	        } catch (SQLException e) {
	           
	            e.printStackTrace();
	        }
	    }
	}

	
	
	
	
}

