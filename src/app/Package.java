package app;



public class Package {
	private String packId;
    private Partner partner;
	private Accomondation accomondation;
	private double cost;
	private boolean fromAdmin;
	public Package(String apackId,Accomondation Aaccomondation,Partner apartner,double aCost,boolean isFromAdmin) {
		 
		this.packId=apackId;
		this.accomondation=Aaccomondation;
		this.partner=apartner;
		this.cost=aCost;
	    this.fromAdmin=isFromAdmin;
	}
	public String getPackId() {
		return packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public Accomondation getAccomondation() {
		return accomondation;
	}
	public void setAccomondation(Accomondation accomondation) {
		this.accomondation = accomondation;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public boolean isFromAdmin() {
		return fromAdmin;
	}
	public void setFromAdmin(boolean fromAdmin) {
		this.fromAdmin = fromAdmin;
	}
	
    
}
