package app;

import java.util.ArrayList;

public class User {

	private String username;
	private String password;
	private String name;
	private String surname;
	private String email;
	private String phone;
	private boolean isAdmin;
	
    //private ArrayList<Package> tourPackages=new ArrayList<Package> ();

	public User(String aUsername, String aPassword,String aName,String aSurname,String anEmail,String aPhone,boolean isAdmin) {
		this.username=aUsername;
		this.password=aPassword;
		this.name=aName;
		this.surname=aSurname;
		this.email=anEmail;
		this.phone=aPhone;
		this.isAdmin=isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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
