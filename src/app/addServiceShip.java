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

public class addServiceShip {
private Ship ship;
private Partner partner;
private double cost;
private String portDep;
private boolean cabinExistance;
private boolean permitAnimal;
private boolean hasAuto;
private Stage stage;
private Scene scene;
private Parent root;
private FXMLLoader loader;
private String shipId;
private int passengerTotal;
private boolean next;
private Dotenv dotenv = Dotenv.load();
private String dbhost = dotenv.get("DB_HOST");
private String usernameDB = dotenv.get("DB_USER");
private String passDB = dotenv.get("DB_PASSWORD");
@FXML
private TextField costShip,portDeparture,shipID,passCapacity;
@FXML
private RadioButton yesExistCabin,notExistCabin,yesAuto,noAuto,yesAnimal,notAnimal;
@FXML
private Label shipIdValidate,passCapacityValidate,costValidate,portDepartureValidate,cabinChoiceValidate,hasAutoValidate,permAnimalValidate;

public void applyShipService(ActionEvent event)
{
	next=false;
	validate();
	if(next)
	{
		ship=new Ship(partner.getCompanyName(),partner.getAFM(),partner.getLocation(),partner.getTypeServ(),partner.getEmail(),partner.getPhone(),partner.getFinancial_agreement(),partner.getUsername(),partner.getPassword(),cabinExistance,hasAuto,portDep,shipId,cost,permitAnimal,passengerTotal);
		try
		{
			Connection conn=DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
			String sql="INSERT INTO tourist_office.ships(shipid,partner_id,existcabin,port,hasauto,permanimal,\"cost\",capacity) VALUES(?,?,?,?,?,?,?,?)";
			PreparedStatement stm=conn.prepareStatement(sql);
			stm.setString(1, ship.getShipId());
			stm.setString(2,ship.getAFM());
			stm.setBoolean(3,ship.isCabinExist());
			stm.setString(4,ship.getPortDep());
			stm.setBoolean(5,ship.isHasAuto());
			stm.setBoolean(6,ship.isPermitAnimal());
			stm.setDouble(7,ship.getCost());
			stm.setInt(8,ship.getCapacity());
			 int rowInserted=stm.executeUpdate();
			 if(rowInserted>0)
			 {
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
	            stm.close();
	            conn.close();
	            
			 
		}catch(SQLException e)
		{
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
		        	
		        	stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		        	loader=new FXMLLoader(getClass().getResource("Menu.fxml"));
		        	try {
						root=loader.load();
						scene=new Scene(root);
						stage.setScene(scene);
					} catch (IOException e) {
						
						e.printStackTrace();
					}        	
		        	
		        	
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
	if(shipID.getText().isEmpty()||!shipID.getText().matches("^[a-zA-Z0-9]{10,}$"))
	{
		i--;
		shipIdValidate.setText("Fill the shipid with lower or upper and digits(0-9)\nLatin alphabet");
		shipID.clear();
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
				if(shipID.getText().equals(name))
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
				shipIdValidate.setText("This shipId already exists");
				shipID.clear();
			}
			else
			{
				shipIdValidate.setText("");
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	if(costShip.getText().isEmpty()||!costShip.getText().matches("^-?\\d+(\\.\\d+)?$"))
	{
		i--;
		costValidate.setText("Fill the cost with integers or decimals");
		costShip.clear();
	}
	else
	{
		costValidate.setText("");
	}
	if(passCapacity.getText().isEmpty()||!passCapacity.getText().matches("^\\d+$"))
	{
		i--;
		passCapacityValidate.setText("Fill ship's passenger capacity");
		passCapacity.clear();
	}
	else
	{
		passCapacityValidate.setText("");
	}
	if(portDeparture.getText().isEmpty()||!portDeparture.getText().matches("^[a-zA-Z]{3,}$"))
	{
		i--;
		portDepartureValidate.setText("Fill departure port \n 3 chars at least & latin alphabet");
		portDeparture.clear();
	}
	else
	{
		portDepartureValidate.setText("");
	}
	if(!yesExistCabin.isSelected()&&!notExistCabin.isSelected())
	{
		i--;
		cabinChoiceValidate.setText("Choose something");
	}
	else
	{
		cabinChoiceValidate.setText("");
	}
	if(!yesAuto.isSelected()&&!noAuto.isSelected())
	{
		i--;
		hasAutoValidate.setText("Choose something");
	}
	else
	{
		hasAutoValidate.setText("");
	}
	if(!yesAnimal.isSelected()&&!notAnimal.isSelected())
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
		next=true;
		cost=Double.parseDouble(costShip.getText());
		portDep=portDeparture.getText();
		shipId=shipID.getText();
		passengerTotal=Integer.parseInt(passCapacity.getText());
		if(yesExistCabin.isSelected())
		{
			cabinExistance=true;
		}
		else
		{
			cabinExistance=false;
		}
		if(yesAnimal.isSelected())
		{
			permitAnimal=true;
		}
		else
		{
			permitAnimal=false;
		}
		if(yesAuto.isSelected())
		{
			hasAuto=true;
		}
		else
		{
			hasAuto=false;
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
	costShip.clear();
	portDeparture.clear();
	shipID.clear();
	passCapacity.clear();
	ToggleGroup cabin=yesExistCabin.getToggleGroup();
	cabin.selectToggle(null);
	ToggleGroup auto=yesAuto.getToggleGroup();
	auto.selectToggle(null);
	ToggleGroup animal=yesAnimal.getToggleGroup();
	animal.selectToggle(null);
}

}
