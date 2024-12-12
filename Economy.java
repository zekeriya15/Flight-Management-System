import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Economy extends Booking {
	private final int MAX_LUGGAGE = 2;
	private final double MAX_WEIGHT = 30;
	
	
	public Economy(String bookingId) {
		super(bookingId);
	}
	
	public Economy(String bookingId, boolean isCheckedIn, int numOfLuggage) {
		super(bookingId, isCheckedIn, numOfLuggage);
	}
	
	
	
	
}
