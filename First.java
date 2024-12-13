import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class First extends Booking {
	private final int MAX_LUGGAGE = 6;
	private final double MAX_WEIGHT = 55;
	
	
	public First(String bookingId) {
		super(bookingId);
	}
	
	public First(String bookingId, boolean isCheckedIn, int numOfLuggage) {
		super(bookingId, isCheckedIn, numOfLuggage);
	}
	
	
}
