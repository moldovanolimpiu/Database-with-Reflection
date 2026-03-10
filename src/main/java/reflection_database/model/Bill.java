package reflection_database.model;

public record Bill(
        Integer id,
        int order_id,
        int customer_id,
        String customer_name,
        int product_id,
        String product_name,
        int quantity,
        int price,
        double weight
) {

    public Bill(int order_id, int customer_id, String customer_name, int product_id, String product_name,
                int quantity, int price, double weight){
        this(null, order_id,customer_id,customer_name, product_id, product_name, quantity, price, weight);
    }
}
