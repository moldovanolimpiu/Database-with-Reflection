package reflection_database.bll;

import reflection_database.bll.validators.OrderQuantityValidator;
import reflection_database.bll.validators.Validator;
import reflection_database.dao.OrderDAO;
import reflection_database.model.Customer_Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Business Logic Layer class for handling operations related to {@link Customer_Order}.
 * It uses validators to ensure data integrity before performing database operations.
 */
public class OrderBLL {
    private List<Validator<Customer_Order>> validators;
    private OrderDAO orderDAO;

    /**
     * Constructs a new {@code OrderBLL} instance, initializes validators and the DAO {@link OrderDAO}.
     */
    public OrderBLL() {
        validators = new ArrayList<Validator<Customer_Order>>();
        validators.add(new OrderQuantityValidator());
        orderDAO = new OrderDAO();
    }

    /**
     * Retrieves all Orders from the database.
     * @return a list of all {@code Customer_Order} objects, or {@code null} if none exist
     */

    public List<Customer_Order> findAllOrders() {
        List<Customer_Order> orders = orderDAO.findAll();
        if(orders == null) {
            System.out.println("Order list is empty");
            return null;
        }
        return orders;
    }
    /**
     * Inserts a new Order into the database after validating.
     * @param order the {@code Customer_Order} object to be inserted
     * @throws IllegalArgumentException if validation fails
     */
    public void insertOrder(Customer_Order order) {
        System.out.println("Inserting order with id " + order.getId());
        orderDAO.insert(order);
    }

}
