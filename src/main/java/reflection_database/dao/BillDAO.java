package reflection_database.dao;

import reflection_database.connection.ConnectionFactory;
import reflection_database.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for the {@link Bill} entity.
 * Provides methods to interact with the Log table in the database.
 * This class handles the creation, retrieval, and specific utility operations related to bills.
 */
public class BillDAO {
    Class<Bill> billClass = Bill.class;
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    /**
     * Inserts a new {@link Bill} record into the Log table in the database.
     * @param bill the {@link Bill} object containing the data to be inserted
     * @return the inserted {@link Bill} object
     * @throws RuntimeException if a SQL error occurs during insertion
     */
    public Bill insert(Bill bill) {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        String billSqlIns = "INSERT INTO Log(order_id, customer_id, customer_name," +
                "product_id, product_name, quantity, price, weight)" +
                "VALUES(?,?,?,?,?,?,?,?)";
        int insertedId = -1;
        try{
            statement = connection.prepareStatement(billSqlIns, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, bill.order_id());
            statement.setInt(2, bill.customer_id());
            statement.setString(3, bill.customer_name());
            statement.setInt(4, bill.product_id());
            statement.setString(5, bill.product_name());
            statement.setInt(6, bill.quantity());
            statement.setInt(7, bill.price());
            statement.setDouble(8, bill.weight());
            insertedId = statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertedId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return bill;
    }

    /**
     * Retrieves the maximum order ID from the "Customer_Order" table.
     * This is typically used to identify the last inserted order.
     * @return the highest order ID found in the table otherwise 0
     * @throws RuntimeException if a SQL error occurs during query execution
     */

    public int returnMaxOrderId(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int orderId = 0;
        String maxOrderIdSql = "SELECT MAX(id) FROM Customer_Order";
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(maxOrderIdSql);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                orderId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return orderId;

    }
    /**
     * Retrieves all {@link Bill} records from the Log table.
     * @return a list of all {@link Bill} entries found in the database,
     *         or {@code null} if an error occurs
     */
    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Bill> billList = new ArrayList<>();
        String query = "SELECT * FROM Log";
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("id"),
                        resultSet.getInt("order_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getString("customer_name"),
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price"),
                        resultSet.getDouble("weight")

                );
                billList.add(bill);
            }
            return billList;

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, billClass.getName() + "DAO:findAll " + e.getMessage());
        }
        finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
