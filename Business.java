import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Business extends Booking {
	private final int MAX_LUGGAGE = 4;
	private final double MAX_WEIGHT = 45;
	
	
	public Business(Connection conn) throws SQLException {
		super(conn);
	}
	
	public Business(String bookingId, boolean isCheckedIn, ArrayList<Luggage> luggages, int numOfLuggage) {
		super(bookingId, isCheckedIn, luggages, numOfLuggage);
	}
	
	
	
	
}
