module reflection_database {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;


    opens reflection_database.model to javafx.base;
    exports reflection_database.presentation;
}