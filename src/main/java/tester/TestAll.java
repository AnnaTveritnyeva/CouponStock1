
package tester;

import MockData.MockData;
import beans.Category;
import db_utils.ConnectionPool;
import db_utils.DB_Manager;
import exeptions.*;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import login.ClientType;
import login.LoginManager;
import threads.CouponsExpirationDailyJob;
import utils.ArtUtils;
import utils.TablePrinter;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.LocalDate;

public class TestAll {
    public static void main(String[] args) {
        System.out.println(ArtUtils.COUPONS_PROJECT);
        //=======================DROP_AND_CREATE_SCHEMA_AND_TABLES===============================
        System.out.println("=======================DROP_AND_CREATE_SCHEMA_AND_TABLES===============================");
        //=================dropDataBase================
        System.out.println("\n=================dropDataBase================");
        try {
            DB_Manager.dropDataBase();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //===============createDataBase================
        System.out.println("\n===============createDataBase================");
        DB_Manager.createDataBase();

        //==============createAllTables==================
        System.out.println("\n==============createAllTables==================");
        DB_Manager.createAllTables();

        //================================STARTING_DAILY_JOB=====================================
        System.out.println("\n================================STARTING_DAILY_JOB=====================================");
        CouponsExpirationDailyJob couponsExpirationDailyJob = new CouponsExpirationDailyJob();
        Thread dailyJob = new Thread(couponsExpirationDailyJob);
        dailyJob.start();
        System.out.println(ArtUtils.DAILY_JOB);

        //==================================== ADMIN ============================================
        System.out.println(ArtUtils.ADMIN_FACADE);
        AdminFacade admin = null;

        //===================login=====================
        System.out.println("\n===================login=====================");
        try {
            //right details:
            admin = (AdminFacade) LoginManager.getInstance()
                    .login("admin@admin.com", "admin", ClientType.Administrator);

            //wrong details:
            /*
            admin = (AdminFacade) LoginManager.getInstance().login(
                    "wrong details", "admin", ClientType.Administrator);
            admin = (AdminFacade) LoginManager.getInstance().login(
                    "admin@admin.com", "wrong details", ClientType.Administrator);
            */
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }

        if (admin != null) {
            //==================addCompany==================
            System.out.println("\n==================addCompany==================");

            try {
                //company doesn't exist yet:
                admin.addCompany(MockData.apple);
                admin.addCompany(MockData.mojo);
                admin.addCompany(MockData.elAl);
                admin.addCompany(MockData.johnBryce);
                admin.addCompany(MockData.mcDonaldS);

                //company already exists:
                //admin.addCompany(MockData.apple);
            } catch (CompanyExceptions e) {
                System.out.println(e.getMessage());
            }

            //============updateCompany=====================
            System.out.println("\n============updateCompany=====================");
            try {
                //right details to update:
                MockData.mcDonaldS.setPassword("myBurger");
                MockData.mcDonaldS.setEmail("burger@gmail.com");
                admin.updateCompany(MockData.mcDonaldS);

                //wrong details to update:
                /*
                MockData.mojo.setId(17);
                admin.updateCompany(MockData.mojo);
                */
            } catch (CompanyExceptions e) {
                System.out.println(e.getMessage());
            }

            //==============deleteCompany======================
            System.out.println("\n============deleteCompany=====================");
            try {
                //right details:
                admin.deleteCompany(MockData.mcDonaldS.getId());

                //wrong details:
                admin.deleteCompany(100);
            } catch (CompanyExceptions e) {
                System.out.println(e.getMessage());
            }

            //=============getAllCompanies======================
            System.out.println("\n=============getAllCompanies===================");
            TablePrinter.print(admin.getAllCompanies());


            //=============getOneCompany========================
            System.out.println("\n=============getOneCompany=====================");
            try {
                //right details:
                TablePrinter.print(admin.getOneCompany(MockData.elAl.getId()));
                TablePrinter.print(admin.getOneCompany(3));

                //wrong details:
                //TablePrinter.print(admin.getOneCompany(100));


            } catch (CompanyExceptions e) {
                System.out.println(e.getMessage());
            }

            //===============addCustomer========================
            System.out.println("\n===============addCustomer========================");
            try {
                //right details:
                admin.addCustomer(MockData.tomer);
                admin.addCustomer(MockData.anna);
                admin.addCustomer(MockData.tal);
                admin.addCustomer(MockData.yair);
                admin.addCustomer(MockData.zeev);

                //wrong:
                //admin.addCustomer(MockData.zeev);
            } catch (CustomerExceptions e) {
                System.out.println(e.getMessage());
            }
            //===============updateCustomer=======================
            System.out.println("\n===============updateCustomer=======================");
            try {
                //right details:
                MockData.zeev.setEmail("zeevMindali@gmail.com");
                admin.updateCustomer(MockData.zeev);

                //wrong details:
                /*
                MockData.zeev.setId(15);
                admin.updateCustomer(MockData.zeev);
                */
            } catch (CustomerExceptions e) {
                System.out.println(e.getMessage());
            }

            //==================deleteCustomer====================
            System.out.println("\n==================deleteCustomer====================");
            try {
                //right details:
                admin.deleteCustomer(MockData.zeev.getId());

                //wrong details:
                //admin.deleteCustomer(100);
            } catch (CustomerExceptions e) {
                System.out.println(e.getMessage());
            }

            //==================getAllCustomers===================
            System.out.println("\n==================getAllCustomers===================");
            TablePrinter.print(admin.getAllCustomers());

            //=================getOneCustomer=======================
            System.out.println("\n=================getOneCustomer=======================");
            try {
                //right details:
                TablePrinter.print(admin.getOneCustomer(MockData.anna.getId()));

                //wrong details:
                //TablePrinter.print(admin.getOneCustomer(100));
            } catch (CustomerExceptions e) {
                System.out.println(e.getMessage());
            }
        }

        //=====================================COMPANY=======================================
        System.out.println(ArtUtils.COMPANY_FACADE);
        CompanyFacade company = null;

        //===========================login=============================
        System.out.println("\n===========================login=============================");
        try {
            //right details:
            company = (CompanyFacade) LoginManager.getInstance()
                    .login(
                            MockData.johnBryce.getEmail(),
                            MockData.johnBryce.getPassword(),
                            ClientType.Company);


            //wrong details:
                /*
                company = (CompanyFacade) LoginManager.getInstance().login(
                        "wrong details",
                        MockData.mcDonaldS.getPassword(),
                        ClientType.Company);
                company = (CompanyFacade) LoginManager.getInstance().login(
                        MockData.mcDonaldS.getEmail(),
                        "wrong details",
                        ClientType.Company);
                */
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }

        if (company != null) {
            //=========================addCoupon=============================
            System.out.println("\n=========================addCoupon=============================");
            try {
                //new coupons
                company.addCoupon(MockData.appleCoupon);
                company.addCoupon(MockData.mojoCoupon);
                company.addCoupon(MockData.elAlCoupon);
                company.addCoupon(MockData.mcDonaldSCoupon);
                company.addCoupon(MockData.johnBryceCoupon);

                //coupon that already exists:
                //company.addCoupon(MockData.johnBryceCoupon);
            } catch (CouponsExceptions e) {
                System.out.println(e.getMessage());
            }
            //=======================updateCoupon=============================
            System.out.println("\n=======================updateCoupon=============================");
            try {
                //right details:
                MockData.appleCoupon.setAmount(15);
                company.updateCoupon(MockData.appleCoupon);

                //wrong details:
                    /*
                    MockData.appleCoupon.setId(10);
                    company.updateCoupon(MockData.appleCoupon);
                     */
            } catch (CouponsExceptions e) {
                System.out.println(e.getMessage());
            }


            //=========================deleteCoupon=============================
            System.out.println("\n=========================deleteCoupon=============================");
            try {
                //right details:
                company.deleteCoupon(MockData.appleCoupon.getId());

                //wrong details:
                //company.deleteCoupon(100);
            } catch (CouponsExceptions e) {
                System.out.println(e.getMessage());
            }

            //=========================getCompanyCoupons=============================
            System.out.println("\n=========================getCompanyCoupons=============================");
            //without parameters:
            TablePrinter.print(company.getCompanyCoupons());

            //by category:
            TablePrinter.print(company.getCompanyCoupons(Category.Food));

            //filtered by maxPrice:
            TablePrinter.print(company.getCompanyCoupons(100.0));

            //=========================getCompanyDetails=============================
            System.out.println("\n=========================getCompanyDetails=============================");
            TablePrinter.print(company.getCompanyDetails());
        }

        //====================================CUSTOMER=======================================
        System.out.println(ArtUtils.CUSTOMER_FACADE);
        CustomerFacade customer = null;

        //===========================login=============================
        System.out.println("\n===========================login=============================");
        try {
            //right details:
            customer = (CustomerFacade) LoginManager.getInstance()
                    .login(
                            MockData.anna.getEmail(),
                            MockData.anna.getPassword(),
                            ClientType.Customer);

            //wrong details:
                /*
                customer = (CustomerFacade) LoginManager.getInstance().login(
                        "wrong details",
                        MockData.mcDonaldS.getPassword(),
                        ClientType.Customer);
                customer = (CustomerFacade) LoginManager.getInstance().login(
                        MockData.mcDonaldS.getEmail(),
                        "wrong details",
                        ClientType.Customer);
                 */
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }

        if (customer != null) {
            //=====================addCouponPurchase=============================
            System.out.println("\n=====================addCouponPurchase=============================");
            try {
                //right details:
                customer.purchaseCoupon(MockData.elAlCoupon);
                customer.purchaseCoupon(MockData.mcDonaldSCoupon);

                /*
                //wrong details:
                //purchase 2 times same coupon
                customer.purchaseCoupon(MockData.elAlCoupon);
                //purchase coupon with 0 available amount:
                MockData.mojoCoupon.setAmount(0);
                customer.purchaseCoupon(MockData.mojoCoupon);
                //purchase coupon after his expiry date:
                MockData.johnBryceCoupon.setEndDate(LocalDate.of(2021, 12, 31));
                customer.purchaseCoupon(MockData.johnBryceCoupon);
                */
            } catch (PurchaseExceptions e) {
                System.out.println(e.getMessage());
            }

            //=====================getAllCustomerPurchases========================
            System.out.println("\n=====================getAllCustomerPurchases========================");
            try {
                //all:
                TablePrinter.print(customer.getCustomerCoupons());
                //by category:
                System.out.println("by category:");
                TablePrinter.print(customer.getCustomerCoupons(Category.Electricity));
                //filtered by price:
                System.out.println("filtered by price:");
                TablePrinter.print(customer.getCustomerCoupons(200.0));
            } catch (CustomerExceptions e) {
                System.out.println(e.getMessage());
            }

            //=======================getCustomerDetails==========================
            System.out.println("\n=======================getCustomerDetails==========================");
            TablePrinter.print(customer.getCustomerDetails());
        }


        //================================STOPPING_DAILY_JOB=====================================
        System.out.println("\n================================STOPPING_DAILY_JOB=====================================");
        couponsExpirationDailyJob.stopThread();

        //stop it immediately
        dailyJob.interrupt();

        //==============================CLOSING_ALL_CONNECTIONS==================================
        System.out.println("\n=============================CLOSING_ALL_CONNECTIONS====================================");
        try {
            ConnectionPool.getInstance().closeAllConnection();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
