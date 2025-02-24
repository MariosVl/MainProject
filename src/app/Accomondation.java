package app;

public class Accomondation extends Partner {

	private int CountRoom;
	private String Launch;
	private double cost;
	private String idHot;
	private boolean permitAnimal;
    private byte[] image;
    private boolean disableEntranceV;
    private boolean childRoomV;
    private boolean ACv;
    private boolean wifiV;
  
	public Accomondation(String companyName, String aFM, String location, String atype, String anEmail, String aPhone,String afinancial_agreement,String aUsername, String aPassword ,int countRoom, String aLaunch, double cost, String idHot,
	boolean permitAnimal, byte[] image, boolean adisableEntrance,boolean achildRoom,boolean aAC,boolean aWifi) {
		super(companyName, aFM, location, atype, anEmail, aPhone, afinancial_agreement,aUsername, aPassword);
		CountRoom = countRoom;
		this.Launch = aLaunch;
		this.cost = cost;
		this.idHot = idHot;
		this.permitAnimal = permitAnimal;
		this.image = image;
		this.disableEntranceV=adisableEntrance;
		this.childRoomV=achildRoom;
		this.ACv=aAC;
		this.wifiV=aWifi;
	}




	public String getLaunch() {
		return Launch;
	}




	public void setLaunch(String launch) {
		Launch = launch;
	}




	public boolean isDisableEntranceV() {
		return disableEntranceV;
	}




	public void setDisableEntranceV(boolean disableEntranceV) {
		this.disableEntranceV = disableEntranceV;
	}




	public boolean isChildRoomV() {
		return childRoomV;
	}




	public void setChildRoomV(boolean childRoomV) {
		this.childRoomV = childRoomV;
	}




	public boolean isACv() {
		return ACv;
	}




	public void setACv(boolean aCv) {
		ACv = aCv;
	}




	public boolean isWifiV() {
		return wifiV;
	}




	public void setWifiV(boolean wifiV) {
		this.wifiV = wifiV;
	}




	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public boolean isPermitAnimal() {
		return permitAnimal;
	}

	public void setPermitAnimal(boolean permitAnimal) {
		this.permitAnimal = permitAnimal;
	}

	public String getIdHot() {
		return idHot;
	}

	public void setIdHot(String idHot) {
		this.idHot = idHot;
	}

	public int getCountRoom() {
		return CountRoom;
	}

	public void setCountRoom(int countRoom) {
		CountRoom = countRoom;
	}


	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	

}
