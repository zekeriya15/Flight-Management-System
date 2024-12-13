import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner s = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        DBConnection dbConn = new DBConnection();
        boolean running = true;

        while (running) {
            System.out.println("\n--------- Garuda Indonesia Airline ----------");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("0. Quit");
            System.out.print("\nChoose an option: ");

            int option = s.nextInt();
            s.nextLine();

            switch (option) {
                case 1:
                    login(dbConn);
                    break;
                case 2:
                    createNewAccount(dbConn);
                    break;
                case 0:
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
    
    private static void createNewAccount(DBConnection dbConn) throws SQLException {
        Connection conn = dbConn.getConnection();
        System.out.println();

        System.out.print("Enter new username: ");
        String username = s.nextLine();

        System.out.print("Enter new password: ");
        String password = s.nextLine();

        String userId = UserService.generateId(conn);
        
        User u = new User(userId, username, password);
        UserService.saveUser(u, conn);

        System.out.print("Enter first name: ");
        String firstName = s.nextLine();

        System.out.print("Enter last name: ");
        String lastName = s.nextLine();

        System.out.print("Enter passport number: ");
        String passportNo = s.nextLine();

        System.out.print("Enter phone number: ");
        String phone = s.nextLine();

        String passengerId = PassengerService.generateId(conn);
        
        Passenger p = new Passenger(passengerId, u, firstName, lastName, passportNo, phone);
        PassengerService.savePassenger(p, conn);

        System.out.println("\nNew user created successfully.");
    }

    private static void login(DBConnection dbConn) throws SQLException {
        Connection conn = dbConn.getConnection();
        System.out.println();

        System.out.print("Enter username: ");
        String username = s.nextLine();

        System.out.print("Enter password: ");
        String password = s.nextLine();

        String query = "SELECT * FROM users u JOIN passengers p ON u.user_id = p.user_id WHERE username = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String role = rs.getString("role");
            System.out.println("\nWelcome, " + username + " (" + role + ")");
            
            String passengerId = rs.getString("passenger_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String passportNo = rs.getString("passport_no");
            String phone = rs.getString("phone");
            String userId = rs.getString("user_id");
            
            User u = UserService.getUserById(userId, conn);
            
            Passenger p = new Passenger(passengerId, u, firstName, lastName, passportNo, phone);
            
            ArrayList<Luggage> luggages = LuggageService.getLuggagesByPassengerId(passengerId, conn);
            p.setLuggages(luggages);
            
            ArrayList<Booking> bookings = PassengerService.getBookings(p, conn);
            p.setBookings(bookings);
            
            if (role.equalsIgnoreCase("admin")) {
                adminMenu(conn);
            } else if (role.equalsIgnoreCase("passenger")) {
//               displayPassengerDetails(rs, conn);
            	passengerMenu(p, conn);
            	
            }
            
        } else {
            System.out.println("\nInvalid username or password. Please try again.");
        }
    }

    private static void adminMenu(Connection conn) throws SQLException {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\n------ Admin Menu ------");
            System.out.println("1. Create Aircraft");
            System.out.println("2. Edit Aircraft");
            System.out.println("3. Create Flight");
            System.out.println("4. Create New Flight Schedule");
            System.out.println("5. Delay Flight");
            System.out.println("6. Cancel Flight");
            System.out.println("7. Edit Flight");
            System.out.println("8. Delete Aircraft");
            System.out.println("9. Delete Flight");
            System.out.println("10. Find Flights by Route");
            System.out.println("11. Show All Aircrafts");
            System.out.println("20. Show All Flights");
            System.out.println("0. Logout");
            System.out.print("\nChoose an option: ");

            int adminOption = s.nextInt();
            s.nextLine();
            System.out.println();
            
            switch (adminOption) {
                case 1:
                    createAircraft(conn);
                    break;
                case 2:
                	editAircraft(conn);
                	break;
                case 3:
                	createFlight(conn);
                	break;
                case 4:
                	createNewSchedule(conn);
                	break;
                case 5:
                	delayFlight(conn);
                	break;
                case 6:
                	cancelFlight(conn);
                	break;
                case 7:
                	editFlight(conn);
                	break;
                case 8:
                	deleteAircraft(conn);
                	break;
                case 9:
                	deleteFlight(conn);
                	break;
                case 10:
                	findFlightsByRoute(conn);
                	break;
                	
                	
                case 11:
                	Airline.printAllAircrafts(conn);
                	break;
                case 20:
                	Airline.printAllFlights(conn);
                	break;
                case 0:
                    adminRunning = false;
                    System.out.println("\nLogged out successfully.");
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    private static void createAircraft(Connection conn) throws SQLException {

        System.out.print("Enter model: ");
        String model = s.nextLine();

        System.out.print("Enter capacity: ");
        int capacity = s.nextInt();
        s.nextLine(); // Consume the newline

        // Generate unique ID for the aircraft
        String aircraftId = AircraftService.generateId(conn);

        // Create and save the aircraft
        Aircraft newAircraft = new Aircraft(aircraftId, model, capacity);
        AircraftService.saveAircraft(newAircraft, conn);

        System.out.println("\nAircraft created successfully");
    }
    
    private static void editAircraft(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Aircraft Id (-1 to show all aircrafts): ");
    	String aircraftId = s.nextLine();
    	
    	if (aircraftId.equals("-1")) {
    		Airline.printAllAircrafts(conn);
    		
    		System.out.print("Enter Aircraft Id: ");
    		aircraftId = s.nextLine();
    	}
    	
    	Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
    	
    	System.out.print("Enter model (-1 to keep old value): ");
    	String model = s.nextLine();
    	if (model.equals("-1")) {
    		model = plane.getModel();
    	}
    	
    	System.out.print("Enter capacity (-1 to keep old value): ");
    	int capacity = s.nextInt();
    	if (capacity == -1) {
    		capacity = plane.getCapacity();
    	}
    	
    	plane.setModel(model);
    	plane.setCapacity(capacity);;
    	
    	AircraftService.editAircraft(plane, conn);
    	
    	System.out.println("\nAircraft edited successfully\n");
    	
    }
    
    private static void createFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Aircraft Id (-1 to show all aircrafts): ");
    	String aircraftId = s.nextLine();
    	
    	if (aircraftId.equals("-1")) {
    		Airline.printAllAircrafts(conn);
    		
    		System.out.print("Enter Aircraft Id: ");
    		aircraftId = s.nextLine();
    	}
    	
    	Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
    	
    	System.out.print("Enter Flight No: ");
    	String flightNo = s.nextLine();
    	
    	System.out.print("Enter origin: ");
    	String origin = s.nextLine();
    	
    	System.out.print("Enter destination: ");
    	String destination = s.nextLine();
    	
    	System.out.print("Enter departure time (yyyy-MM-dd HH:mm): ");
    	String departureTime = s.nextLine();
    	
    	System.out.print("Enter arrival time (yyyy-MM-dd HH:mm): ");
    	String arrivalTime = s.nextLine();
    	
    	String flightId = FlightService.generateId(conn);
    	
    	Flight f = new Flight(flightId, flightNo, plane, origin, destination);
    	f.setDepartureTime(departureTime);
    	f.setArrivalTime(arrivalTime);
    	
    	FlightService.saveFlight(f, conn);
    	
    	System.out.println("\nFlight created successfully\n");
    }
    
    private static void createNewSchedule(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight No (-1 to show all flights): ");
    	String flightNo = s.nextLine();
    	
    	if (flightNo.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight No: ");
    		flightNo = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightByNo(flightNo, conn);
    	
    	System.out.print("Enter departure time (yyyy-MM-dd HH:mm): ");
    	String departureTime = s.nextLine();
    	
    	System.out.print("Enter arrival time (yyyy-MM-dd HH:mm): ");
    	String arrivalTime = s.nextLine();
    	
    	String flightId = FlightService.generateId(conn);
    	
    	flightNo = f.getFlightNo();
    	Aircraft plane = f.getAircraft();
    	String origin = f.getOrigin();
    	String destination = f.getDestination();
    	
    	Flight newSchedule = new Flight(flightId, flightNo, plane, origin, destination);
    	newSchedule.setDepartureTime(departureTime);
    	newSchedule.setArrivalTime(arrivalTime);
    	
    	FlightService.saveFlight(newSchedule, conn);
    	
    	System.out.println("\nNew scheduled added for flight " + flightNo.toUpperCase() + "\n");
    }
    
    private static void delayFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight Id (-1 to show all flights): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightById(flightId, conn);
    	
    	FlightService.delayFlight(f, conn);
    }
    
    private static void cancelFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight Id (-1 to show all flights): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightById(flightId, conn);
    	
    	FlightService.cancelFlight(f, conn);
    }
    
    private static void editFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight Id (-1 to show all flights): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightById(flightId, conn);
    	
    	System.out.print("Enter Flight No (-1 to keep old value): ");
    	String flightNo = s.nextLine();
    	if (flightNo.equals("-1")) {
    		flightNo = f.getFlightNo();
    	}
    	
    	System.out.print("Enter origin (-1 to keep old value): ");
    	String origin = s.nextLine();
    	if (origin.equals("-1")) {
    		origin = f.getOrigin();
    	}
    	
    	System.out.print("Enter destination (-1 to keep old value): ");
    	String destination = s.nextLine();
    	if (destination.equals("-1")) {
    		destination = f.getDestination();
    	}
    	
    	System.out.print("Enter departure time (yyyy-MM-dd HH:mm) (-1 to keep old value): ");
    	String departureTime = s.nextLine();
    	if (departureTime.equals("-1")) {
    		departureTime = f.getStrDepartureTime();
    	}
    	
    	System.out.print("Enter arrival time (yyyy-MM-dd HH:mm) (-1 to keep old value): ");
    	String arrivalTime = s.nextLine();
    	if (arrivalTime.equals("-1")) {
    		arrivalTime = f.getStrArrivalTime();
    	}
    	
    	f.setFlightNo(flightNo);
    	f.setOrigin(origin);
    	f.setDestination(destination);
    	f.setDepartureTime(departureTime);
    	f.setArrivalTime(arrivalTime);
    	
    	FlightService.editFlight(f, conn);
    	
    	System.out.println("\nFlight edited successfully\n");
    }
    
    private static void deleteAircraft(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Aircraft Id (-1 to show all aircrafts): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAllAircrafts(conn);
    		
    		System.out.print("Enter Aircraft Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Aircraft plane = AircraftService.getAircraftById(flightId, conn);
    	
    	System.out.print("Are you sure you want to delete this aircraft? (Y/N) ");
    	char yN = s.next().charAt(0);
    	
    	if (yN == 'y' || yN == 'Y') {
    		AircraftService.deleteAicraft(plane, conn);
        	
        	System.out.println("\nAircraft deleted successfully\n");
    	} 
    	
    	return;
    }
    
    private static void deleteFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight Id (-1 to show all flights): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightById(flightId, conn);
    	
    	System.out.print("Do you want to delete this flight? (Y/N) ");
    	char yN = s.next().charAt(0);
    	
    	if (yN == 'y' || yN == 'Y') {
    		FlightService.deleteFlight(f, conn);
    		
    		System.out.println("\nFlight deleted successfully\n");
    	}
    	
    	return;
    }
    
    
    private static void findFlightsByRoute(Connection conn) throws SQLException {
    	
    	System.out.print("Enter origin: ");
    	String origin = s.nextLine();
    	
    	System.out.print("Enter destination: ");
    	String destination = s.nextLine();
    	
    	Airline.findFlightsByRoute(origin, destination, conn);
    }
    
    private static void passengerMenu(Passenger p, Connection conn) throws SQLException {
    	boolean psgRunning = true;
    	
    	while (psgRunning) {
    		System.out.println("\n------------------------------------\n");
        	System.out.print(p.getFirstName() + "\t" + p.getLastName() + "\t" + p.getPassportNo() + "\t" + p.getPhone());
        	System.out.println("\n------------------------------------\n");
        	
        	System.out.println("1. Show All Available Flights");
        	System.out.println("2. Find Flights by Route");
        	System.out.println("3. Book a Flight");
        	System.out.println("4. Check-In");
        	System.out.println("5. Show Your Luggages");
        	System.out.println("6. Cancel Booking");
        	System.out.println("9. Show Your Bookings");
        	System.out.println("10. Edit profile");
        	System.out.println("0. Logout");
        	
        	System.out.println();
        	
        	System.out.print("Choose an option: ");
        	int option = s.nextInt(); s.nextLine();
        	
        	System.out.println();
        	
        	switch (option) {
        		case 1:
        			Airline.printAvailableFlights(conn);
        			break;
        		case 2:
        			findFlightsByRoute(conn);
        			break;
        		case 3:
        			bookFlight(p, conn);
        			break;
        		case 4:
        			checkIn(p, conn);
        			break;
        		case 5:
        			showLuggages(p, conn);
        			break;
        		case 6:
        			cancelBooking(p, conn);
        			break;
        			
        			
        		case 9:
        			PassengerService.printBooking(p, conn);
        			break;
        		case 10:
        			editProfile(p, conn);
        			break;
        			
        			
        		case 0:
        			psgRunning = false;
        			System.out.println("\nLogout successfully\n");
        			break;
        	}
    	}
    	
    }
    
    
    private static void bookFlight(Passenger p, Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight Id (-1 to show all available flights): ");
    	String flightId = s.nextLine();
    	
    	if (flightId.equals("-1")) {
    		Airline.printAvailableFlights(conn);
    		
    		System.out.print("Enter Flight Id: ");
    		flightId = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightById(flightId, conn);
    	
    	String bookingClass = null;
    	
    	if (f.getSeatAvailable() > 0 && !f.getStatus().equalsIgnoreCase("Canceled")) {
    		do {
        		System.out.print("\nChoose class\n1. First Class\n2. Business Class\n3. Economy Class\nOption: ");
        		int option = s.nextInt(); s.nextLine();
        		
        		switch (option) {
        			case 1:
        				bookingClass = "First";
        				break;
        			case 2:
        				bookingClass = "Business";
        				break;
        			case 3:
        				bookingClass = "Economy";
        				break;
        			default:
        				System.out.println("\nInvalid option\n");
        				break;
        		}
        		
        	} while (bookingClass == null);
    		
    		PassengerService.addBooking(p, f, bookingClass, conn);
    		
    		System.out.println("\nYou have successfully booked flight from " + f.getOrigin() + " to " + f.getDestination() + 
    				" with " + bookingClass + " class scheduled to fly at " + f.getStrDepartureTime());
    		
    		return;
    	}
    		
    	System.out.println("\nThe flight has no seat left avilable\n");
    }
    
    private static void checkIn(Passenger p, Connection conn) throws SQLException {
    	
    	System.out.print("Enter Booking Id (-1 to show all of your bookings): ");
    	String bookingId = s.nextLine();
    	
    	if (bookingId.equals("-1")) {
    		PassengerService.printBooking(p, conn);
    		
    		System.out.print("Enter Booking Id: ");
    		bookingId = s.nextLine();
    	}
    	
    	Booking b = BookingService.getBookingById(bookingId, conn);
    	
    	if (!b.isCheckedIn()) {
    		if (!FlightService.isCanceled(bookingId, conn)) {
    			
    			System.out.print("Do you want to add luggage? (Y/N): ");
    	        char option = s.next().toUpperCase().charAt(0);
    	        
    	        while (option == 'Y') {
    	            System.out.print("Enter luggage type (ex. suitcase, carry-on): ");
    	            s.nextLine(); // Clear the buffer
    	            String type = s.nextLine();
    	            
    	            double weight = 0;
    	            boolean validWeight = false;
    	            
    	            while (!validWeight) {
    	                System.out.print("Enter weight (in kg): ");
    	                
    	                if (s.hasNextDouble()) {
    	                    weight = s.nextDouble();
    	                    s.nextLine(); // Clear the buffer
    	                    if (weight > 0) {
    	                        validWeight = true;
    	                    } else {
    	                        System.out.println("Weight must be a positive value.");
    	                    }
    	                } else {
    	                    System.out.println("Invalid weight input. Please enter a numeric value.");
    	                    s.nextLine(); // Clear the invalid input
    	                }
    	            }
    	            
    	            b.addLuggage(p, new Luggage(type, weight, p));
    	            
    	            System.out.print("Continue adding luggage? (Y/N): ");
    	            option = s.next().toUpperCase().charAt(0); // Normalize input to uppercase
    	        }
    	        
    	        PassengerService.checkIn(b, p, conn);
    	        System.out.println("\nYou have checked in successfully.\n");
    	        
    	        return;
    		}
    			
    		System.out.println("\nYou can't check-in on a canceled flight\n");
    		return;
    	} 
    	
    	System.out.println("\nYou have already checked in for this flight\n");
    	return;
    }
    
    private static void showLuggages(Passenger p, Connection conn) throws SQLException {
    	
    	System.out.print("Enter Booking Id (-1 to show all of your bookings): ");
    	String bookingId = s.nextLine();
    	
    	if (bookingId.equals("-1")) {
    		PassengerService.printBooking(p, conn);
    		
    		System.out.print("Enter Booking Id: ");
    		bookingId = s.nextLine();
    	}
    }
    
    private static void cancelBooking(Passenger p, Connection conn) throws SQLException {
    	
    	System.out.print("Enter Booking Id (-1 to show all of your bookings): ");
    	String bookingId = s.nextLine();
    	
    	if (bookingId.equals("-1")) {
    		PassengerService.printBooking(p, conn);
    		
    		System.out.print("Enter Booking Id: ");
    		bookingId = s.nextLine();
    	}
    	
    	Booking b = BookingService.getBookingById(bookingId, conn);
    	Flight f = FlightService.getFlightByBookingId(bookingId, conn);
    	
    	if (!b.isCheckedIn()) {
    		BookingService.deleteBooking(b, conn);
    		
    		System.out.print("\nYour booking for flight " + f.getFlightNo() + " " + f.getOrigin() + "-" + f.getDestination() +
    				" has been canceled successfully\n");
    		return;
    	}
    	
    	System.out.println("\nYou can't cancel a booking that you have already checked-in\n");
    	
    }
    
    
    private static void editProfile(Passenger p, Connection conn) throws SQLException {
    	
    	System.out.print("Enter first name (-1 to keep old value): ");
    	String firstName = s.nextLine();
    	if (firstName.equals("-1")) {
    		firstName = p.getFirstName();
    	}
    	
    	System.out.print("Enter last name (-1 to keep old value): ");
    	String lastName = s.nextLine();
    	if (lastName.equals("-1")) {
    		lastName = p.getLastName();
    	}
    	
    	System.out.print("Enter passport no (-1 to keep old value): ");
    	String passportNo = s.nextLine();
    	if (passportNo.equals("-1")) {
    		passportNo = p.getPassportNo();
    	}
    	
    	System.out.print("Enter phone (-1 to keep old value): ");
    	String phone = s.nextLine();
    	if (phone.equals("-1")) {
    		phone = p.getPhone();
    	}
    	
    	p.setFirstName(firstName);
    	p.setLastName(lastName);
    	p.setPassportNo(passportNo);
    	p.setPhone(phone);
    	
    	PassengerService.editPassenger(p, conn);
    	
    	System.out.println("\nYour profile edited successfully\n");
    }

    private static void displayPassengerDetails(ResultSet rs, Connection conn) throws SQLException {
        System.out.println("\n====== Passenger Details ======");
        System.out.println("Passenger ID: " + rs.getString("passenger_id"));
        System.out.println("First Name: " + rs.getString("first_name"));
        System.out.println("Last Name: " + rs.getString("last_name"));
        System.out.println("Passport Number: " + rs.getString("passport_no"));
        System.out.println("Phone: " + rs.getString("phone"));

        System.out.println("\nPress Enter to log out...");
        s.nextLine();
    }

    
}
