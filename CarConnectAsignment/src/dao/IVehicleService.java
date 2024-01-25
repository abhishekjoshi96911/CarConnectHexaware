package dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import exception.InvalidInputException;
import model.Vehicle;

public interface IVehicleService {
	
	public Vehicle getVehicleById(int vehicleId) throws SQLException, InvalidInputException, IOException, ClassNotFoundException;
	public List<Vehicle> getAvailableVehicles()  throws SQLException, IOException, ClassNotFoundException;
	public void addVehicle(Vehicle vehicle) throws SQLException, IOException, ClassNotFoundException;
	public void updateVehicle(Vehicle vehicle) throws SQLException, InvalidInputException, IOException, ClassNotFoundException;
	
	public void removeVehicle(int vehicleId) throws SQLException, InvalidInputException, IOException, ClassNotFoundException;
}
