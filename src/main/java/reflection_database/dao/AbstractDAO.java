package reflection_database.dao;

import reflection_database.connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * A generic abstract Data Access Object (DAO) class for performing common database operations
 * such as find, insert, update, and delete using reflection.
 *
 * @param <T> the type of object this DAO handles
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;
    /**
     * Constructs a new AbstractDAO and determines the runtime type of the generic parameter.
     */
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    /**
     * Creates a SQL SELECT query for a specific field.
     *
     * @param field the field to filter by
     * @return the constructed SQL query string
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Retrieves all records from the corresponding database table.
     *
     * @return a list of all objects found, or null if an error occurs
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }
        finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * Finds a record by its ID.
     *
     * @param id the ID of the record
     * @return the object with the specified ID, or null if not found or on error
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * Maps the ResultSet to a list of objects using reflection.
     *
     * @param resultSet the SQL result set
     * @return a list of mapped objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();

                    Class<?> parameterType = method.getParameterTypes()[0];
                    if(value != null) {
                        if(parameterType == int.class || parameterType == Integer.class) {
                            value = ((Number)value).intValue();
                        }else if(parameterType == long.class || parameterType == Long.class) {
                            value = ((Number)value).longValue();
                        }else if(parameterType == boolean.class || parameterType == Boolean.class) {
                            value = ((Boolean)value).booleanValue();
                        }else if(parameterType == double.class || parameterType == Double.class) {
                            value = ((Number)value).doubleValue();
                        }else if(parameterType == float.class || parameterType == Float.class) {
                            value = ((Number)value).floatValue();
                        }else if(parameterType == String.class) {
                            value = value.toString();
                        }
                    }

                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Creates the SQL INSERT query string using reflection.
     *
     * @param sb           the StringBuilder to construct the query
     * @param tableColumns the fields of the class
     * @return the completed SQL INSERT query
     */
    private StringBuilder createInsertQuery(StringBuilder sb, Field[] tableColumns) {
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName());
        sb.append(" (");
        String result = IntStream.range(1, tableColumns.length)
                        .mapToObj(i->tableColumns[i].getName())
                                .collect(Collectors.joining(","));
        sb.append(result);
        sb.append(") VALUES (");

        String result2 = IntStream.range(1, tableColumns.length)
                        .mapToObj(i->"?").collect(Collectors.joining(","));
        sb.append(result2);
        sb.append(")");

        return sb;
    }
    /**
     * Inserts a new object into the database.
     *
     * @param t the object to insert
     * @return the inserted object, with ID updated if applicable
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] tableColumns = type.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb = createInsertQuery(sb, tableColumns);

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            for(int i = 1; i < tableColumns.length; i++){
                tableColumns[i].setAccessible(true);
                statement.setObject(i, tableColumns[i].get(t));
            }

            statement.executeUpdate();

            ResultSet keygen = statement.getGeneratedKeys();
            if(keygen.next()){
                Field idField = tableColumns[0];
                idField.setAccessible(true);
                idField.set(t, keygen.getInt(1));
            }

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
            e.printStackTrace();
        } finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * Deletes an object from the database by its ID.
     *
     * @param id the ID of the object to delete
     */
    public void deleteById(int id){
        Connection connection = null;
        PreparedStatement statement = null;

        String deleteQuery = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if(rows == 0){
                LOGGER.warning("No record with id =" + id + " found to delete in table " + type.getSimpleName());
            }else{
                LOGGER.info("Deleted record with id =" + id + " from table " + type.getSimpleName());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteById " + e.getMessage());
            e.printStackTrace();
        }finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
    /**
     * Creates the SQL UPDATE query string using reflection.
     *
     * @param sb           the StringBuilder to construct the query
     * @param tableColumns the fields of the class
     * @return the completed SQL UPDATE query
     */
    private StringBuilder createUpdateQuery(StringBuilder sb, Field[] tableColumns) {
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");

        String result = IntStream.range(1, tableColumns.length)
                        .mapToObj(i->tableColumns[i].getName() + " = ?").collect(Collectors.joining(","));
        sb.append(result);
        sb.append(" WHERE id = ?");
        return sb;
    } /**
     * Updates an existing object in the database.
     *
     * @param t the object to update
     * @return the updated object
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] tableColumns = type.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb = createUpdateQuery(sb, tableColumns);
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sb.toString());
            for(int i = 1; i < tableColumns.length; i++){
                tableColumns[i].setAccessible(true);
                statement.setObject(i,tableColumns[i].get(t));
            }

            tableColumns[0].setAccessible(true);
            statement.setObject(tableColumns.length, tableColumns[0].get(t));

            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
            e.printStackTrace();
        }

        return t;
    }

}
