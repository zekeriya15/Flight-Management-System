import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AircraftService {
	private static final String AFCODE = Aircraft.getCode();
	
	public static String generateId(Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT aircraft_id FROM aircrafts ORDER BY aircraft_id DESC LIMIT 1";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String lastAircraftId = rs.getString("aircraft_id");
			String numValue = lastAircraftId.substring(AFCODE.length());
			int numValueParsed = Integer.parseInt(numValue);
			
			id = ++numValueParsed;
		}
		
		
		return AFCODE + id;
	}
	
	public static void saveAircraft(Aircraft aircraft, Connection conn) throws SQLException {
        String query = "INSERT INTO aircrafts (aircraft_id, model, capacity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, aircraft.getAircraftId());
            ps.setString(2, aircraft.getModel());
            ps.setInt(3, aircraft.getCapacity());

            ps.executeUpdate();
        }
    }
	
	public static void editAircraft(Aircraft plane, Connection conn) throws SQLException {
		String query = "UPDATE aircrafts SET model = ?, capacity = ? WHERE aircraft_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, plane.getModel());
			ps.setInt(2, plane.getCapacity());
			ps.setString(3, plane.getAircraftId());
			
			ps.executeUpdate();
		}
	}
	
	public static void deleteAicraft(Aircraft plane, Connection conn) throws SQLException {
		String query = "DELETE FROM aircrafts WHERE aircraft_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, plane.getAircraftId());
			
			ps.executeUpdate();
		}
	}
	
	public static Aircraft getAircraftById(String aircraftId, Connection conn) throws SQLException {
		aircraftId = aircraftId.toUpperCase();
		Aircraft plane = null;
		String query = "SELECT * FROM aircrafts WHERE aircraft_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, aircraftId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String model = rs.getString("model");
				int capacity = rs.getInt("capacity");
				
				plane = new Aircraft(aircraftId, model, capacity);
			} else {
				System.out.println("\nAircraft Id " + aircraftId + " is not found\n");
			}
		}
		
		return plane;
	}

}
