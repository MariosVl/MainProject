package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CellAirController {

    @FXML
    private Label Location,airId,companyName,cost,overweight,permitAnimal,hasBuis,airport,capacity;
   
	public void setData(Airplane airplane)
	{
		 Location.setText("Location:" + airplane.getLocation());
         companyName.setText("Airplane Company:" + airplane.getCompanyName());
         cost.setText("Cost:" +airplane.getCost());
         if(airplane.isOverWeight())
         {
       	  overweight.setText("Εxtra cost:" + "yes");
         }
         else
         {
       	  overweight.setText("Extra cost:" + "no");
         }
         if(airplane.isPermitAnimal())
         {
       	  permitAnimal.setText("Permit Animal:" + "yes");
         }
         else
         {
       	  permitAnimal.setText("Permit Animal:" + "no");
         }
         if(airplane.isHasBuis())
         {
        	 hasBuis.setText("Has Buisness Seat:" + "yes");
         }
         else
         {
        	 hasBuis.setText("Has Buisness Seat:" + "no");
         }
         airport.setText("Destination Airport:"+ airplane.getDestairport());
         airId.setText("Airplane Id:"+ airplane.getAirplaneId());
         capacity.setText("Airplane Capacity:" + airplane.getAirplanecapacity());
		
	}

}
