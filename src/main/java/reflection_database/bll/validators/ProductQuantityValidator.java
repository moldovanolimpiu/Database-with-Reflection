package reflection_database.bll.validators;

import reflection_database.model.Product;
/**
 * Validator class that ensures the quantity of a newly inserted product is above 0, used in the {@link Product} class
 */
public class ProductQuantityValidator implements Validator<Product>{
    ShowError showError = new ShowError();
    @Override
    public void validate(Product obj) {
        if(obj.getQuantity() < 0){
            showError.showError("Invalid input", "Quantity must be greater than zero");
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
