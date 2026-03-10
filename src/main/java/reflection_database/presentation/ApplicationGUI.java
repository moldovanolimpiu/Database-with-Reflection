package reflection_database.presentation;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reflection_database.bll.BillBLL;
import reflection_database.bll.CustomerBLL;
import reflection_database.bll.OrderBLL;
import reflection_database.bll.ProductBLL;
import reflection_database.model.Bill;
import reflection_database.model.Customer;
import reflection_database.model.Customer_Order;
import reflection_database.model.Product;

import java.util.List;

/**
 * The main GUI application class for managing clients, products, orders, and bill logs.
 * Provides windows for CRUD operations using JavaFX.
 *  GUI elements include:
 * Main menu with navigation buttons
 * Client window for adding, editing, and deleting customers
 * Product window for adding, editing, and deleting products
 * Order window for placing new orders
 * Bill log window to display billing history
 */
public class ApplicationGUI extends Application {
    private CustomerBLL customerBLL = new CustomerBLL();
    private TableView<Customer> customerTable;
    private ProductBLL productBLL = new ProductBLL();
    private TableView<Product> productTable;
    private OrderBLL orderBLL = new OrderBLL();
    private TableView<Customer_Order> orderTable;
    private TableView<Bill> billTable;
    private BillBLL billBLL = new BillBLL();

