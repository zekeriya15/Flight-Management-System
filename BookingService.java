import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class BookingService {
	private static final String BKCODE = Booking.getCode();
	
	public static String generateId(String passengerId, Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT booking_id FROM bookings WHERE booking_id LIKE ? ORDER BY booking_id DESC LIMIT 1";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, passengerId + "%");
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String lastBookingId = rs.getString("booking_id");
				String numValue = lastBookingId.substring(lastBookingId.lastIndexOf(BKCODE) + BKCODE.length());
				int numValueParsed = Integer.parseInt(numValue);
				
				id = ++numValueParsed;
			}
			
		}
		
		return passengerId + BKCODE + id;
	}
	
	public static Booking getBookingById(String bookingId, Connection conn) throws SQLException {
		bookingId = bookingId.toUpperCase();
		Booking b = null;
		String query = "SELECT * FROM bookings WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, bookingId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String bookingClass = rs.getString("booking_class");
				boolean isCheckedIn = rs.getInt("is_checked_in") == 1;
				int numOfLuggage = rs.getInt("num_of_luggage");
				
				ArrayList<Luggage> luggages = getLuggagesByBookingId(bookingId, conn);
				
				switch (bookingClass) {
					case "Economy":
						b = new Economy(bookingId, isCheckedIn, numOfLuggage);
						break;
					case "Business":
						b = new Business(bookingId, isCheckedIn, numOfLuggage);
						break;
					case "First":
						b = new Business(bookingId, isCheckedIn, numOfLuggage);
						break;
				}
				
				b.setLuggages(luggages);
			}
		}
		
		return b;		
	}
	
	
	public static ArrayList<Luggage> getLuggagesByBookingId(String bookingId, Connection conn) throws SQLException {
		ArrayList<Luggage> luggages = new ArrayList<>();
		
		String query = "SELECT luggage_id FROM luggages WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, bookingId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String luggageId = rs.getString("luggage_id");
				Luggage l = LuggageService.getLuggageById(luggageId, conn);
				
				luggages.add(l);
			}
		}
		
		return luggages;
	}
	
	public static void deleteBooking(Booking b, Connection conn) throws SQLException {
		
		String query = "DELETE FROM bookings WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, b.getBookingId());
			
			ps.executeUpdate();
		}
	}
	
	
	
}
