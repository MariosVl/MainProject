package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class PrintReservationController {
    private Stage stage;
    private Parent root;
    private Scene scene;
   
	private User user;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
	@FXML
	private TextArea print;
	public void onPrint(ActionEvent event) {
	    
	    try {
	        print.setText(""); 
	        
	        Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB); 
	        String sql ="SELECT p.\"cost\" AS total_cost, " +
	                "r.start_date AS start_date, " +
	                "r.end_date AS end_date, " +
	                "ar.airid AS airplane_id, " +
	                "ar.airport AS airport, " +
	                "ar.overweight AS overweight, " +
	                "part.company_name AS company, " +
	                "part.email AS company_email, " +
	                "s.shipid AS ship_id, " +
	                "s.port AS port, " +
	                "a.totalbed AS beds, " +
	                "a.launch AS launch, " +
	                "part1.company_name AS hotelname, " +
	                "part.loc AS location_start, " +
	                "part.phone AS company_phone, " +
	                "ar.\"cost\" AS airplane_cost, " +
	                "s.\"cost\" AS ship_cost, " +
	                "a.price AS accom_cost " +  
	                "FROM tourist_office.reservation r " +
	                "INNER JOIN tourist_office.package p ON p.packageid = r.package_id " +
	                "INNER JOIN tourist_office.accomondation a ON p.accomid = a.hotel_id " +
	                "INNER JOIN tourist_office.partners part1 ON a.partner_id = part1.afm " +
	                "LEFT JOIN tourist_office.ships s ON p.shipid = s.shipid " +
	                "LEFT JOIN tourist_office.airplanes ar ON p.airplaneid = ar.airid " +
	                "LEFT JOIN tourist_office.partners part ON (part.afm = s.partner_id OR part.afm = ar.partner_id) " +
	                "WHERE r.user_id = ?";
	                     
	        PreparedStatement stm = conn.prepareStatement(sql);
	        stm.setString(1, user.getUsername());
	        ResultSet rs = stm.executeQuery();
	        
	        StringBuilder finalText=new StringBuilder();;
	        
	        while (rs.next()) {
	            print.setText("");
	            double cost=rs.getDouble("total_cost");
	            double accomcost=rs.getDouble("accom_cost");
	            LocalDate startdate=rs.getDate("start_date").toLocalDate();
	            LocalDate endDate=rs.getDate("end_date").toLocalDate();
	            long days= ChronoUnit.DAYS.between(startdate, endDate); 
	            String airport=rs.getString("airport");
	            String companytrans=rs.getString("company");
	            String companyemailtrans=rs.getString("company_email");
	            String companyphone=rs.getString("company_phone");
	            String port=rs.getString("port");
	            String beds=Integer.toString(rs.getInt("beds"));
	            String launch=rs.getString("launch");
	            String hotel=rs.getString("hotelname");
	            String locationStart=rs.getString("location_start");
	            String overweight;
	            if(rs.getBoolean("overweight"))
	            {
	            	overweight="χρεώνει υπέρβαρο";
	            }
	            else
	            {
	            	overweight=" δεν χρεώνει υπέρβαρο";
	            }
	           
	            String airplaneid=rs.getString("airplane_id");
	            String shipid=rs.getString("ship_id");
	            String txt1,txt2,txt3,txt4;
	            txt1="Ο πελάτης μας"+" "+user.getName()+" "+user.getSurname()+" "+" θα ταξιδέψει από " + " "+startdate+" "+"έως"+" "+endDate +"από"+" "+locationStart+" "+"προς"+" ";
	            if(rs.wasNull())
	            {
	            	
	            	txt2="το αεροδρόμιο"+" "+airport+"."+"H αεροπορική εταιρεία είναι η "+" "+companytrans+" "+" "+"η οποία"+" "
	                +overweight+"."+"Τα στοιχεία επικοινωνίας της εταιρείας είναι email: "+" "+"και τηλέφωνο:"+" "+companyphone+".";
	            }
	            else
	            {
	            	
	            	txt2="το λιμάνι της"+" "+port+"."+"H ακτοπλοικη εταιρεία είναι η "+" "+companytrans+"."+"Τα στοιχεία επικοινωνίας της εταιρείας είναι email:"+companyemailtrans+" "+"και τηλέφωνο:"+" "+companyphone+"."+" ";
	            			
	            }
	            txt3="Ο πελάτης θα μείνει στο ξενοδοχείο "+" "+ hotel+" "+"σε"+" "+beds+" "+"κλινο"+" "+"και γευμα:"+" "+launch+".";
	            cost=(cost-accomcost)*2 + (accomcost*days);
	            txt4="To συνολικό κόστος του πακέτου ανέρχεται στα "+ " "+Double.toString(cost) +""+"ευρώ."+"\n\n\n";
	             
	            String fullText=txt1+txt2+txt3+txt4;
	            finalText.append(fullText);
	            print.setText(finalText.toString()); 
	            
	        }
	        
	       
	        
	        rs.close();
	        stm.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
    public void onBackMenu(ActionEvent event)
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

	
	 public void setUser(User aUser)
	 {
		 this.user=aUser;
	 }
	
}
