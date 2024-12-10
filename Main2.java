import java.sql.Connection;
import java.sql.SQLException;


public class Main2 {
	public static void main(String[] args) {
		
		DBConnection c = new DBConnection();
		try {
			Connection conn = c.getConnection();
			
			System.out.println("connection estabilished");
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
		}
		
		

	}

}
