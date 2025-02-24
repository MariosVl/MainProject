package app;

public class Payment {

	private String cardnumber;
	private String cardcode;
	private String owner;
	private boolean isPaid;
	private double amount;
	public Payment(String cardnumber, String cardcode, String owner,double amount,boolean paid) {
		
		this.cardnumber = cardnumber;
		this.cardcode = cardcode;
		this.owner = owner;
		this.amount=amount;
		this.isPaid=paid;
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public String getCardcode() {
		return cardcode;
	}
	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public boolean isPaid() {
		return isPaid;
	}
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
