package app;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DIYController implements Initializable {
	private Scene scene ;
	private Parent root;
	private Stage stage;
	private FXMLLoader loader;
	private Accomondation selectedAccomondation;
	private Ship selectedShip;
	private Airplane selectedAirplane;
	 private ObservableList<String> dest = FXCollections.observableArrayList();
	 private Dotenv dotenv = Dotenv.load();
	 private String dbhost = dotenv.get("DB_HOST");
	 private String usernameDB = dotenv.get("DB_USER");
	 private String passDB = dotenv.get("DB_PASSWORD");
    @FXML
	private ChoiceBox<String> destChoice;
    @FXML
    private ListView<Accomondation> hotLV;
    private ObservableList<Accomondation> dataHot = FXCollections.observableArrayList();
    @FXML
    private ListView<Ship> shipLV;
    private ObservableList<Ship> dataShip = FXCollections.observableArrayList();
    @FXML
    private ListView<Airplane> airplaneLV;
    private ObservableList<Airplane> dataAir = FXCollections.observableArrayList();
    @FXML 
    private Label price;
    private double cost;
    
	@Override  
	public void initialize(URL arg0, ResourceBundle arg1) {
		
			try {
	   	 Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	        Statement stmt = conn.createStatement();
	        String  query ="SELECT DISTINCT loc FROM tourist_office.partners" ;
	        ResultSet rs=stmt.executeQuery(query);
	       while (rs.next()) {
	           dest.add(rs.getString("loc"));
	       }
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	    
	    destChoice.setItems(dest);
	    
	    destChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    getLoc(newValue);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
		hotLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		hotLV.setCellFactory(new Callback<ListView<Accomondation>, ListCell<Accomondation>>() {
		    @Override
		    public ListCell<Accomondation> call(ListView<Accomondation> listView) {
		        return new ListCell<Accomondation>() {
		            @Override
		            protected void updateItem(Accomondation accomondation, boolean empty) {
		                super.updateItem(accomondation, empty);
		                if (empty || accomondation == null) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellAccom.fxml"));
		                    try {
		                        Parent root = loader.load();
		                        CellAccomController controller = loader.getController();
		                        controller.setData(accomondation);
		                        setGraphic(root);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		    }
		});
		shipLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		shipLV.setCellFactory(new Callback<ListView<Ship>, ListCell<Ship>>() {
		    @Override
		    public ListCell<Ship> call(ListView<Ship> listView) {
		        return new ListCell<Ship>() {
		            @Override
		            protected void updateItem(Ship ship, boolean empty) {
		                super.updateItem(ship, empty);
		                if (empty || ship == null) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellShip.fxml"));
		                    try {
		                        Parent root = loader.load();
		                        CellShipController controller = loader.getController();
		                        controller.setData(ship);
		                        setGraphic(root);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		    }
		});
		airplaneLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		airplaneLV.setCellFactory(new Callback<ListView<Airplane>, ListCell<Airplane>>() {
		    @Override
		    public ListCell<Airplane> call(ListView<Airplane> listView) {
		        return new ListCell<Airplane>() {
		            @Override
		            protected void updateItem(Airplane airplane, boolean empty) {
		                super.updateItem(airplane, empty);
		                if (empty || airplane == null) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellAir.fxml"));
		                    try {
		                        Parent root = loader.load();
		                        CellAirController controller = loader.getController();
		                        controller.setData(airplane);
		                        setGraphic(root);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		    }
		});
        
		
	hotLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Accomondation>()
		
		{

			@Override
			public void changed(ObservableValue<? extends Accomondation>  observable , Accomondation oldValue , Accomondation newValue) {
				  
				  selectedAccomondation=newValue;
				 
			     
			        
			}
	
		}
		
		
		
		);

		
		
		shipLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>()
		
		{

			@Override
			public void changed(ObservableValue<? extends Ship>  observable , Ship oldValue , Ship newValue) {
				  if (newValue != null) {
			            airplaneLV.getSelectionModel().clearSelection();
			            airplaneLV.setDisable(true);
			            
			        } else {
			            airplaneLV.setDisable(false);
			           
			        }
				  selectedShip=newValue;
				
			}
	
		}
		
		
		
		);



      airplaneLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Airplane>()
		
		{

			@Override
			public void changed(ObservableValue<? extends Airplane> observable, Airplane oldValue, Airplane newValue) {
				  if (newValue != null) {
			            shipLV.getSelectionModel().clearSelection();
			            shipLV.setDisable(true);
			            
			        } else {
			            shipLV.setDisable(false);
			           
			        }
				  selectedAirplane=newValue;
				 
			}
	
		}
		
		
		
		);
		
		
		
		
		
	}
	@FXML
	public void applyDIY(ActionEvent event)
	
	{   
		String packId,accomId,shipId,airplaneId;
		Package packed;
		boolean fromAdmin;
		
	
		if(selectedAccomondation!=null &&(selectedShip!=null || selectedAirplane!=null))
		{    fromAdmin=true;
			if(selectedShip!=null)
			{      cost=selectedShip.getCost()+selectedAccomondation.getCost();
		           packed=new Package(selectedAccomondation.getIdHot()+selectedShip.getShipId(),selectedAccomondation,selectedShip,cost,fromAdmin);
		           packId=packed.getPackId();
		           accomId=packed.getAccomondation().getIdHot();
		           airplaneId=null;
		           Ship ship=(Ship)packed.getPartner();
		           shipId=ship.getShipId();
			}
			else
			{
				   cost=selectedAirplane.getCost()+selectedAccomondation.getCost();
				   packed=new Package(selectedAccomondation.getIdHot()+selectedAirplane.getAirplaneId(),selectedAccomondation,selectedAirplane,cost,fromAdmin);
				   packId=packed.getPackId();
		           accomId=packed.getAccomondation().getIdHot();
		           shipId=null;
		           Airplane airplane=(Airplane)packed.getPartner();
		           airplaneId=airplane.getAirplaneId();
			}
		
		
		     
			   
			try {
				
				 boolean find=true;
			   	 Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
			   	 
			   	 
			   	String sql = "SELECT packageid FROM tourist_office.package";
			   	PreparedStatement stm = conn.prepareStatement(sql);
			   	ResultSet rs=stm.executeQuery();
			   	while(rs.next()&&find)
			   	{
			   		String name=rs.getString("packageid");
			   		if(name.equals(packId))
			   		{
			   			find=false;
			   		}
			   	}
			   	rs.close();
			   	stm.close();
			   	if(!find)
			   	{
			   		conn.close();
			   		Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Information");
	                alert.setHeaderText("Important Information");
	                alert.setContentText("This package already exist \nInput again.");

	                
	                alert.getButtonTypes().setAll(ButtonType.OK);

	               
	                alert.showAndWait().ifPresent(response -> {
	                    if (response == ButtonType.OK) {
	                       
	                        alert.close();
	                    }
	                });
	                
	                hotLV.getSelectionModel().clearSelection();
	        		airplaneLV.getSelectionModel().clearSelection();
	        		shipLV.getSelectionModel().clearSelection();
	        		shipLV.setDisable(false);
	        		airplaneLV.setDisable(false);
	        		 price.setText("");
	                
			   	}
			   	else
			   	{
			   	     String query = "INSERT INTO tourist_office.package (packageid,accomid, shipid, airplaneid,\"cost\",fromadmin) VALUES (?, ?, ?,?,?,?)";
			   	     PreparedStatement statement=conn.prepareStatement(query);
			   	     if(shipId==null)
			   	     {
			   	    	 statement.setNull(3, Types.VARCHAR);
			   	    	statement.setString(4, airplaneId);
			   	     }
			   	     else
			   	     {
			   	    	statement.setNull(4, Types.VARCHAR);
			   	    	statement.setString(3, shipId);
			   	     }
			   	     statement.setString(1, packId);
			   	     statement.setString(2, accomId);
			   	     statement.setDouble(5, cost);
			   	     statement.setBoolean(6, fromAdmin);
			   	     int rowInserted=statement.executeUpdate();
			   	     if(rowInserted>0)
			   	     {
			   	    	 statement.close();
			   	    	 conn.close();
			   	    	Alert alert = new Alert(AlertType.INFORMATION);
		                alert.setTitle("Information");
		                alert.setHeaderText("Important Information");
		                alert.setContentText("Successfully entered into package base \nPress OK to close.");

		                
		                alert.getButtonTypes().setAll(ButtonType.OK);

		               
		                alert.showAndWait().ifPresent(response -> {
		                    if (response == ButtonType.OK) {
		                       
		                        alert.close();
		                    }
		                });
		                
		                
		                
			   	     }
			   	     
			   			
			   	     
			   	     
			   	     
			   	  Alert alert = new Alert(AlertType.CONFIRMATION);
		    	    alert.setTitle("Continue or Exit");
		    	    alert.setHeaderText(null);
		    	    alert.setContentText("Do you want to make a new package  or exit?");

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
		    	        	
		    	        	hotLV.getSelectionModel().clearSelection();
		    	    		airplaneLV.getSelectionModel().clearSelection();
		    	    		shipLV.getSelectionModel().clearSelection();
		    	    		shipLV.setDisable(false);
		    	    		airplaneLV.setDisable(false);
		    	    		 price.setText("");
		    	        	
		    	        }
		    	    });
			   	     
			   	     
			   	     
			   	
			   	}
			}catch(SQLException e)
			   	{
			   		e.printStackTrace();
			   	}
				
	}
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Missing data");
            alert.setContentText("You must fill one accomondation and one airplane OR one ship\n Press ok to input again");

            
            alert.getButtonTypes().setAll(ButtonType.OK);

           
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                   
                    alert.close();
                }
            });
            
            price.setText("");
		}	
			
		   
	} 
	
	@FXML
	public void backMenu(ActionEvent event) throws IOException
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		 scene = new Scene(root);
		stage.setScene(scene);
	}
	public void getLoc(String loc) throws SQLException {
		    dataHot.clear();
	        dataShip.clear();
	        dataAir.clear();
	        cost=0;
	        price.setText(""); 
	        
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Connection conn = null;

	    try {
	        conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 

	        String query = "SELECT * " +
	                       "FROM tourist_office.accomondation a " +
	                       "INNER JOIN tourist_office.partners p " +
	                       "ON p.afm = a.partner_id " +
	                       "WHERE p.loc=?";
	        preparedStatement = conn.prepareStatement(query);
	        preparedStatement.setString(1, loc);
	        resultSet = preparedStatement.executeQuery();

	       
	    

            while (resultSet.next()) {
                String companyNameP = resultSet.getString("company_name");
                String afmP = resultSet.getString("afm");
                String locationP = resultSet.getString("loc");
                String typeServP=resultSet.getString("typeservice");
                int totalBedA = resultSet.getInt("totalbed");
                double priceA = resultSet.getDouble("price");
                String LaunchA = resultSet.getString("launch");
                boolean permitAnimalA = resultSet.getBoolean("permanimal");
                byte[] imageA = resultSet.getBytes("image");
                String hotIDA=resultSet.getString("hotel_id");
                String phoneP=resultSet.getString("phone"); 
                String emailP=resultSet.getString("email"); 
                String financial_agreementP=resultSet.getString("financial_agreement");
                String usernameP=resultSet.getString("username");
                String passwordP=resultSet.getString("password");
                boolean disableEntranceA=resultSet.getBoolean("disableentrance");
                boolean acA=resultSet.getBoolean("hasac");
                boolean wifiA=resultSet.getBoolean("haswifi");
                boolean childRoomA=resultSet.getBoolean("haschildroom");
                Accomondation accomondation = new Accomondation(companyNameP,afmP ,locationP, typeServP,emailP,phoneP,financial_agreementP,usernameP,passwordP,totalBedA,LaunchA,priceA,hotIDA,permitAnimalA,imageA,disableEntranceA,childRoomA,acA,wifiA);
                dataHot.add(accomondation);
            }
           
           hotLV.setItems(dataHot);
          
           
           
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        if (resultSet != null) {
	            resultSet.close();
	        }
	        
	         query = "SELECT * " +
                    "FROM tourist_office.ships s " +
                    "INNER JOIN tourist_office.partners p " +
                    "ON p.afm = s.partner_id " +
                    "WHERE p.loc=?";
     preparedStatement = conn.prepareStatement(query);
     preparedStatement.setString(1, loc);
     resultSet = preparedStatement.executeQuery();
            
       
       
       
       while (resultSet.next()) {
           String companyName = resultSet.getString("company_name");
           String afm = resultSet.getString("afm");
           String location = resultSet.getString("loc");
           String typeServ=resultSet.getString("typeservice");
           boolean cabinExist = resultSet.getBoolean("existcabin");
           double price = resultSet.getDouble("cost");
           boolean hasauto = resultSet.getBoolean("hasauto");
           boolean permitAnimal = resultSet.getBoolean("permanimal");
           String port=resultSet.getString("port");
           String shipId=resultSet.getString("shipid");
           String phone=resultSet.getString("phone"); 
           String email=resultSet.getString("email"); 
           String financial_agreement=resultSet.getString("financial_agreement");
           String username=resultSet.getString("username");
           String password=resultSet.getString("password");
           int capacity=resultSet.getInt("capacity");
           Ship ship = new Ship(companyName, afm, location,typeServ,email,phone,financial_agreement,username,password, cabinExist, hasauto, port, shipId, price, permitAnimal,capacity);
           dataShip.add(ship);
       }
       shipLV.setItems(dataShip);
      
       if (preparedStatement != null) {
           preparedStatement.close();
       }
       if (resultSet != null) {
           resultSet.close();
       }
       
       query = "SELECT * " +
               "FROM tourist_office.airplanes ar " +
               "INNER JOIN tourist_office.partners p " +
               "ON p.afm = ar.partner_id " +
               "WHERE p.loc=?";
       
       preparedStatement=conn.prepareStatement(query);
       preparedStatement.setString(1, loc);
       resultSet=preparedStatement.executeQuery();
       
       
       while (resultSet.next()) {
           String companyName = resultSet.getString("company_name");
           String afm = resultSet.getString("afm");
           String location = resultSet.getString("loc");
           String typeServ=resultSet.getString("typeservice");
           boolean hasBuis = resultSet.getBoolean("hasbuis");
           double price = resultSet.getDouble("cost");
           boolean overweight = resultSet.getBoolean("overweight");
           boolean permitAnimal = resultSet.getBoolean("permanimal");
           String airport=resultSet.getString("airport");
           String airId=resultSet.getString("airid");
           String phone=resultSet.getString("phone"); 
           String email=resultSet.getString("email"); 
           String financial_agreement=resultSet.getString("financial_agreement");
           String username=resultSet.getString("username");
           String password=resultSet.getString("password");
           int capacity=resultSet.getInt("capacity");
           Airplane airplane = new Airplane(companyName ,afm,location,typeServ,email, phone,financial_agreement,username,password, hasBuis,airport,overweight,airId,price,permitAnimal,capacity);
           dataAir.add(airplane);
       }
       airplaneLV.setItems(dataAir);
      
       if (preparedStatement != null) {
           preparedStatement.close();
       }
       if (resultSet != null) {
           resultSet.close();
       }
       if(conn!=null)
       {
    	   conn.close();
       }
            
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	   
	    if (!(hotLV.getItems().size()!=0 && (shipLV.getItems().size()!=0 ||airplaneLV.getItems().size()!=0)))
	    {
			
	    	Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Unready Services for this location");
            alert.setContentText("This destination has not choices of a full holiday exprerience.\n Please choose an other destination \nPress OK to close.");

            
            alert.getButtonTypes().setAll(ButtonType.OK);

           
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                	
                    alert.close();
                   
                }
            });
	    }
	    
	    
	    
	    
	    
	}
	@FXML
	public void onClearButt(ActionEvent event)
	{
		hotLV.getSelectionModel().clearSelection();
		airplaneLV.getSelectionModel().clearSelection();
		shipLV.getSelectionModel().clearSelection();
		shipLV.setDisable(false);
		airplaneLV.setDisable(false);
		price.setText("");
	}
    
	@FXML
	public void setLabel(MouseEvent event)
	{
		if(selectedShip!=null&&selectedAccomondation!=null)
		{
			price.setText(Double.toString(selectedAccomondation.getCost()+selectedShip.getCost()));
		}
		else
		{
			price.setText(Double.toString(selectedAccomondation.getCost()+selectedAirplane.getCost()));
		}
	}


}
