package reflection_database.bll;

import reflection_database.bll.validators.CustomerAgeValidator;
import reflection_database.bll.validators.EmailValidator;
import reflection_database.bll.validators.Validator;
import reflection_database.dao.CustomerDAO;
import reflection_database.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Business Logic Layer class for handling operations related to {@link Customer}.
 * It uses validators to ensure data integrity before performing database operations.
 */
public class CustomerBLL {

    private List<Validator<Customer>> validators;
    private CustomerDAO customerDAO;
    /**
     * Constructs a new {@code CustomerBLL} instance, initializes validators and the DAO {@link CustomerDAO}.
     */
    public CustomerBLL() {
        validators = new ArrayList<Validator<Customer>>();
        validators.add(new EmailValidator());
        validators.add(new CustomerAgeValidator());

        customerDAO = new CustomerDAO();
    }
    /**
     * Finds a customer by their ID.
     * @param id the ID of the customer to retrieve
     * @return the {@code Customer} if found, otherwise {@code null}
     */
    public Customer findCustomerById(int id) {
        Customer customer = customerDAO.findById(id);
        if(customer == null){
            System.out.println("Customer not found");
        }
        return customer;
    }
    /**
     * Retrieves all customers from the database.
     * @return a list of all {@code Customer} objects, or {@code null} if none exist
     */
    public List<Customer> findAllCustomers() {
        List<Customer> customers = customerDAO.findAll();
        if(customers == null){
            System.out.println("Customer list is empty");
            return null;
        }
        return customers;
    }
    /**
     * Inserts a new customer into the database after validating.
     * @param customer the {@code Customer} object to be inserted
     * @throws IllegalArgumentException if validation fails
     */
    public void insertCustomer(Customer customer) {
        for(Validator<Customer> validator : validators){
            validator.validate(customer);
        }
        System.out.println("Inserting customer " + customer.getName());
        customerDAO.insert(customer);
    }
    /**
     * Deletes a customer by their ID.
     * @param id the ID of the customer to delete
     */
    public void deleteCustomer(int id){
        System.out.println("Deleting customer with id " + id);
        customerDAO.deleteById(id);
    }
    /**
     * Updates an existing customer in the database.
     * @param customer the {@code Customer} object with updated data
     */
    public void updateCustomer(Customer customer) {
        System.out.println("Updating customer " + customer.getName());
        customerDAO.update(customer);
    }
}
