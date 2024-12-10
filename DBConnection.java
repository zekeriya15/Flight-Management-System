import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DBConnection {
	private final MysqlDataSource dataSource = new MysqlDataSource();
	private final String DB_URL = "jdbc:mysql://localhost:3306/airline?serverTimezone=Asia/Jakarta";
	private final String DB_USERNAME = "phpmyadmin";
	private final String DB_PASSWORD = "";
	
	public Connection getConnection() throws SQLException {
		dataSource.setUrl(DB_URL);
		dataSource.setUser(DB_USERNAME);
		dataSource.setPassword(DB_PASSWORD);
		Connection conn = dataSource.getConnection();
		
		return conn;
	}
	
	
}

