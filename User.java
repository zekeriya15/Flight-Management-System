import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class User {
	private static final String CODE = "US";
	
	private String userId;
	private String username;
	private String password;
	
	
	public User(String userId, String username, String password) {
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
	
	
	// getters
	public static String getCode() {
		return CODE;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	// setters
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void saveUser(Connection conn) throws SQLException {
		
		String query = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, userId);
		ps.setString(2, username);
		ps.setString(3, password);
		
		int rowAffected = ps.executeUpdate();
	}
}
