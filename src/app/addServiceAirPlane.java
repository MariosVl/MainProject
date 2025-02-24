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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class addServiceAirPlane {

	private Partner partner;
	private Airplane airplane;
	private double cost;
	private boolean permitAnimal;
	private boolean hasBuis;
	private String airport;
	private boolean overweight;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private FXMLLoader loader;
	private String airPlaneId;
	private int capacity;
    private boolean next;
    private Dotenv dotenv = Dotenv.load();
    private String dbhost = dotenv.get("DB_HOST");
    private String usernameDB = dotenv.get("DB_USER");
    private String passDB = dotenv.get("DB_PASSWORD");
	    
	   

	    @FXML
	    private Label CostValidate,DestAirportValidate,OverWPackValidate,airplaneCapacityValidate,permAnimalValidate,airplaneIdValidate,hasBuisValidate;
	    @FXML
	    private TextField DestAirPort,Cost,AirplaneCapacity,AirPlaneId;
	    @FXML
	    private RadioButton yesPermAnimal,noPermAnim,yesBuis,noBuis,noOver,yesOver;
	    @FXML
	    private ToggleGroup hasBuisToggle,overweightToggle,permitAnimalToggle;
	
	public void applyairplaneservice(ActionEvent Event)
	{
		next=true;
		validate();
		if(!next)
		{
			airplane=new Airplane(partner.getCompanyName(),partner.getAFM(),partner.getLocation(),partner.getTypeServ(),partner.getEmail(),partner.getPhone(),partner.getFinancial_agreement(),partner.getUsername(),partner.getPassword(),hasBuis,airport,overweight,airPlaneId,cost,permitAnimal,capacity);
            
			try {
	            Connection connection = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	            String sql = "INSERT INTO tourist_office.airplanes (airid,partner_id ,hasbuis ,airport,overweight,\"cost\",permanimal,capacity) VALUES (?, ?, ?, ?, ?,?,?,?)";

	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, airplane.getAirplaneId()); 
	            preparedStatement.setString(2, airplane.getAFM()); 
	            preparedStatement.setBoolean(3,airplane.isHasBuis() );
	            preparedStatement.setString(4, airplane.getDestairport()); 
	            preparedStatement.setBoolean(5,airplane.isOverWeight()); 
	            preparedStatement.setBigDecimal(6, BigDecimal.valueOf(airplane.getCost()));
	            preparedStatement.setBoolean(7,airplane.isPermitAnimal());
	            preparedStatement.setInt(8, airplane.getAirplanecapacity());



	            int rowsInserted = preparedStatement.executeUpdate();
	            if (rowsInserted > 0) {
	            	Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Information");
	                alert.setHeaderText("Important Information");
	                alert.setContentText("Successfully entered into base \nPress OK to close.");

	                
	                alert.getButtonTypes().setAll(ButtonType.OK);

	               
	                alert.showAndWait().ifPresent(response -> {
	                    if (response == ButtonType.OK) {
	                       
	                        alert.close();
	                    }
	                });
	            }
	            

	            preparedStatement.close();
	            connection.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        	
	        }
		    Alert alert = new Alert(AlertType.CONFIRMATION);
		    alert.setTitle("Continue or Exit");
		    alert.setHeaderText(null);
		    alert.setContentText("Do you want to continue adding data or exit?");

		    ButtonType buttonContinue = new ButtonType("Continue");
		    ButtonType buttonExit = new ButtonType("Exit");

		    alert.getButtonTypes().setAll(buttonContinue, buttonExit);

		    alert.showAndWait().ifPresent(type -> {
		        if (type == buttonExit) {
		        	
		        	stage=(Stage)((Node)Event.getSource()).getScene().getWindow();
		        	loader=new FXMLLoader(getClass().getResource("Menu.fxml"));
		        	try {
						root=loader.load();
						scene=new Scene(root);
						stage.setScene(scene);
					} catch (IOException e) {
						
						e.printStackTrace();
					}        	
		        	
		        	System.out.println(partner.getCompanyName());
		        }
		        else if(type==buttonContinue)
		        {
		        	
		        	clean();
		        	
		        }
		    });	
		}
		
		
	}
	public void setPartner(Partner aPartner)
	{
		
	    this.partner=aPartner;
		
	}
	public void validate()
	{
		int i=7;
		boolean find=true;
		if(AirPlaneId.getText().isEmpty()||!AirPlaneId.getText().matches("^[a-zA-Z0-9]{10,}$"))
		{
			i--;
			airplaneIdValidate.setText("Fill the airid with lower or upper and digits(0-9)\nLatin alphabet");
			AirPlaneId.clear();
		}
		else
		{
			try {
				
				Connection conn=DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
				String sql = "SELECT shipid AS id FROM tourist_office.ships "
	    		           + "UNION "
	    		           + "SELECT airid AS id FROM tourist_office.airplanes "
	    		           + "UNION "
	    		           + "SELECT hotel_id AS id FROM tourist_office.accomondation";
				PreparedStatement stm=conn.prepareStatement(sql);
				ResultSet rs=stm.executeQuery();
				while(rs.next()&&find)
				{
					String name=rs.getString("id");
					if(AirPlaneId.getText().equals(name))
					{
						find=false;
					}
				}
				rs.close();
				stm.close();
				conn.close();
				if(!find)
				{
					i--;
					airplaneIdValidate.setText("This airId already exists");
					AirPlaneId.clear();
				}
				else
				{
					airplaneIdValidate.setText("");
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		if(Cost.getText().isEmpty()||!Cost.getText().matches("^-?\\d+(\\.\\d+)?$"))
		{
			i--;
			CostValidate.setText("Fill the cost with integers or decimals");
			Cost.clear();
		}
		else
		{
			CostValidate.setText("");
		}
		if(AirplaneCapacity.getText().isEmpty()||!AirplaneCapacity.getText().matches("^\\d+$"))
		{
			i--;
			airplaneCapacityValidate.setText("Fill airplanes's passenger capacity");
			AirplaneCapacity.clear();
		}
		else
		{
			airplaneCapacityValidate.setText("");
		}
		if(DestAirPort.getText().isEmpty()||!DestAirPort.getText().matches("^[a-zA-Z]{3,}$"))
		{
			i--;
			DestAirportValidate.setText("Fill destination airport \n 3 chars at least & latin alphabet");
			DestAirPort.clear();
		}
		else
		{
			DestAirportValidate.setText("");
		}
		if(!yesOver.isSelected()&&!noOver.isSelected())
		{
			i--;
			OverWPackValidate.setText("Choose something");
		}
		else
		{
			OverWPackValidate.setText("");
		}
		if(!yesBuis.isSelected()&&!noBuis.isSelected())
		{
			i--;
			hasBuisValidate.setText("Choose something");
		}
		else
		{
			hasBuisValidate.setText("");
		}
		if(!yesPermAnimal.isSelected()&&!noPermAnim.isSelected())
		{
			i--;
			permAnimalValidate.setText("Choose something");
		}
		else
		{
			permAnimalValidate.setText("");
		}
		
		if(i==7)
		{
			next=false;
			cost=Double.parseDouble(Cost.getText());
			airport=DestAirPort.getText();
			airPlaneId=AirPlaneId.getText();
			capacity=Integer.parseInt(AirplaneCapacity.getText());
			if(yesBuis.isSelected())
			{
				hasBuis=true;
			}
			else
			{
				hasBuis=false;
			}
			if(yesPermAnimal.isSelected())
			{
				permitAnimal=true;
			}
			else
			{
				permitAnimal=false;
			}
			if(yesOver.isSelected())
			{
				overweight=true;
			}
			else
			{
				overweight=false;
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
		DestAirPort.clear();
		Cost.clear();
		AirplaneCapacity.clear();
		AirPlaneId.clear();
		ToggleGroup hasBuisT=yesBuis.getToggleGroup();
		hasBuisT.selectToggle(null);
		ToggleGroup overweightT=yesOver.getToggleGroup();
		overweightT.selectToggle(null);
		ToggleGroup permitAnimalT=yesPermAnimal.getToggleGroup();
		permitAnimalT.selectToggle(null);
		
		
	}
}
