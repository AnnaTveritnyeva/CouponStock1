package dbdao;

import beans.Category;
import beans.Coupon;
import dao.CouponsDAO;
//import db_utils.ConnectionPool;
import db_utils.DB_Utils;
import utils.DateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponsDBDAO implements CouponsDAO {
    //attributes:

    //private ConnectionPool connectionPool;

    private final String ADD_COUPON;

    {
        ADD_COUPON = "INSERT INTO `luxury_coupons`.`coupons` " +
                "(`company_id`,`category_id`,`title`,`description`," +
                "`start_date`, `end_date`,`amount`,`price`,`image`)" +
                "VALUES (?,?,?,?,?,?,?,?,?);";
    }

    private final String UPDATE_COUPON;

    {
        UPDATE_COUPON = "UPDATE luxury_coupons.coupons " +
                "SET `category_id` = ? , `title` = ? , `description` = ? , `start_date`=?, " +
                "`end_date` = ?, `amount` = ?, `price` = ?, `image`=? WHERE id = ? AND `company_id`=?";
    }

    private final String DELETE_COUPON;

    {
        DELETE_COUPON = "DELETE FROM `luxury_coupons`.`coupons` " +
                "WHERE `id`=";
    }

    private final String GET_ALL_COUPONS;

    {
        GET_ALL_COUPONS = "SELECT * FROM `luxury_coupons`.`coupons`";
    }

    private final String GET_ONE_COUPON;

    {
        GET_ONE_COUPON = "SELECT * FROM `luxury_coupons`.`coupons` " +
                "WHERE `id`=";
    }

    private final String ADD_COUPON_PURCHASE;

    {
        ADD_COUPON_PURCHASE = "INSERT INTO `luxury_coupons`.`customers_coupons`" +
                "(`customer_id`,`coupon_id`) VALUES (?,?);";
    }

    private final String DELETE_COUPON_PURCHASE;

    {
        DELETE_COUPON_PURCHASE = "DELETE FROM `luxury_coupons`.`customers_coupons` " +
                "WHERE `customer_id` = ? AND `coupon_id` = ?";
    }

    private final String UPDATE_COUPON_AMOUNT;

    {
        UPDATE_COUPON_AMOUNT = "UPDATE `luxury_coupons`.`coupons` " +
                "SET amount = amount-1 " +
                "WHERE id =?";
    }

    private final String DELETE_COUPON_HISTORY;

    {
        DELETE_COUPON_HISTORY = "DELETE FROM `luxury_coupons`.`customers_coupons` " +
                "WHERE `coupon_id` = ?";
    }

    //methods:

    /**
     * adding coupon
     *
     * @param coupon Coupon
     */
    @Override
    public void addCoupon(Coupon coupon) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> addCoupon = new HashMap<>();
        addCoupon.put(1, coupon.getCompanyID());
        addCoupon.put(2, coupon.getCategory().getValue());
        addCoupon.put(3, coupon.getTitle());
        addCoupon.put(4, coupon.getDescription());
        addCoupon.put(5, coupon.getStartDate());
        addCoupon.put(6, coupon.getEndDate());
        addCoupon.put(7, coupon.getAmount());
        addCoupon.put(8, coupon.getPrice());
        addCoupon.put(9, coupon.getImage());

        //run query
        DB_Utils.runQuery(ADD_COUPON, addCoupon);
        System.out.println("Coupon was successfully added");
    }

    /**
     * updating coupon by couponID and companyID
     *
     * @param coupon Coupon
     */
    @Override
    public void updateCoupon(Coupon coupon) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> update = new HashMap<>();
        update.put(1, coupon.getCategory().getValue());
        update.put(2, coupon.getTitle());
        update.put(3, coupon.getDescription());
        update.put(4, coupon.getStartDate());
        update.put(5, coupon.getEndDate());
        update.put(6, coupon.getAmount());
        update.put(7, coupon.getPrice());
        update.put(8, coupon.getImage());
        update.put(9, coupon.getId());
        update.put(10, coupon.getCompanyID());

        //run query
        DB_Utils.runQuery(UPDATE_COUPON, update);
        System.out.println("Coupon was successfully updated!");
    }

    /**
     * deleting coupon with it history
     *
     * @param couponID int
     */
    @Override
    public void deleteCoupon(int couponID) {
        //using method for deleting coupon history
        deleteCouponHistory(couponID);
        //run query
        DB_Utils.runQuery(DELETE_COUPON + couponID);
        System.out.println("Coupon was successfully deleted");
    }

    /**
     * returns a List of all coupons
     *
     * @return List of Coupon
     */
    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> allCoupons = new ArrayList<>();

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_ALL_COUPONS);

        try {
            //adding new coupon to List from DB Table by resultSet
            while (resultSet.next()) {
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
                        resultSet.getString("image")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allCoupons;
    }

    /**
     * returns Coupon by couponID
     *
     * @param couponID int
     * @return Coupon
     */
    @Override
    public Coupon getOneCoupon(int couponID) {
        Coupon coupon = null;

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_ONE_COUPON + couponID);
        try {
            //creates coupon from DB Table by resultSet
            while (resultSet.next()) {
                coupon = (new Coupon(
                        resultSet.getInt("id"),
                        resultSet.getInt("company_id"),
                        Category.valueOf(resultSet.getInt("category_id")),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        DateUtils.sqlDateToLocalDate(resultSet.getDate("start_date")),
                        DateUtils.sqlDateToLocalDate(resultSet.getDate("end_date")),
                        resultSet.getInt("amount"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coupon;
    }

    /**
     * adds coupon purchase by customerID and couponID and updates coupon amount after purchase
     *
     * @param customerID int
     * @param couponID   int
     */
    @Override
    public void addCouponPurchase(int customerID, int couponID) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> addPurchase = new HashMap<>();
        addPurchase.put(1, customerID);
        addPurchase.put(2, couponID);

        //run query
        DB_Utils.runQuery(ADD_COUPON_PURCHASE, addPurchase);
        //update coupon amount after purchase
        updateCouponAmount(couponID);
        System.out.println("Purchase was completed successfully");
    }

    /**
     * deleting coupon purchase by customerID and couponID
     *
     * @param customerID int
     * @param couponID   int
     */
    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> deletePurchase = new HashMap<>();
        deletePurchase.put(1, customerID);
        deletePurchase.put(2, couponID);

        //run query
        DB_Utils.runQuery(DELETE_COUPON_PURCHASE, deletePurchase);
        System.out.println("Purchase was successfully deleted");
    }


    //private methods:

    /**
     * private method for deleting coupon history by couponID
     *
     * @param couponID int
     */
    private void deleteCouponHistory(int couponID) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> deleteCouponHistory = new HashMap<>();
        deleteCouponHistory.put(1, couponID);

        //run query
        DB_Utils.runQuery(DELETE_COUPON_HISTORY, deleteCouponHistory);
    }

    /**
     * decreases coupon amount by 1
     *
     * @param couponID int
     */
    private void updateCouponAmount(int couponID) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> updateCouponAmount = new HashMap<>();
        updateCouponAmount.put(1, couponID);

        //run query
        DB_Utils.runQuery(UPDATE_COUPON_AMOUNT, updateCouponAmount);
    }
}
