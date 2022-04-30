package facade;

import beans.Category;
import beans.Company;
import beans.Coupon;
import db_utils.DB_Utils;
import dbdao.CompaniesDBDAO;
import dbdao.CouponsDBDAO;
import dbdao.CustomersDBDAO;
import exeptions.CouponsExceptions;
import exeptions.ErrorMsg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompanyFacade extends ClientFacade {
    //attributes:
    private Integer companyID;

    private final String LOGIN;

    {
        LOGIN = "SELECT * FROM `luxury_coupons`.`companies` WHERE `email`=? AND password=?";
    }

    private final String CHECK_COUPON_TITLE_AND_COMPANY_ID_EXISTENCE;

    {
        CHECK_COUPON_TITLE_AND_COMPANY_ID_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`coupons`" +
                " WHERE `title`=? AND company_id=?);";
    }

    private final String UPDATE_COUPON_ID;

    {
        UPDATE_COUPON_ID = "SELECT * FROM `luxury_coupons`.`coupons` " +
                "WHERE company_id=? AND title=?";
    }

    private final String CHECK_COUPON_ID_EXISTENCE;

    {
        CHECK_COUPON_ID_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`coupons`" +
                " WHERE id=?);";
    }


    //constructor:

    /**
     * initializes:
     * companiesDao as CompaniesDBDAO
     * customersDao as CustomersDBDAO
     * couponsDao as CouponsDBDAO
     */
    public CompanyFacade() {
        customersDAO = new CustomersDBDAO();
        companiesDAO = new CompaniesDBDAO();
        couponsDAO = new CouponsDBDAO();
    }

    //methods:

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
                //updates this companyID to the one that has logged in
                this.companyID = resultSet.getInt("id");

                //if company exists returns true
                loggedIn = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            loggedIn = false;
        }
        return loggedIn;
    }

    /**
     * adding coupon if wasn't added yet but the same company
     *
     * @param coupon Coupon
     * @throws CouponsExceptions if coupon with this title already exists in this company
     */
    public void addCoupon(Coupon coupon) throws CouponsExceptions {
        //make sure coupon with this title yet wasn't created by this company or throw exception
        if (checkCouponTitleExistence(coupon)) {
            throw new CouponsExceptions(ErrorMsg.COUPON_ADD, ErrorMsg.COUPON_TITLE_EXIST);
        }

        //updates coupon's company id to this companyID
        coupon.setCompanyID(this.companyID);

        //adding coupon
        couponsDAO.addCoupon(coupon);

        //updates couponID as it is in DB
        update_id(coupon);
    }

    /**
     * updates coupon if it exists
     * @param coupon Coupon
     * @throws CouponsExceptions if coupon with those details doesn't exist
     */
    public void updateCoupon(Coupon coupon) throws CouponsExceptions {
        //updates couponID if not updated yet
        update_id(coupon);

        //check id coupon exists or throw exception
        if (!checkCouponIdExistence(coupon.getId())) {
            throw new CouponsExceptions(ErrorMsg.COUPON_UPDATE, ErrorMsg.COUPON_ID_NOT_EXIST);
        }

        //updates coupon
        couponsDAO.updateCoupon(coupon);
    }

    /**
     * delete coupon by id
     * @param couponID int
     * @throws CouponsExceptions if coupon with this id doesn't exist
     */
    public void deleteCoupon(int couponID) throws CouponsExceptions {
        //check id coupon exists or throw exception
        if (!checkCouponIdExistence(couponID)) {
            throw new CouponsExceptions(ErrorMsg.COUPON_DELETE, ErrorMsg.COUPON_ID_NOT_EXIST);
        }

        //delete coupon
        couponsDAO.deleteCoupon(couponID);
    }

    /**
     * returns all company's coupon
     *
     * @return List of Coupon
     */
    public List<Coupon> getCompanyCoupons() {
        //return company's coupons
        return couponsDAO.getAllCoupons().stream()
                .filter(coupon -> coupon.getCompanyID().equals(companyID))
                .collect(Collectors.toList());

    }

    /**
     * returns company's coupons that matches to required category
     *
     * @param category Category
     * @return List of Coupon
     */
    public List<Coupon> getCompanyCoupons(Category category) {
        //returns company's coupons
        return couponsDAO.getAllCoupons().stream()
                .filter(coupon -> coupon.getCompanyID().equals(companyID))
                .filter(coupon -> coupon.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * returns company's coupons that under the required maximum price
     *
     * @param maxPrice double
     * @return List of Coupon
     */
    public List<Coupon> getCompanyCoupons(double maxPrice) {
        //return company's coupons
        return couponsDAO.getAllCoupons().stream()
                .filter(coupon -> coupon.getCompanyID().equals(companyID))
                .filter(coupon -> coupon.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * returns company's details
     *
     * @return Company
     */
    public Company getCompanyDetails() {
        //returns company by this companyID
        Company company = companiesDAO.getOneCompany(this.companyID);

        //updates company's coupons
        company.setCoupons(getCompanyCoupons());

        return company;
    }


    //private methods:

    /**
     * updates coupons id if not updated and if exists
     *
     * @param coupon Coupon
     */
    private void update_id(Coupon coupon) {
        //only if customerID doesn't update
        if (coupon.getId() == 0) {

            //creating map for insert values to every key in the query
            Map<Integer, Object> updateId = new HashMap<>();
            updateId.put(1, coupon.getCompanyID());
            updateId.put(2, coupon.getTitle());

            ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(UPDATE_COUPON_ID, updateId);
            try {
                //update couponID as it in DB
                while (resultSet != null && resultSet.next()) {
                    coupon.setId(resultSet.getInt("id"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * check if coupon exists by title
     *
     * @param coupon Coupon
     * @return boolean
     */
    private boolean checkCouponTitleExistence(Coupon coupon) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkNameAndCompanyId = new HashMap<>();
        checkNameAndCompanyId.put(1, coupon.getTitle());
        checkNameAndCompanyId.put(2, this.companyID);

        //runs query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(CHECK_COUPON_TITLE_AND_COMPANY_ID_EXISTENCE,
                checkNameAndCompanyId);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }

    /**
     * check if coupon id exist
     *
     * @param couponID int
     * @return boolean
     */
    private boolean checkCouponIdExistence(int couponID) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> checkCouponId = new HashMap<>();
        checkCouponId.put(1, couponID);

        //runs query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(CHECK_COUPON_ID_EXISTENCE,
                checkCouponId);

        //check existence by results from result set
        return DB_Utils.checkExistence(resultSet);
    }
}
