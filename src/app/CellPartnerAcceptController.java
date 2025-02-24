package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CellPartnerAcceptController {

	

    @FXML
    private Label AFM,Location,companyName,email,phone,typeService;
    @FXML
    private TextArea financial_agreement;
    
    public void setData(Partner partner)
    {
    	AFM.setText("AFM:"+partner.getAFM());
    	Location.setText("Location:"+partner.getLocation());
    	companyName.setText("Company:"+partner.getCompanyName());
    	email.setText("Email:"+partner.getEmail());
    	phone.setText("Phone:"+partner.getPhone());
    	typeService.setText("Service Type:"+partner.getTypeServ());
    	financial_agreement.setText("Financial Agreement:"+partner.getFinancial_agreement());
    	
    }

   
	
	
}
