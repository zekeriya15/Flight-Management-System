import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Airline {
	
	
//	private static ArrayList<Aircraft> aircrafts = new ArrayList<>();
//	private static ArrayList<Flight> flights = new ArrayList<>();
	private static ArrayList<Passenger> passengers = new ArrayList<>();
	
	
	private static ArrayList<Aircraft> getAircrafts(Connection conn) throws SQLException {
	    ArrayList<Aircraft> aircrafts = new ArrayList<>();
	    
	    String query = "SELECT * FROM aircrafts";
	    
	    try (PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	            String aircraftId = rs.getString("aircraft_id");
	            String model = rs.getString("model");
	            int capacity = rs.getInt("capacity");
	            
	            Aircraft plane = new Aircraft(aircraftId, model, capacity);
	            aircrafts.add(plane);
	        }
	    }
	    
	    return aircrafts;
	}

	
	public static void printAllAircrafts(Connection conn) throws SQLException {
		ArrayList<Aircraft> aircrafts = getAircrafts(conn);
		
		for (Aircraft af : aircrafts) {
			System.out.println();
			af.print();
			System.out.println();
		}
	}
	
	private static ArrayList<Flight> getFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = new ArrayList<>();
		
		String query = "SELECT * FROM flights ORDER BY departure_time";
		
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
				
				Flight f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				
				flights.add(f);
			}
		}
		
		return flights;
	}
	
	public static void printAllFlights(Connection conn) throws SQLException {
		ArrayList<Flight> flights = getFlights(conn);
		
		for (Flight f : flights) {
			System.out.println();
			f.print();
			System.out.println();
		}
	}

}
