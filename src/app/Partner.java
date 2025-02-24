package app;

public class Partner {
    protected String CompanyName;
    protected String AFM;
    protected String Location;
    protected String typeServ;
    protected String email;
    protected String phone;
    protected String financial_agreement;
    protected String username;
    protected String password;
	public Partner(String companyName, String aFM, String location,String atype, String anEmail, String aPhone,String afinancial_agreement,String aUsername,String aPassword) {
	
		this.CompanyName = companyName;
		this.AFM = aFM;
		this.Location = location;
		this.typeServ=atype;
		this.email=anEmail;
		this.phone=aPhone;
		this.financial_agreement=afinancial_agreement;
		this.username=aUsername;
		this.password=aPassword;
	}
    public Partner()
    {
    	
    }
	

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFinancial_agreement() {
		return financial_agreement;
	}
	public void setFinancial_agreement(String financial_agreement) {
		this.financial_agreement = financial_agreement;
	}
	protected String getCompanyName() {
		return CompanyName;
	}

    protected void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	protected String getAFM() {
		return AFM;
	}

	protected void setAFM(String aFM) {
		AFM = aFM;
	}

	protected String getLocation() {
		return Location;
	}

	protected void setLocation(String location) {
		Location = location;
	}
	public String getTypeServ() {
		return typeServ;
	}
	public void setTypeServ(String typeServ) {
		this.typeServ = typeServ;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

}
