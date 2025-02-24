package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

public class CellAccomController {
	@FXML
    private Label compNm,LocComp,BedsNum,disableEntr,animal,cost,launch,childRoom,AC,wifi;
    @FXML
    private ImageView imgV;
    
    public void setData(Accomondation accomondation) {
        if (accomondation.getImage() != null) {
            imgV.setImage(new Image(new ByteArrayInputStream(accomondation.getImage())));
            imgV.setPreserveRatio(false);
            imgV.setFitHeight(400); 
            imgV.setFitWidth(200); 

            
        }
        compNm.setText("Hotel:" + accomondation.getCompanyName());
        LocComp.setText("Location:" + accomondation.getLocation());
        BedsNum.setText("Room Type:" + accomondation.getCountRoom() + "κλινο");
        launch.setText("Food:"+accomondation.getLaunch());
        cost.setText("Cost/night:" + accomondation.getCost());
        if(accomondation.isPermitAnimal()==true)
        {
        	 animal.setText("Permit Animal:" + "yes");
        }
        else
        {
        	animal.setText("Permit Animal:" + "no");
        }
       
        if(accomondation.isDisableEntranceV()==true)
        {
        	disableEntr.setText("Disabled Entrance:"+"yes");
        }
        else
        {
        	disableEntr.setText("Disabled Entrance:"+"no");
        }
        if(accomondation.isChildRoomV()==true)
        {
        	childRoom.setText("Has chlid room:"+"yes");
        }
        else
        {
        	childRoom.setText("Has chlid room:"+"no");
        }
        if(accomondation.isACv()==true)
        {
        	AC.setText("Has A/C:"+"yes");
        }
        else
        {
        	AC.setText("Has A/C:"+"no");
        }
        if(accomondation.isWifiV()==true)
        {
        	wifi.setText("Has Wifi:"+"yes");
        }
        else
        {
        	wifi.setText("Has Wifi:"+"no");
        }
    }

   
}
