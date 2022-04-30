package exeptions;

public enum ErrorMsg {
    ERROR("ERROR "),

    //Company exceptions:
    COMPANY_NAME_EXIST("this company name already exist"),
    COMPANY_EMAIL_EXIST("this company email already exist"),
    COMPANY_ID_NOT_EXIST("company with this id doesn't exist"),
    COMPANY_ID_AND_NAME_NOT_EXIST("company with this name and id doesn't exist"),

    //Customer exceptions:
    CUSTOMER_EMAIL_EXIST("this customer email already exist"),
    CUSTOMER_ID_NOT_EXIST("customer with this id doesn't exist"),

    //Coupon exceptions
    COUPON_TITLE_EXIST("coupon with this title already exist in this company"),
    COUPON_ID_NOT_EXIST("coupon with this id doesn't exist"),

    //Purchase exceptions
    ALREADY_BOUGHT_COUPON("you already bought this coupon"),
    NO_COUPONS_LEFT("there's no more coupons left to buy"),
    COUPON_EXPIRED("coupon expired"),

    //Company methods:
    COMPANY_ADD(ERROR.getMsg() + "Cannot add company: "),
    COMPANY_UPDATE(ERROR.getMsg() + "Cannot update company: "),
    COMPANY_DELETE(ERROR.getMsg() + "Cannot delete company: "),
    GET_COMPANY(ERROR.getMsg() + "Cannot find company: "),
    GET_COMPANY_COUPONS(ERROR.getMsg() + "No coupons found"),

    //Customer methods:
    CUSTOMER_ADD(ERROR.getMsg() + "Cannot add customer: "),
    CUSTOMER_UPDATE(ERROR.getMsg() + "Cannot update customer: "),
    CUSTOMER_DELETE(ERROR.getMsg() + "Cannot delete customer: "),
    GET_CUSTOMER(ERROR.getMsg() + "Cannot find customer: "),
    GET_CUSTOMER_PURCHASES(ERROR.getMsg() + "No coupons purchases found"),

    //Coupon methods
    COUPON_ADD(ERROR.getMsg() + "Cannot add coupon: "),
    COUPON_UPDATE(ERROR.getMsg() + "Cannot update coupon: "),
    COUPON_DELETE(ERROR.getMsg() + "Cannot delete coupon: "),
    GET_COUPON(ERROR.getMsg() + "Cannot find coupon: "),

    //Purchase method
    ADD_COUPON_PURCHASE(ERROR.getMsg() + "Cannot make coupon purchase: "),

    //login method
    LOGIN(ERROR.getMsg() + "Cannot login: invalid email or password");

    private String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
