package reflection_database.bll.validators;

import reflection_database.model.Customer_Order;

/**
 * Validator class that ensures the quantity of an order is above 0, used in the {@link Customer_Order} class
 */
public class OrderQuantityValidator implements Validator<Customer_Order> {

    ShowError showError = new ShowError();
    @Override
    public void validate(Customer_Order obj) {
        if(obj.getOrder_quantity()<1){
            showError.showError("Invalid input","Order quantity must be greater than zero");
            throw new IllegalArgumentException("Order quantity must be greater than 0");
        }
    }
}
