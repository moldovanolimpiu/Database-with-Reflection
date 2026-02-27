package reflection_database.presentation;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;

/**
 * Utility class for building JavaFX TableViews using Java Reflection.
 * This class provides a method to dynamically construct a {@link TableView}
 * based on the fields of a given class. It uses reflection to generate columns automatically
 */
public class Reflection {

    public static <T>TableView<T> buildTable(Class<T> tableClass) {
        TableView<T> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(Field f : tableClass.getDeclaredFields()) {
            f.setAccessible(true);
            TableColumn<T,Object>tableColumn = new TableColumn<>(f.getName());
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
            tableColumn.setPrefWidth(1);
            tableView.getColumns().add(tableColumn);
        }

        return tableView;

    }

}
