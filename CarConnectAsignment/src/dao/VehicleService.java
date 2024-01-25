package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.DatabaseConnectionException;
import exception.InvalidInputException;
import exception.VehicleNotFoundException;
import model.Vehicle;
import util.DBConnUtil;
//import util.DBPropertyUtil;
import util.Dateutil;

public class VehicleService implements IVehicleService {
//
//    private static final String fileName = "../src/db.properties";
//    private static final String URL = DBPropertyUtil.getConnectionString(fileName);
     Connection connection= null ;

    public VehicleService() throws SQLException, IOException, ClassNotFoundException {
        this.connection = DBConnUtil.getConnection();
    }

    @Override
    public Vehicle getVehicleById(int vehicleId) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        String sql = "SELECT * FROM Vehicle WHERE VehicleID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setInt(1, vehicleId);
		    try (ResultSet resultSet = statement.executeQuery()) {
		        if (resultSet.next()) {
		            return mapResultSetToVehicle(resultSet);
		            
		        } else {
		            throw new VehicleNotFoundException("Vehicle not found for vehicle id " + vehicleId);
		        }
		    }
		}catch(DatabaseConnectionException e) {
			System.out.println("Unable to connect to database");
		}
		return null;
    }

    @Override
    public List<Vehicle> getAvailableVehicles() throws SQLException, IOException, ClassNotFoundException {
        List<Vehicle> availableVehicles = new ArrayList<>();
        String sql = "SELECT * FROM Vehicle WHERE Availability = true";
		try (PreparedStatement statement = connection.prepareStatement(sql);
		     ResultSet resultSet = statement.executeQuery()) {
		    while (resultSet.next()) {
		        availableVehicles.add(mapResultSetToVehicle(resultSet));
		    }
		    if(!availableVehicles.isEmpty()) {
		    	return availableVehicles;
		    }
		    else {
		    	throw new VehicleNotFoundException("No vehicles are available right now!");
		    }
		}
        
    }

    @Override
    public void addVehicle(Vehicle vehicle) throws SQLException, IOException, ClassNotFoundException {
    	validateVehicleForInsert(vehicle);
    	Dateutil dUtil=new Dateutil(vehicle.getYear());
    	
    	if(!dUtil.validateYearFormat()) {
    		throw new InvalidInputException("Wrong year format !. Give year in yyyy format.");
    	}
    	
        String sql = "INSERT INTO Vehicle (Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setString(1, vehicle.getModel());
		    statement.setString(2, vehicle.getMake());
		    statement.setString(3, vehicle.getYear());
		    statement.setString(4, vehicle.getColor());
		    statement.setString(5, vehicle.getRegistrationNumber());
		    statement.setBoolean(6, vehicle.isAvailability());
		    statement.setDouble(7, vehicle.getDailyRate());
		    statement.executeUpdate();
		    System.out.println("Vehicle added successfully");
		}
    }

    @Override
    public void updateVehicle(Vehicle vehicle) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        validateVehicleForUpdate(vehicle);
        if (!isVehicleExists(vehicle.getVehicleId())) {
		    throw new VehicleNotFoundException("Vehicle with VehicleID " +vehicle.getVehicleId()+ " not found.");
		}
        Dateutil dUtil=new Dateutil(vehicle.getYear());
    	
    	if(!dUtil.validateYearFormat()) {
    		throw new InvalidInputException("Wrong year format !. Give year in yyyy format.");
    	}
		String sql = "UPDATE Vehicle SET Model=?, Make=?, Year=?, Color=?,  Availability=?, DailyRate=? WHERE VehicleID=?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setString(1, vehicle.getModel());
		    statement.setString(2, vehicle.getMake());
		    statement.setString(3, vehicle.getYear());
		    statement.setString(4, vehicle.getColor());
		    
		    statement.setBoolean(5, vehicle.isAvailability());
		    statement.setDouble(6, vehicle.getDailyRate());
		    statement.setInt(7, vehicle.getVehicleId());
		    statement.executeUpdate();
		    System.out.println("Vehicle updated succesfully");
		}
    }

    private void validateVehicleForUpdate(Vehicle vehicle) throws InvalidInputException {
        if (vehicle.getVehicleId() <= 0) {
            throw new InvalidInputException("Invalid VehicleID for update.");
        }
        if (vehicle.getModel().isEmpty() || vehicle.getMake().isEmpty() || vehicle.getYear().isEmpty()
                || vehicle.getColor().isEmpty() ) {
            throw new InvalidInputException("All fields must be filled out.");
        }
        // Add more validation rules as needed
    }
    private void validateVehicleForInsert(Vehicle vehicle) throws InvalidInputException {
        
        if (vehicle.getModel().isEmpty() || vehicle.getMake().isEmpty() || vehicle.getYear().isEmpty()
                || vehicle.getColor().isEmpty() || vehicle.getRegistrationNumber().isEmpty()) {
            throw new InvalidInputException("All fields must be filled out.");
        }
        // Add more validation rules as needed
    }

    @Override
    public void removeVehicle(int vehicleId) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        if (!isVehicleExists(vehicleId)) {
		    throw new VehicleNotFoundException("Vehicle with VehicleID " + vehicleId + " not found.");
		}
		String sql = "DELETE FROM Vehicle WHERE VehicleID=?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setInt(1, vehicleId);
		    statement.executeUpdate();
		    System.out.println("Vehicle removed succesfully");
		}
    }

    private boolean isVehicleExists(int vehicleId) throws SQLException {
        String sql = "SELECT * FROM Vehicle WHERE VehicleID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vehicleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet resultSet) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(resultSet.getInt("VehicleID"));
        vehicle.setModel(resultSet.getString("Model"));
        vehicle.setMake(resultSet.getString("Make"));
        vehicle.setYear(resultSet.getString("Year"));
        vehicle.setColor(resultSet.getString("Color"));
        vehicle.setRegistrationNumber(resultSet.getString("RegistrationNumber"));
        vehicle.setAvailability(resultSet.getBoolean("Availability"));
        vehicle.setDailyRate(resultSet.getDouble("DailyRate"));
    
        return vehicle;
    }
    
	
}

