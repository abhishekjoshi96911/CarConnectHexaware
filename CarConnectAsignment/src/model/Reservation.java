package model;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reservation {

	private int reservationId;
	private int customerId;
	private int vehicleId;
	private String startDate;
	private String endDate;
	private double totalCost;
	private String status;
	public Reservation(int reservationId, int customerId, int vehicleId, String startDate, String endDate, double totalCost,
			String status) {
		super();
		this.reservationId = reservationId;
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalCost = totalCost;
		this.status = status;
	}
	
	public Reservation(int customerId, int vehicleId, String startDate, String endDate, double totalCost,
			String status) {
		super();
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalCost = totalCost;
		this.status = status;
	}
	

	public void setReservation1(int reservationId, int vehicleId, String startDate, String endDate, double totalCost,
			String status) {
		
		this.reservationId = reservationId;
		this.vehicleId = vehicleId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalCost = totalCost;
		this.status = status;
	}

	public Reservation() {
		super();
	}
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	 public Reservation(String startDate, String endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	 

	

	// Method to calculate total cost based on a fixed daily rate
    public  double calculateTotalCost(double dailyRate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateObj = dateFormat.parse(startDate);
            Date endDateObj = dateFormat.parse(endDate);

        
            long duration = (endDateObj.getTime() - startDateObj.getTime()) / (24 * 60 * 60 * 1000);

            
            totalCost = duration * dailyRate;
            return totalCost;
            
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        }
        return 0.0;
    }

	
}

