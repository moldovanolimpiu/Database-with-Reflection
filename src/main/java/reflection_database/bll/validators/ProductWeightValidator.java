package reflection_database.bll.validators;

import reflection_database.model.Product;

/**
 * Validator class that ensures the weight of a newly introduced product is above 0, used in the {@link Product} class
 */
public class ProductWeightValidator implements Validator<Product> {
    ShowError showError = new ShowError();
    @Override
    public void validate(Product obj) {
        if(obj.getKilos_per_unit()<0){
            showError.showError("Invalid input", "Weight cannot be negative");
            throw new IllegalArgumentException("Weight cannot be negative");
        }
    }
}
