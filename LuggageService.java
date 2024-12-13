import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LuggageService {
	private static String LGCODE = Luggage.getCode();
	
	public static String generateId(String passengerId, Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT luggage_id FROM luggages WHERE luggage_id LIKE ? ORDER BY luggage_id DESC LIMIT 1";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, passengerId + "%");
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String lastLuggageId = rs.getString("luggage_id");
				String numValue = lastLuggageId.substring(lastLuggageId.lastIndexOf(LGCODE) + LGCODE.length());
				int numValueParsed = Integer.parseInt(numValue);
				
				id = ++numValueParsed;
			}
			
		}
		
		return passengerId + LGCODE + id;
	}
	
	
}
