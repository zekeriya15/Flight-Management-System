import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Airline {

	public static void printBriefInfo(Connection conn) throws SQLException {
		String aircraftsQuery = "SELECT COUNT(*) FROM aircrafts";
	    String flightsQuery = "SELECT COUNT(*) FROM flights";
	    String bookingsQuery = "SELECT COUNT(*) FROM bookings";
	    String passengersQuery = "SELECT COUNT(*) FROM passengers";
	    
	    try (PreparedStatement ps = conn.prepareStatement(aircraftsQuery);
	         PreparedStatement ps2 = conn.prepareStatement(flightsQuery);
	         PreparedStatement ps3 = conn.prepareStatement(bookingsQuery);
	         PreparedStatement ps4 = conn.prepareStatement(passengersQuery)) {
	        
	        try (ResultSet rs1 = ps.executeQuery();
	             ResultSet rs2 = ps2.executeQuery();
	             ResultSet rs3 = ps3.executeQuery();
	             ResultSet rs4 = ps4.executeQuery()) {
	        	
	        	int totalAircrafts = 0;
	        	int totalFlights = 0;
	        	int totalBookings = 0;
	        	int totalPassengers = 0;

	            if (rs1.next()) {
	            	totalAircrafts = rs1.getInt(1);
	            }

	            if (rs2.next()) {
	            	totalFlights = rs2.getInt(1);
	            }

	            if (rs3.next()) {
	            	totalBookings = rs3.getInt(1);
	            }

	            if (rs4.next()) {
	            	totalPassengers = rs4.getInt(1);
	            }
	            
	            System.out.println("\n-----------------------------------------------------------------");
	            System.out.println("Total Aircrafts\tTotal Flights\tTotal Bookings\tTotal Passengers");
	            System.out.println(totalAircrafts + "\t\t" + totalFlights + "\t\t" + totalBookings + "\t\t" + totalPassengers);
	            System.out.println("-----------------------------------------------------------------\n");

	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	private static ArrayList<Aircraft> getAircrafts(Connection conn) throws SQLException {
	    ArrayList<Aircraft> aircrafts = new ArrayList<>();
	    
	    String query = "SELECT a.aircraft_id, model, capacity, COUNT(flight_id) as num_of_flight FROM aircrafts a LEFT JOIN flights f ON a.aircraft_id = f.aircraft_id GROUP BY a.aircraft_id";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	        	String aircraftId = rs.getString("aircraft_id");
	            String model = rs.getString("model");
	            int capacity = rs.getInt("capacity");
	            int numOfFlight = rs.getInt("num_of_flight");
	            
	            Aircraft plane = new Aircraft(aircraftId, model, capacity);
	            
	            aircrafts.add(plane);
	        }
	    }
	    
	    return aircrafts;
	}

	
	public static void printAllAircrafts(Connection conn) throws SQLException {
		String query = "SELECT a.aircraft_id, model, capacity, COUNT(flight_id) as num_of_flight FROM aircrafts a LEFT JOIN flights f ON a.aircraft_id = f.aircraft_id GROUP BY a.aircraft_id";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {
	        
	    	System.out.println("\n----------------------------------------------------------------");
            System.out.println("Aircraft Id\tModel\t\tCapacity\tNum of Flights");
	        while (rs.next()) {
	        	String aircraftId = rs.getString("aircraft_id");
	            String model = rs.getString("model");
	            int capacity = rs.getInt("capacity");
	            int numOfFlight = rs.getInt("num_of_flight");
	            
	            
	            System.out.println(aircraftId + "\t\t" + model + "\t" + capacity + "\t\t" + numOfFlight);
	        }
            System.out.println("\n----------------------------------------------------------------");

	    }
	}
	
	
	
	private static ArrayList<Flight> getFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights";

		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				String flightNo = rs.getString("flight_no");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = FlightService.getBookingsByFlightId(flightId, conn);

				
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				f.setBooking(bookings);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printAllFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlights(conn);
		Collections.sort(flights);
		
		if (!flights.isEmpty()) {
			System.out.println("\n----------------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-12s %-15s %-15s %-25s %-20s %-20s %-15s\n", 
	                          "Flight Id", "Flight No", "Status", "Aircraft", 
	                          "Route", "Departure Time", "Arrival Time", "Seat Available");			
			for (Flight f : flights) {
				f.print();
			}
			
			System.out.println("----------------------------------------------------------------------------------------------------\n");

		} else {
			System.out.println("\nThere are no any flights available\n");
		}
	}
	
	private static ArrayList<Flight> getAvailableFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights WHERE status != 'Cancelled'";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				String flightNo = rs.getString("flight_no");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = FlightService.getBookingsByFlightId(flightId, conn);

				
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				f.setBooking(bookings);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printAvailableFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = getAvailableFlights(conn);
		Collections.sort(flights);
		
		if (!flights.isEmpty()) {
			System.out.println("\n----------------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-12s %-15s %-15s %-25s %-20s %-20s %-15s\n", 
	                          "Flight Id", "Flight No", "Status", "Aircraft", 
	                          "Route", "Departure Time", "Arrival Time", "Seat Available");
			for (Flight f : flights) {
				f.print();
			}
			System.out.println("----------------------------------------------------------------------------------------------------\n");

		} else {
			System.out.println("\nThere are no available flights\n");
		}
	}

	private static ArrayList<Flight> getFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights WHERE origin = ? AND destination = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, origin);
			ps.setString(2, destination);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				String flightNo = rs.getString("flight_no");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = FlightService.getBookingsByFlightId(flightId, conn);
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arivalTime);
				f.setBooking(bookings);

				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void findFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlightsByRoute(origin, destination, conn);
		Collections.sort(flights);
		
		if (!flights.isEmpty()) {
			System.out.println("\n----------------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-12s %-15s %-15s %-25s %-20s %-20s %-15s\n", 
	                          "Flight Id", "Flight No", "Status", "Aircraft", 
	                          "Route", "Departure Time", "Arrival Time", "Seat Available");
			for (Flight f : flights) {
				f.print();
			}
			System.out.println("----------------------------------------------------------------------------------------------------\n");

		} else {
			System.out.println("\nThere are no flights from " + origin + " to " + destination + "\n");
		}
		
	}
	
	
	private static ArrayList<Flight> getAvailableFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND status = 'On Time'";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, origin);
			ps.setString(2, destination);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				String flightNo = rs.getString("flight_no");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = FlightService.getBookingsByFlightId(flightId, conn);
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arivalTime);
				f.setBooking(bookings);

				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void findAvailableFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = getAvailableFlightsByRoute(origin, destination, conn);
		Collections.sort(flights);
		
		if (!flights.isEmpty()) {
			System.out.println("\n----------------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-12s %-15s %-15s %-25s %-20s %-20s %-15s\n", 
	                          "Flight Id", "Flight No", "Status", "Aircraft", 
	                          "Route", "Departure Time", "Arrival Time", "Seat Available");
			for (Flight f : flights) {
				f.print();
			}
			System.out.println("----------------------------------------------------------------------------------------------------\n");

		} else {
			System.out.println("\nThere are no flights from " + origin + " to " + destination + "\n");
		}
		
	}
	
	public static void getCheckedInPassengers(Flight f, Connection conn) throws SQLException {
//		ArrayList<Passenger> passengers = new ArrayList<>();
		
		String query = "SELECT * FROM passengers p JOIN bookings b ON p.passenger_id = b.passenger_id WHERE is_checked_in = 1 AND flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, f.getFlightId());
			
			ResultSet rs = ps.executeQuery();
			
			System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Passenger Id\tBooking Id\tFirst Name\tLast Name\tPassport No\tPhone\t\tClass\t\tNum of Luggage");
			while (rs.next()) {
				String passengerId = rs.getString("passenger_id");
				String bookingId = rs.getString("booking_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String passportNo = rs.getString("passport_no");
				String phone = rs.getString("phone");
				String bookingClass = rs.getString("booking_class");
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				System.out.println(passengerId + "\t\t" + bookingId + "\t\t" + firstName + "\t\t" + lastName + "\t\t" + passportNo + "\t" +
							phone + "\t" + bookingClass + "\t\t" + numOfLuggage);
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------\n");

		}
	}
	
	
	public static void getPassengersByFlightId(String flightId, Connection conn) throws SQLException {
		
		String query = "SELECT * from passengers p JOIN bookings b ON p.passenger_id = b.passenger_id WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, flightId);
			
			ResultSet rs = ps.executeQuery();
			
			System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Passenger Id\tBooking Id\tFirst Name\tLast Name\tPassport No\tPhone\t\tClass\t\tChecked-in?\tNum of Luggage");
			while (rs.next()) {
				String passengerId = rs.getString("passenger_id");
				String bookingId = rs.getString("booking_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String passportNo = rs.getString("passport_no");
				String phone = rs.getString("phone");
				String bookingClass = rs.getString("booking_class");
				boolean status = rs.getInt("is_checked_in") == 1;
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				System.out.println(passengerId + "\t\t" + bookingId + "\t\t" + firstName + "\t\t" + lastName + "\t\t" + passportNo + "\t" +
							phone + "\t" + bookingClass + "\t\t"  + status + "\t\t" + numOfLuggage);
			}
			
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------\n");
		}
	}
	
	private static ArrayList<Passenger> getAllPassengers(Connection conn) throws SQLException {
		ArrayList<Passenger> passengers = new ArrayList<>();
		
		String query = "SELECT * FROM passengers ORDER BY passenger_id DESC";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String passengerId = rs.getString("passenger_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String passportNo = rs.getString("passport_no");
				String phone = rs.getString("phone");
				String userId = rs.getString("user_id");
				
				if (lastName.equalsIgnoreCase("admin")) {
					continue;
				}
				
				Passenger p = new Passenger(passengerId, UserService.getUserById(userId, conn), firstName, lastName, passportNo, phone);
				
				ArrayList<Booking> bookings = PassengerService.getBookings(p, conn);
				p.setBookings(bookings);
				
				ArrayList<Luggage> luggage = PassengerService.getLuggages(p, conn);
				p.setLuggages(luggage);
				
				passengers.add(p);
			}
		}
		
		return passengers;
	}
	
	public static void printAllPassengers(Connection conn) throws SQLException {
		ArrayList<Passenger> passengers = getAllPassengers(conn);
		
		System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Passenger Id\tFirst Name\tLast Name\tPassport No\tPhone\t\tNum of Bookings\t\tNum of Luggages");
		for (Passenger p : passengers) {
			p.print();
		}
		
		System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	private static ArrayList<Flight> getFlightsByAircraftId(String aircraftId, Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights WHERE aircraft_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, aircraftId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				String flightNo = rs.getString("flight_no");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
//				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = FlightService.getBookingsByFlightId(flightId, conn);
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				f.setBooking(bookings);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printFlightsByAircraftId(String aircraftId, Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlightsByAircraftId(aircraftId, conn);
		
		if (!flights.isEmpty()) {
			System.out.println("\n----------------------------------------------------------------------------------------------------");
	        System.out.printf("%-15s %-12s %-15s %-15s %-25s %-20s %-20s %-15s\n", 
	                          "Flight Id", "Flight No", "Status", "Aircraft", 
	                          "Route", "Departure Time", "Arrival Time", "Seat Available");
			for (Flight f : flights) {
				f.print();
			}
			System.out.println("----------------------------------------------------------------------------------------------------\n");

		} else {
			System.out.println("There is no flight for aircfart id " + aircraftId);
		}
	}
	
	public static void printAllBookings(Connection conn) throws SQLException {
		
		String query = "SELECT * FROM bookings b JOIN passengers p ON b.passenger_id = p.passenger_id JOIN flights f ON b.flight_id = f.flight_id JOIN aircrafts a ON a.aircraft_id = f.aircraft_id";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm");

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("Booking Id\tPassenger Id\tFirst Name\tLast Name\tFlight No\tAircraft\t\tClass\t\tStatus\t\tRoute\t\t\tDeparture Time\t\t\t\tArrival Time\t\t\t\tChecked In?\tNum of Luggage");
			
			while (rs.next()) {
				String bookingId = rs.getString("booking_id");
				String passengerId = rs.getString("passenger_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String flightNo = rs.getString("flight_no");
				String aircraft = rs.getString("model");
				String status = rs.getString("status");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				boolean isCheckedIn = rs.getInt("is_checked_in") == 1;
				String bookingClass = rs.getString("booking_class");
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				
				
				System.out.println(bookingId + "\t\t " + passengerId + "\t\t" + firstName + "\t\t" + lastName + "\t\t" + flightNo + "\t\t" + aircraft + "\t\t" + bookingClass + "\t\t" + status + "\t\t" + origin + "-" + destination +
						"\t\t" + departureTime.format(formatter) + "\t\t" + arrivalTime.format(formatter) + "\t\t\t" + isCheckedIn + "\t\t" + numOfLuggage);
				
			}
			
			System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
	}
	
	
}
