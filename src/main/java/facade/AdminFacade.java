package facade;

import beans.Company;
import beans.Customer;
import db_utils.DB_Utils;
import dbdao.CompaniesDBDAO;
import dbdao.CouponsDBDAO;
import dbdao.CustomersDBDAO;
import exeptions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminFacade extends ClientFacade {
    //attributes:
    private final String CHECK_COMPANY_NAME_AND_ID_EXISTENCE;

    {
        CHECK_COMPANY_NAME_AND_ID_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`companies`" +
                " WHERE `name`=? AND id=?);";
    }

    private final String CHECK_CUSTOMER_EMAIL_EXISTENCE;

    {
        CHECK_CUSTOMER_EMAIL_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`customers`" +
                " WHERE `email`=?);";
    }

    private final String CHECK_CUSTOMER_ID_EXISTENCE;

    {
        CHECK_CUSTOMER_ID_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`customers`" +
                " WHERE `id`=?);";
    }

    private final String CHECK_COMPANY_ID_EXISTENCE;

    {
        CHECK_COMPANY_ID_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`companies`" +
                " WHERE `id`=?);";
    }

    private final String DELETE_PURCHASE_HISTORY;

    {
        DELETE_PURCHASE_HISTORY = "DELETE * FROM `luxury_coupons`.`customers_coupons` WHERE coupon_id=?";
    }

    private final String UPDATE_COMPANY_ID;

    {
        UPDATE_COMPANY_ID = "SELECT * FROM `luxury_coupons`.`companies` WHERE `name` = ?";
    }

    private final String UPDATE_CUSTOMER_ID;

    {
        UPDATE_CUSTOMER_ID = "SELECT * FROM `luxury_coupons`.`customers` WHERE email=? AND password=?";
    }

    private final String CHECK_COMPANY_NAME_EXISTENCE;

    {
        CHECK_COMPANY_NAME_EXISTENCE =
                "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`companies`" +
                        " WHERE `name`= ?);";
    }

    private final String CHECK_COMPANY_EMAIL_EXISTENCE;

    {
        CHECK_COMPANY_EMAIL_EXISTENCE =
                "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`companies`" +
                        " WHERE `email`= ?);";
    }

    //constructor:
    /**
     * initializes:
     * companiesDao as CompaniesDBDAO
     * customersDao as CustomersDBDAO
     * couponsDao as CouponsDBDAO
     */
    public AdminFacade() {
        companiesDAO = new CompaniesDBDAO();
        couponsDAO = new CouponsDBDAO();
        customersDAO = new CustomersDBDAO();
    }


    //methods:

    /**
     * login as admin by email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean login(String email, String password) {
        return email.equals("admin@admin.com") && password.equals("admin");
    }

    /**
     * adding company if company doesn't exist yet
     *
     * @param company Company
     * @throws CompanyExceptions throws if company already exists
     */
    public void addCompany(Company company) throws CompanyExceptions {
        //make sure company with this name or email doesn't exist yet or throw exception
        if (checkCompanyEmailExistence(company.getEmail())) {
            throw new CompanyExceptions(ErrorMsg.COMPANY_ADD, ErrorMsg.COMPANY_EMAIL_EXIST);
        }
        if (checkCompanyNameExistence(company.getName())) {
            throw new CompanyExceptions(ErrorMsg.COMPANY_ADD, ErrorMsg.COMPANY_NAME_EXIST);
        }

        //add company
        companiesDAO.addCompany(company);

        //update company id from DB
        update_id(company);
    }

    /**
     * updates company if company exists
     *
     * @param company Company
     * @throws CompanyExceptions throws if company doesn't exist
     */
    public void updateCompany(Company company) throws CompanyExceptions {
        //make sure companyID is updated from DB
        update_id(company);

        //make sure that company that requires updating exists or throw exception
        if (!checkNameAndIdExistence(company)) {
            throw new CompanyExceptions(ErrorMsg.COMPANY_UPDATE, ErrorMsg.COMPANY_ID_AND_NAME_NOT_EXIST);
        }

        //update company
        companiesDAO.updateCompany(company);
    }

    /**
     * deletes company, company's coupons and their purchase history
     *
     * @param companyID int
     * @throws CompanyExceptions throws if company doesn't exist
     */
    public void deleteCompany(int companyID) throws CompanyExceptions {
        //make sure that company exist or throw exception
        if (!checkCompanyIdExistence(companyID)) {
            throw new CompanyExceptions(ErrorMsg.COMPANY_DELETE, ErrorMsg.COMPANY_ID_NOT_EXIST);
        }

        //delete company coupons
        couponsDAO.getAllCoupons()
                .forEach(coupon -> {
                    if (coupon.getCompanyID().equals(companyID)) {
                        couponsDAO.deleteCoupon(coupon.getId());
                        deleteCouponPurchaseHistory(coupon.getId());
                    }
                });

        //delete company
        companiesDAO.deleteCompany(companyID);

    }

    /**
     * return List of all companies
     *
     * @return List of Company
     */
    public List<Company> getAllCompanies() {
        //get all companies
        List<Company> companies = companiesDAO.getAllCompanies();

        //updates companies List of coupons
        for (Company company : companies) {
            company.setCoupons(couponsDAO.getAllCoupons().stream()
                    .filter(coupon -> coupon.getCompanyID().equals(company.getId()))
                    .collect(Collectors.toList()));
        }
        return companies;

    }

    /**
     * return company by companyID if company exists
     *
     * @param companyID int
     * @return Company
     * @throws CompanyExceptions throws if company doesn't exist
     */
    public Company getOneCompany(int companyID) throws CompanyExceptions {
        Company company;

        //make sure that company exist or throw exception
        if (!checkCompanyIdExistence(companyID)) {
            throw new CompanyExceptions(ErrorMsg.GET_COMPANY, ErrorMsg.COMPANY_ID_NOT_EXIST);
        }

        //gets company by id
        company = companiesDAO.getOneCompany(companyID);

        //updating company's coupons
        company.setCoupons(
                couponsDAO.getAllCoupons().stream()
                        .filter(coupon -> coupon.getCompanyID().equals(companyID))
                        .collect(Collectors.toList())
        );

        return company;
    }

    /**
     * adding customer if it doesn't exist yet
     *
     * @param customer Customer
     * @throws CustomerExceptions throws if customer already exist
     */
    public void addCustomer(Customer customer) throws CustomerExceptions {
        //make sure customer doesn't exist or throw exception
        if (checkCustomerEmailExistence(customer)) {
            throw new CustomerExceptions(ErrorMsg.CUSTOMER_ADD, ErrorMsg.CUSTOMER_EMAIL_EXIST);
        }

        //add customer
        customersDAO.addCustomer(customer);

        //update customerId as it in DB
        update_id(customer);
    }

    /**
     *  updates customer if exists
     * @param customer Customer
     * @throws CustomerExceptions if customer with this data doesn't exist
     */
    public void updateCustomer(Customer customer) throws CustomerExceptions {
        //make sure that customerID is updated as in DB
        update_id(customer);

        //make sure this customer exists
        if (!checkCustomerIdExistence(customer.getId())) {
            throw new CustomerExceptions(ErrorMsg.CUSTOMER_UPDATE, ErrorMsg.CUSTOMER_ID_NOT_EXIST);
        }

        //update customer
        customersDAO.updateCustomer(customer);
    }

    /**
     * deleting customer if exists and his coupons purchases
     *
     * @param customerID int
     * @throws CustomerExceptions throws if customer doesn't exist
     */
    public void deleteCustomer(int customerID) throws CustomerExceptions {
        //make sure customer exist or throw exception
        if (!checkCustomerIdExistence(customerID)) {
            throw new CustomerExceptions(ErrorMsg.CUSTOMER_DELETE, ErrorMsg.CUSTOMER_ID_NOT_EXIST);
        }

        //delete customer's coupons purchases
        customersDAO.getOneCustomer(customerID).getCoupons()
                .forEach(coupon -> couponsDAO.deleteCouponPurchase(customerID, coupon.getId()));

        //delete customer
        customersDAO.deleteCustomer(customerID);
    }

    /**
     * returns List of all customers
     *
     * @return List of Customer
     */
    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    /**
     * return Customer by id if exists
     *
     * @param customerID int
     * @return Customer
     * @throws CustomerExceptions throws if customer doesn't exist
     */
    public Customer getOneCustomer(int customerID) throws CustomerExceptions {
        //make sure that customer exists or throw exception
        if (!checkCustomerIdExistence(customerID)) {
            throw new CustomerExceptions(ErrorMsg.GET_CUSTOMER, ErrorMsg.CUSTOMER_ID_NOT_EXIST);
        }

        //get customer by id
        return customersDAO.getOneCustomer(customerID);
    }


    //private methods:

    /**
     * updates company's id if not updated
     *
     * @param company Company
     */
    private void update_id(Company company) {
        //only if companyID doesn't update
        if (company.getId() == 0) {

            //creating map for insert values to every key in the query
            Map<Integer, Object> updateId = new HashMap<>();
            updateId.put(1, company.getName());

            //run query for results
            ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(UPDATE_COMPANY_ID, updateId);

            try {
                //update companyID as it in DB
                while (resultSet != null && resultSet.next()) {
                    company.setId(resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * updates customer's id if not updated
     *
     * @param customer Customer
     */
    private void update_id(Customer customer) {
        //only if customerID doesn't update
        if (customer.getId() == 0) {

            //creating map for insert values to every key in the query
            Map<Integer, Object> updateId = new HashMap<>();
            updateId.put(1, customer.getEmail());
            updateId.put(2, customer.getPassword());

            //run query for results
            ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(UPDATE_CUSTOMER_ID, updateId);
            try {
                //update customerID as it in DB
                while (resultSet != null && resultSet.next()) {
                    customer.setId(resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * check if company with the same name already exist
     *
     * @param name String
     * @return boolean
     */
    private boolean checkCompanyNameExistence(String name) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkName = new HashMap<>();
        checkName.put(1, name);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_COMPANY_NAME_EXISTENCE, checkName);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }

    /**
     * check if company with the same email already exist
     *
     * @param email String
     * @return boolean
     */
    private boolean checkCompanyEmailExistence(String email) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkEmail = new HashMap<>();
        checkEmail.put(1, email);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_COMPANY_EMAIL_EXISTENCE, checkEmail);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);

    }

    /**
     * checks if company with rhos name and id exists
     *
     * @param company Company
     * @return boolean
     */
    private boolean checkNameAndIdExistence(Company company) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkNameAndId = new HashMap<>();
        checkNameAndId.put(1, company.getName());
        checkNameAndId.put(2, company.getId());

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_COMPANY_NAME_AND_ID_EXISTENCE, checkNameAndId);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }

    /**
     * checks if company with this id already exist
     *
     * @param id int
     * @return boolean
     */
    private boolean checkCompanyIdExistence(Integer id) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkId = new HashMap<>();
        checkId.put(1, id);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_COMPANY_ID_EXISTENCE, checkId);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }

    /**
     * deletes coupon purchase history
     *
     * @param couponId int
     */
    private void deleteCouponPurchaseHistory(int couponId) {
        //run query
        DB_Utils.runQuery(DELETE_PURCHASE_HISTORY + couponId);
    }

    /**
     * checks if customer exists by email
     *
     * @param customer Customer
     * @return boolean
     */
    private boolean checkCustomerEmailExistence(Customer customer) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkEmail = new HashMap<>();
        checkEmail.put(1, customer.getEmail());

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_CUSTOMER_EMAIL_EXISTENCE, checkEmail);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }

    /**
     * checks if customer exists by id
     *
     * @param id int
     * @return boolean
     */
    private boolean checkCustomerIdExistence(Integer id) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkId = new HashMap<>();
        checkId.put(1, id);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(
                CHECK_CUSTOMER_ID_EXISTENCE, checkId);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }
}
