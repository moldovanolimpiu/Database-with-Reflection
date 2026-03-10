package reflection_database.connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * A class for creating and managing database connections.
     */
    private ConnectionFactory() {
        try{
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    /**
     * Creates a new {@link Connection} to the database.
     * @return a {@link Connection} instance or null if an error occurs
     */
    private Connection createConnection(){
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DBURL,USER,PASS);
        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"An error occured while trying to connect to the database");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * Gets a new database connection from the singleton instance.
     * @return a {@link Connection} to the database
     */
    public static Connection getConnection(){
        return singleInstance.createConnection();
    }
    /**
     * Closes a database {@link Connection}.
     * @param connection the connection to close
     */
    public static void close(Connection connection){
        if(connection != null){
            try{
                connection.close();
            }catch(SQLException e){
                LOGGER.log(Level.WARNING,"An error occured while closing the connection");
            }
        }
    }
    /**
     * Closes a {@link Statement}.
     * @param statement the statement to close
     */
    public static void close(Statement statement){
        if(statement != null){
            try{
               statement.close();
            }catch(SQLException e){
                LOGGER.log(Level.WARNING,"An error occured while closing the statement");
            }
        }
    }
    /**
     * Closes a {@link ResultSet}.
     * @param resultSet the result set to close
     */
    public static void close(ResultSet resultSet){
        if(resultSet != null){
            try{
                resultSet.close();
            }catch(SQLException e){
                LOGGER.log(Level.WARNING,"An error occured while closing the resultSet");
            }
        }
    }
}
