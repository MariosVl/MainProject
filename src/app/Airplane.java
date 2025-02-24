package app;

public class Airplane extends Partner {

	private boolean hasBuis;
	private String Destairport;
	private boolean overWeight;
	private int Airplanecapacity;
	private String airplaneId;
	private double cost;
	private boolean permitAnimal;



	public Airplane(String companyName, String aFM, String location, String atype, String anEmail, String aPhone,
			String afinancial_agreement,String aUsername,String aPassword, boolean hasBuis, String Destairport, boolean overWeight, String airplaneId,
			double cost, boolean permitAnimal,int Airplanecapacity) {
		super(companyName, aFM, location, atype, anEmail, aPhone, afinancial_agreement, aUsername, aPassword);
		this.hasBuis = hasBuis;
		this.Destairport = Destairport;
		this.overWeight = overWeight;
		this.airplaneId = airplaneId;
		this.cost = cost;
		this.permitAnimal = permitAnimal;
		this.Airplanecapacity=Airplanecapacity;
	}



	public boolean isHasBuis() {
		return hasBuis;
	}



	public void setHasBuis(boolean hasBuis) {
		this.hasBuis = hasBuis;
	}



	public String getDestairport() {
		return Destairport;
	}



	public void setDestairport(String destairport) {
		Destairport = destairport;
	}



	public boolean isOverWeight() {
		return overWeight;
	}



	public void setOverWeight(boolean overWeight) {
		this.overWeight = overWeight;
	}



	public int getAirplanecapacity() {
		return Airplanecapacity;
	}



	public void setAirplanecapacity(int airplanecapacity) {
		Airplanecapacity = airplanecapacity;
	}



	public String getAirplaneId() {
		return airplaneId;
	}



	public void setAirplaneId(String airplaneId) {
		this.airplaneId = airplaneId;
	}



	public double getCost() {
		return cost;
	}



	public void setCost(double cost) {
		this.cost = cost;
	}



	public boolean isPermitAnimal() {
		return permitAnimal;
	}



	public void setPermitAnimal(boolean permitAnimal) {
		this.permitAnimal = permitAnimal;
	}



	
}
