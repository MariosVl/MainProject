package app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ReadyPackageController implements Initializable {
    @FXML
    private ChoiceBox<String> dest;
    private ObservableList<String> destChoice = FXCollections.observableArrayList();
	@FXML 
	private ListView<Package> RdPack;
	private ObservableList<Package> ready = FXCollections.observableArrayList();
	@FXML
	private DatePicker startDate,endDate;
	private LocalDate startDateV,endDateV;
	private User user;
	private Package selectedPackage;
	private PaymentController control;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private FXMLLoader loader;
	private Reservation reserve;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
		   	 Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
		        Statement stmt = conn.createStatement();
		        String  query ="SELECT DISTINCT loc FROM tourist_office.partners" ;
		        ResultSet rs=stmt.executeQuery(query);
		       while (rs.next()) {
		           destChoice.add(rs.getString("loc"));
		       }
		       rs.close();
		       conn.close();
		   } catch (Exception e) {
		       e.printStackTrace();
		   }
		    dest.setItems(destChoice);
		    
		    dest.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	                
	                    getReadyPackages(newValue);
	                
	            }
	        });
		    
		
		RdPack.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		RdPack.setCellFactory(new Callback<ListView<Package>, ListCell<Package>>() {
		    @Override
		    public ListCell<Package> call(ListView<Package> listView) {
		        return new ListCell<Package>() {
		            @Override
		            protected void updateItem(Package packageR, boolean empty) {
		                super.updateItem(packageR, empty);
		                if (empty || packageR == null) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                	
		                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellReadyPack.fxml"));
		                    try {
		                        Parent root = loader.load();
		                        CellReadyPackController controller = loader.getController();
		                        controller.setData(packageR);
		                        setGraphic(root);
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		    }
		});
		
		RdPack.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Package>()
		
		{

			@Override
			public void changed(ObservableValue<? extends Package>  observable , Package oldValue , Package newValue) {
				  

					  selectedPackage=newValue;
				       
			}
	
		});
		
		
		
		
		
	}
	public void getReadyPackages(String loc)
	{
		Package packageLst;
		ready.clear();
		boolean find =true;
		try {
			Connection con = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
			     String query="  SELECT "
			     		+ "                p.packageid,"
			     		+ "                p.accomid,"
			     		+ "                a.totalbed AS accommodation_total_bed,"
			     		+ "                a.price AS accommodation_price,"
			     		+ "                a.launch AS accommodation_launch,"
			     		+ "                a.partner_id AS accommodation_partner_id,"
			     		+ "                a.permanimal AS accommodation_permanimal,"
			     		+ "                a.image AS accommodation_image,"
			     		+ "                a.disableentrance AS accommodation_disable_entrance,"
			     		+ "                a.hasac AS accommodation_has_ac,"
			     		+ "                a.haswifi AS accommodation_has_wifi,"
			     		+ "                a.haschildroom AS accommodation_has_child_room,"
			     		+ "                p.shipid,"
			     		+ "                s.existcabin AS ship_exist_cabin,"
			     		+ "                s.port AS ship_port,"
			     		+ "                s.hasauto AS ship_has_auto,"
			     		+ "                s.permanimal AS ship_permanimal,"
			     		+ "                s.cost AS ship_cost,"
			     		+ "                s.capacity AS ship_capacity,"
			     		+ "                s.partner_id AS ship_partner_id,"
			     		+ "                p.airplaneid,"
			     		+ "                ap.partner_id AS airplane_partner_id,"
			     		+ "                ap.hasbuis AS airplane_has_buis,"
			     		+ "                ap.airport AS airplane_airport,"
			     		+ "                ap.overweight AS airplane_overweight,"
			     		+ "                ap.cost AS airplane_cost,"
			     		+ "                ap.permanimal AS airplane_permanimal,"
			     		+ "                ap.capacity AS airplane_capacity,"
			     		+ "                part.company_name AS partner_company_name,"
			     		+ "                part.loc AS partner_location,"
			     		+ "                part.typeservice AS partner_service_type,"
			     		+ "                part.phone AS partner_phone,"
			     		+ "                part.email AS partner_email,"
			     		+ "                part.financial_agreement AS partner_financial_agreement,"
			     		+ "                part.username AS partner_username,"
			     		+ "                part.ispartner AS partner_is_partner,"
			     		+ "                part.password AS partner_password,  "
			     		+ "                p.cost,"
			     		+ "                p.fromadmin"
			     		+ "            FROM "
			     		+ "               tourist_office.package p "
			     		+ "            INNER JOIN "
			     		+ "                 tourist_office.accomondation a ON p.accomid = a.hotel_id"
			     		+ "            LEFT JOIN "
			     		+ "                 tourist_office.ships s ON p.shipid = s.shipid"
			     		+ "            LEFT JOIN "
			     		+ "                 tourist_office.airplanes ap ON p.airplaneid = ap.airid"
			     		+ "            LEFT JOIN "
			     		+ "                 tourist_office.partners part ON (s.partner_id = part.afm OR ap.partner_id = part.afm)"
			     		+ "            INNER JOIN"
			     		+ "                 tourist_office.partners part2 ON(a.partner_id = part2.afm)"
			     		+ "            WHERE "
			     		+ "               ( ((p.shipid IS NOT NULL AND p.airplaneid IS NULL) OR"
			     		+ "                (p.shipid IS NULL AND p.airplaneid IS NOT NULL) ) AND p.fromadmin = TRUE AND part.loc=?)";
	             PreparedStatement pst = con.prepareStatement(query);
	             pst.setString(1, loc);
	             ResultSet rs = pst.executeQuery() ;
	           
	            while (rs.next()) {
	            	find=false;
	                String packageId = rs.getString("packageid");
	                String accomId = rs.getString("accomid");
	                int totalBed = rs.getInt("accommodation_total_bed");
	                double accommodationPrice = rs.getDouble("accommodation_price");
	                String accommodationLaunch = rs.getString("accommodation_launch");
	                String accommodationPartnerId = rs.getString("accommodation_partner_id");
	                boolean accommodationPermanimal = rs.getBoolean("accommodation_permanimal");
	                byte[] accommodationImage = rs.getBytes("accommodation_image");
	                boolean accommodationDisableEntrance = rs.getBoolean("accommodation_disable_entrance");
	                boolean accommodationHasAc = rs.getBoolean("accommodation_has_ac");
	                boolean accommodationHasWifi = rs.getBoolean("accommodation_has_wifi");
	                boolean accommodationHasChildRoom = rs.getBoolean("accommodation_has_child_room");
	                String shipId = rs.getString("shipid");
	                if (rs.wasNull()) 
	                	{
	                	shipId = null;
	                	}
	                boolean shipExistCabin = rs.getBoolean("ship_exist_cabin");
	                String shipPort = rs.getString("ship_port");
	                boolean shipHasAuto = rs.getBoolean("ship_has_auto");
	                boolean shipPermanimal = rs.getBoolean("ship_permanimal");
	                double shipCost = rs.getDouble("ship_cost");
	                int shipCapacity = rs.getInt("ship_capacity");
	                String shipPartnerId = rs.getString("ship_partner_id");
	                String airplaneId = rs.getString("airplaneid");
	                if (rs.wasNull()) 
	                {
	                	airplaneId = null;
	                }
	                String airplanePartnerId = rs.getString("airplane_partner_id");
	                boolean airplaneHasBuis = rs.getBoolean("airplane_has_buis");
	                String airplaneAirport = rs.getString("airplane_airport");
	                boolean airplaneOverweight = rs.getBoolean("airplane_overweight");
	                double airplaneCost = rs.getDouble("airplane_cost");
	                boolean airplanePermanimal = rs.getBoolean("airplane_permanimal");
	                int airplaneCapacity = rs.getInt("airplane_capacity");
	                String partnerCompanyName = rs.getString("partner_company_name");
	                String partnerLocation = rs.getString("partner_location");
	                String partnerServiceType = rs.getString("partner_service_type");
	                String partnerPhone = rs.getString("partner_phone");
	                String partnerEmail = rs.getString("partner_email");
	                String partnerFinancialAgreement = rs.getString("partner_financial_agreement");
	                String partnerUsername = rs.getString("partner_username");
	                boolean partnerIsPartner = rs.getBoolean("partner_is_partner");
	                double cost = rs.getDouble("cost");
	                boolean fromAdmin = rs.getBoolean("fromadmin");
	                String partnerPassword=rs.getString("partner_password");
	                
	              
	                Accomondation accoLst=new Accomondation(partnerCompanyName,accommodationPartnerId,partnerLocation,partnerServiceType,partnerEmail,partnerPhone,partnerFinancialAgreement, partnerUsername,partnerPassword,totalBed, accommodationLaunch,accommodationPrice,accomId,accommodationPermanimal,accommodationImage,accommodationDisableEntrance,accommodationHasChildRoom,accommodationHasAc,accommodationHasWifi);
	                if(shipId!=null)
	                {
	                	 Ship shipLst=new Ship(partnerCompanyName,shipPartnerId ,partnerLocation,partnerServiceType,partnerEmail,partnerPhone,partnerFinancialAgreement,partnerUsername,partnerPassword, shipExistCabin,shipHasAuto,shipPort,shipId,shipCost,shipPermanimal,shipCapacity);
	                	  packageLst=new Package(packageId,accoLst,shipLst,cost,fromAdmin);
	                	 
	                }
	                else
	                {
	                	Airplane airplaneLst=new Airplane(partnerCompanyName,airplanePartnerId,partnerLocation,partnerServiceType,partnerEmail,partnerPhone,partnerFinancialAgreement,partnerUsername,partnerPassword,airplaneHasBuis,airplaneAirport,airplaneOverweight,airplaneId,airplaneCost,airplanePermanimal,airplaneCapacity);
	                	  packageLst=new Package(packageId,accoLst,airplaneLst,cost,fromAdmin);
	                }
	                ready.add(packageLst);    
	            }
	            if(!find)
	            {
	            RdPack.setItems(ready);
	            }
	            else
	            {
	            	Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Information");
	                alert.setHeaderText("Important Information");
	                alert.setContentText("Not choices for this destination \nPress OK to close.");

	                
	                alert.getButtonTypes().setAll(ButtonType.OK);

	               
	                alert.showAndWait().ifPresent(response -> {
	                    if (response == ButtonType.OK) {
	                       
	                        alert.close();
	                    }
	                });
	            }
	            rs.close();
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		
	}
	
	@FXML
	public void onReserveReadyPack(ActionEvent event)
	{
		
	    startDateV=startDate.getValue();
	    endDateV=endDate.getValue();
	    long daysBetween = ChronoUnit.DAYS.between(startDateV, endDateV);
	    Package shadowselectedPack;
	    double cost;
	    if((startDateV!=null&&endDateV!=null)&&(selectedPackage!=null)&&(endDateV.isAfter(startDateV))&&(startDateV.isAfter(LocalDate.now())&&endDateV.isAfter(LocalDate.now())))
	    {  
	    	 
	    	if(selectedPackage.getPartner() instanceof Ship)
	    	{
	    		 Ship ship=(Ship)selectedPackage.getPartner();
	    		 cost=(selectedPackage.getCost()-selectedPackage.getAccomondation().getCost())*2 +(selectedPackage.getAccomondation().getCost())*daysBetween;
	    		 shadowselectedPack=new Package(selectedPackage.getPackId(),selectedPackage.getAccomondation(),ship,cost,selectedPackage.isFromAdmin());
	    	}
	    	else
	    	{
	    		 Airplane airplane=(Airplane)selectedPackage.getPartner();
	    		 cost=(selectedPackage.getCost()-selectedPackage.getAccomondation().getCost())*2 +(selectedPackage.getAccomondation().getCost())*daysBetween;
	    		 shadowselectedPack=new Package(selectedPackage.getPackId(),selectedPackage.getAccomondation(),airplane,cost,selectedPackage.isFromAdmin());
	    	}
	         
	        		  
	        String reserveId=user.getUsername()+startDateV.toString()+endDateV.toString()+selectedPackage.getPackId();		  
	        Payment pay=new Payment("","","",0.0,false);
	    	reserve=new Reservation(reserveId,user,shadowselectedPack,startDateV,endDateV,pay);
	    	
	        
	    	try
	    	{
	    		
	    		Connection conn=DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	    		String sqlRes="select *from tourist_office.reservation";
	    		PreparedStatement stm=conn.prepareStatement(sqlRes,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	    		ResultSet rs=stm.executeQuery();
	    		if(!rs.next())
	    		{
	    			
	    			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    			 try {
	    				loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
	    				root=loader.load();
	    				control=loader.getController();
	    				control.setReserve(reserve,selectedPackage);
	    				
	    				
	    			} catch (IOException e) {
	    				
	    				e.printStackTrace();
	    			}
	    			    rs.close();
			    		stm.close();
			    		conn.close();
	    			 scene = new Scene(root); 
	    			 stage.setScene(scene);
	    		}
	    		else
	    		{
	    			if(!isValid(rs,startDateV,endDateV))
	    			{
	    				showAlert(AlertType.INFORMATION,"Warning","Not available dates \n Try again");
	    				clean();
	    				rs.close();
			    		stm.close();
			    		conn.close();
	    			}
	    			else
	    			{
	    				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		    			 try {
		    				loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
		    				root=loader.load();
		    				control=loader.getController();
		    				control.setReserve(reserve,selectedPackage);
		    				
		    				
		    				
		    			} catch (IOException e) {
		    				
		    				e.printStackTrace();
		    			}
		    			    rs.close();
				    		stm.close();
				    		conn.close();
		    			 scene = new Scene(root);
		    			 stage.setScene(scene);
	    			}
	    		}
	    		
	    	
	    		
	    	}catch(SQLException e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	    else
	    {
	    	clean();
	    }
	    
	}
	 
	public void setUser(User aUser)
	{
		this.user=aUser;
	}
	
	public boolean isValid(ResultSet rs ,LocalDate startDateV, LocalDate endDatev)
	{  boolean findValid = true;
    try {
        boolean a1, a2, a3, a4,  bool;
        rs.beforeFirst();
     
        while (rs.next() && findValid) {
        	
            Date startdate = rs.getDate("start_date");
            LocalDate rangstartDate = startdate.toLocalDate();
            Date enddate = rs.getDate("end_date");
            LocalDate rangeEndDate = enddate.toLocalDate();
            a1 = (startDateV.isEqual(rangstartDate) || endDatev.isEqual(rangeEndDate))||(startDateV.isEqual(rangeEndDate) || endDatev.isEqual(rangstartDate));
            a2=endDatev.isAfter(rangstartDate) && endDatev.isBefore(rangeEndDate);
            a3=startDateV.isAfter(rangstartDate) && startDateV.isBefore(rangeEndDate);
            a4 = rangstartDate.isAfter(startDateV) && rangeEndDate.isBefore(endDatev);
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
		startDate.setValue(null);
		endDate.setValue(null);
		RdPack.getSelectionModel().clearSelection();
	}
	private static void showAlert(AlertType type, String title, String message) {
	    Alert alert = new Alert(type);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
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
	@FXML
	public void onClearButt(ActionEvent event)
	{
		clean();
	
	}

}
