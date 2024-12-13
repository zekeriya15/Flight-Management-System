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
	
	@Override
	public void addLuggage(Passenger p, Luggage l) {
		double currentWeight = 0;
		if (!this.luggages.isEmpty()) {
			for (Luggage lu : this.luggages) {
				currentWeight += lu.getWeight();
			}
		}
		
		double totalWeight = currentWeight + l.getWeight();
		
		if (numOfLuggage < MAX_LUGGAGE && totalWeight <= MAX_WEIGHT) {
			this.luggages.add(l);
			p.getLuggages().add(l);
			this.numOfLuggage++;
			
			System.out.println("\nLuggage added successfully\n");
		} else {
			System.out.println("\nMaximum of allowed luggages in Economy Class is 2 and maximum weight is 30 kg\n");
		}
	}
	
	
}
