package model;



public class Vehicle {
	
	private int vehicleId;
	private String model;
	private String make;
	private String year;
	private String color;
	private String registrationNumber;
	private boolean availability;
	private double dailyRate;
	public Vehicle(int vehicleId, String model, String make, String year, String color, String registrationNumber,
			boolean availability, double dailyRate) {
		super();
		this.vehicleId = vehicleId;
		this.model = model;
		this.make = make;
		this.year = year;
		this.color = color;
		this.registrationNumber = registrationNumber;
		this.availability = availability;
		this.dailyRate = dailyRate;
	}
	public Vehicle( String model, String make, String year, String color, String registrationNumber,
			boolean availability, double dailyRate) {
		super();
		
		this.model = model;
		this.make = make;
		this.year = year;
		this.color = color;
		this.registrationNumber = registrationNumber;
		this.availability = availability;
		this.dailyRate = dailyRate;
	}
	public Vehicle() {
		super();
	}
	
	public Vehicle(int vehicleId)
	{
		this.vehicleId=vehicleId;
	}

	public Vehicle(int vehicleId, String model, String make, String year, String color, boolean availability,
			double dailyRate) {
		super();
		this.vehicleId = vehicleId;
		this.model = model;
		this.make = make;
		this.year = year;
		this.color = color;
		this.availability = availability;
		this.dailyRate = dailyRate;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	public double getDailyRate() {
		return dailyRate;
	}
	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}
	
}
