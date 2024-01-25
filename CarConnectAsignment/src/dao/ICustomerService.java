package dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import exception.DatabaseConnectionException;
import exception.InvalidInputException;
import model.Customer;

public interface ICustomerService {
	
	public Customer getCustomerById(int customerId) throws FileNotFoundException, ClassNotFoundException, IOException, InvalidInputException ;
	public Customer getCustomerByUsername(String username) throws InvalidInputException, FileNotFoundException, ClassNotFoundException, IOException;
	public void registerCustomer(Customer customer) ;
	public void updateCustomer(Customer customer) throws InvalidInputException, DatabaseConnectionException, FileNotFoundException;
	public void deleteCustomer(int customerId);
}
