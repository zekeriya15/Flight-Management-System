import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Business extends Booking {
	private final int MAX_LUGGAGE = 4;
	private final double MAX_WEIGHT = 45;
	
	
	public Business(Passenger p, String f, Connection conn) throws SQLException {
		super(p, f, conn);
	}
	
	public Business(String bookingId, Passenger p, String f, boolean isCheckedIn, ArrayList<Luggage> luggages, int numOfLuggage) {
		super(bookingId, p, f, isCheckedIn, luggages, numOfLuggage);
	}
	
	
	
	
}
