# Database with Reflection Project

  This is a project which uses Reflection to access and modify the database of a shop, which contains clients, products, orders, etc. It mainly uses Java and consequently, JavaFX for the interface, while MySQL has been used for the database. The application is structured into multiple layers: Models, Data Access Objects (DAOs), Business logic and the GUI. This makes the application modular, easy to understand, and as effortless as possible to maintain. Connecting the user to the app, the User Interface is simple but clear and easy to use.

## How to use

Upon opening the application, the user is greeted by a window with five buttons: Clients, Products, Orders, Bills and Exit.
### Clients:
  -opens a tab where each client's information is visible: ID, name, email, address and age
  -the operations that can be performed are: adding a client (beware, the client must be over 18 years old), editing a client's info and deleting a client altogether.
  
### Product:
  -opens a tab where the user can see all the shop's stock. Information includes the ID of a product, the name, its quantity, the price per unit and the weight per unit.
  -the same three operations are present, while performing the same aforementioned tasks.

### Orders:
  -this is where the user can see the information about orders placed in the shop: The ID of the order, customer, product and the quantity that was ordered.
  -orders can be added here. For each one, only one customer and one item can be selected. The quantity can also be specified, and if there is not enough, of said item, an error will occur.

### Bill Logs:
  -a more detailed view of the order. While the order has only the IDs and quantity, here, alongside those IDs, there are also the customer and product names, as well ass the total price and weight of said order.
  -the bill is automatically created when an order has been created, and persists even if the customer or product have been deleted from the database

    
    
  
