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
	
	public Booking(String bookingId) {
		this.bookingId = bookingId;
		this.isCheckedIn = false;
		this.luggages = new ArrayList<>();
//		this.numOfLuggage = 0;
	}
	
	public Booking(String bookingId, boolean isCheckedIn, int numOfLuggage) {
		this.bookingId = bookingId;
		this.isCheckedIn = isCheckedIn;
		this.luggages = new ArrayList<>();
		this.numOfLuggage = numOfLuggage;
	}
	
	
	
	// getters
	public static String getCode() {
		return CODE;
	}
	
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
	
	public void setNumOfLuggage(int num) {
		this.numOfLuggage = num;
	}
	
	
	public abstract void addLuggage(Passenger p, Luggage l);
	

	
}
