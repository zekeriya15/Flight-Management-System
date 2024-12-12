import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Passenger {
	private static final String CODE = "PS";
	
	private String passengerId;
	private User user;
	private String firstName;
	private String lastName;
	private String passportNo;
	private String phone;
	private ArrayList<Booking> bookings;
	private ArrayList<Luggage> luggages;
	
//	public Passenger(User user, String firstName, String lastName, String passportNo, String phone, Connection conn) throws SQLException {
//		this.passengerId = CODE + generateId(conn);
//		this.user = user;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.passportNo = passportNo;
//		this.phone = phone;
//		this.bookings = new ArrayList<>();
//		this.luggages = new ArrayList<>();
//	}
	
	public Passenger(String passengerId, User user, String firstName, String lastName, String passportNo, String phone) {
		this.passengerId = passengerId;
		this.user = user;
		this.firstName = firstName;;
		this.lastName = lastName;
		this.passportNo = passportNo;
		this.phone = phone;
		this.bookings = new ArrayList<>();
		this.luggages = new ArrayList<>();
	}
	
//	private static int generateId(Connection conn) throws SQLException {
//		int id = 0;
//		
//		String query = "SELECT passenger_id FROM passengers ORDER BY passenger_id DESC LIMIT 1";
//		PreparedStatement ps = conn.prepareStatement(query);
//		
//		ResultSet rs = ps.executeQuery();
//		
//		if (rs.next()) {
//			String lastPassengerId = rs.getString("passenger_id");
//			String numValue = lastPassengerId.substring(CODE.length());
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
	
	public String getPassengerId() {
		return passengerId;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getPassportNo() {
		return passportNo;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public ArrayList<Booking> getBookings() {
		return bookings;
	}
	
	public ArrayList<Luggage> getLuggages() {
		return luggages;
	}
	
	
	// setters
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	
	public void setUser(User u) {
		this.user = u;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setLuggages(ArrayList<Luggage> luggages) {
		this.luggages = luggages;
	}
	
	public void setBookings(ArrayList<Booking> bookings) {
		this.bookings = bookings;
	}
	
	
	
	public void savePassenger(Connection conn) throws SQLException {
		
		String query = "INSERT INTO passengers VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, passengerId);
		ps.setString(2, firstName);
		ps.setString(3, lastName);
		ps.setString(4, passportNo);
		ps.setString(5, phone);
		ps.setString(6, user.getUserId());
		
		int rowAffected = ps.executeUpdate();
		
	}
}
