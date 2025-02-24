package app;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PaymentController {

    @FXML
    private TextField price, creditCard, name;
    @FXML
    private PasswordField code;
    @FXML
    private Label codeValidate, creditValidate, nameValidate, notFound;

    private boolean next;
    private Reservation reserve;
    private String cardnameV;
    private String cardV;
    private String cardcodeV;
    private double priceV;  
    private double amountV;
    private boolean isTherePack;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private FXMLLoader loader;
    private Package mainPackage;
	private Dotenv dotenv = Dotenv.load();
	private String dbhost = dotenv.get("DB_HOST");
	private String usernameDB = dotenv.get("DB_USER");
	private String passDB = dotenv.get("DB_PASSWORD");
    @FXML
    public void onPay(ActionEvent event) {
        next = false;
        
        validate();

        if (next) {
            try (Connection conn = getConnection()) {
                String sql = "SELECT * FROM tourist_office.payment WHERE cardnumber=? AND ownername=? AND cardcvc=?";
                try (PreparedStatement stm = conn.prepareStatement(sql)) {
                    stm.setString(1, cardV);
                    stm.setString(2, cardnameV);
                    stm.setString(3, cardcodeV);
                    try (ResultSet rs = stm.executeQuery()) {
                        if (!rs.next()) {
                            clean();
                            displayError("Payment failure by bank system");
                        } else {
                            setPayCredits(rs);
                            if (check()) {
                            	 reserve.getPay().setPaid(true);
                                if (!isDIYPackage()) {
                                	
                                	updateAmount(conn);
                            		insertinReserve(conn);
                                }
                                else
                                {
                                	if(!isTherePack)
                                	{
                                		
                                		updateAmount(conn);
                                		addPackage(conn);
                                		insertinReserve(conn);
                                	}
                                	else
                                	{
                                		updateAmount(conn);
                                		insertinReserve(conn);
                                	}
                                }
                               showAlert(AlertType.INFORMATION,"Information","Successfull payment , thank you!\nPress ok to close");
                               stage=(Stage)((Node)event.getSource()).getScene().getWindow();
           		        	loader=new FXMLLoader(getClass().getResource("Menu.fxml"));
           		        	try {
           						root=loader.load();
           						scene=new Scene(root);
           						stage.setScene(scene);
           					} catch (IOException e) {
           						
           						e.printStackTrace();
           					} 
                            } else {
                                clean();
                                displayError("Not enough amount for this reservation");
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                    		    alert.setTitle("Continue or Exit");
                    		    alert.setHeaderText(null);
                    		    alert.setContentText("Do you want use another card?");

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
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isDIYPackage() {
        return reserve.getReserveid().substring(reserve.getReserveid().length() - 3).equals("DIY");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://" + dbhost + "/postgres?sslmode=disable", usernameDB, passDB);
    }

    private void validate() {
        int validFields = 3;
        if (!creditCard.getText().matches("5351\\d{12}")) {
            validFields--;
            creditValidate.setText("Fill the 16-digit credit card number");
            creditCard.clear();
        } else {
            creditValidate.setText("");
        }
        if (name.getText().isEmpty() || !name.getText().matches("[A-Z ]+")) {
            validFields--;
            nameValidate.setText("Fill your whole name\nOnly Latin alphabet and spaces");
            name.clear();
        } else {
            nameValidate.setText("");
        }
        if (!code.getText().matches("\\d{3}")) {
            validFields--;
            codeValidate.setText("Fill card's 3-digit password");
            code.clear();
        } else {
            codeValidate.setText("");
        }
        if (validFields == 3) {
            next = true;
            cardV = creditCard.getText();
            cardnameV = name.getText();
            cardcodeV = code.getText();
        } 
    }

    public void setReserve(Reservation aReservation,Package aPackage) {
        this.reserve = aReservation;
        this.mainPackage=aPackage;
    }
    public void setPack(boolean aPack)
    {
    	this.isTherePack=aPack;
    }

    public void clean() {
        codeValidate.setText("");
        creditValidate.setText("");
        nameValidate.setText("");
        code.clear();
        creditCard.clear();
        name.clear();
        notFound.setOpacity(0.0);
    }

    public void setPayCredits(ResultSet rs) throws SQLException {
        amountV = rs.getDouble("amount");
        reserve.getPay().setCardcode(cardcodeV);
        reserve.getPay().setCardnumber(cardV);
        reserve.getPay().setOwner(cardnameV);
        reserve.getPay().setAmount(amountV);
    }

    public boolean check() {
        if (amountV < priceV) {
            return false;
        } else {
            amountV -= priceV;
            return true;
        }
    }

    public void updateAmount(Connection conn) throws SQLException {
        String sql = "UPDATE tourist_office.payment SET amount=? WHERE cardnumber=? AND ownername=? AND cardcvc=?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setBigDecimal(1, BigDecimal.valueOf(amountV));
            stm.setString(2, cardV);
            stm.setString(3, cardnameV);
            stm.setString(4, cardcodeV);
            stm.executeUpdate();
        }
    }

    public void insertinReserve(Connection conn) throws SQLException {
        String sql = "INSERT INTO tourist_office.reservation(reservation_id,user_id,package_id,start_date,end_date,ispaid) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, reserve.getReserveid());
            stm.setString(2, reserve.getUser().getUsername());
            stm.setString(3, reserve.getPackageR().getPackId());
            stm.setDate(4, Date.valueOf(reserve.getDateStart()));
            stm.setDate(5, Date.valueOf(reserve.getDateEnd()));
            stm.setBoolean(6, reserve.getPay().isPaid());
            int insertedRow = stm.executeUpdate();
            if (insertedRow > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Successful entrance into reservation base\nPress OK to close");
            }
        }
    }

    public void addPackage(Connection conn) throws SQLException {
    	 String sql = "INSERT INTO tourist_office.package (packageid,accomid, shipid, airplaneid,\"cost\",fromadmin) VALUES (?, ?, ?,?,?,?)";
        try {
        	PreparedStatement stm = conn.prepareStatement(sql);
        	stm.setString(1, reserve.getPackageR().getPackId());
		     stm.setString(2, reserve.getPackageR().getAccomondation().getIdHot());
		     stm.setBigDecimal(5, BigDecimal.valueOf(mainPackage.getCost()));
		     stm.setBoolean(6, false);
		     if(reserve.getPackageR().getPartner() instanceof Airplane)
		     {
			  Partner part=reserve.getPackageR().getPartner();
			  Airplane air=(Airplane)part;
			  stm.setString(4, air.getAirplaneId());
			  stm.setNull(3,Types.VARCHAR);
		     }
		     else if(reserve.getPackageR().getPartner() instanceof Ship)
		     {     
			  Partner part=reserve.getPackageR().getPartner();
			  Ship ship=(Ship)part;
			  stm.setString(3, ship.getShipId());
			  stm.setNull(4,Types.VARCHAR );
			  
		     }
		    stm.executeUpdate();
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
        }
    }

    public static void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void displayError(String message) {
        notFound.setText(message);
        notFound.setOpacity(1.0);
    }
    @FXML
    public void setPriceLabel(MouseEvent event)
    {
    	 priceV = reserve.getPackageR().getCost();
    	 price.setText(Double.toString(priceV));
    }
}
