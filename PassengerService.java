import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
	
	
	
}
