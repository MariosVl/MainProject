package app;

import java.time.LocalDate;

public class Reservation {

	private User user;
	private Package packageR;
	private LocalDate dateStart;
	private LocalDate dateEnd;
	private String reserveid;
	private Payment pay;
	public Reservation(String areserveid,User user, Package packageR, LocalDate dateStart, LocalDate dateEnd,Payment pay) {
	    this.reserveid=areserveid;
		this.user = user;
		this.packageR = packageR;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.pay=pay;
		
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Package getPackageR() {
		return packageR;
	}
	public void setPackageR(Package packageR) {
		this.packageR = packageR;
	}
	public LocalDate getDateStart() {
		return dateStart;
	}
	public void setDateStart(LocalDate dateStart) {
		this.dateStart = dateStart;
	}
	public LocalDate getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	public String getReserveid() {
		return reserveid;
	}
	public void setReserveid(String reserveid) {
		this.reserveid = reserveid;
	}
	public Payment getPay() {
		return pay;
	}
	public void setPay(Payment pay) {
		this.pay = pay;
	}

	
	
	
}
