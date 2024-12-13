import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FlightService {
	private static final String FLCODE = Flight.getCode();
	
	public static String generateId(Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT flight_id FROM flights ORDER BY flight_id DESC LIMIT 1";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String lastFlightId = rs.getString("flight_id");
			String numValue = lastFlightId.substring(FLCODE.length());
			int numValueParsed = Integer.parseInt(numValue);
			
			id = ++numValueParsed;
		}
		
		return FLCODE + id;
	}
	
	public static void saveFlight(Flight f, Connection conn) throws SQLException {
		String query = "INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, f.getFlightId());
            ps.setString(2, f.getFlightNo());
            ps.setString(3, f.getOrigin());
            ps.setString(4, f.getDestination());
            ps.setTimestamp(5, Timestamp.valueOf(f.getDepartureTime()));
            ps.setTimestamp(6, Timestamp.valueOf(f.getArrivalTime()));
            ps.setInt(7, f.getSeatAvailable());
            ps.setString(8, f.getStatus());
            ps.setString(9, f.getAircraft().getAircraftId().toUpperCase());
            
            ps.executeUpdate();
        }
	}
	
	public static void editFlight(Flight f, Connection conn) throws SQLException {
		String query = "UPDATE flights SET flight_no = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ? WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, f.getFlightNo());
			ps.setString(2, f.getOrigin());
			ps.setString(3, f.getDestination());
			ps.setTimestamp(4, Timestamp.valueOf(f.getDepartureTime()));
			ps.setTimestamp(5, Timestamp.valueOf(f.getArrivalTime()));
			ps.setString(6, f.getFlightId());
			
			ps.executeUpdate();
		}
	}
	
	public static void deleteFlight(Flight f, Connection conn) throws SQLException {
		String query = "DELETE FROM flights WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, f.getFlightId());
			
			ps.executeUpdate();
		}
	}
	
	public static Flight getFlightById(String flightId, Connection conn) throws SQLException {
		flightId = flightId.toUpperCase();
		Flight f = null;
		String query = "SELECT * FROM flights WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, flightId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String flightNo = rs.getString("flight_no");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = getBookingsByFlightId(flightId, conn);
				
				f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				f.setBooking(bookings);
			} else {
				System.out.println("\nFlight Id " + flightId + " is not found\n");
			}
		}
		
		return f;
	}
	
	public static Flight getFlightByNo(String flightNo, Connection conn) throws SQLException {
		flightNo = flightNo.toUpperCase();
		Flight f = null;
		String query = "SELECT * FROM flights WHERE flight_no = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, flightNo);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String flightId = rs.getString("flight_id");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				int seatAvailable = rs.getInt("seat_available");
				String status = rs.getString("status");
				String aircraftId = rs.getString("aircraft_id");
				
				Aircraft plane = AircraftService.getAircraftById(aircraftId, conn);
				ArrayList<Booking> bookings = getBookingsByFlightId(flightId, conn);
				
				f = new Flight(flightId, flightNo, plane, origin, destination, seatAvailable, status);
				f.setDepartureTime(departureTime);
				f.setArrivalTime(arrivalTime);
				f.setBooking(bookings);
			} else {
				System.out.println("\nFlight Id " + flightNo + " is not found\n");
			}
		}
		return f;
	}
	
	public static Flight getFlightByBookingId(String bookingId, Connection conn) throws SQLException {
		bookingId = bookingId.toUpperCase();
		Flight f = null;
		String query = "SELECT b.flight_id FROM bookings b JOIN flights f ON b.flight_id = f.flight_id WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, bookingId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String flightId = rs.getString("flight_id");
				
				f = getFlightById(flightId, conn);
			}
		}
		
		return f;
	}
	
	public static ArrayList<Booking> getBookingsByFlightId(String flightId, Connection conn) throws SQLException {
		ArrayList<Booking> bookings = new ArrayList<>();
		
		String query = "SELECT booking_id FROM bookings WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, flightId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String bookingId = rs.getString("booking_id");
				
				Booking b = BookingService.getBookingById(bookingId, conn);
				ArrayList<Luggage> luggages = BookingService.getLuggagesByBookingId(bookingId, conn);
				
				b.setLuggages(luggages);
				bookings.add(b);
			}
		}
 		
		return bookings;
	}
	
	public static void delayFlight(Flight f, Connection conn) throws SQLException {
		String query = "UPDATE flights SET status = 'Delayed' WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, f.getFlightId());
			
			ps.executeUpdate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy, HH:mm");
			
			System.out.println("\nFlight no " + f.getFlightNo() + " with route " + f.getOrigin() + " - " + f.getDestination() + " scheduled to fly at " +
					f.getDepartureTime().format(formatter) + " is delayed");
		}
	}
	
	public static void cancelFlight(Flight f, Connection conn) throws SQLException {
		String query = "UPDATE flights SET status = 'Canceled' WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, f.getFlightId());
			
			ps.executeUpdate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy, HH:mm");
			
			System.out.println("\nFlight no " + f.getFlightNo() + " with route " + f.getOrigin() + " - " + f.getDestination() + " scheduled to fly at " +
					f.getDepartureTime().format(formatter) + " is canceled");
		}
	}
	
	public static void updateSeatAvailable(Flight f, Connection conn) throws SQLException {
		int seatAvailable = f.getSeatAvailable() - 1;
		
		String query = "UPDATE flights SET seat_available = ? WHERE flight_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, seatAvailable);
			ps.setString(2, f.getFlightId());
			
			ps.executeUpdate();
		}
	}
	
	public static boolean isCanceled(String bookingId, Connection conn) throws SQLException {
		String query = "SELECT status FROM flights f JOIN bookings b ON f.flight_id = b.flight_id WHERE booking_id = ?";
		
		boolean status = false;
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, bookingId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				status = rs.getString("status").equalsIgnoreCase("Canceled");
			}
		}
		
		return status;
	}
	
	
}
