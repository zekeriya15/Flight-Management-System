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
	
}
