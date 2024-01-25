package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;
import exception.ReservationException;
import model.Reservation;
import util.DBConnUtil;
//import util.DBPropertyUtil;
import util.Dateutil;

public class ReservationService implements IReservationService {
//	private static final String fileName="../src/db.properties"; 
//    private static final String URL = DBPropertyUtil.getConnectionString(fileName);
   private Connection connection;

    // Constructor to initialize the connection
    public ReservationService() throws SQLException, IOException, ClassNotFoundException {
        this.connection = DBConnUtil.getConnection();
    }

    @Override
    public Reservation getReservationById(int reservationId) throws FileNotFoundException, ClassNotFoundException, IOException, InvalidInputException{
        
            String sql = "SELECT * FROM Reservation WHERE ReservationID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToReservation(resultSet);
                    }
                    else {
                    	throw new ReservationException("Reservation not found for Reservation Id "+reservationId);
                    }
                }
            }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reservation> getReservationsByCustomerId(int customerId) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        validateCustomerId(customerId); // Validate input data
        if (!isCustomerExists(customerId)) {
		    throw new InvalidInputException("Customer with CustomerID " +customerId + " not found.");
		}
        
		String sql = "SELECT * FROM Reservation WHERE CustomerID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setInt(1, customerId);
		    try (ResultSet resultSet = statement.executeQuery()) {
		        List<Reservation> reservations = new ArrayList<>();
		        while (resultSet.next()) {
		            reservations.add(mapResultSetToReservation(resultSet));
		        }
		        return reservations;
		    }
		}
    }
    private void validateCustomerId(int customerId) throws InvalidInputException {
        if (customerId <= 0) {
            throw new InvalidInputException("Invalid CustomerID.");
        }
    }
    private boolean isCustomerExists(int customerId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   return true;
                }
            }
        } 
        return false;
        
    }

    @Override
    public void createReservation(Reservation reservationData) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        validateReservationData(reservationData); // Validate input data
        if (isReservationExists(reservationData.getCustomerId())) {
            throw new ReservationException("Reservation with customerId " + reservationData.getCustomerId() + "Already exists!");
        }
        Dateutil dUtil = new Dateutil(reservationData.getEndDate());
        if (!dUtil.validateDateFormat()) {
            throw new InvalidInputException("Wrong date format !. Give end date in yyyy-MM-dd format.");
        }
        Dateutil dUtil1 = new Dateutil(reservationData.getStartDate());
        if (!dUtil1.validateDateFormat()) {
            throw new InvalidInputException("Wrong date format !. Give start date in yyyy-MM-dd format.");
        }

        String sql = "INSERT INTO Reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationData.getCustomerId());
            statement.setInt(2, reservationData.getVehicleId());
            statement.setString(3, reservationData.getStartDate());
            statement.setString(4, reservationData.getEndDate());
            statement.setDouble(5, reservationData.getTotalCost());
            statement.setString(6, reservationData.getStatus());

            // Update vehicle availability
            updateVehicleAvailability(reservationData.getVehicleId(), false); // Assuming false means not available

            statement.executeUpdate();
            System.out.println("Reservation created successfully");
        }
    }

    private void updateVehicleAvailability(int vehicleId, boolean availability) throws SQLException {
        String updateSql = "UPDATE Vehicle SET Availability = ? WHERE VehicleID = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setBoolean(1, availability);
            updateStatement.setInt(2, vehicleId);
            updateStatement.executeUpdate();
        }
    }


    private void validateReservationData(Reservation reservation) throws InvalidInputException {
    	if (reservation.getCustomerId() <= 0 || reservation.getVehicleId() <= 0 || reservation.getStartDate().isEmpty() ||
    	        reservation.getEndDate().isEmpty() || reservation.getTotalCost() < 0 || reservation.getStatus().isEmpty()) {
    	    throw new InvalidInputException("All fields must be filled out and have valid values.");
    	}

        // Add more validation rules for other fields if needed
    }
    private void validateReservationDataForUpdate(Reservation reservation) throws InvalidInputException {
    	if (reservation.getReservationId()<=0|| reservation.getVehicleId() <= 0 || reservation.getStartDate().isEmpty() ||
    	        reservation.getEndDate().isEmpty() || reservation.getTotalCost() < 0 || reservation.getStatus().isEmpty()) {
    	    throw new InvalidInputException("All fields must be filled out and have valid values.");
    	}

        // Add more validation rules for other fields if needed
    }
    private boolean isReservationExists(int customerId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Reservation WHERE CustomerID = ?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   return true;
                }
            }
        } 
        return false;
        
    }
    private boolean isReservationExistsForReserveId(int ReservationId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Reservation WHERE ReservationID = ?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ReservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   return true;
                }
            }
        } 
        return false;
        
    }
 

    @Override
    public void updateReservation(Reservation reservationData) throws SQLException, InvalidInputException, IOException, ClassNotFoundException {
        validateReservationDataForUpdate(reservationData); // Validate input data
        Dateutil dUtil=new Dateutil(reservationData.getEndDate());
    	if(!dUtil.validateDateFormat()) {
    		throw new InvalidInputException("Wrong date format !. Give end date in yyyy-MM-dd format.");
    	}
    	Dateutil dUtil1=new Dateutil(reservationData.getStartDate());
    	if(!dUtil1.validateDateFormat()) {
    		throw new InvalidInputException("Wrong date format !. Give start date in yyyy-MM-dd format.");
    	}
		String sql = "UPDATE Reservation SET VehicleID=?, StartDate=?, EndDate=?, TotalCost=?, Status=? WHERE ReservationID=?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setInt(1, reservationData.getVehicleId());
		    statement.setString(2, reservationData.getStartDate());
		    statement.setString(3, reservationData.getEndDate());
		    statement.setDouble(4, reservationData.getTotalCost());
		    statement.setString(5, reservationData.getStatus());
		    statement.setInt(6, reservationData.getReservationId());

		    statement.executeUpdate();
		    System.out.println("Reservation updated successfully");
		}
    }

   
   
   

    @Override
    public void cancelReservation(int reservationId) {
        try (Connection connection = DBConnUtil.getConnection()) {
        	
        	if (!isReservationExistsForReserveId(reservationId)) {
			    throw new ReservationException("Reservation  with ReservationId " + reservationId + " not found.");
			}
        	
        	int vehicleId = getVehicleIdForReservation(reservationId);
            String sql = "DELETE FROM Reservation WHERE ReservationID=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, reservationId);
                statement.executeUpdate();
                System.out.println("Reservation deleted succesfully");
                updateVehicleAvailability(vehicleId, true);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
    }
    
    public int getVehicleIdForReservation(int reservationId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT VehicleID FROM Reservation WHERE ReservationID=?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("VehicleID");
                }
            }
        }
        throw new SQLException("VehicleId not found for ReservationId: " + reservationId);
    }

    // Helper method to map ResultSet to Customer entity
    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {
    	Reservation reservation=new Reservation();
    	reservation.setReservationId(resultSet.getInt("reservationID"));
    	reservation.setCustomerId(resultSet.getInt("CustomerID"));
    	reservation.setVehicleId(resultSet.getInt("VehicleID"));
    	reservation.setStartDate(resultSet.getString("StartDate"));
    	reservation.setEndDate(resultSet.getString("EndDate"));
    	reservation.setTotalCost(resultSet.getDouble("TotalCost"));
    	reservation.setStatus(resultSet.getString("Status"));
        
    
        return reservation;
    }
}
