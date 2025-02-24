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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEntrance {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private User user;
	private boolean find;
	private FXMLLoader loader;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	@FXML
	private Label empty_password_Validate, empty_username_Validate;
	@FXML
	private PasswordField password;
	@FXML
	private TextField username;
	private UsersFunct control;
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
     public void onSignUp(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 try {
			root = FXMLLoader.load(getClass().getResource("UserCreation.fxml"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 scene = new Scene(root);
		stage.setScene(scene);
    }
	
	
	@FXML
	public void onLogIn(ActionEvent event)
	{
		find=false;
		validate();
		if(find)
		{
			if(user.isAdmin())
			{
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				 try {
					loader = new FXMLLoader(getClass().getResource("Administrators.fxml"));
					root=loader.load();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				 scene = new Scene(root);
				 stage.setScene(scene);
			}
			else
			{
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				 try {
					loader = new FXMLLoader(getClass().getResource("UsersFunct.fxml"));
					root=loader.load();
					 control = loader.getController();
                    control.setUser(user);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				 
				 scene = new Scene(root);
				 stage.setScene(scene);
				 
			} 
				 	
	}
	}
	public void validate()
	{
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
		    if(i==2)
		    {
		    	try {
		            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		            String sql = "SELECT * FROM tourist_office.users u WHERE u.username=? AND u.\"password\"=? ";
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            stmt.setString(1, username.getText());
		            stmt.setString(2, password.getText());
		            ResultSet rs = stmt.executeQuery();

		            if (rs.next()) {
		               
		                String name = rs.getString("name");
		                String surname = rs.getString("surname");
		                String email = rs.getString("email");
		                String phone = rs.getString("phone");
		                String username = rs.getString("username");
		                String password = rs.getString("password");
		                boolean isAdmin=rs.getBoolean("isadmin");
		                 user= new User(username,password,name,surname,email,phone,isAdmin);
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
