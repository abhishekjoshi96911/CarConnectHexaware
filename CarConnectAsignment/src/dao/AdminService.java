package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.DatabaseConnectionException;
import exception.InvalidInputException;
import model.Admin;
import util.DBConnUtil;
//import util.DBPropertyUtil;
import util.Dateutil;

public class AdminService implements IAdminService {
	//private static final String fileName="../src/db.properties"; 
   // private static final String URL = DBPropertyUtil.getConnectionString(fileName);
    private Connection connection;

    // Constructor to initialize the connection
    public AdminService() throws SQLException, IOException, ClassNotFoundException {
        this.connection = DBConnUtil.getConnection();
    }

    @Override
    public Admin getAdminById(int AdminId) throws FileNotFoundException, ClassNotFoundException, IOException, InvalidInputException{
        
            String sql = "SELECT * FROM Admin WHERE AdminID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, AdminId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAdmin(resultSet);
                    }
                    else {
                    	throw new InvalidInputException("Admin not found for Admin Id "+AdminId);
                    }
                }
            }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Admin getAdminByUsername(String username) throws InvalidInputException, FileNotFoundException, ClassNotFoundException, IOException  {
        
            String sql = "SELECT * FROM Admin WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAdmin(resultSet);
                    }
                    else {
                    	throw new InvalidInputException("Admin not found by Admin username "+username);
                    }
                }
            }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void registerAdmin(Admin admin) {
        try (Connection connection = DBConnUtil.getConnection()) {
        	if(isUsernameExists(connection,admin.getUsername())){
        		System.out.println("Customer with username "+admin.getUsername()+" already exists!");
        	}
        	Dateutil dUtil=new Dateutil(admin.getJoinDate());
        	if(!dUtil.validateDateFormat()) {
        		throw new InvalidInputException("Wrong date format !. Give join date in yyyy-MM-dd format.");
        	}
        	else {
            String sql = "INSERT INTO Admin (FirstName, LastName, Email, PhoneNumber, Username, Password, Role, JoinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, admin.getFirstName());
                statement.setString(2, admin.getLastName());
                statement.setString(3, admin.getEmail());
                statement.setString(4, admin.getPhoneNumber());
                statement.setString(5, admin.getUsername());
                statement.setString(6, admin.getPassword());
                statement.setString(7, admin.getRole());
                statement.setString(8, admin.getJoinDate());
                statement.executeUpdate();
                System.out.println("Admin registered succesfully");
            }
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
    private boolean isUsernameExists(Connection connection, String username) throws SQLException {
        String sql = "SELECT * FROM Admin WHERE Username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateAdmin(Admin admin) throws InvalidInputException, DatabaseConnectionException, FileNotFoundException {
        try (Connection connection = DBConnUtil.getConnection()) {
            validateAdminForUpdate(admin); // Validate input data
            Dateutil dUtil=new Dateutil(admin.getJoinDate());
        	if(!dUtil.validateDateFormat()) {
        		throw new InvalidInputException("Wrong date format !. Give join date in yyyy-MM-dd format.");
        	}
            String sql = "UPDATE Admin SET FirstName=?, LastName=?, Email=?, PhoneNumber=?, Role=?, Password=?, JoinDate=? WHERE Username=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, admin.getFirstName());
                statement.setString(2, admin.getLastName());
                statement.setString(3, admin.getEmail());
                statement.setString(4, admin.getPhoneNumber());
                statement.setString(5, admin.getRole());
                statement.setString(6, admin.getPassword());
                statement.setString(7, admin.getJoinDate());
                statement.setString(8, admin.getUsername());
                statement.executeUpdate();
                System.out.println("Updation successful");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error accessing the database.", e);
        } catch (ClassNotFoundException | IOException e1) {
            throw new DatabaseConnectionException("Error accessing the database.", e1);
        }
    }

    private void validateAdminForUpdate(Admin admin) throws InvalidInputException {
        
        if (admin.getFirstName().isEmpty() || admin.getLastName().isEmpty() || admin.getEmail().isEmpty()
                || admin.getPhoneNumber().isEmpty() || admin.getRole().isEmpty() || admin.getUsername().isEmpty() ||
                admin.getJoinDate().isEmpty()|| admin.getPassword().isEmpty()) {
            throw new InvalidInputException("All fields must be filled out.");
        }
        if (admin.getPhoneNumber().length() != 10) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }
        try {
			try {
				if (!isAdminExistsForUpdate(admin.getUsername())) {
				    throw new InvalidInputException("Admin with Admin Username " + admin.getUsername() + " not found.");
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
        // Add more validation rules as needed
    }
   
    private boolean isAdminExistsForUpdate(String username) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Admin WHERE username = ?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   return true;
                }
            }
        } 
        return false;
        
    }
    private boolean isAdminExists(int adminId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Admin WHERE AdminID = ?";
        try (Connection connection = DBConnUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, adminId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                   return true;
                }
            }
        } 
        return false;
        
    }

    @Override
    public void deleteAdmin(int adminId) {
        try (Connection connection = DBConnUtil.getConnection()) {
        	
        	if (!isAdminExists(adminId)) {
			    throw new InvalidInputException("Admin with AdminId " + adminId + " not found.");
			}
            String sql = "DELETE FROM Admin WHERE AdminID=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, adminId);
                statement.executeUpdate();
                System.out.println("Admin deleted succesfully");
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

    // Helper method to map ResultSet to Customer entity
    private Admin mapResultSetToAdmin(ResultSet resultSet) throws SQLException {
        Admin admin=new Admin();
        admin.setAdminId(resultSet.getInt("AdminID"));
        admin.setFirstName(resultSet.getString("FirstName"));
        admin.setLastName(resultSet.getString("LastName"));
        admin.setEmail(resultSet.getString("Email"));
        admin.setPhoneNumber(resultSet.getString("PhoneNumber"));
        
        admin.setUsername(resultSet.getString("Username"));
        admin.setPassword(resultSet.getString("Password"));
        admin.setRole(resultSet.getString("Role"));
        admin.setJoinDate(resultSet.getString("JoinDate"));
        return admin;
    }
}
