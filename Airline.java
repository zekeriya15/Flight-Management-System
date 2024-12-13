import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Airline {

	private static ArrayList<Passenger> passengers = new ArrayList<>();
	
	
	private static ArrayList<Aircraft> getAircrafts(Connection conn) throws SQLException {
	    ArrayList<Aircraft> aircrafts = new ArrayList<>();
	    
	    String query = "SELECT aircraft_id FROM aircrafts";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	            String aircraftId = rs.getString("aircraft_id");
	            Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
	            
	            aircrafts.add(plane);
	        }
	    }
	    
	    return aircrafts;
	}

	
	public static void printAllAircrafts(Connection conn) throws SQLException {
		ArrayList<Aircraft> aircrafts = getAircrafts(conn);
		
		if (!aircrafts.isEmpty()) {
			for (Aircraft af : aircrafts) {
				System.out.println();
				af.print();
				System.out.println();
			}
		} else {
			System.out.println("\nThere are no aircrafts\n");
		}
	}
	
	private static ArrayList<Flight> getFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT flight_id FROM flights ORDER BY departure_time";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				Flight f = FlightService.getFlightById(flightId, conn);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printAllFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlights(conn);
		
		if (!flights.isEmpty()) {
			for (Flight f : flights) {
				System.out.println();
				f.print();
				System.out.println();
			}
		} else {
			System.out.println("\nThere are no any flights available\n");
		}
	}
	
	private static ArrayList<Flight> getAvailableFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT flight_id FROM flights WHERE status != 'Canceled' ORDER BY departure_time";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				Flight f = FlightService.getFlightById(flightId, conn);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printAvailableFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = getAvailableFlights(conn);
		
		for (Flight f : flights) {
			System.out.println();
			f.print();
			System.out.println();
		}
	}

	private static ArrayList<Flight> getFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT flight_id FROM flights WHERE origin = ? AND destination = ? AND status = 'On Time' ORDER BY departure_time";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, origin);
			ps.setString(2, destination);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String flightId = rs.getString("flight_id");
				Flight f = FlightService.getFlightById(flightId, conn);
				
					
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void findFlightsByRoute(String origin, String destination, Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlightsByRoute(origin, destination, conn);
		
		if (!flights.isEmpty()) {
			for (Flight f : flights) {
				System.out.println();
				f.print();
				System.out.println();
			}
		} else {
			System.out.println("\nThere are no flights from " + origin + " to " + destination + "\n");
		}
		
	}
}
