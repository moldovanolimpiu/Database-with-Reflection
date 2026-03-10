package reflection_database.bll;

import reflection_database.bll.validators.ProductPriceValidator;
import reflection_database.bll.validators.ProductQuantityValidator;
import reflection_database.bll.validators.ProductWeightValidator;
import reflection_database.bll.validators.Validator;
import reflection_database.dao.ProductDAO;
import reflection_database.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Business Logic Layer class for handling operations related to {@link Product}.
 * It uses validators to ensure data integrity before performing database operations.
 */
public class ProductBLL {
    private List<Validator<Product>> validators;
    private ProductDAO productDAO;
    /**
     * Constructs a new {@code ProductBLL} instance, initializes validators and the DAO {@link ProductDAO}.
     */
    public ProductBLL() {
        validators = new ArrayList<Validator<Product>>();
        validators.add(new ProductPriceValidator());
        validators.add(new ProductQuantityValidator());
        validators.add(new ProductWeightValidator());
        productDAO = new ProductDAO();
    }
    /**
     * Finds a product by its ID.
     * @param id the ID of the product to retrieve
     * @return the {@code Product} if found, otherwise {@code null}
     */
    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if(product == null){
            System.out.println("Product not found");
            return null;
        }
        return product;
    }
    /**
     * Retrieves all products from the database.
     * @return a list of all {@code Customer} objects, or {@code null} if none exist
     */
    public List<Product> findAllProducts() {
        List<Product> products = productDAO.findAll();
        if(products == null){
            System.out.println("No products found");
            return null;
        }
        return products;
    }
    /**
     * Deletes a product by its ID.
     * @param id the ID of the product to delete
     */
    public void deleteProductById(int id) {
        System.out.println("Deleting product with id " + id);
        productDAO.deleteById(id);
    }
    /**
     * Inserts a new product into the database after validating.
     * @param product the {@code Product} object to be inserted
     * @throws IllegalArgumentException if validation fails
     */
    public void insertProduct(Product product) {
        System.out.println("Inserting product with id " + product.getId());
        productDAO.insert(product);
    }
    /**
     * Updates an existing product in the database.
     * @param product the {@code Product} object with updated data
     */
    public void updateProduct(Product product) {
        System.out.println("Updating product with id " + product.getId());
        productDAO.update(product);
    }

}
