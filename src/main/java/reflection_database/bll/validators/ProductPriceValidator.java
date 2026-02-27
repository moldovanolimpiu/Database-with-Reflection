package reflection_database.bll.validators;

import reflection_database.model.Product;

/**
 * Validator class that ensures the price of a product is above 0, used in the {@link Product} class
 */
public class ProductPriceValidator implements Validator<Product> {
    ShowError showError = new ShowError();
    @Override
    public void validate(Product obj) {
        if(obj.getPrice_per_unit()<0){
            showError.showError("Invalid input","The price per unit must be a positive number");
            throw new IllegalArgumentException("The price per unit must be a positive number");
        }
    }
}
