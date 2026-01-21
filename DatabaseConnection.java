package dal;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection implements IDatabaseConnection {
    @Override
    public Connection getConnection() throws SQLException {
        return DatabaseConfigure.getConnection();
    }
}
