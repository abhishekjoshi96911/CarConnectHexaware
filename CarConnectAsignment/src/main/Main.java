package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.AdminService;
import dao.CustomerService;
import dao.IAdminService;
import dao.ICustomerService;
import dao.IReservationService;
import dao.IVehicleService;
import dao.ReportGenerator;
import dao.ReservationService;
import dao.VehicleService;
import exception.AuthenticationException;
import exception.InvalidInputException;
import exception.ReservationException;
import exception.VehicleNotFoundException;
import model.Admin;
import model.Customer;
import model.Reservation;
import model.Vehicle;

public class Main {
	
	public static void main(String args[])  {
		int choice;
		do {
			Scanner sc=new Scanner(System.in);
			int choiceAfterAuth;
			
			System.out.println("----------CarConnect----------");
			System.out.println("Enter 1 if you are a Customer");
			System.out.println("Enter 2 if you are an Admin");
			System.out.println("Enter 3 to exit the Application");
			
					
			choice=sc.nextInt();
			switch(choice) {
			case 1:
				try {
				System.out.println("New Customer Enter 1");
				System.out.println("Existing Customer Enter 2");
				int neworold=sc.nextInt();
				switch(neworold) {
				case 1:
					System.out.println("Enter your details ");
					System.out.println("Enter First Name:");
					String firstName=sc.next();
					System.out.println("Enter Last Name:");
					String lastName=sc.next();
					System.out.println("Enter your E-mail:");
					String email=sc.next();
					System.out.println("Enter Phone Number:");
					String phonenumber=sc.next();
					System.out.println("Enter your Address:");
					String address=sc.next();
					System.out.println("Enter a unique Username:");
					String usernamenew=sc.next();
					System.out.println("Enter your password:");
					String passwordnew=sc.next();
					System.out.println("Enter Today's date in yyyy-MM-dd format:");
					String date=sc.next();
					Customer customernew=new Customer(firstName,lastName,email,phonenumber,address,usernamenew,passwordnew,date);
					ICustomerService ics=new CustomerService();
					ics.registerCustomer(customernew);
					break;
				case 2:
					System.out.println("Enter your username:");
					String username=sc.next();
					CustomerService cs=new CustomerService();
					Customer customer=cs.getCustomerByUsername(username);
					System.out.println("Enter your password:");
					String password=sc.next();
					if(customer.authenticate(password)) {
						System.out.println("Hi "+customer.getFirstName()+" "+customer.getLastName()+" welcome to CarConnect");
						System.out.println("Please tell us what would you like to do");
						do {
							System.out.println("Enter");
							System.out.println("1.Update");
							System.out.println("2.Get self details");
							System.out.println("3.delete your account");
							System.out.println("4.Check available vehicles to rent");
							System.out.println("5.Get your reservations");
							System.out.println("6.Reserve a vehicle");
							System.out.println("7.Update your Reservation");
							System.out.println("8.Cancel your Reservation");
							System.out.println("9.Logout");
							choiceAfterAuth=sc.nextInt();
							Methods methods=new Methods();
							switch(choiceAfterAuth){
							case 1:
								
								methods.updateCustomer();
								break;
							case 2:
								
								methods.getSelfDetails(customer.getUserName());
								break;
							case 3:
								methods.deleteAccount(customer.getCustomerId());
								break;
							case 4:
								methods.availableVehicles();
								break;
							case 5:
								methods.getReservationsByCustomer(customer.getCustomerId());
								break;
							case 6:
								System.out.println("Enter Vehicle Id:");
								int vehicleId=sc.nextInt();
								VehicleService vs=new VehicleService();
								Vehicle vehicle=vs.getVehicleById(vehicleId);
								System.out.println("Enter Start Date:");
								String startDate=sc.next();
								System.out.println("Enter End Date:");
								String endDate=sc.next();
								Reservation res=new Reservation(startDate,endDate);
								double totalCost=res.calculateTotalCost(vehicle.getDailyRate());
								System.out.println("Enter status whether Confirm,pending,completed:");
								String status=sc.next();
								Reservation reservation=new Reservation(customer.getCustomerId(),vehicleId,startDate,endDate,totalCost,status);
								methods.reserveAVehicle(reservation);
								break;
							case 7:
								
								System.out.println("Enter reservation Id:");
								int reservationIdForUpdate =sc.nextInt();
								System.out.println("Enter Vehicle Id:");
								int vehicleIdForUpdate=sc.nextInt();
								VehicleService vs1=new VehicleService();
								Vehicle vehicleForUpdate=vs1.getVehicleById(vehicleIdForUpdate);
								System.out.println("Enter Start Date:");
								String startDateForUpdate=sc.next();
								System.out.println("Enter End Date:");
								String endDateForUpdate=sc.next();
								Reservation res1=new Reservation(startDateForUpdate,endDateForUpdate);
								double totalCostForUpdate=res1.calculateTotalCost(vehicleForUpdate.getDailyRate());
								System.out.println("Enter status whether Confirm,pending,completed:");
								String statusForUpdate=sc.next();
								Reservation reservationForUpdate=new Reservation();
								reservationForUpdate.setReservation1(reservationIdForUpdate, vehicleIdForUpdate, startDateForUpdate, endDateForUpdate, totalCostForUpdate, statusForUpdate);
								methods.updateReservation(reservationForUpdate);
								break;
							case 8:
								System.out.println("Enter reservation Id:");
								int reservationIdForDelete =sc.nextInt();
								methods.deleteReservation(reservationIdForDelete);
								break;
								
								
							case 9:
								System.out.println("You have successfully logged out!");
								break;
							default:
								System.out.println("Wrong input please enter a number in valid range of 1-9");
								break;
							}
						}while(choiceAfterAuth!=9);
					}
					else {
						System.out.println("Wrong password please try again!!!");
					}
					break;
				default:
					System.out.println("Wrong inut please give in given range of inputs!!!");
					break;
					
				}
				} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException|AuthenticationException e) {
						System.out.println(e.getMessage());
					
				}
				break;
			
			case 2:
				try {
					System.out.println("Hi Admin ");
					System.out.println("Are you new or do you have Carconnect Admin account");
					System.out.println("If you are new then press 1 if you are already an admin then press 2");
					int neworold=sc.nextInt();
					switch(neworold) {
						case 1:
							System.out.println("Please enter all your details ");
							System.out.println("please enter your first name:");
							String firstName=sc.next();
							System.out.println("please enter your last name:");
							String lastName=sc.next();
							System.out.println("Please enter your email:");
							String email=sc.next();
							System.out.println("Enter your phone number:");
							String phonenumber=sc.next();
							
							System.out.println("Enter your Username Remember!!! username should be unique and it will be used to login to your CarConnect account so please remember Username:");
							String usernamenew=sc.next();
							System.out.println("Enter your password:");
							String passwordnew=sc.next();
							System.out.println("Please enter your Role:");
							String role=sc.next();
							System.out.println("Enter your join date in yyyy-MM-dd format:");
							String joinDate=sc.next();
							Admin adminnew=new Admin(firstName,lastName,email,phonenumber,usernamenew,passwordnew,role,joinDate);
							IAdminService ias=new AdminService();
							ias.registerAdmin(adminnew);
							break;
						
						case 2:
							System.out.println("Please enter your username:");
							String username=sc.next();
							AdminService as=new AdminService();
							Admin admin=as.getAdminByUsername(username);
							System.out.println("Please enter your password:");
							String password=sc.next();
							if(admin.authenticate(password)) {
								System.out.println("Hi "+admin.getFirstName()+" "+admin.getLastName()+" welcome to CarConnect");
								System.out.println("Please tell us what would you like to do");
								do {
									System.out.println("Enter");
									System.out.println("1.Update");
									System.out.println("2.Get self details");
									System.out.println("3.delete your account");
									System.out.println("4.Check available vehicles ");
									System.out.println("5.Add Vehicle");
									System.out.println("6.Update vehicle");
									System.out.println("7.Remove vehicle");
									System.out.println("8.Generate reservation report");
									System.out.println("9.Generate vehicle report");
									System.out.println("10.Logout");
									choiceAfterAuth=sc.nextInt();
									Methods methods=new Methods();
									switch(choiceAfterAuth) {
									case 1:
										methods.updateAdmin();
										break;
									case 2:
										
										methods.getSelfDetailsForAdmin(admin.getUsername());
										break;
									case 3:
										methods.deleteAdminAccount(admin.getAdminId());
										break;
									case 4:
										methods.availableVehicles();
										break;
									case 5:
										System.out.println("Enter vehicle details");
										System.out.println("Enter Vehicle model");
										String model=sc.next();
										System.out.println("Enter Vehicle make");
										String make=sc.next();
										System.out.println("Enter Vehicle year");
										String year=sc.next();
										System.out.println("Enter Vehicle color");
										String color=sc.next();
										System.out.println("Enter Vehicle Registration number:");
										String regnumber=sc.next();
										System.out.println("Enter vehicle availablity:");
										boolean availability=sc.nextBoolean();
										System.out.println("Enter vehicle Daily Rate:");
										double dailyRate=sc.nextDouble();
										Vehicle vehicle=new Vehicle(model,make,year,color,regnumber,availability,dailyRate);
										
										methods.addVehicle(vehicle);
										break;
									case 6:
										System.out.println("Enter every detail to update vehicle");
										System.out.println("Enter vehicle Id:");
										int vehicleid=sc.nextInt();
										System.out.println("Enter Vehicle model");
										String modelup=sc.next();
										System.out.println("Enter Vehicle make");
										String makeup=sc.next();
										System.out.println("Enter Vehicle year");
										String yearup=sc.next();
										System.out.println("Enter Vehicle color");
										String colorup=sc.next();
										System.out.println("Enter vehicle availablity:");
										boolean availabilityup=sc.nextBoolean();
										System.out.println("Enter vehicle Daily Rate:");
										double dailyRateup=sc.nextDouble();
										Vehicle vehicleup=new Vehicle(vehicleid,modelup,makeup,yearup,colorup,availabilityup,dailyRateup);
										methods.updatevehicle(vehicleup);
										break;
									case 7:
										System.out.println("Enter Vehicle Id:");
										int vehicleIdForDelete =sc.nextInt();
										methods.deleteVehicle(vehicleIdForDelete);
										break;
									case 8:
										ReportGenerator rg=new ReportGenerator();
										rg.generateReservationReport();
										break;
									case 9:
										ReportGenerator rgv=new ReportGenerator();
										rgv.generateVehicleReport();
										break;
									case 10:
										System.out.println("Logging out....");
										System.out.println("Successfully logged out!");
										break;
									default:
										System.out.println("Wrong input please enter in given range!!!");
										break;
									}
									
								}while(choiceAfterAuth!=10);
							}
							else {
								System.out.println("Wrong password please try again!!!");
							}
							break;
			
						default:
							System.out.println("Wrong input please enter in given range!!!");
							break;
			}
				
				}catch (ClassNotFoundException | SQLException | IOException|InvalidInputException|AuthenticationException e) {
					System.out.println(e.getMessage());
					
				}
				
				break;	
				
			case 3:
				System.out.println("Exiting for application....");
				System.out.println("You have succesfully exited from application");
				break;
			default:
				System.out.println("Wrong inut please give in given range of inputs!!!");
				break;
			
		}
			
			}while(choice!=3);
		
		}
		
}
class Methods{
	public void updateCustomer() {
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter your details to update");
		System.out.println("please enter your first name:");
		String firstName=sc.next();
		System.out.println("please enter your last name:");
		String lastName=sc.next();
		System.out.println("Please enter your email:");
		String email=sc.next();
		System.out.println("Enter your phone number:");
		String phonenumber=sc.next();
		System.out.println("Please enter your address:");
		String address=sc.next();
		System.out.println("Enter your Username Remember!!! username should be unique and it will be used to login to your CarConnect account so please remember Username:");
		String username=sc.next();
		System.out.println("Enter your password:");
		String passwordnew=sc.next();
		System.out.println("Enter today date in yyyy-MM-dd format:");
		String date=sc.next();
		Customer customer=new Customer(firstName,lastName,email,phonenumber,address,username,passwordnew,date);
		try {
			ICustomerService ics=new CustomerService();
			ics.updateCustomer(customer);
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
		
	}
	public void getSelfDetails(String username) {
		try {
			ICustomerService ics=new CustomerService();
			Customer gotCustomer=ics.getCustomerByUsername(username);
			if(gotCustomer!=null) {
				System.out.println("Your Id : "+gotCustomer.getCustomerId());
				System.out.println("Your Name : "+gotCustomer.getFirstName()+" "+gotCustomer.getLastName());
				System.out.println("Your email: "+gotCustomer.getEmail());
				System.out.println("Your Phone number: "+gotCustomer.getPhoneNumber());
				System.out.println("Your Adress: "+gotCustomer.getAddress());
				System.out.println("Your Username: "+gotCustomer.getUserName());
				System.out.println("Your Password: "+gotCustomer.getPassword());
				System.out.println("Your Registration date: "+gotCustomer.getRegistrationDate());
			}
			else {
				System.out.println("No data for given username");
			}
			
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
		
	}
	public void deleteAccount(int customerId) {
		try {
			ICustomerService ics=new CustomerService();
			ics.deleteCustomer(customerId);
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
	}
	public void availableVehicles() {
		try {
			IVehicleService ivs=new VehicleService();
			List<Vehicle> availableVehicles=ivs.getAvailableVehicles();
			for(Vehicle vehi:availableVehicles) {
				System.out.println("Vehicle Id: "+vehi.getVehicleId());
				System.out.println("Vehicle Model: "+vehi.getModel());
				System.out.println("Vehicle Company: "+vehi.getMake());
				System.out.println("Vehicle Color: "+vehi.getColor());
				System.out.println("Vehicle Daily Rate: "+vehi.getDailyRate());
				
			}
		} catch (ClassNotFoundException | SQLException | IOException|VehicleNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
	}
	public void getReservationsByCustomer(int customerId) {
		try {
			IReservationService irs=new ReservationService();
			List<Reservation> reservations=irs.getReservationsByCustomerId(customerId);
			if(reservations.isEmpty()) {
				System.out.println("There are no reservations");
			}
			else {
				for(Reservation res:reservations) {
					System.out.println("Reservation Id: "+res.getReservationId());
					System.out.println("Vehicle Id: "+res.getVehicleId());
					System.out.println("Start date: "+res.getStartDate());
					System.out.println("End date: "+res.getEndDate());
					System.out.println("Total cost: "+res.getTotalCost());
					System.out.println("Status: "+res.getStatus());
					
				}
			}
			
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			System.out.println(e.getMessage());
		}
		
			
			
		
	}
	public void reserveAVehicle(Reservation reservation) {
		
			try {
				IReservationService irs=new ReservationService();
				irs.createReservation(reservation);
			} catch (ClassNotFoundException | SQLException | IOException|ReservationException|InvalidInputException e) {
				System.out.println(e.getMessage());
			}
			
		
	}
	public void updateReservation(Reservation reservation) {
		
		try {
			IReservationService irs=new ReservationService();
			irs.updateReservation(reservation);
		} catch (ClassNotFoundException | SQLException | IOException|ReservationException|InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	
	}
	public void deleteReservation(int reservationId) {
		try {
			IReservationService irs=new ReservationService();
			irs.cancelReservation(reservationId);
		} catch (ClassNotFoundException | SQLException | IOException|ReservationException|InvalidInputException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * 
	 * 
	 * 
	 * For Admins Only
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public void updateAdmin() {
		Scanner sc=new Scanner(System.in);
		System.out.println("Please enter all your details ");
		System.out.println("please enter your first name:");
		String firstName=sc.next();
		System.out.println("please enter your last name:");
		String lastName=sc.next();
		System.out.println("Please enter your email:");
		String email=sc.next();
		System.out.println("Enter your phone number:");
		String phonenumber=sc.next();
		
		System.out.println("Enter your Username Remember!!! username should be unique and it will be used to login to your CarConnect account so please remember Username:");
		String usernamenew=sc.next();
		System.out.println("Enter your password:");
		String passwordnew=sc.next();
		System.out.println("Please enter your Role:");
		String role=sc.next();
		System.out.println("Enter your join date in yyyy-MM-dd format:");
		String joinDate=sc.next();
		Admin admin=new Admin(firstName,lastName,email,phonenumber,usernamenew,passwordnew,role,joinDate);
		try {
			IAdminService ics=new AdminService();
			ics.updateAdmin(admin);
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
		
	}
	
	public void getSelfDetailsForAdmin(String username) {
		try {
			IAdminService ias=new AdminService();
			Admin gotAdmin=ias.getAdminByUsername(username);
			if(gotAdmin!=null) {
				System.out.println("Your Id : "+gotAdmin.getAdminId());
				System.out.println("Your Name : "+gotAdmin.getFirstName()+" "+gotAdmin.getLastName());
				System.out.println("Your email: "+gotAdmin.getEmail());
				System.out.println("Your Phone number: "+gotAdmin.getPhoneNumber());
				System.out.println("Your Role: "+gotAdmin.getRole());
				System.out.println("Your Username: "+gotAdmin.getUsername());
				System.out.println("Your Password: "+gotAdmin.getPassword());
				System.out.println("Your Joining date: "+gotAdmin.getJoinDate());
			}
			else {
				System.out.println("No data for given username");
			}
			
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
		
	}
	
	public void deleteAdminAccount(int adminId) {
		try {
			IAdminService ias=new AdminService();
			ias.deleteAdmin(adminId);
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
	}
	
	public void addVehicle(Vehicle vehicle) {
		try {
			IVehicleService ivs=new VehicleService();
			ivs.addVehicle(vehicle);
		}catch(SQLException| IOException|ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updatevehicle(Vehicle vehicle) {
		try {
			IVehicleService ivs=new VehicleService();
			ivs.updateVehicle(vehicle);
		}catch(SQLException| IOException|ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	public void deleteVehicle(int vehicleId) {
		try {
			IVehicleService ivs=new VehicleService();
			ivs.removeVehicle(vehicleId);
		} catch (ClassNotFoundException | SQLException | IOException|InvalidInputException e) {
			
			System.out.println(e.getMessage());
		}
	}
	
}

