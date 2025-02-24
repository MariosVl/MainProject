package app; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class addServiceAccom {
	private Accomondation accomondation;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Partner partner;
	private FXMLLoader loader;
    private File selectedImageFile;
    private boolean disableAccess;
    private boolean permAnimal;
    private boolean childRoom;
    private boolean AC;
    private boolean wifi;
    private String food;
    private String hotId;
    private int beds;
    private double cost;
    private  byte[] imageBytes;
    private boolean next;
    private Dotenv dotenv = Dotenv.load();
    private String dbhost = dotenv.get("DB_HOST");
    private String usernameDB = dotenv.get("DB_USER");
    private String passDB = dotenv.get("DB_PASSWORD");
    
	@FXML
	private RadioButton yesDisableAccess,noDisableAccess,yesPermAnAccom,noPermAnAccom,morningFood,nightFood,fullFood,noFood,yesChildRoom,noChildRoom,yesAC,noAC,yesWIFI,noWifi ;
	@FXML
	private TextField hotelIDname,bedPerRoom,costAccom;
	@FXML
	private Label hotelidValidate,bedPerRoomValidate,priceValidate,photoValidate,disableAccessValidate,permAnimalValidate,foodValidate,childRoomAccomValidate,ACvalidate,wifiValidate;
	

	
	@FXML
	public void applyServiceAccom(ActionEvent event)
	{
		next=false;
		validate();
		if(next)
		{
			accomondation=new Accomondation(partner.getCompanyName(),partner.getAFM(),partner.getLocation(),partner.getTypeServ(),partner.getEmail(),partner.getPhone(),partner.getFinancial_agreement(),partner.getUsername(),partner.getPassword(),beds,food,cost,hotId,permAnimal,imageBytes,disableAccess,childRoom,AC,wifi);
			try {
	            Connection connection = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	            
	            String sql = "INSERT INTO tourist_office.accomondation (hotel_id,totalbed,price,launch,partner_id,permanimal,image,disableentrance,hasac,haswifi,haschildroom) VALUES (?, ?, ?, ?, ?,?,?,?,?,?,?)";

	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1,accomondation.getIdHot() ); 
	            preparedStatement.setInt(2, accomondation.getCountRoom()); 
	            preparedStatement.setBigDecimal(3,BigDecimal.valueOf(accomondation.getCost()) );
	            preparedStatement.setString(4, accomondation.getLaunch()); 
	            preparedStatement.setString(5, accomondation.getAFM()); 
	            preparedStatement.setBoolean(6, accomondation.isPermitAnimal());
	            preparedStatement.setBytes(7,accomondation.getImage() );
	            preparedStatement.setBoolean(8,accomondation.isDisableEntranceV() );
	            preparedStatement.setBoolean(9,accomondation.isACv() );
	            preparedStatement.setBoolean(10,accomondation.isWifiV() );
	            preparedStatement.setBoolean(11,accomondation.isChildRoomV() );


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
	@FXML
	public void onUPLOADPHOTO(ActionEvent event) {
		
	    FileChooser fileChooser = new FileChooser();
	    FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
	    FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
	    fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

	    File file = fileChooser.showOpenDialog(null);
	    if (file != null) {
	    	
	        if (isValidImageFile(file)) {
	            selectedImageFile = file;
	        } else {
	            showAlert(AlertType.ERROR, "Invalid File", "Please select a valid image file (JPG or PNG).");
	           
	        }
	    } else {
	        showAlert(AlertType.ERROR, "No File Selected", "Please select an image file.");
	        
	    }
	}

	public boolean isValidImageFile(File file) {
	    String fileName = file.getName();
	    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	    return extension.equals("jpg") || extension.equals("png");
	}

	public static void showAlert(AlertType type, String title, String message) {
	    Alert alert = new Alert(type);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	public void setPartner(Partner aPartner)
	{
		
	    this.partner=aPartner;
	    
		
	}
	public void validate()
	{   imageBytes=null;
		int i=10;
	  
	    		
		if(!yesDisableAccess.isSelected() && !noDisableAccess.isSelected())
		{
			i--;
			disableAccessValidate.setText("Choose something");
				
		}
		else
		{
			disableAccessValidate.setText("");
		}
		if(!yesPermAnAccom.isSelected() && !noPermAnAccom.isSelected())
		{
			i--;
			permAnimalValidate.setText("Choose something");
		}
		else
		{
			permAnimalValidate.setText("");
		}
		if(!yesChildRoom.isSelected() && !noChildRoom.isSelected())
		{
			i--;
			childRoomAccomValidate.setText("Choose something");
		}
		else
		{
			childRoomAccomValidate.setText("");
		}
		if(!morningFood.isSelected() && !nightFood.isSelected() && !fullFood.isSelected() && !noFood.isSelected())
		{
			i--;
			foodValidate.setText("Choose something");
		}
		else
		{
			foodValidate.setText("");
		}
		if(!yesAC.isSelected() && !noAC.isSelected())
		{
			i--;
			ACvalidate.setText("Choose something");
		}
		else
		{
			ACvalidate.setText("");
		}
		if(!yesWIFI.isSelected() && !noWifi.isSelected())
		{
			i--;
			wifiValidate.setText("Choose something");
		}
		else
		{
			wifiValidate.setText("");
		}
		if(hotelIDname.getText().isEmpty() || !hotelIDname.getText().matches("^[a-zA-Z0-9]{10,}$"))
		{
			i--;
			hotelidValidate.setText("Fill the accomondation service id\n Only latin alphabet & digits(0-9)\n Minimum length 10 characters");
			hotelIDname.clear();
		}
		else
		{
			try
	    	{
				boolean find=true;
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
	    			if(name.equals(hotelIDname.getText()))
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
	    			hotelidValidate.setText("This hotel service is already exist");
	    			hotelIDname.clear();
	    			
	    		}
	    		else
	    		{
	    			hotelidValidate.setText("");
	    		}
	    	}
	    	catch (SQLException e) {
               
	    		
	    		e.printStackTrace();
            }
			
		}
		if(bedPerRoom.getText().isEmpty()||!bedPerRoom.getText().matches("^[123]$"))
		{
			i--;
			bedPerRoomValidate.setText("Fill the room's total bed\n Only numbers('1' or '2' or '3') ");
			bedPerRoom.clear();
			
		}
		else
		{
			bedPerRoomValidate.setText("");
		}
		if(costAccom.getText().isEmpty()||!costAccom.getText().matches("^-?\\d+(\\.\\d+)?$"))
		{
			i--;
			priceValidate.setText("Fill the cost with integers or decimals");
			costAccom.clear();
		}
		else
		{
			priceValidate.setText("");
		}
		if(selectedImageFile == null)
		{
			i--;
			photoValidate.setText("Upload a valid phot please \n Type png or jpg only");
			
		}
		else
		{
			photoValidate.setText("");
		}
		if(i==10)
		{
			next=true;
			cost=Double.parseDouble(costAccom.getText());
			hotId=hotelIDname.getText();
			beds=Integer.parseInt(bedPerRoom.getText());
			if(yesDisableAccess.isSelected())
			{
				disableAccess=true;
			}
			else
			{
				disableAccess=false;
			}
			if(yesPermAnAccom.isSelected())
			{
				permAnimal=true;
			}
			else
			{
				permAnimal=false;
			}
			if(morningFood.isSelected())
			{
				food=morningFood.getText();
			}
			else if(nightFood.isSelected())
			{
				food=nightFood.getText();
			}
			else if(fullFood.isSelected())
			{
				food=fullFood.getText();
			}
			else if(noFood.isSelected())
			{
				food=noFood.getText();
			}
			if(yesAC.isSelected())
			{
				AC=true;
			}
			else
			{
				AC=false;
			}
			if(yesWIFI.isSelected())
			{
				wifi=true;
			}
			else
			{
				wifi=false;
			}
			if(yesChildRoom.isSelected())
			{
				childRoom=true;
			}
			else
			{
				childRoom=false;
			}
			 
		            try {
		                imageBytes = Files.readAllBytes(selectedImageFile.toPath());
		            } catch (IOException e) {
		                e.printStackTrace();
		                showAlert(AlertType.ERROR, "File Error", "Failed to read the selected image file.");
		                
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
		 hotelIDname.clear();
     	bedPerRoom.clear();
     	costAccom.clear();
     	
     	ToggleGroup food=morningFood.getToggleGroup();
     	food.selectToggle(null);
     	
     	ToggleGroup permAnimal=yesPermAnAccom.getToggleGroup();
     	permAnimal.selectToggle(null);
     	
     	ToggleGroup disableAccess=yesDisableAccess.getToggleGroup();
     	disableAccess.selectToggle(null);
     	
     	ToggleGroup AC=yesAC.getToggleGroup();
     	AC.selectToggle(null);
     	
     	ToggleGroup WIFI=yesWIFI.getToggleGroup();
     	WIFI.selectToggle(null);
     	
     	ToggleGroup childRoom=yesChildRoom.getToggleGroup();
     	childRoom.selectToggle(null);
     	
     	selectedImageFile=null;
	 }
	
}