    /**
     * Method hat starts the application
     *@param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Ammunition Depot");
        customerTable = Reflection.buildTable(Customer.class);
        productTable = Reflection.buildTable(Product.class);
        orderTable = Reflection.buildTable(Customer_Order.class);
        billTable = buildBillTable();

        Button clientsButton = new Button("Clients");
        Button productsButton = new Button("Products");
        Button ordersButton = new Button("Orders");
        Button billLogsButton = new Button("Bill Logs");
        Button exitButton = new Button("Exit");

        clientsButton.setOnAction(e -> openClientsWindow());
        productsButton.setOnAction(e -> openProductsWindow());
        ordersButton.setOnAction(e -> openOrdersWindow());
        billLogsButton.setOnAction(e-> openBillLogWindow());
        exitButton.setOnAction(e -> stage.close());

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(clientsButton, productsButton, ordersButton, billLogsButton, exitButton);

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens a new window showing all customers and options to add, delete, or edit them.
     */
    public void openClientsWindow() {
        Stage clientStage = new Stage();
        clientStage.setTitle("Clients Window");

        customerTable = Reflection.buildTable(Customer.class);
        List<Customer> customerData = customerBLL.findAllCustomers();
        ObservableList<Customer> visibleCustomerData = FXCollections.observableArrayList(customerData);
        customerTable.setItems(visibleCustomerData);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        Button addClientAction = new Button("Add Client");
        addClientAction.setOnAction(e -> {
            openAddClientWindow();
        });
        Button deleteClientAction = new Button("Delete Client");
        deleteClientAction.setOnAction(e -> {
            openDeleteClientWindow();
        });
        Button editClientAction = new Button("Edit Client");
        editClientAction.setOnAction(e -> {
            openEditClientWindow();
        });

        HBox buttonBox = new HBox(10, addClientAction, deleteClientAction, editClientAction);
        buttonBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(buttonBox,customerTable);

        clientStage.setScene(new Scene(vbox, 800, 600));
        clientStage.show();
    }
    /**
     * Opens a form to add a new customer with fields for name, email, address, and age.
     */
    public void openAddClientWindow() {
        Stage addClientStage = new Stage();
        addClientStage.setTitle("Add New Client");

        // Input fields
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Label addressLabel = new Label("Address");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        Label ageLabel = new Label("Age");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        nameField.setPrefWidth(200);
        emailField.setPrefWidth(200);
        addressField.setPrefWidth(200);
        ageField.setPrefWidth(200);

        HBox nameBox = new HBox(10,nameLabel,nameField);
        nameBox.setAlignment(Pos.CENTER);
        HBox emailBox = new HBox(10,emailLabel,emailField);
        emailBox.setAlignment(Pos.CENTER);
        HBox addressBox = new HBox(10,addressLabel,addressField);
        addressBox.setAlignment(Pos.CENTER);
        HBox ageBox = new HBox(10,ageLabel,ageField);
        ageBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                int age = Integer.parseInt(ageField.getText());
                customerBLL.insertCustomer(new Customer(name, address, email, age));
                customerTable.setItems(FXCollections.observableArrayList(customerBLL.findAllCustomers()));
                System.out.println("Client added: " + name + ", " + email + ", " + address + ", " +age);
                addClientStage.close();
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for age and non-null fields");
            }


        });

        VBox vbox = new VBox(10, nameBox, emailBox, addressBox, ageBox,submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        addClientStage.setScene(new Scene(vbox, 300, 250));
        addClientStage.show();
    }
    /**
     * Opens a form to delete a customer by ID.
     */
    public void openDeleteClientWindow() {
        Stage deleteClientStage = new Stage();
        deleteClientStage.setTitle("Delete Client");

        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        idField.setPromptText("ID");

        HBox idBox = new HBox(10,idLabel,idField);
        idBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                customerBLL.deleteCustomer(id);
                customerTable.setItems(FXCollections.observableArrayList(customerBLL.findAllCustomers()));

            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and non-null values");
            }
        });

        VBox vbox = new VBox(10, idBox, submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        deleteClientStage.setScene(new Scene(vbox, 300, 250));
        deleteClientStage.show();
    }
    /**
     * Opens a form to edit an existing customer.
     * Allows the user to retrieve current details, modify values and save changes.
     */
    public void openEditClientWindow() {
        Stage editClientStage = new Stage();
        editClientStage.setTitle("Edit Client");

        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        idField.setPromptText("ID");

        Button retrieveDetails = new Button("Retrieve Details");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        Button submitButton = new Button("Submit");

        HBox idBox = new HBox(10,idLabel,idField);
        idBox.setAlignment(Pos.CENTER);



        retrieveDetails.setOnAction(e -> {
            try{
                int id = Integer.parseInt(idField.getText());
                Customer customer = customerBLL.findCustomerById(id);
                nameField.setText(customer.getName());
                emailField.setText(customer.getEmail());
                addressField.setText(customer.getAddress());
                ageField.setText(Integer.toString(customer.getAge()));
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and non-null fields");
            }

        });

        submitButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                int age = Integer.parseInt(ageField.getText());
                customerBLL.updateCustomer(new Customer(id, name, address, email, age));

                customerTable.setItems(FXCollections.observableArrayList(customerBLL.findAllCustomers()));
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and age and non-null fields");
            }
        });

        VBox vbox = new VBox(10, idBox, retrieveDetails, nameField, emailField, addressField, ageField,submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        editClientStage.setScene(new Scene(vbox, 300, 250));
        editClientStage.show();
    }
    /**
     * Opens a window displaying all products and options to add, delete or edit products.
     */
    public void openProductsWindow() {
        Stage productStage = new Stage();
        productStage.setTitle("Products Window");

        productTable = Reflection.buildTable(Product.class);
        List<Product> productData = productBLL.findAllProducts();
        ObservableList<Product> visibleProductData = FXCollections.observableArrayList(productData);
        productTable.setItems(visibleProductData);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        Button addProductAction = new Button("Add Product");
        Button deleteProductAction = new Button("Delete Product");
        Button editProductAction = new Button("Edit Product");

        addProductAction.setOnAction(e -> {
            openAddProductWindow();
        });
        deleteProductAction.setOnAction(e->{
            openDeleteProductWindow();
        });
        editProductAction.setOnAction(e->{
            openEditProductWindow();
        });

        HBox productButtonBox = new HBox(10,addProductAction,deleteProductAction,editProductAction);
        productButtonBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(productButtonBox, productTable);

        productStage.setScene(new Scene(vbox, 800, 600));
        productStage.show();
    }
    /**
     * Opens a form to add a new product with fields for name, quantity, price, and weight.
     */

    public void openAddProductWindow() {
        Stage addProductStage = new Stage();
        addProductStage.setTitle("Add New Product");

        // Input fields
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Label quantityLabel = new Label("Quantity");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Label priceLabel = new Label("Price/Unit");
        TextField priceField = new TextField();
        priceField.setPromptText("Price/Unit");

        Label weightLabel = new Label("Kilos/Unit");
        TextField weightField = new TextField();
        weightField.setPromptText("Kilos/Unit");

        nameField.setPrefWidth(200);
        quantityField.setPrefWidth(200);
        priceField.setPrefWidth(200);
        weightField.setPrefWidth(200);

        HBox nameBox = new HBox(10,nameLabel,nameField);
        nameBox.setAlignment(Pos.CENTER);
        HBox quantityBox = new HBox(10,quantityLabel,quantityField);
        quantityBox.setAlignment(Pos.CENTER);
        HBox priceBox = new HBox(10,priceLabel,priceField);
        priceBox.setAlignment(Pos.CENTER);
        HBox weightBox = new HBox(10,weightLabel,weightField);
        weightBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int price = Integer.parseInt(priceField.getText());
                double weight = Double.parseDouble(weightField.getText());
                productBLL.insertProduct(new Product(name, quantity, price, weight));
                productTable.setItems(FXCollections.observableArrayList(productBLL.findAllProducts()));
                System.out.println("Product added: " + name + ", " + quantity + ", " + price + ", " +weight);
                addProductStage.close();
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for age and non-null fields");
            }

        });

        VBox vbox = new VBox(10, nameBox, quantityBox, priceBox, weightBox,submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        addProductStage.setScene(new Scene(vbox, 300, 250));
        addProductStage.show();
    }
    /**
     * Opens a form to delete a product by ID.
     */
    public void openDeleteProductWindow() {
        Stage deleteProductStage = new Stage();
        deleteProductStage.setTitle("Delete Product");

        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        idField.setPromptText("ID");

        HBox idBox = new HBox(10,idLabel,idField);
        idBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                productBLL.deleteProductById(id);
                productTable.setItems(FXCollections.observableArrayList(productBLL.findAllProducts()));

            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and non-null values");
            }
        });

        VBox vbox = new VBox(10, idBox, submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        deleteProductStage.setScene(new Scene(vbox, 300, 250));
        deleteProductStage.show();
    }
    /**
     * Opens a form to edit an existing product.
     * Allows retrieval, modification of values and update of product details.
     */
    public void openEditProductWindow() {
        Stage editProductStage = new Stage();
        editProductStage.setTitle("Edit Product");

        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        idField.setPromptText("ID");

        Button retrieveDetails = new Button("Retrieve Details");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField priceField = new TextField();
        priceField.setPromptText("Price/Unit");
        TextField weightField = new TextField();
        weightField.setPromptText("Kilos/Unit");
        Button submitButton = new Button("Submit");

        HBox idBox = new HBox(10,idLabel,idField);
        idBox.setAlignment(Pos.CENTER);



        retrieveDetails.setOnAction(e -> {
            try{
                int id = Integer.parseInt(idField.getText());
                Product product = productBLL.findProductById(id);
                nameField.setText(product.getName());
                quantityField.setText(Integer.toString(product.getQuantity()));
                priceField.setText(Integer.toString(product.getPrice_per_unit()));
                weightField.setText(Double.toString(product.getKilos_per_unit()));
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and non-null fields");
            }

        });

        submitButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int price = Integer.parseInt(priceField.getText());
                double weight = Double.parseDouble(weightField.getText());
                productBLL.updateProduct(new Product(id, name, quantity, price, weight));

                productTable.setItems(FXCollections.observableArrayList(productBLL.findAllProducts()));
            }catch(NumberFormatException nfe){
                showError("Invalid input","Use numbers for id and age and non-null fields");
            }
        });

        VBox vbox = new VBox(10, idBox, retrieveDetails, nameField, quantityField, priceField, weightField,submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        editProductStage.setScene(new Scene(vbox, 300, 250));
        editProductStage.show();

    }
    /**
     * Opens a window displaying all customer orders and a button to add new orders.
     */
    public void openOrdersWindow() {
        Stage orderStage = new Stage();
        orderStage.setTitle("Orders Window");

        orderTable = Reflection.buildTable(Customer_Order.class);
        List<Customer_Order> orderData = orderBLL.findAllOrders();
        ObservableList<Customer_Order> visibleOrderData = FXCollections.observableArrayList(orderData);
        orderTable.setItems(visibleOrderData);

        Button addOrderAction = new Button("Add Order");

        HBox buttonBox = new HBox(10,addOrderAction);
        buttonBox.setAlignment(Pos.CENTER);

        addOrderAction.setOnAction(e -> {
            openAddOrderWindow();
        });


        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(buttonBox,orderTable);

        orderStage.setScene(new Scene(vbox, 800, 600));
        orderStage.show();
    }
    /**
     * Opens a form to place a new order.
     * Allows selection of a customer, a product, and the quantity.
     * Updates order and product data, and creates a bill.
     */
    public void openAddOrderWindow() {
        Stage addOrderStage = new Stage();
        addOrderStage.setTitle("Add New Order");

        Label customerLabel = new Label("Customer");
        ComboBox<Customer> customerComboBox = new ComboBox<>();
        customerComboBox.setItems(FXCollections.observableArrayList(customerBLL.findAllCustomers()));
        customerComboBox.setPromptText("Select Customer");

        Label productLabel = new Label("Product");
        ComboBox<Product> productComboBox = new ComboBox<>();
        productComboBox.setItems(FXCollections.observableArrayList(productBLL.findAllProducts()));
        productComboBox.setPromptText("Select Product");

        customerComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        customerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        productComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        productComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        Label quantityLabel = new Label("Quantity");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        HBox customerBox = new HBox(10, customerLabel, customerComboBox);
        HBox productBox = new HBox(10, productLabel, productComboBox);
        HBox quantityBox = new HBox(10, quantityLabel, quantityField);
        customerBox.setAlignment(Pos.CENTER);
        productBox.setAlignment(Pos.CENTER);
        quantityBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());

                Customer customer = customerComboBox.getValue();
                Product product = productComboBox.getValue();

                if (customer == null) {
                    showError("Error", "Please select a customer.");
                } else if (product == null) {
                    showError("Error", "Please select a product.");
                } else {
                    if (quantity > product.getQuantity()) {
                        showError("Error", "Order quantity exceeds product stock");
                    } else {
                        orderBLL.insertOrder(new Customer_Order(customer.getId(), product.getId(), quantity));
                        orderTable.setItems(FXCollections.observableArrayList(orderBLL.findAllOrders()));

                        productBLL.updateProduct(new Product(product.getId(), product.getName(),
                                product.getQuantity() - quantity, product.getPrice_per_unit(), product.getKilos_per_unit()));
                        productTable.setItems(FXCollections.observableArrayList(productBLL.findAllProducts()));

                        billBLL.insertBill(new Bill(
                                billBLL.getMaxOrderId(),
                                customer.getId(), customer.getName(),
                                product.getId(), product.getName(),
                                quantity, quantity * product.getPrice_per_unit(),
                                quantity * product.getKilos_per_unit()
                        ));
                        billTable.setItems(FXCollections.observableArrayList(billBLL.findAllBills()));

                        System.out.println("Order added: " + customer.getId() + ", " + product.getId() + ", " + quantity);
                        addOrderStage.close();
                    }
                }

            } catch (NumberFormatException nfe) {
                showError("Invalid input", "Use numbers for quantity");
            }
        });

        VBox vbox = new VBox(10, customerBox, productBox, quantityBox, submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        addOrderStage.setScene(new Scene(vbox, 500, 250));
        addOrderStage.show();
    }
    /**
     * Opens a window displaying all generated bills.
     */
    private void openBillLogWindow(){
        Stage billLogStage = new Stage();
        billLogStage.setTitle("Bills Window");

        billTable = buildBillTable();
        List<Bill> billData = billBLL.findAllBills();
        ObservableList<Bill> visibleBillData = FXCollections.observableArrayList(billData);
        billTable.setItems(visibleBillData);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(billTable);

        billLogStage.setScene(new Scene(vbox, 800, 600));
        billLogStage.show();
    }
    /**
     * Constructs and returns a table to display bill information.
     * @return a TableView populated with columns for bill attributes
     */
    private TableView<Bill> buildBillTable(){
        TableView<Bill> billTable = new TableView<>();
        billTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Bill, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().id()));
        idColumn.setPrefWidth(1);

        TableColumn<Bill, Integer> customerId = new TableColumn<>("Customer ID");
        customerId.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().customer_id()));
        customerId.setPrefWidth(1);

        TableColumn<Bill, String> customerName = new TableColumn<>("Customer Name");
        customerName.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().customer_name()));
        customerName.setPrefWidth(1);

        TableColumn<Bill, Integer> productId = new TableColumn<>("Product ID");
        productId.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().product_id()));
        productId.setPrefWidth(1);

        TableColumn<Bill, String> productName = new TableColumn<>("Product Name");
        productName.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().product_name()));
        productName.setPrefWidth(1);

        TableColumn<Bill, Integer> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().quantity()));
        quantity.setPrefWidth(1);

        TableColumn<Bill, Integer> price = new TableColumn<>("Price/Unit");
        price.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().price()));
        price.setPrefWidth(1);

        TableColumn<Bill, Double> kilos = new TableColumn<>("Kilos/Unit");
        kilos.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().weight()));
        kilos.setPrefWidth(1);

        billTable.getColumns().addAll(idColumn,customerId,customerName,productId,productName,quantity,price,kilos);

        return billTable;
    }

    /**
     * Displays an error alert dialog with the given title and message.
     * @param title   the title of the error
     * @param message the message content
     */
    public void showError(String title, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setContentText(message);
        error.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
