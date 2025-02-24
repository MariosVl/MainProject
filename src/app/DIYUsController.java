package app;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DIYUsController implements Initializable  {
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
    private User user;
    @FXML
    private DatePicker startDate,endDate;
    private LocalDate startDateV,endDateV;
    private Reservation reserve;
    private PaymentController control;
    private boolean isThere;
    
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
	public void applyDIY(ActionEvent event) {
	    String packId, accomId, shipId, airplaneId;
	    Package packed;
	    Package shadowPack ;
	    boolean fromAdmin;
	    startDateV = startDate.getValue();
	    endDateV = endDate.getValue();
	    long daysBetween = ChronoUnit.DAYS.between(startDateV, endDateV);
	    double cost;
	    double costShadow;
	     
	    

	    if ((selectedAccomondation != null && (selectedShip != null || selectedAirplane != null)) &&
	    	    (startDateV != null && endDateV != null) && 
	    	    (endDateV.isAfter(startDateV)) && 
	    	    (startDateV.isAfter(LocalDate.now()) && endDateV.isAfter(LocalDate.now()))) {
	    	    
	    	    fromAdmin = false;

	    	    if (selectedShip != null) {
	    	        cost = selectedAccomondation.getCost() + selectedShip.getCost();
	    	        costShadow=(cost-selectedAccomondation.getCost())*2 +(selectedAccomondation.getCost())*daysBetween;
	    	        packed = new Package(selectedAccomondation.getIdHot() + selectedShip.getShipId(), selectedAccomondation, selectedShip, cost, fromAdmin);
	    	        shadowPack=new Package(packed.getPackId(),packed.getAccomondation(),selectedShip,costShadow,fromAdmin);
	    	        packId = packed.getPackId();
	    	        accomId = packed.getAccomondation().getIdHot();
	    	        airplaneId = null;
	    	        Ship ship = (Ship) packed.getPartner();
	    	        shipId = ship.getShipId();
	    	    } else {
	    	        cost = selectedAccomondation.getCost() + selectedAirplane.getCost();
	    	        costShadow=(cost-selectedAccomondation.getCost())*2 +(selectedAccomondation.getCost())*daysBetween;
	    	        packed = new Package(selectedAccomondation.getIdHot() + selectedAirplane.getAirplaneId(), selectedAccomondation, selectedAirplane, cost, fromAdmin);
	    	        shadowPack=new Package(packed.getPackId(),packed.getAccomondation(),selectedAirplane,costShadow,fromAdmin);
	    	        packId = packed.getPackId();
	    	        accomId = packed.getAccomondation().getIdHot();
	    	        shipId = null;
	    	        Airplane airplane = (Airplane) packed.getPartner();
	    	        airplaneId = airplane.getAirplaneId();
	    	    }

	    	  
	    	    Payment pay = new Payment("", "", "", 0.0, false);
	    	    reserve = new Reservation(packId + user.getUsername() + startDateV.toString() + endDateV.toString() + "DIY", user, shadowPack, startDateV, endDateV, pay);
	    	  
           
	        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB)) {
	        	
	            String sqlRes = "select * from tourist_office.reservation";
	            try (PreparedStatement stm = conn.prepareStatement(sqlRes, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
	                ResultSet rs = stm.executeQuery();

	                if (!rs.next()) {
	                    rs.close();
	                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                    try {
	                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
	                        root = loader.load();
	                         control = loader.getController();
	                        control.setReserve(reserve,packed);
	                        boolean isThere = isPackageExists(conn, packed.getPackId());
	                       control.setPack(isThere);
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    Scene scene = new Scene(root);
	                    stage.setScene(scene);
	                } else {
	                    if (!isValid(rs, startDateV, endDateV)) {
	                        showAlert(AlertType.INFORMATION, "Warning", "Not available dates for this reservation");
	                        clean();
	                    } else {
	                        boolean isThere = isPackageExists(conn, packed.getPackId());
	                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                        try {
	                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
	                            root = loader.load();
	                            control = loader.getController();
	                            control.setReserve(reserve,packed);
	                            control.setPack(isThere);
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                      
	                        Scene scene = new Scene(root);
	                        stage.setScene(scene);
	                    }
	                }
	                rs.close();
	            }catch (SQLException e) {
		            e.printStackTrace();
	            }
	        }catch (SQLException e) {
	            e.printStackTrace();
            }
	                
	    }
	    else {
	        clean();
	    }
	}

	
	public boolean isPackageExists(Connection conn, String packageId) throws SQLException {
	    String sql = "SELECT packageid FROM tourist_office.package WHERE packageid=?";
	    try (PreparedStatement stm = conn.prepareStatement(sql)) {
	        stm.setString(1, packageId); 
	        try (ResultSet rs = stm.executeQuery()) {
	            return rs.next();
	        }
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
		startDate.setValue(null);
		endDate.setValue(null);
	
	}

   
	public void setUser(User aUser)
	{
		this.user=aUser;
	}

	
	
	private static void showAlert(AlertType type, String title, String message) {
	    Alert alert = new Alert(type);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	public boolean isValid(ResultSet rs, LocalDate startDateV, LocalDate endDateV) {
		
	    boolean findValid = true;
	    try {
	        boolean a1, a2, a3, a4,  bool;
	        rs.beforeFirst();
         
	        while (rs.next() && findValid) {
	        	
	            Date startdate = rs.getDate("start_date");
	            LocalDate rangstartDate = startdate.toLocalDate();
	            Date enddate = rs.getDate("end_date");
	            LocalDate rangeEndDate = enddate.toLocalDate();
	            a1 = (startDateV.isEqual(rangstartDate) || endDateV.isEqual(rangeEndDate))||(startDateV.isEqual(rangeEndDate) || endDateV.isEqual(rangstartDate));
	            a2=endDateV.isAfter(rangstartDate) && endDateV.isBefore(rangeEndDate);
	            a3=startDateV.isAfter(rangstartDate) && startDateV.isBefore(rangeEndDate);
	            a4 = rangstartDate.isAfter(startDateV) && rangeEndDate.isBefore(endDateV);
	            bool = a1 || a2 ||a3|| a4;
	          
	       
	            if (bool) {
	                findValid = false;
	            }
	        }
	        rs.close();
	       
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return findValid;
	    
	}

	public void clean()
	{
		
		   
        hotLV.getSelectionModel().clearSelection();
		airplaneLV.getSelectionModel().clearSelection();
		shipLV.getSelectionModel().clearSelection();
		shipLV.setDisable(false);
		airplaneLV.setDisable(false);
		price.setText("");
		startDate.setValue(null);
		endDate.setValue(null);
	}
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



	
	
