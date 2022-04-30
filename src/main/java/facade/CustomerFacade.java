package facade;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import db_utils.DB_Utils;
import dbdao.CompaniesDBDAO;
import dbdao.CouponsDBDAO;
import dbdao.CustomersDBDAO;
import exeptions.CustomerExceptions;
import exeptions.ErrorMsg;
import exeptions.PurchaseExceptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerFacade extends ClientFacade {
    //attributes:
    private Integer customerID;

    private final String LOGIN;

    {
        LOGIN = "SELECT * FROM `luxury_coupons`.`customers` WHERE `email`=? AND password=?";
    }

    private final String GET_CUSTOMER_COUPONS;

    {
        GET_CUSTOMER_COUPONS = "SELECT * " +
                "FROM  luxury_coupons.coupons, luxury_coupons.customers_coupons " +
                "WHERE luxury_coupons.coupons.id=luxury_coupons.customers_coupons.coupon_id " +
                "AND luxury_coupons.customers_coupons.customer_id = ?";
    }

    private final String CHECK_IF_BOUGHT_COUPON_YET;

    {
        CHECK_IF_BOUGHT_COUPON_YET = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`customers_coupons` " +
                "WHERE `coupon_id`=? AND customer_id =?);";
    }

    /* constructor: */
    /**
     * initializes:
     * companiesDao as CompaniesDBDAO
     * customersDao as CustomersDBDAO
     * couponsDao as CouponsDBDAO
     */
    public CustomerFacade() {
        customersDAO = new CustomersDBDAO();
        companiesDAO = new CompaniesDBDAO();
        couponsDAO = new CouponsDBDAO();
    }

    /**
     * logging in with email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean login(String email, String password) {
        boolean loggedIn = false;

        //creating map for insert values to every key in the query
        Map<Integer, Object> login = new HashMap<>();
        login.put(1, email);
        login.put(2, password);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(LOGIN, login);

        try {
            while (resultSet.next()) {
                //updates this customerID to the one that has logged in
                this.customerID = resultSet.getInt("id");

                //if customer exists returns true
                loggedIn = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            loggedIn = false;
        }
        return loggedIn;
    }

    /**
     * adds coupon purchase for this customer if possible
     *
     * @param coupon Coupon
     * @throws PurchaseExceptions if coupon doesn't exist, if this customer already bought it, if coupon expired, if coupons amount is 0
     */
    public void purchaseCoupon(Coupon coupon) throws PurchaseExceptions {
        //checking if coupon available to purchase for the customer
        checkIfCanBuy(coupon);

        //adding coupon purchase for this customer
        couponsDAO.addCouponPurchase(this.customerID, coupon.getId());
    }

    /**
     * get all customer's coupons
     *
     * @return List of Coupon
     * @throws CustomerExceptions if coupons not found
     */
    public List<Coupon> getCustomerCoupons() throws CustomerExceptions {
        List<Coupon> customerCoupons = new ArrayList<>();

        //creating map for insert values to every key in the query
        Map<Integer, Object> getCustomerCoupons = new HashMap<>();
        getCustomerCoupons.put(1, this.customerID);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_CUSTOMER_COUPONS, getCustomerCoupons);

        try {
            while (resultSet.next()) {
                //adding coupons to customer coupons List if it matches his id
                customerCoupons.add(couponsDAO.getOneCoupon(resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //check if found coupons
        checkCouponsExistence(customerCoupons);

        //return customer's coupons
        return customerCoupons;
    }

    /**
     * returns costumer coupons that matches this category
     *
     * @param category Category
     * @return List of Coupon
     * @throws CustomerExceptions if coupons not found
     */
    public List<Coupon> getCustomerCoupons(Category category) throws CustomerExceptions {
        List<Coupon> customerCoupons = getCustomerCoupons().stream()
                .filter(coupon -> coupon.getCategory().equals(category))
                .collect(Collectors.toList());

        //check if found coupons
        checkCouponsExistence(customerCoupons);

        //return costumer coupons
        return customerCoupons;
    }

    /**
     * returns costumer coupons that under maximum price
     *
     * @param maxPrice double
     * @return List of Coupon
     * @throws CustomerExceptions if coupons not found
     */
    public List<Coupon> getCustomerCoupons(double maxPrice) throws CustomerExceptions {
        List<Coupon> customerCoupons = getCustomerCoupons().stream()
                .filter(coupon -> coupon.getPrice() < maxPrice)
                .collect(Collectors.toList());

        //check if found coupons
        checkCouponsExistence(customerCoupons);

        //return customer's coupons
        return customerCoupons;
    }

    /**
     * returns customer's details
     *
     * @return Customer
     */
    public Customer getCustomerDetails() {
        //return details of this customer
        return customersDAO.getOneCustomer(this.customerID);
    }


    //private methods:

    /**
     * checks if coupon available to purchase for the customer
     *
     * @param coupon Coupon
     * @throws PurchaseExceptions if coupon doesn't exist, if this customer already bought it, if coupon expired, if coupons amount is 0
     */
    private void checkIfCanBuy(Coupon coupon) throws PurchaseExceptions {
        //throws exception if coupon doesn't exist
        if (couponsDAO.getOneCoupon(coupon.getId()) == null) {
            throw new PurchaseExceptions(ErrorMsg.ADD_COUPON_PURCHASE, ErrorMsg.COUPON_ID_NOT_EXIST);
        }
        // throws exception if this customer already bought it
        if (checkIfBoughtCouponYet(coupon.getId())) {
            throw new PurchaseExceptions(ErrorMsg.ADD_COUPON_PURCHASE, ErrorMsg.ALREADY_BOUGHT_COUPON);
        }
        //throws exception if coupon expired
        if (coupon.getEndDate().isBefore(LocalDate.now())) {
            throw new PurchaseExceptions(ErrorMsg.ADD_COUPON_PURCHASE, ErrorMsg.COUPON_EXPIRED);
        }
        //throws exception if coupon's amount is 0
        if (coupon.getAmount() == 0) {
            throw new PurchaseExceptions(ErrorMsg.ADD_COUPON_PURCHASE, ErrorMsg.NO_COUPONS_LEFT);
        }
    }

    /**
     * checks if coupons found
     *
     * @param customerCoupons List of Coupon
     * @throws CustomerExceptions if List is empty
     */
    private void checkCouponsExistence(List<Coupon> customerCoupons) throws CustomerExceptions {
        if (customerCoupons.isEmpty()) {
            throw new CustomerExceptions(ErrorMsg.GET_CUSTOMER_PURCHASES);
        }
    }

    /**
     * checks if customer bought this coupon yet
     *
     * @param couponId int
     * @return boolean
     */
    private boolean checkIfBoughtCouponYet(int couponId) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkIfBoughtYet = new HashMap<>();
        checkIfBoughtYet.put(1, couponId);
        checkIfBoughtYet.put(2, this.customerID);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_IF_BOUGHT_COUPON_YET, checkIfBoughtYet);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }
}
