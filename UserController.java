import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserController {
	
	public static void saveUser(User u, Connection conn) throws SQLException {
		
		String query = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, u.getUserId());
		ps.setString(2, u.getUsername());
		ps.setString(3, u.getPassword());
		
		int rowAffected = ps.executeUpdate();
	}

	
}
