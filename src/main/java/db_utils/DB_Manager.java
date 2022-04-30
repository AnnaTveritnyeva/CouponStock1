package db_utils;

import beans.Category;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DB_Manager {
    //DB login details:
    public static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String USER_NAME = "root";
    public static final String USER_PASS = "12345678";

    //max connection possible
    public static final int MAX_CONNECTION = 10;

    //queries:
    public static final String CREATE_DB = "CREATE DATABASE IF NOT EXISTS `luxury_coupons`";

    public static final String DROP_DB = "DROP DATABASE IF EXISTS `luxury_coupons`";

    public static final String CREATE_TABLE_COMPANIES;

    static {
        CREATE_TABLE_COMPANIES = "CREATE TABLE `luxury_coupons`.`companies` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`));";
    }

    public static final String CREATE_TABLE_CUSTOMERS;

    static {
        CREATE_TABLE_CUSTOMERS = "CREATE TABLE `luxury_coupons`.`customers` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `first_name` VARCHAR(45) NOT NULL,\n" +
                "  `last_name` VARCHAR(45) NOT NULL,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`));\n";
    }

    public static final String CREATE_TABLE_CATEGORIES;

    static {
        CREATE_TABLE_CATEGORIES = "CREATE TABLE `luxury_coupons`.`categories` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`));";
    }

    public static final String CREATE_TABLE_COUPONS;

    static {
        CREATE_TABLE_COUPONS = "\n" +
                "CREATE TABLE `luxury_coupons`.`coupons` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `company_id` INT NOT NULL,\n" +
                "  `category_id` INT NOT NULL,\n" +
                "  `title` VARCHAR(45) NOT NULL,\n" +
                "  `description` VARCHAR(45) NOT NULL,\n" +
                "  `start_date` DATE NOT NULL,\n" +
                "  `end_date` DATE NOT NULL,\n" +
                "  `amount` INT NOT NULL,\n" +
                "  `price` DOUBLE NOT NULL,\n" +
                "  `image` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  INDEX `company_id_idx` (`company_id` ASC) VISIBLE,\n" +
                "  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `company_id`\n" +
                "    FOREIGN KEY (`company_id`)\n" +
                "    REFERENCES `luxury_coupons`.`companies` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `category_id`\n" +
                "    FOREIGN KEY (`category_id`)\n" +
                "    REFERENCES `luxury_coupons`.`categories` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";
    }

    public static final String CREATE_TABLE_CUSTOMERS_VS_COUPONS;

    static {
        CREATE_TABLE_CUSTOMERS_VS_COUPONS = "CREATE TABLE `luxury_coupons`.`customers_coupons` (\n" +
                "  `customer_id` INT NOT NULL,\n" +
                "  `coupon_id` INT NOT NULL,\n" +
                "  PRIMARY KEY (`customer_id`, `coupon_id`),\n" +
                "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `customer_id`\n" +
                "    FOREIGN KEY (`customer_id`)\n" +
                "    REFERENCES `luxury_coupons`.`customers` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `coupon_id`\n" +
                "    FOREIGN KEY (`coupon_id`)\n" +
                "    REFERENCES `luxury_coupons`.`coupons` (`id`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";
    }

    public static final String DROP_TABLE_COMPANIES;

    static {
        DROP_TABLE_COMPANIES = "DROP TABLE IF EXISTS `luxury_coupons`.`companies`";
    }

    public static final String DROP_TABLE_CUSTOMERS;

    static {
        DROP_TABLE_CUSTOMERS = "DROP TABLE IF EXISTS `luxury_coupons`.`customers`";
    }

    public static final String DROP_TABLE_CATEGORIES;

    static {
        DROP_TABLE_CATEGORIES = "DROP TABLE IF EXISTS `luxury_coupons`.`categories`";
    }

    public static final String DROP_TABLE_COUPONS;

    static {
        DROP_TABLE_COUPONS = "DROP TABLE IF EXISTS `luxury_coupons`.`coupons`";
    }

    public static final String DROP_TABLE_CUSTOMERS_VS_COUPONS;

    static {
        DROP_TABLE_CUSTOMERS_VS_COUPONS = "DROP TABLE IF EXISTS `luxury_coupons`.`customers_coupons`";
    }

    private static final String ADD_CATEGORY;

    static {
        ADD_CATEGORY = "INSERT INTO `luxury_coupons`.`categories` " +
                "(id , name) VALUES (?,?)";
    }


    //methods:

    /**
     * creates DB
     */
    public static void createDataBase() {
        DB_Utils.runQuery(CREATE_DB);
        System.out.println("DB was successfully created");
    }

    /**
     * drops DB
     *
     * @throws SQLException if query has not succeed
     */
    public static void dropDataBase() throws SQLException {
        DB_Utils.runQuery(DROP_DB);
        System.out.println("DB was successfully deleted");
    }

    /**
     * creates table by table category
     *
     * @param tableCategory Table Category
     */
    public static void createTable(TableCategories tableCategory) {
        switch (tableCategory) {
            case COMPANIES:
                DB_Utils.runQuery(CREATE_TABLE_COMPANIES);
                break;
            case CUSTOMERS:
                DB_Utils.runQuery(CREATE_TABLE_CUSTOMERS);
                break;
            case CATEGORIES:
                DB_Utils.runQuery(CREATE_TABLE_CATEGORIES);
                DB_Manager.addAllCategories();
                break;
            case COUPONS:
                DB_Utils.runQuery(CREATE_TABLE_COUPONS);
                break;
            case CUSTOMERS_VS_COUPONS:
                DB_Utils.runQuery(CREATE_TABLE_CUSTOMERS_VS_COUPONS);
                break;
        }
    }

    /**
     * creates all tables
     */
    public static void createAllTables() {
        for (TableCategories tableCategories : TableCategories.values()) {
            createTable(tableCategories);
        }
        System.out.println("All tables was successfully created");
    }

    /**
     * drops table by table category
     *
     * @param tableCategory TableCategory
     */
    public static void dropTable(TableCategories tableCategory) {
        switch (tableCategory) {
            case COMPANIES:
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_COMPANIES);
                break;
            case CUSTOMERS:
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS);
                break;
            case CATEGORIES:
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_CATEGORIES);
                break;
            case COUPONS:
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
                DB_Utils.runQuery(DROP_TABLE_COUPONS);
                break;
            case CUSTOMERS_VS_COUPONS:
                DB_Utils.runQuery(DROP_TABLE_CUSTOMERS_VS_COUPONS);
                break;
        }
    }

    /**
     * drops all tables
     */
    public static void dropAllTables() {
        for (TableCategories tableCategories : TableCategories.values()) {
            dropTable(tableCategories);
        }
    }


    // private methods:

    /**
     * adds category to DB
     *
     * @param category Category
     */
    private static void addCategory(Category category) {
        Map<Integer, Object> addCategory = new HashMap<>();
        addCategory.put(1, category.getValue());
        addCategory.put(2, category.name());

        DB_Utils.runQuery(ADD_CATEGORY, addCategory);

    }

    /**
     * adds All Categories to DB
     */
    private static void addAllCategories() {
        for (Category category : Category.values()) {
            addCategory(category);
        }
    }
}
