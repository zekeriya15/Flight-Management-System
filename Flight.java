import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Flight {
	private static final String CODE = "FL";

	
	private String flightId;
	private String flightNo;
	private Aircraft plane;
	private String origin;
	private String destination;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private int seatAvailable;
	private String status;
	private ArrayList<Booking> bookings;
	
	
	public Flight(String flightId, String flightNo, Aircraft plane, String origin, String destination) {
		this.flightId = flightId;
		this.flightNo = flightNo;
		this.plane = plane;
		this.origin = origin;
		this.destination = destination;
		this.seatAvailable = plane.getCapacity();
		this.status = "On Time";
		this.bookings = new ArrayList<>();
	}
	
	public Flight(String flightId, String flightNo, Aircraft plane, String origin, String destination, int seats, String status) {
		this.flightId = flightId;
		this.flightNo = flightNo;
		this.plane = plane;
		this.origin = origin;
		this.destination = destination;
		this.seatAvailable = seats;
		this.status = status;
		this.bookings = new ArrayList<>();
	}
	
//	private static int generateId(Connection conn) throws SQLException {
//		int id = 0;
//		
//		String query = "SELECT flight_id FROM flights ORDER BY flight_id DESC LIMIT 1";
//		PreparedStatement ps = conn.prepareStatement(query);
//		
//		ResultSet rs = ps.executeQuery();
//		
//		if (rs.next()) {
//			String lastFlightId = rs.getString("flight_id");
//			String numValue = lastFlightId.substring(CODE.length());
//			int numValueParsed = Integer.parseInt(numValue);
//			
//			id = ++numValueParsed;
//		}
//		
//		return id;
//	}
	
	
	// getters
	public static String getCode() {
		return CODE;
	}
	
	public String getFlightId() {
		return flightId;
	}
	
	public String getFlightNo() {
		return flightNo;
	}
	
	public Aircraft getAircraft() {
		return plane;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public LocalDateTime getDepartureTime() {
		return departureTime;
	}
	
	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}
	
	public String getStrDepartureTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return departureTime.format(formatter);
	}
	public String getStrArrivalTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return arrivalTime.format(formatter);
	}
	
	public int getSeatAvailable() {
		return seatAvailable;
	}
	
	public String getStatus() {
		return status;
	}
	
	public ArrayList<Booking> getBookings() {
		return bookings;
	}
	
	
	// setters
	public void setFlightId(String id) {
		this.flightId = id;
	}
	
	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}
	
	public void setAircraft(Aircraft plane) {
		this.plane = plane;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public void setDepartureTime(int year, int month, int day, int hour, int minute) {
		this.departureTime = LocalDateTime.of(year, month, day, hour, minute);
	}
	
	public void setArrivalTime(int year, int month, int day, int hour, int minute) {
		this.arrivalTime = LocalDateTime.of(year, month, day, hour, minute);
	}
	
	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}
	
	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public void setDepartureTime(String departureTime) {
		this.departureTime = parseDateTime(departureTime);
	}
	
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = parseDateTime(arrivalTime);
	}
	
	private static LocalDateTime parseDateTime(String dateTimeInput) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		try {
			return LocalDateTime.parse(dateTimeInput, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date-time format. Please use 'yyyy-MM-dd HH:mm'");
			throw e;
		}
	}
	
	public void setSeatAvailable(int seats) {
		this.seatAvailable = seats;
	}
	
	public void setBooking(ArrayList<Booking> bookings) {
		this.bookings = bookings;
	}
	
	
	
	public void print() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy, HH:mm");
	
		
	    String formattedDepartureTime = departureTime.format(formatter);
	    String formattedArrivalTime = arrivalTime.format(formatter);

	    // Use String.format to pad and align all fields to the desired length
	    String departureTimePadded = String.format("%-25s", formattedDepartureTime);  // 25 characters wide
	    String arrivalTimePadded = String.format("%-25s", formattedArrivalTime);        // 25 characters wide

	    // Print flight details with aligned columns
	    System.out.printf("%-15s %-12s %-15s %-15s %-25s %-25s %-25s %-15s\n", 
	                      flightId, flightNo, status, plane.getModel(), 
	                      origin + "-" + destination, 
	                      departureTimePadded, arrivalTimePadded, 
	                      seatAvailable + "/" + plane.getCapacity());
				
	}
}
