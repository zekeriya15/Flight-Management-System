

public class Aircraft {
	private static final String CODE = "AF";
	
	String aircraftId;
	String model;
	int capacity;
	
//	public Aircraft(String model, int capacity, Connection conn) throws SQLException {
//		this.aircraftId = CODE + generateId(conn);
//		this.model = model;
//		this.capacity = capacity;
//	}
	
	public Aircraft(String aircraftId, String model, int capacity) {
		this.aircraftId = aircraftId;
		this.model = model;
		this.capacity = capacity;
	}
	
//	private static int generateId(Connection conn) throws SQLException {
//		int id = 0;
//		
//		String query = "SELECT aircraft_id FROM aircrafts ORDER BY aircraft_id DESC LIMIT 1";
//		PreparedStatement ps = conn.prepareStatement(query);
//		
//		ResultSet rs = ps.executeQuery();
//		
//		if (rs.next()) {
//			String lastAircraftId = rs.getString("aircraft_id");
//			String numValue = lastAircraftId.substring(CODE.length());
//			int numValueParsed = Integer.parseInt(numValue);
//			
//			id = ++numValueParsed;
//		}
//		
//		return id;
//	}
	
	
	// getters
	public static String getCode() {
		return CODE;
	}
	
	public String getAircraftId() {
		return aircraftId;
	}
	
	public String getModel() {
		return model;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	// setters
	public void setAircraftId(String id) {
		this.aircraftId = id;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	
	public void print() {
		System.out.println("aircraft id: " + aircraftId);
		System.out.println("model: " + model);
		System.out.println("capacity: " + capacity);
	}
	
//	public void saveAircraft(Connection conn) throws SQLException {
//		
//		String query = "INSERT INTO aircrafts VALUES (?, ?, ?)";
//		PreparedStatement ps = conn.prepareStatement(query);
//		
//		ps.setString(1, aircraftId);
//		ps.setString(2, model);
//		ps.setInt(3, capacity);
//		
//		int rowAffected = ps.executeUpdate();
//	}
}
