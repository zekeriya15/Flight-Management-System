import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Business extends Booking {
	private final int MAX_LUGGAGE = 4;
	private final double MAX_WEIGHT = 45;
	
	
	public Business(String bookingId) {
		super(bookingId);
	}
	
	public Business(String bookingId, boolean isCheckedIn, int numOfLuggage) {
		super(bookingId, isCheckedIn, numOfLuggage);
	}
	
	
	
	
}
