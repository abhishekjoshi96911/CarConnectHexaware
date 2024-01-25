package dao;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.DatabaseConnectionException;
import exception.InvalidInputException;
import model.Customer;
import util.DBConnUtil;
//import util.DBPropertyUtil;
import util.Dateutil;

public class CustomerService implements ICustomerService {

	//private static final String fileName="../src/db.properties"; 
   // private static final String URL = DBPropertyUtil.getConnectionString(fileName);
    private Connection connection =  DBConnUtil.getConnection();

    // Constructor to initialize the connection
    public CustomerService() throws SQLException, IOException, ClassNotFoundException {
        this.connection = DBConnUtil.getConnection();
    }

    @Override
    public Customer getCustomerById(int customerId) throws FileNotFoundException, ClassNotFoundException, IOException, InvalidInputException{
        
            String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCustomer(resultSet);
                    }
                    else {
                    	throw new InvalidInputException("Customer not found for customer id "+customerId);
                    }
                }
            }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Customer getCustomerByUsername(String username) throws InvalidInputException, FileNotFoundException, ClassNotFoundException, IOException  {
        
            String sql = "SELECT * FROM Customer WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToCustomer(resultSet);
                    }
                    else {
                    	throw new InvalidInputException("Customer not found by customer username "+username);
                    }
                }
            }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void registerCustomer(Customer customer) {
        try (Connection connection = DBConnUtil.getConnection()) {
        	if(isUsernameExists(connection,customer.getUserName())){
        		System.out.println("Customer with username "+customer.getUserName()+" already exists!");
        	}
        	Dateutil dUtil=new Dateutil(customer.getRegistrationDate());
        	if(!dUtil.validateDateFormat()) {
        		throw new InvalidInputException("Wrong date format !. Give Registration date in yyyy-MM-dd format.");
        	}
        	
        	else {
            String sql = "INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, Address, Username, Password, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, customer.getFirstName());
                statement.setString(2, customer.getLastName());
                statement.setString(3, customer.getEmail());
                statement.setString(4, customer.getPhoneNumber());
                statement.setString(5, customer.getAddress());
                statement.setString(6, customer.getUserName());
                statement.setString(7, customer.getPassword());
                statement.setString(8,customer.getRegistrationDate());
                statement.executeUpdate();
                System.out.println("Customer registered succesfully");
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
        String sql = "SELECT * FROM Customer WHERE Username = ?";
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
    public void updateCustomer(Customer customer) throws InvalidInputException, DatabaseConnectionException, FileNotFoundException {
        try (Connection connection = DBConnUtil.getConnection()) {
            validateCustomerForUpdate(customer); // Validate input data
            Dateutil dUtil=new Dateutil(customer.getRegistrationDate());
        	if(!dUtil.validateDateFormat()) {
        		throw new InvalidInputException("Wrong date format !. Give Registration date in yyyy-MM-dd format.");
        	}
            String sql = "UPDATE Customer SET FirstName=?, LastName=?, Email=?, PhoneNumber=?, Address=?, Password=? WHERE username=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, customer.getFirstName());
                statement.setString(2, customer.getLastName());
                statement.setString(3, customer.getEmail());
                statement.setString(4, customer.getPhoneNumber());
                statement.setString(5, customer.getAddress());
                statement.setString(6, customer.getPassword());
                statement.setString(7, customer.getUserName());
                statement.executeUpdate();
                System.out.println("Updation successful");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error accessing the database.", e);
        } catch (ClassNotFoundException | IOException e1) {
            throw new DatabaseConnectionException("Error accessing the database.", e1);
        }
    }

    // Helper method to validate customer data before update
    private void validateCustomerForUpdate(Customer customer) throws InvalidInputException {
        
        if (customer.getFirstName().isEmpty() || customer.getLastName().isEmpty() || customer.getEmail().isEmpty()
                || customer.getPhoneNumber().isEmpty() || customer.getAddress().isEmpty()||customer.getUserName().isEmpty() ||
                customer.getRegistrationDate().isEmpty()
                || customer.getPassword().isEmpty()) {
            throw new InvalidInputException("All fields must be filled out.");
        }
        if (customer.getPhoneNumber().length() != 10) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }
        try {
			try {
				if (!isCustomerExistsByUsername(customer.getUserName())) {
				    throw new InvalidInputException("Customer with Username " + customer.getUserName() + " not found.");
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
    private boolean isCustomerExistsByUsername(String username) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Customer WHERE username = ?";
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
    private boolean isCustomerExistsByUserId(int customerId) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException {
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
    public void deleteCustomer(int customerId) {
        try (Connection connection = DBConnUtil.getConnection()) {
        	
        	if (!isCustomerExistsByUserId(customerId)) {
			    throw new InvalidInputException("Customer with CustomerID " + customerId + " not found.");
			}
            String sql = "DELETE FROM Customer WHERE CustomerID=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                statement.executeUpdate();
                System.out.println("Customer deleted succesfully");
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
    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("CustomerID"));
        customer.setFirstName(resultSet.getString("FirstName"));
        customer.setLastName(resultSet.getString("LastName"));
        customer.setEmail(resultSet.getString("Email"));
        customer.setPhoneNumber(resultSet.getString("PhoneNumber"));
        customer.setAddress(resultSet.getString("Address"));
        customer.setUserName(resultSet.getString("Username"));
        customer.setPassword(resultSet.getString("Password"));
        customer.setRegistrationDate(resultSet.getString("RegistrationDate"));
        return customer;
    }
}
