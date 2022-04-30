package dao;


import beans.Coupon;

import java.util.List;

public interface CouponsDAO {
    /**
     * adding coupon
     *
     * @param coupon Coupon
     */
    void addCoupon(Coupon coupon);

    /**
     * updating coupon by couponID and companyID
     *
     * @param coupon Coupon
     */
    void updateCoupon(Coupon coupon);

    /**
     * deleting coupon with it history
     *
     * @param couponID int
     */
    void deleteCoupon(int couponID);

    /**
     * returns a List of all coupons
     *
     * @return List of Coupon
     */
    List<Coupon> getAllCoupons();

    /**
     * returns Coupon by couponID
     *
     * @param couponID int
     * @return Coupon
     */
    Coupon getOneCoupon(int couponID);

    /**
     * adds coupon purchase by customerID and couponID and updates coupon amount after purchase
     *
     * @param customerID int
     * @param couponID   int
     */
    void addCouponPurchase(int customerID, int couponID);

    /**
     * deleting coupon purchase by customerID and couponID
     *
     * @param customerID int
     * @param couponID   int
     */
    void deleteCouponPurchase(int customerID, int couponID);
}
