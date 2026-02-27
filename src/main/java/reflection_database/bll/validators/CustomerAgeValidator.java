package reflection_database.bll.validators;

import reflection_database.model.Customer;

/**
 * Validator class for handling age verification for the {@link Customer} class
 */
public class CustomerAgeValidator implements Validator<Customer> {
    private static final int MIN_AGE = 18;
    ShowError showError = new ShowError();
    @Override
    public void validate(Customer obj) {
        if(obj.getAge() < MIN_AGE){
            showError.showError("Invalid input", "Customer age must be greater than or equal to 18");
            throw new IllegalArgumentException("Customer age must be greater than or equal to 18");
        }
    }
}
