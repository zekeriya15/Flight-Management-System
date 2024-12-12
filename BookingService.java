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
	
}
