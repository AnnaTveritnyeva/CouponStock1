package dao;

import beans.Customer;

import java.util.List;

public interface CustomersDAO {
    /**
     * Checks if customer exists by email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    boolean isCustomerExist(String email, String password);

    /**
     * adding customer
     *
     * @param customer Customer
     */
    void addCustomer(Customer customer);

    /**
     * updates customer details by id
     *
     * @param customer Customer
     */
    void updateCustomer(Customer customer);

    /**
     * deleting customer by id
     *
     * @param customerID int
     */
    void deleteCustomer(int customerID);

    /**
     * creating a List of all customers
     *
     * @return List of Customer
     */
    List<Customer> getAllCustomers();

    /**
     * returns customer by customerID
     *
     * @param customerID int
     * @return Customer
     */
    Customer getOneCustomer(int customerID);
}
