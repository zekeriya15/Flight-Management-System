import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Booking {
	private static final String CODE = "BK";
	
	protected String bookingId;
	protected boolean isCheckedIn;
	protected ArrayList<Luggage> luggages;
	protected int numOfLuggage;
	
	public Booking(Connection conn) throws SQLException {
//		this.bookingId = generateId(conn);
		this.isCheckedIn = false;
		this.luggages = new ArrayList<>();
	}
	
	public Booking(String bookingId, boolean isCheckedIn, ArrayList<Luggage> luggages, int numOfLuggage) {
		this.bookingId = bookingId;
		this.isCheckedIn = isCheckedIn;
		this.luggages = luggages;
		this.numOfLuggage = numOfLuggage;
	}
	
	private static String generateId(Passenger p, Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT booking_id FROM bookings WHERE passenger_id = ? ORDER BY booking_id DESC LIMIT 1";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, p.getPassengerId());
		
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String lastBookingId = rs.getString("booking_id");
			String numValue = lastBookingId.substring(lastBookingId.lastIndexOf(CODE) + CODE.length());
			int numValueParsed = Integer.parseInt(numValue);
			
			id = ++numValueParsed;
		}
		
		return p.getPassengerId() + CODE + id;
	}
	
	
	// getters
	public String getBookingId() {
		return bookingId;
	}
	
	public boolean isCheckedIn() {
		return isCheckedIn;
	}
	
	public ArrayList<Luggage> getLuggages() {
		return luggages;
	}
	
	public int getNumOfLuggage() {
		return numOfLuggage;
	}
	
	
	// setters
	public void setBookingId(String id) {
		this.bookingId = id;
	}
	
	public void setIsCheckedIn(boolean b) {
		this.isCheckedIn = b;
	}
	
	public void setLuggages(ArrayList<Luggage> luggages) {
		this.luggages = luggages;
	}
	
//	public  void saveBooking(Connection conn) throws SQLException {
//		String bookingClass = "";
//		int checkedIn = 0;
//		
//		if (this instanceof Economy) {
//			bookingClass = "Economy";
//		} else if (this instanceof Business) {
//			bookingClass = "Business";
//		} else if (this instanceof First) {
//			bookingClass = "First";
//		}
//		
//		if (this.isCheckedIn == true) {
//			checkedIn = 1;
//		}
//		
//		
//		String query = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?)";
//		PreparedStatement ps = conn.prepareStatement(query);
//		
//		ps.setString(1, bookingId);
//		ps.setString(2, bookingClass);
//		ps.setInt(3, checkedIn);
//		ps.setInt(4, numOfLuggage);
//		ps.setString(5, flightId);
//		ps.setString(6, passenger.getPassengerId());
//	}
	
}
