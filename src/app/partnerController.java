package app;

import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class partnerController {

	private String companyName;
	private String AFMP;
	private String location;
	private String phoneV;
	private String emailV;
	private String financial_ΑgreementV;
	private String usernameV;
	private String passwordV;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean next;
	private String typeServ;
	private Partner partner;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	@FXML
	private Label afmValidate,cnValidate,emailValidate,locValidate,phoneValidate,financial_ΑgreementValidate,usernameSignValidate,passwordSignValidate,serviceValidate;

	@FXML
	private TextField cn,afm,loc,email,phone,usernameSign;
	@FXML
	private PasswordField passwordSign;
	@FXML
	private TextArea financial_Αgreement;
	@FXML
	RadioButton Accom,Ship,Airplane;
	
	public void backMenu(ActionEvent event) throws IOException
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		 scene = new Scene(root);
		 stage.setScene(scene);
	}
	
	
	public void partnerCreation(ActionEvent event) throws IOException
	{
	    next=true;
	   
	    validate();
	    if(!next)
	    {     
	    	 partner=new Partner(companyName,AFMP,location,typeServ,emailV,phoneV,financial_ΑgreementV,usernameV,passwordV);
	    	 try {
	             Connection connection = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	             String sql = "INSERT INTO tourist_office.partners (company_name,afm,loc,typeservice,phone,email,financial_agreement,username,\"password\",ispartner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	             PreparedStatement preparedStatement = connection.prepareStatement(sql);
	             preparedStatement.setString(1, partner.getCompanyName()); 
	             preparedStatement.setString(2,partner.getAFM() ); 
	             preparedStatement.setString(3,partner.getLocation() ); 
	             preparedStatement.setString(4,partner.getTypeServ() ); 
	             preparedStatement.setString(5,partner.getPhone() ); 
	             preparedStatement.setString(6,partner.getEmail() ); 
	             preparedStatement.setString(7,partner.getFinancial_agreement() ); 
	             preparedStatement.setString(8,partner.getUsername() ); 
	             preparedStatement.setString(9,partner.getPassword() ); 
	             preparedStatement.setBoolean(10,false); 
	             
	           

	             int rowsInserted = preparedStatement.executeUpdate();
	             if (rowsInserted > 0) {
	             	Alert alert = new Alert(AlertType.INFORMATION);
	                 alert.setTitle("Information");
	                 alert.setHeaderText("Important Information");
	                 alert.setContentText("Successfully partner's  entrance into base \nPress OK to close.");

	                 
	                 alert.getButtonTypes().setAll(ButtonType.OK);

	                
	                 alert.showAndWait().ifPresent(response -> {
	                     if (response == ButtonType.OK) {
	                        
	                         alert.close();
	                     }
	                 });
	                 preparedStatement.close();
		             connection.close();
	             }
	             
	             else
	             {
	            	 System.out.println("Failed to make connection!");
	             }
	         } catch (SQLException e) {
	         	
	         	
	         	e.printStackTrace();
	         	
	         }
	    	 
	    	 returning(event);
	    }

      
	    
	
	   
	}
	
	public void validate()
	{
		    int i=9;
		    if (cn.getText().isEmpty() || !cn.getText().matches("[a-zA-Z\\u0370-\\u03FF]+")) {
		        cnValidate.setText("Fill company name and use only letters\n Both greek & latin alphabet");
		        i = i - 1;
		        cn.clear();
		        
		    } 
		    else
		    {
		    	 cnValidate.setText("");
		    }
		    if (!afm.getText().matches("\\d{9}")) {
		        afmValidate.setText("Fill AFM with 9 digits (numbers only)");
		        i = i - 1;
		        afm.clear();
		    }
		    else
		    {
		    	 boolean find=true;
		    	  try {
		    		  Connection conn= DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		    		  String sql="SELECT afm FROM tourist_office.partners";
		    		  PreparedStatement stm=conn.prepareStatement(sql);
		    		  ResultSet rs=stm.executeQuery();
		    		  while(rs.next()&&find)
		    		  {
		    			  String name=rs.getString("afm");
		    			  if(name.equals(afm.getText()))
		    			  {
		    				  find=false;
		    			  }
		    		  }
		    		    rs.close();
			    		stm.close();
			    		conn.close();
		    		  if(!find)
		    		  {   i--;
		    			  afmValidate.setText("This afm already exists");
		    			  afm.clear();
		    		  }
		    		  else
		    		  {
		    			  afmValidate.setText("");
		    		  }
		    		  
		    	  }
		    	  catch(SQLException e)
		    	  {
		    		  e.printStackTrace();
		    	  }
		    }
		    if (loc.getText().isEmpty() || !loc.getText().matches("[a-zA-Z]+")) {
		        locValidate.setText("Fill location where you serve\n Only latin alphabet");
		        i = i - 1;
		        loc.clear();
		    }
		    else
		    {
		    	 locValidate.setText("");
		    }
		    if (email.getText().isEmpty() || !email.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
		        emailValidate.setText("Fill company email which is valid\n Only latin alphabet");
		        i = i - 1;
		        email.clear();
		    }
		    else
		    {
		    	 emailValidate.setText("");
		    }
		    if (phone.getText().isEmpty() || !phone.getText().matches("^69\\d{8}$")) {
		        phoneValidate.setText("Fill Greek mobile phone number");
		        i = i - 1;
		        phone.clear();
		    }
		    else
		    {
		    	phoneValidate.setText("");
		    }
		    
		    if (financial_Αgreement.getText().isEmpty() || 
		    	    financial_Αgreement.getText().length() < 20 ||
		    	    !financial_Αgreement.getText().matches("[a-zA-Z0-9€\\u0370-\\u03FF\\p{L}.\\s,;:?!-()]*"))
		    {
		    	   financial_ΑgreementValidate.setText("Fill the finacial agreement\n Both latin & greek alphabet ");
			        i = i - 1;
			        financial_Αgreement.clear();
		    }
		    else
		    {
		    	financial_ΑgreementValidate.setText("");
		    }
		    if (passwordSign.getText().isEmpty() || !passwordSign.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-_+=|{}[\\\\]:;\\\"'<>,.?/]).{8,}$")) 
		    {
		        passwordSignValidate.setText("Fill the password (at least one capital, one lower, one number, one symbol, not shorter than 8 digits)\n Only latin alphabet");
		        i = i - 1;
		        passwordSign.clear();
		    } 
		    else {
		        passwordSignValidate.setText("");
		    }


		    if (usernameSign.getText().isEmpty() || !usernameSign.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{10,}$")) {
		        usernameSignValidate.setText("Fill the username (at least one capital, one lower, one number)\nNot shorter than 10 digits\nOnly latin alphabet");
		        i = i - 1;
		        usernameSign.clear();
		    } else {
		    	
		    	try
		    	{
		    		boolean find=true;
		    		
		    		
		    	    Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		    	    String sql = "SELECT username FROM tourist_office.partners "; 
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
		    if(!Accom.isSelected() && !Ship.isSelected() && !Airplane.isSelected())
		    {
		    	i--;
		    	serviceValidate.setText("Choose something");
		    }
		    else
		    {
		    	serviceValidate.setText("");
		    }

		    if(i==9)
		    {
		    	  next=false;
		    	  companyName = cn.getText();
		  	      AFMP = afm.getText();
		  	      location = loc.getText();
		  	      phoneV=phone.getText();
		  	      emailV=email.getText();
		  	      financial_ΑgreementV=financial_Αgreement.getText();
		  	      usernameV=usernameSign.getText();
		  	      passwordV=passwordSign.getText();
		  	      if(Accom.isSelected())
		  	      {
		  	    	  typeServ=Accom.getText();
		  	      }
		  	      else if (Ship.isSelected())
		  	      {
		  	    	  typeServ=Ship.getText();
		  	      }
		  	      else
		  	      {
		  	    	 typeServ=Airplane.getText();
		  	      }
		  	      
		    	  Alert alert = new Alert(AlertType.INFORMATION);
		          alert.setTitle(null);
		          alert.setHeaderText("Validation succeed");
		          alert.setContentText("Press ok to close");

		          
		          alert.getButtonTypes().setAll(ButtonType.OK);

		         
		          alert.showAndWait().ifPresent(response -> {
		              if (response == ButtonType.OK) {
		                 
		                 alert.close();
		                 clean();
		                 
		               
		                 
		              }
		          });
		  	    
		    }
		   

	}
	public void clean()
	{
		 cn.clear();
	     afm.clear();
	     loc.clear();
	     email.clear();
	     phone.clear();
	     financial_Αgreement.clear();
	     usernameSign.clear();
	     passwordSign.clear();
	     ToggleGroup service=Accom.getToggleGroup();
	     service.selectToggle(null);
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
