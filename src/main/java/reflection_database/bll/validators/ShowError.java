package reflection_database.bll.validators;

import javafx.scene.control.Alert;

/**
 * Class with a method that displays errors in the UI depending on the conditions
 * Used in the validator classes {@link CustomerAgeValidator},{@link EmailValidator}
 * ,{@link OrderQuantityValidator},{@link ProductPriceValidator},{@link ProductQuantityValidator}
 * ,{@link ProductWeightValidator}
 */
public class ShowError {
    public void showError(String title, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setContentText(message);
        error.showAndWait();
    }
}
