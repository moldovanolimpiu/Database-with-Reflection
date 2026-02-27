package reflection_database.model;

/**
 * Represents an order with an ID, customer ID, address ID and order quantity.
 */
public class Customer_Order {
    private int id;
    private int customer_id;
    private int product_id;
    private int order_quantity;

    public Customer_Order(int customer_id, int product_id, int order_quantity) {
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.order_quantity = order_quantity;
    }
    public Customer_Order() {}
    /**
     *Setters and getters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }
}
