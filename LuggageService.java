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
	
	public static Luggage getLuggageById(String luggageId, Connection conn) throws SQLException {
		Luggage l = null;
		String query = "SELECT * FROM luggages WHERE luggage_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, luggageId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String type = rs.getString("type");
				double weight = rs.getDouble("weight");
				
				l = new Luggage(luggageId, type, weight);
			}
		}
		
		return l;
	}
	
	public static ArrayList<Luggage> getLuggagesByPassengerId(String passengerId, Connection conn) throws SQLException {
		ArrayList<Luggage> luggages = new ArrayList<>();
		
		String query = "SELECT luggage_id FROM luggages WHERE passenger_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, passengerId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String luggageId = rs.getString("luggage_id");
				Luggage l = getLuggageById(luggageId, conn);
				
				luggages.add(l);
			}	
		}
		
		return luggages;
	}
	
	private static ArrayList<Luggage> getLuggagesByBookingId(String bookingId, Connection conn) throws SQLException {
		ArrayList<Luggage> luggages = new ArrayList<>();
		
		String query = "SELECT luggage_id FROM luggages WHERE booking_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, bookingId);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String luggageId = rs.getString("luggage_id");
				Luggage l = getLuggageById(luggageId, conn);
				
				luggages.add(l);
			}	
		}
		
		
		return luggages;
	}
	
	public static void printLuggages(String bookingId, Connection conn) throws SQLException {
		ArrayList<Luggage> luggages = getLuggagesByBookingId(bookingId, conn);
		double total = 0;
		
		if (!luggages.isEmpty()) {
			System.out.println("\n-------------------------------------------------");
			System.out.println("Type\t\tWeight");
			
			for (Luggage l : luggages) {
				l.print();
				total += l.getWeight();
			}
			System.out.println("\nTotal Weight:\t" + total + " kg");
			System.out.println("-------------------------------------------------\n");

		} else {
			System.out.println("\nYou don't have any luggages in this flight\n");
		}
		
		
		
	}
}
