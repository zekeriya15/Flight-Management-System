import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Luggage {
	private static final String CODE = "LG";
	
	private String luggageId;
	private String type;
	private double weight;
	
	public Luggage(String luggageId, String type, double weight) {
		this.luggageId = luggageId;
		this.type = type;
		this.weight = weight;
	}
	
	public Luggage(String type, double weight, Passenger p) {
		this.luggageId = generateId(p);
		this.type = type;
		this.weight = weight;
	}
	
	private static String generateId(Passenger p) {
		int id = 0;
		
		if (p.getLuggages().size() != 0) {
			Luggage lastIndexValue = p.getLuggages().get(p.getLuggages().size() - 1);
			String lastLuggageId = lastIndexValue.getLuggageId();
			
			String numValue = lastLuggageId.substring(lastLuggageId.lastIndexOf(CODE) + CODE.length(), lastLuggageId.length());
			int numValueParsed = Integer.parseInt(numValue);
			
			id = ++numValueParsed;
		}
		
		return p.getPassengerId() + CODE + id;
	}
	
	
	// getters
	public static String getCode() {
		return CODE;
	}
	
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
	

}
