package dbdao;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import dao.CustomersDAO;
//import db_utils.ConnectionPool;
import db_utils.DB_Utils;
import lombok.NoArgsConstructor;
import utils.DateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class CustomersDBDAO implements CustomersDAO {
    //attributes:

    //private ConnectionPool connectionPool;

    private final String CHECK_CUSTOMER_EXISTENCE;

    {
        CHECK_CUSTOMER_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`customers` " +
                "WHERE `email`=? AND `password`= ?);";
    }

    private final String ADD_CUSTOMER;

    {
        ADD_CUSTOMER = "INSERT INTO `luxury_coupons`.`customers` " +
                "(`first_name`,`last_name`,`email`,`password`)" +
                "VALUES (?,?,?,?);";
    }

    private final String UPDATE_CUSTOMER;

    {
        UPDATE_CUSTOMER = "UPDATE luxury_coupons.customers " +
                "SET first_name = ? , last_name = ? , email = ? , password=? WHERE id = ?";
    }

    private final String DELETE_CUSTOMER;

    {
        DELETE_CUSTOMER = "DELETE FROM `luxury_coupons`.`customers` WHERE `id`=";
    }

    private final String GET_ALL_CUSTOMERS;

    {
        GET_ALL_CUSTOMERS = "SELECT * FROM `luxury_coupons`.`customers`";
    }

    private final String GET_ONE_CUSTOMER;

    {
        GET_ONE_CUSTOMER = "SELECT * FROM `luxury_coupons`.`customers` WHERE `id`=";
    }

    private final String GET_CUSTOMER_COUPONS;

    {
        GET_CUSTOMER_COUPONS = "SELECT *\n" +
                "FROM luxury_coupons.customers_coupons\n" +
                "RIGHT JOIN luxury_coupons.coupons\n" +
                "ON luxury_coupons.coupons.id = luxury_coupons.customers_coupons.coupon_id\n" +
                "WHERE luxury_coupons.customers_coupons.customer_id = ?";
    }

    //methods:

    /**
     * Checks if customer exists by email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean isCustomerExist(String email, String password) {
        boolean exists = false;

        //creating map for insert values to every key in the query
        Map<Integer, Object> findCustomer = new HashMap<>();
        findCustomer.put(1, email);
        findCustomer.put(2, password);

        //run query for getting results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(CHECK_CUSTOMER_EXISTENCE, findCustomer);

        try {
            while (resultSet.next()) {
                //makes "exists" return the same result as the query for checking customer existence
                exists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    /**
     * adding customer
     *
     * @param customer Customer
     */
    @Override
    public void addCustomer(Customer customer) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> addCustomer = new HashMap<>();
        addCustomer.put(1, customer.getFirstName());
        addCustomer.put(2, customer.getLastName());
        addCustomer.put(3, customer.getEmail());
        addCustomer.put(4, customer.getPassword());

        //run query
        DB_Utils.runQuery(ADD_CUSTOMER, addCustomer);
        System.out.println("Customer was successfully added");
    }

    /**
     * updates customer details by id
     *
     * @param customer Customer
     */
    @Override
    public void updateCustomer(Customer customer) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> customerDetails = new HashMap<>();
        customerDetails.put(1, customer.getFirstName());
        customerDetails.put(2, customer.getLastName());
        customerDetails.put(3, customer.getEmail());
        customerDetails.put(4, customer.getPassword());
        customerDetails.put(5, customer.getId());

        //run query
        DB_Utils.runQuery(UPDATE_CUSTOMER, customerDetails);
        System.out.println("Customer was successfully updated");

    }

    /**
     * deleting customer by id
     *
     * @param customerID int
     */
    @Override
    public void deleteCustomer(int customerID) {
        //run query
        DB_Utils.runQuery(DELETE_CUSTOMER + customerID);
        System.out.println("Customer was successfully deleted");
    }

    /**
     * creating a List of all customers
     *
     * @return List of Customer
     */
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_ALL_CUSTOMERS);
        try {
            //adding new customer to List from DB Table by resultSet
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                customer.setCoupons(customerCoupons(customer.getId()));
                customers.add(customer);
                /*
                customers.add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ));

                 */

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    /**
     * returns customer by customerID
     *
     * @param customerID int
     * @return Customer
     */
    @Override
    public Customer getOneCustomer(int customerID) {
        Customer customer = null;

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_ONE_CUSTOMER + customerID);


        try {
            //creating company by results from DB table
            while (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
            customer.setCoupons(customerCoupons(customerID));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }


    //private methods:

    /**
     * returns customer's coupons by customerID
     *
     * @param customerId int
     * @return List of Coupon
     */
    private List<Coupon> customerCoupons(Integer customerId) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> customerCoupons = new HashMap<>();
        customerCoupons.put(1, customerId);

        List<Coupon> allCoupons = new ArrayList<>();

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_CUSTOMER_COUPONS, customerCoupons);

        try {
            while (resultSet.next()) {
                //add customer's coupons to list from DB
                allCoupons.add(new Coupon(
                        resultSet.getInt("id"),
                        resultSet.getInt("company_id"),
                        Category.valueOf(resultSet.getInt("category_id")),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        DateUtils.sqlDateToLocalDate(resultSet.getDate("start_date")),
                        DateUtils.sqlDateToLocalDate(resultSet.getDate("end_date")),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image")

                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allCoupons;
    }
}


