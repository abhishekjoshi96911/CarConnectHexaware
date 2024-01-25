package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnUtil;
//mport util.DBPropertyUtil;

public class ReportGenerator {
	
//	private static final String fileName="../src/db.properties"; 
//    private static final String URL = DBPropertyUtil.getConnectionString(fileName);
    // Method to generate a report based on reservation data
    public void generateReservationReport() {

        String sql = "SELECT * FROM reservation";

        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Reservation Report:");
            
            while (resultSet.next()) {
                System.out.println("Reservation ID: " + resultSet.getInt("ReservationID"));
                System.out.println("Customer ID: " + resultSet.getInt("CustomerID"));
                System.out.println("Vehicle ID: " + resultSet.getInt("VehicleID"));
                System.out.println("Start Date: " + resultSet.getString("StartDate"));
                System.out.println("End Date: " + resultSet.getString("EndDate"));
                System.out.println("Total Cost: " + resultSet.getDouble("TotalCost"));
                System.out.println("Status: " + resultSet.getString("Status"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        } catch (FileNotFoundException e1) {
        	System.out.println(e1.getMessage());
		} catch (ClassNotFoundException e1) {
			System.out.println(e1.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
    }

    // Method to generate a report based on vehicle data
    public void generateVehicleReport() {
        String sql = "SELECT * FROM vehicle";
        
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Vehicle Report:");

            while (resultSet.next()) {
                System.out.println("Vehicle ID: " + resultSet.getInt("VehicleID"));
                System.out.println("Model: " + resultSet.getString("Model"));
                System.out.println("Make: " + resultSet.getString("Make"));
                System.out.println("Year: " + resultSet.getInt("Year"));
                System.out.println("Color: " + resultSet.getString("Color"));
                System.out.println("Registration Number: " + resultSet.getString("RegistrationNumber"));
                System.out.println("Availability: " + resultSet.getBoolean("Availability"));
                System.out.println("Daily Rate: " + resultSet.getDouble("DailyRate"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } catch (FileNotFoundException e1) {
        	System.out.println(e1.getMessage());
		} catch (ClassNotFoundException e1) {
			System.out.println(e1.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
    }
}
