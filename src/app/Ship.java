package app;

public class Ship extends Partner {

	private boolean cabinExist;
	private boolean hasAuto;
	private String PortDep;
	private String shipId;
	private double cost;
	private boolean permitAnimal;
	private int capacity;
	


	public Ship(String companyName, String aFM, String location, String atype, String anEmail, String aPhone,
			String afinancial_agreement,String aUsername, String aPassword ,boolean isCabin, boolean hasAuto, String dePport, String shipId, double cost,
			boolean permitAnimal,int aCapacity) {
		super(companyName, aFM, location, atype, anEmail, aPhone, afinancial_agreement,aUsername, aPassword);
		this.cabinExist = isCabin;
		this.hasAuto = hasAuto;
		this.PortDep = dePport;
		this.shipId = shipId;
		this.cost = cost;
		this.permitAnimal = permitAnimal;
		this.capacity=aCapacity;
	}



	public boolean isCabinExist() {
		return cabinExist;
	}



	public void setCabinExist(boolean cabinExist) {
		this.cabinExist = cabinExist;
	}



	public boolean isHasAuto() {
		return hasAuto;
	}



	public void setHasAuto(boolean hasAuto) {
		this.hasAuto = hasAuto;
	}



	public String getPortDep() {
		return PortDep;
	}



	public void setPortDep(String portDep) {
		PortDep = portDep;
	}



	public String getShipId() {
		return shipId;
	}



	public void setShipId(String shipId) {
		this.shipId = shipId;
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



	public int getCapacity() {
		return capacity;
	}



	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	

}
