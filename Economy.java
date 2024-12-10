import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Economy extends Booking {
	private final int MAX_LUGGAGE = 2;
	private final double MAX_WEIGHT = 30;
	
	
	public Economy(Connection conn) throws SQLException {
		super(conn);
	}
	
	public Economy(String bookingId, boolean isCheckedIn, ArrayList<Luggage> luggages, int numOfLuggage) {
		super(bookingId, isCheckedIn, luggages, numOfLuggage);
	}
	
	
	
	
}
