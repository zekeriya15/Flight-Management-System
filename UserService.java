import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UserService {
	private static final String USCODE = User.getCode();
	
	public static String generateId(Connection conn) throws SQLException {
		int id = 0;
		
		String query = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String lastUserId = rs.getString("user_id");
				String numValue = lastUserId.substring(USCODE.length());
				int numValueParsed = Integer.parseInt(numValue);
				
				id = ++numValueParsed;
			}
		}
		
		return USCODE + id;
	}
	
	public static void saveUser(User u, Connection conn) throws SQLException {
		String query = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, u.getUserId());
			ps.setString(2, u.getUsername());
			ps.setString(3, u.getPassword());
			
			ps.executeUpdate();
		}
	}
	
	public static User getUserById(String userId, Connection conn) throws SQLException {
		userId = userId.toUpperCase();
		User u = null;
		String query = "SELECT * FROM users WHERE user_id = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, userId);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				u = new User(userId, username, password);
				
			} else {
				System.out.println("\nUser Id " + userId + " is not found\n");
			}
		}
		
		return u;
	}
	
}
