package model;

import exception.AuthenticationException;

public class Customer {
	
	private int customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String address;
	private String username;
	private String password;
	private String registrationDate;
	public Customer(int customerId, String firstName, String lastName, String email, String phoneNumber, String address,
			String userName, String password, String registrationDate) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.username = userName;
		this.password = password;
		this.registrationDate = registrationDate;
	}
	public Customer( String firstName, String lastName, String email, String phoneNumber, String address,
			String userName, String password, String registrationDate) {
		super();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.username = userName;
		this.password = password;
		this.registrationDate = registrationDate;
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Customer() {
		
	}
	
	
	public Customer(String firstName, String lastName, String email, String phoneNumber, String address,
			String password, String registrationDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.password = password;
		this.registrationDate = registrationDate;
	}
	// Authenticate method
    public boolean authenticate(String enteredPassword) throws AuthenticationException {
        if (enteredPassword.equals(password)) {
            return true;
        } else {
            throw new AuthenticationException("Incorrect password");
        }
    }
}
