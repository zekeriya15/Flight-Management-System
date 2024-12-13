import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class PassengerService {
	private static final String PSCODE = Passenger.getCode();

	public static String generateId(Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT passenger_id FROM passengers ORDER BY passenger_id DESC LIMIT 1";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String lastPassengerId = rs.getString("passenger_id");
				String numValue = lastPassengerId.substring(PSCODE.length());
				int numValueParsed = Integer.parseInt(numValue);
				
				id = ++numValueParsed;
			}
		}
		
		return PSCODE + id;
	}
	
	public static void savePassenger(Passenger p, Connection conn) throws SQLException {
		String query = "INSERT INTO passengers VALUES (?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, p.getPassengerId());
			ps.setString(2, p.getFirstName());
			ps.setString(3, p.getLastName());
			ps.setString(4, p.getPassportNo());
			ps.setString(5, p.getPhone());
			ps.setString(6, p.getUser().getUserId());
			
			ps.executeUpdate();
		}
	}
	
	public static void editPassenger(Passenger p, Connection conn) throws SQLException {
		String query = "UPDATE passengers SET first_name = ?, last_name = ?, passport_no = ?, phone = ? WHERE passenger_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, p.getFirstName());
			ps.setString(2, p.getLastName());
			ps.setString(3, p.getPassportNo());
			ps.setString(4, p.getPhone());
			ps.setString(5, p.getPassengerId());
			
			ps.executeUpdate();
		}
	}
	
	public static void addBooking(Passenger p, Flight f, String bookingClass, Connection conn) throws SQLException {
		Booking b = null;
		String bookingId = BookingService.generateId(p.getPassengerId(), conn);
		
		switch (bookingClass) {
			case "Economy":
				b = new Economy(bookingId);
				break;
			case "Business":
				b = new Business(bookingId);
				break;
			case "First":
				b = new First(bookingId);
				break;
		}
		
		int isCheckedIn = 0;
		if (b.isCheckedIn) {
			isCheckedIn = 1;
		}
		
		String query = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, b.getBookingId());
			ps.setString(2, bookingClass);
			ps.setInt(3, isCheckedIn);
			ps.setInt(4, b.getNumOfLuggage());
			ps.setString(5, f.getFlightId());
			ps.setString(6, p.getPassengerId());
			
			ps.executeUpdate();
		}
		
		FlightService.updateSeatAvailable(f, conn);
	}
	
	public static ArrayList<Booking> getBookings(Passenger p, Connection conn) throws SQLException {
		ArrayList<Booking> bookings = new ArrayList<>();
		
		String query = "SELECT * FROM bookings WHERE passenger_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, p.getPassengerId());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Booking b = null;
				
				String bookingId = rs.getString("booking_id");
				String bookingClass = rs.getString("booking_class");
				boolean isCheckedIn = rs.getInt("is_checked_in") == 1;
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				ArrayList<Luggage> luggages = BookingService.getLuggagesByBookingId(bookingId, conn);
				
				switch (bookingClass) {
					case "Economy":
						b = new Economy(bookingId, isCheckedIn, numOfLuggage);
//						b.setLuggages(luggages);
						break;
					case "Business":
						b = new Business(bookingId, isCheckedIn, numOfLuggage);
//						b.setLuggages(luggages);
						break;
					case "First":
						b = new Business(bookingId, isCheckedIn, numOfLuggage);
//						b.setLuggages(luggages);
						break;
				}
				
				b.setLuggages(luggages);
				bookings.add(b);
			}
			
		}
		
		return bookings;
	}
	
	public static void printBooking(Passenger p, Connection conn) throws SQLException {
		String query = "SELECT * FROM bookings b JOIN flights f ON b.flight_id = f.flight_id WHERE passenger_id = ?";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, p.getPassengerId());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String bookingId = rs.getString("booking_id");
				String flightNo = rs.getString("flight_no");
				String status = rs.getString("status");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureTime = rs.getTimestamp("departure_time").toLocalDateTime();
				LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
				boolean isCheckedIn = rs.getInt("is_checked_in") == 1;
				String bookingClass = rs.getString("booking_class");
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				
				System.out.println();
				System.out.println("Booking id: " + bookingId);
				System.out.println("Flight no: " + flightNo);
				System.out.println("status: " + status);
				System.out.println("Route: " + origin + " " + destination);
				System.out.println("Schedule: " + departureTime.format(formatter) + " - " + arrivalTime.format(formatter));
				System.out.println("Check in? " + isCheckedIn);
				System.out.println("Class: " + bookingClass);
				System.out.println("num of luggage: " + numOfLuggage);
				System.out.println();
				
			}
			
		}
	}
	
	
	public static void checkIn(Booking b, Passenger p, Connection conn) throws SQLException {
		String query = "UPDATE bookings SET is_checked_in = ?, num_of_luggage = ? WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, 1);
			ps.setInt(2, b.getNumOfLuggage());
			ps.setString(3, b.getBookingId());
			
			ps.executeUpdate();
		}
		
		if (!b.getLuggages().isEmpty()) {
			addLuggage(b.getLuggages(), p.getPassengerId(), b.getBookingId(), conn);
		}
	}
	
	public static void addLuggage(ArrayList<Luggage> luggages, String passengerId, String bookingId, Connection conn) throws SQLException {
		
		for (Luggage l : luggages) {
			
			String query = "INSERT INTO luggages VALUES (?, ?, ?, ?, ?)";
			
			try (PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, l.getLuggageId());
				ps.setString(2, l.getType());
				ps.setDouble(3, l.getWeight());
				ps.setString(4, passengerId);
				ps.setString(5, bookingId);
				
				ps.executeUpdate();
			}
		}
	}
	
}
