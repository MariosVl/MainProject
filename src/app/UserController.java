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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UserController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private User user;
	private boolean next;
	private String username;
	private String password;
	private String emailV;
	private String phoneV;
	private String nameV;
	private String surnameV;
	private boolean isAdmin;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	  @FXML
	    private TextField email,name,phone,surname,usernameSign;
	  @FXML
	    private PasswordField passwordSign;
	  @FXML
	    private Label nameValidate,passwordSignValidate,phoneValidate,surnameValidate,usernameSignValidate,emailValidate;
      @FXML
	  public void UserCreation(ActionEvent event)
	  { 
		  next=false;
		  validate();
		  if(next)
		  {   
			  
			  user=new User(username,password,nameV,surnameV,emailV, phoneV,isAdmin);
			  try {
		             Connection connection = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		             String sql = "INSERT INTO tourist_office.users (username,\"password\",isadmin,phone,email,\"name\",surname) VALUES (?, ?, ?, ?, ?, ?, ?)";

		             PreparedStatement preparedStatement = connection.prepareStatement(sql);
		             preparedStatement.setString(1,user.getUsername() ); 
		             preparedStatement.setString(2, user.getPassword()); 
		             preparedStatement.setBoolean(3, user.isAdmin()); 
		             preparedStatement.setString(4,user.getPhone()); 
		             preparedStatement.setString(5,user.getEmail() ); 
		             preparedStatement.setString(6,user.getName() ); 
		             preparedStatement.setString(7,user.getSurname() ); 
		         
		           

		             int rowsInserted = preparedStatement.executeUpdate();
		             if (rowsInserted > 0) {
		             	Alert alert = new Alert(AlertType.INFORMATION);
		                 alert.setTitle("Information");
		                 alert.setHeaderText("Important Information");
		                 alert.setContentText("Successfully users's  entrance into base \nPress OK to close.");

		                 
		                 alert.getButtonTypes().setAll(ButtonType.OK);

		                
		                 alert.showAndWait().ifPresent(response -> {
		                     if (response == ButtonType.OK) {
		                        
		                         alert.close();
		                     }
		                 });
		                 preparedStatement.close();
			             connection.close();
		             }
		             
		             
		         } catch (SQLException e) {
		         	
		         	e.printStackTrace();
		         	
		         }
			  returning(event);
		  }
		 
	  }
      
	  public void validate()
	  {
		  int i=6;
		  if (name.getText().isEmpty() || (!name.getText().matches("[a-zA-Z]+") && !name.getText().matches("[\\u0370-\\u03FF\\u1F00-\\u1FFF]+"))) {
			    i--;
			    nameValidate.setText("Fill the name\n Only or greek or latin alphabet");
			    name.clear();
			} else {
			    nameValidate.setText("");
			}

			if (surname.getText().isEmpty() || (!surname.getText().matches("[a-zA-Z]+") && !surname.getText().matches("[\\u0370-\\u03FF\\u1F00-\\u1FFF]+"))) {
			    i--;
			    surnameValidate.setText("Fill the surname\n Only or greek or latin alphabet");
			    surname.clear();
			} else {
			    surnameValidate.setText("");
			}

		  
		  if (email.getText().isEmpty() || !email.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
		  {
		        emailValidate.setText("Fill  email which is valid\n Only latin alphabet");
		        i --;
		        email.clear();
		   }
		  else
		    {
		    	 emailValidate.setText("");
		    }
		  if (phone.getText().isEmpty() || !phone.getText().matches("^69\\d{8}$"))
	        {
		        phoneValidate.setText("Fill Greek mobile phone number");
		        i --;
		        phone.clear();
		    }
		    else
		    {
		    	phoneValidate.setText("");
		    }
		  if (passwordSign.getText().isEmpty() || !passwordSign.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-_+=|{}[\\\\]:;\\\"'<>,.?/]).{8,}$")) 
		    {
		        passwordSignValidate.setText("Fill the password (at least one capital, one lower, one number, one symbol, not shorter than 8 digits)\n Only latin alphabet");
		        i --;
		        passwordSign.clear();
		    } 
		    else {
		        passwordSignValidate.setText("");
		    }
		  
		  if (usernameSign.getText().isEmpty() || !usernameSign.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{10,}$")) {
		        usernameSignValidate.setText("Fill the username (at least one capital, one lower, one number)\nNot shorter than 10 digits\nOnly latin alphabet");
		        i --;
		        usernameSign.clear();
		    } else {
		    	
		    	try
		    	{
		    		boolean find=true;
		    		
		    		
		    	    Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		    	    String sql = "SELECT username FROM tourist_office.users "; 
		    		PreparedStatement stmt=conn.prepareStatement(sql);
		    		ResultSet rs=stmt.executeQuery();
		    		while(rs.next()&&find)
		    		{
		    			String name=rs.getString("username");
		    			if(name.equals(usernameSign.getText()))
		    			{
		    				find=false;
		    			}
		    		}
		    		rs.close();
		    		stmt.close();
		    		conn.close();
		    		if(!find)
		    		{
		    			i--;
		    			 usernameSignValidate.setText("This username already exists\n Try again");
		    			 usernameSign.clear();
		    		}
		    		else
		    		{
		    			usernameSignValidate.setText("");
		    		}
		    		
		    	}
		    	catch (SQLException e) {
	               
		    		
		    		e.printStackTrace();
	            }
		      
		    }
		  if(i==6)
		  {
			  next=true;
			  emailV=email.getText();
			  phoneV=phone.getText();
			  username=usernameSign.getText();
			  password=passwordSign.getText();
			  nameV=name.getText();
			  surnameV=surname.getText();
			  isAdmin=false;
			  Alert alert = new Alert(AlertType.INFORMATION);
	          alert.setTitle(null);
	          alert.setHeaderText("Validation succeed");
	          alert.setContentText("Press ok to close");

	          
	          alert.getButtonTypes().setAll(ButtonType.OK);

	         
	          alert.showAndWait().ifPresent(response -> {
	              if (response == ButtonType.OK) {
	                 
	                 alert.close();
	                
	                 
	               
	                 
	              }
	          });
		  }

	  }
	   
	public void returning(ActionEvent event)
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
