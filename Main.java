import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        User u = new User(username, password, conn);
        u.saveUser(conn);

        System.out.print("Enter first name: ");
        String firstName = s.nextLine();

        System.out.print("Enter last name: ");
        String lastName = s.nextLine();

        System.out.print("Enter passport number: ");
        String passportNo = s.nextLine();

        System.out.print("Enter phone number: ");
        String phone = s.nextLine();

        Passenger p = new Passenger(u, firstName, lastName, passportNo, phone, conn);
        p.savePassenger(conn);

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
            
            if (role.equalsIgnoreCase("admin")) {
                adminMenu(conn);
            } else if (role.equalsIgnoreCase("passenger")) {
                displayPassengerDetails(rs, conn);
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
            System.out.println("4. Delay Flight");
            System.out.println("5. Cancel Flight");
            System.out.println("6. Edit Flight");
            System.out.println("7. Delete Aircraft");
            System.out.println("8. Delete Flight");
            System.out.println("10. Show All Aircrafts");
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
                	delayFlight(conn);
                	break;
                case 5:
                	cancelFlight(conn);
                	break;
                case 6:
                	editFlight(conn);
                	break;
                case 7:
                	deleteAircraft(conn);
                	break;
                case 8:
                	deleteFlight(conn);
                	break;
                	
                	
                case 10:
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
    
    private static void delayFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight No (-1 to show all flights): ");
    	String flightNo = s.nextLine();
    	
    	if (flightNo.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight No: ");
    		flightNo = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightByNo(flightNo, conn);
    	
    	FlightService.delayFlight(f, conn);
    }
    
    private static void cancelFlight(Connection conn) throws SQLException {
    	
    	System.out.print("Enter Flight No (-1 to show all flights): ");
    	String flightNo = s.nextLine();
    	
    	if (flightNo.equals("-1")) {
    		Airline.printAllFlights(conn);
    		
    		System.out.print("Enter Flight No: ");
    		flightNo = s.nextLine();
    	}
    	
    	Flight f = FlightService.getFlightByNo(flightNo, conn);
    	
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
