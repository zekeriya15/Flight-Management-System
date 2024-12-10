import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class First extends Booking {
	private final int MAX_LUGGAGE = 6;
	private final double MAX_WEIGHT = 55;
	
	
	public First(Passenger p, String f, Connection conn) throws SQLException {
		super(p, f, conn);
	}
	
	public First(String bookingId, Passenger p, String f, boolean isCheckedIn, ArrayList<Luggage> luggages, int numOfLuggage) {
		super(bookingId, p, f, isCheckedIn, luggages, numOfLuggage);
	}
	
	
}
