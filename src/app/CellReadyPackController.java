package app;

import java.io.ByteArrayInputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CellReadyPackController {

    @FXML
    private Label accomId,airid,airport,capacityAir,capacityShip,companyAcc,companyAir, companyShip,disable,emailAcc,emailAir,emailShip,existCabin,
    hasAC,hasAuto,hasBuis,hasWifi,haschildRoom,launch,locationAcc,locationAir,locationShip,overweight,permAnimalAir,permAnimalShip,phoneAc,
    phoneAir,phoneShip,port,priceAccom,priceAir,priceShip,prmAnimalAccom,shipid,totalbed,totalCost;
    @FXML
    private ImageView img;

 
  
	
	   public void setData(Package packageT)
	    {
		   
		 
		   
		   
		   Accomondation accom=packageT.getAccomondation();
		   
		   if(accom.getImage()!=null)
		   {
			   img.setImage(new Image(new ByteArrayInputStream(accom.getImage())));
	            img.setPreserveRatio(false);
	            img.setFitHeight(400); 
	            img.setFitWidth(400);
		   }
		   
		   accomId.setText("Code:"+accom.getIdHot());
		   companyAcc.setText("Hotel:"+accom.getCompanyName());
		   if(accom.isDisableEntranceV())
		   {
		   disable.setText("Disable entrance:"+"yes");
		   }
		   else
		   {
			   disable.setText("Disable entrance:"+"no");
		   }
		   emailAcc.setText("Email:"+accom.getEmail());
		   if(accom.isACv())
		   {
		   hasAC.setText("A/C:"+"yes");
		   }
		   else
		   {
			   hasAC.setText("A/C:"+"no");
		   }
		   if(accom.isWifiV())
		   {
		   hasWifi.setText("Wifi:"+"yes");
		   }
		   else
		   {
			   hasWifi.setText("Wifi:"+"no");
		   }
		   if(accom.isChildRoomV())
		   {
		   haschildRoom.setText("Child Room:"+"yes");
		   }
		   else
		   {
			   haschildRoom.setText("Child Room:"+"no");
		   }
		   launch.setText("Food:"+accom.getLaunch());
		   locationAcc.setText("Location:"+accom.getLocation());
		   phoneAc.setText("Phone:"+accom.getPhone());
		   priceAccom.setText("Room price:"+ Double.toString(accom.getCost()));
		   if(accom.isPermitAnimal())
		   {
		   prmAnimalAccom.setText("Permit Animal:"+"yes");
		   }
		   else
		   {
			   prmAnimalAccom.setText("Permit Animal:"+"no");
		   }
		   totalbed.setText("Beds:"+accom.getCountRoom());
	    	
	    	if(packageT.getPartner() instanceof Ship)
	    	{
	    		Ship ship=(Ship) packageT.getPartner();
	    		capacityShip.setText("Ship capacity:"+Integer.toString(ship.getCapacity()));
	    		companyShip.setText("Company:"+ship.getCompanyName());
	    		emailShip.setText("Email:"+ship.getEmail());
	    		if(ship.isCabinExist())
	    		{
	    			existCabin.setText("Has Cabin:"+ "yes");
	    		}
	    		else
	    		{
	    			existCabin.setText("Has Cabin:"+ "no");
	    		}
	    		if(ship.isHasAuto())
	    		{
	    			hasAuto.setText("Has Auto:"+"yes");
	    		}
	    		else
	    		{
	    			hasAuto.setText("Has Auto:"+"no");
	    		}
	    		locationShip.setText("Location:"+ship.getLocation());
	    		if(ship.isPermitAnimal())
	    		{
	    			permAnimalShip.setText("Permit Animal:"+"yes");
	    			
	    		}
	    		else
	    		{
	    			permAnimalShip.setText("Permit Animal:"+"no");
	    		}
	    		phoneShip.setText("Phone:"+ship.getPhone());
	    		port.setText("Port"+ship.getPortDep());
	    		priceShip.setText("Price:"+Double.toString(ship.getCost()));
	    		shipid.setText("Ship:"+ship.getShipId());
	    		blank(packageT);
	    	}
	    	else if(packageT.getPartner() instanceof Airplane)
	    	{
	    		Airplane air=(Airplane)packageT.getPartner();
	    		airid.setText("Airplane:"+ air.getAirplaneId());
	    		airport.setText("Airport:"+air.getDestairport());
	    		capacityAir.setText("Airplane Capacity:"+Integer.toString( air.getAirplanecapacity()) );
	    		companyAir.setText("Company:"+air.getCompanyName());
	    		emailAir.setText("Email :"+air.getEmail());
	    		if(air.isHasBuis())
	    		{
	    			hasBuis.setText("Buisness class:"+"yes");
	    		}
	    		else
	    		{
	    			hasBuis.setText("Buisness class:"+"no");
	    		}
	    		locationAir.setText("Location"+air.getLocation());
	    		if(air.isOverWeight())
	    		{
	    			overweight.setText("Overweight extra :"+"yes");
	    			
	    		}
	    		else
	    		{
	    			overweight.setText("Overweight extra :"+"no");
	    		}
	    		if(air.isPermitAnimal())
	    		{
	    			permAnimalAir.setText("Permit Animal:"+"yes");
	    		}
	    		else
	    		{
	    			permAnimalAir.setText("Permit Animal:"+"no");
	    		}
	    		 phoneAir.setText("Phone:"+air.getPhone());
	    		 priceAir.setText("Price:"+ Double.toString(air.getCost()));
	    		 blank(packageT);
	    	}
	    	totalCost.setText(Double.toString( packageT.getCost()));
	    	
	    }
	    public void blank(Package packageblank)
	    {
	    	if(packageblank.getPartner() instanceof Airplane)
	    	{

	    		capacityShip.setText("");
	    		companyShip.setText("");
	    		emailShip.setText("");
	    	    existCabin.setText("");
	    		hasAuto.setText("");
	    		locationShip.setText("");
                permAnimalShip.setText("");
	    		phoneShip.setText("");
	    		port.setText("");
	    		priceShip.setText("");
	    		shipid.setText("");
	    			
	    	}
	    	else if(packageblank.getPartner() instanceof Ship)
	    	{
	    		
	    		airid.setText("");
	    		airport.setText("");
	    		capacityAir.setText("");
	    		companyAir.setText("");
	    		emailAir.setText("");
	    		phoneAir.setText("");
	    		 priceAir.setText("");
	    		 locationAir.setText("");
	    		 hasBuis.setText("");
	    		 overweight.setText("");
	    		 permAnimalAir.setText("");
	    		
	    	}
	    		
	    			
	    		
	    		
	    		
	    		
	    	}
	    

}
