import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Luggage {
	private static final String CODE = "LG";
	
	private String luggageId;
	private String type;
	private double weight;
	
	public Luggage(String type, double weight, Passenger p, Connection conn) throws SQLException {
		this.luggageId = generateId(p, conn);
		this.type = type;
		this.weight = weight;
	}
	
	public Luggage(String luggageId, String type, double weight) {
		this.luggageId = luggageId;
		this.type = type;
		this.weight = weight;
	}
	
	private static String generateId(Passenger p, Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT luggage_id FROM luggages WHERE passenger_id = ? ORDER BY luggage_id DESC LIMIT 1";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, p.getPassengerId());
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String lastLuggageId = rs.getString("luggage_id");
			String numValue = lastLuggageId.substring(lastLuggageId.lastIndexOf(CODE) + CODE.length());
			int numValueParsed = Integer.parseInt(numValue);
			
			id = ++numValueParsed;
		}
		
		return p.getPassengerId() + CODE + id;
	}
	
	
	// getters
	public String getLuggageId() {
		return luggageId;
	}
	
	public String getType() {
		return type;
	}
	
	public double getWeight() {
		return weight;
	}
	
	// setters
	public void setLuggageId(String id) {
		this.luggageId = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	public void saveLuggage(Passenger p, Booking b, Connection conn) throws SQLException {
		
		String query = "INSERT INTO luggages VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, luggageId);
		ps.setString(2, type);
		ps.setDouble(3, weight);
		ps.setString(4, p.getPassengerId());
		ps.setString(5, b.getBookingId());
		
		int rowAffected = ps.executeUpdate();
	}

}
