package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CellShipController {
    @FXML
    private Label Location,capacity,companyName,cost,hasAuto,hasCabin,permitAnimal,portDep,shipId;

  

	public void setData(Ship ship)
	{
          Location.setText("Location:" + ship.getLocation());
          companyName.setText("Ship Company:" + ship.getCompanyName());
          cost.setText("Cost:" +ship.getCost());
          if(ship.isHasAuto())
          {
        	  hasAuto.setText("Permit Auto:" + "yes");
          }
          else
          {
        	  hasAuto.setText("Permit Auto:" + "no");
          }
          if(ship.isPermitAnimal())
          {
        	  permitAnimal.setText("Permit Animal:" + "yes");
          }
          else
          {
        	  permitAnimal.setText("Permit Animal:" + "no");
          }
          if(ship.isCabinExist())
          {
        	  hasCabin.setText("Cabin Existance:" + "yes");
          }
          else
          {
        	  hasCabin.setText("Cabin Existance:"+ "no");
          }
          portDep.setText("Arrival_Port:"+ ship.getPortDep());
          shipId.setText("Ship Id:"+ ship.getShipId());
          capacity.setText("Ship Capacity:"+ship.getCapacity());
	}

}
