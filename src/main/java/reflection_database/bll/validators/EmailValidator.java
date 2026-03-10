package reflection_database.bll.validators;

import reflection_database.model.Customer;

import java.util.regex.Pattern;

/**
 * Validator class that ensures the email corresponds to a specific format for the {@link Customer} class
 */
public class EmailValidator implements Validator<Customer> {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    ShowError showError = new ShowError();

    @Override
    public void validate(Customer obj) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(obj.getEmail()).matches()) {
            showError.showError("Invalid input", "Invalid email address");
            throw new IllegalArgumentException("Invalid email address");
        }
    }

}
