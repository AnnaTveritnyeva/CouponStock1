package dbdao;

import beans.Company;
import dao.CompaniesDAO;
//import db_utils.ConnectionPool;
import db_utils.DB_Utils;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
public class CompaniesDBDAO implements CompaniesDAO {
    //attributes:

    //private ConnectionPool connectionPool;

    private final String CHECK_COMPANY_EXISTENCE;

    {
        CHECK_COMPANY_EXISTENCE = "SELECT EXISTS (SELECT * FROM `luxury_coupons`.`companies`" +
                " WHERE `email`=? AND `password`= ?);";
    }

    private final String ADD_COMPANY;

    {
        ADD_COMPANY = "INSERT INTO `luxury_coupons`.`companies` " +
                "(`name`,`email`,`password`)" +
                "VALUES (?,?,?);";
    }

    private final String UPDATE_COMPANY;

    {
        UPDATE_COMPANY = "UPDATE luxury_coupons.companies " +
                "SET email=? , password=? WHERE id =? AND name=?";
    }

    private final String DELETE_COMPANY;

    {
        DELETE_COMPANY = "DELETE FROM `luxury_coupons`.`companies` WHERE `id`=";
    }

    private final String GET_ALL_COMPANIES;

    {
        GET_ALL_COMPANIES = "SELECT * FROM `luxury_coupons`.`companies`;";
    }

    private final String GET_COMPANY;

    {
        GET_COMPANY = "SELECT * FROM `luxury_coupons`.`companies` WHERE `id`=";
    }


    //methods:

    /**
     * Checks if the company exists by email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean isCompanyExist(String email, String password) {
        boolean exists = false;
        //creating map for insert values to every key in the query
        Map<Integer, Object> findCompany = new HashMap<>();
        findCompany.put(1, email);
        findCompany.put(2, password);

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(CHECK_COMPANY_EXISTENCE, findCompany);

        try {
            while (resultSet.next()) {
                //makes "exists" return the same result as the query for checking company existence
                exists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exists;
    }

    /**
     * Adding company
     *
     * @param company Company
     */
    @Override
    public void addCompany(Company company) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> addCompany = new HashMap<>();
        addCompany.put(1, company.getName());
        addCompany.put(2, company.getEmail());
        addCompany.put(3, company.getPassword());

        //run query
        DB_Utils.runQuery(ADD_COMPANY, addCompany);
        System.out.println("Company added successfully!");
    }

    /**
     * updates company details by id and name (that cannot be changed)
     *
     * @param company Company
     */
    @Override
    public void updateCompany(Company company) {
        //creating map for insert values to every key in the query
        Map<Integer, Object> companyDetails = new HashMap<>();
        companyDetails.put(1, company.getEmail());
        companyDetails.put(2, company.getPassword());
        companyDetails.put(3, company.getId());
        companyDetails.put(4, company.getName());

        //run query
        DB_Utils.runQuery(UPDATE_COMPANY, companyDetails);
        System.out.println("Company was successfully updated!");
    }

    /**
     * deleting company by company ID
     *
     * @param companyID int
     */
    @Override
    public void deleteCompany(int companyID) {
        //run query
        DB_Utils.runQuery(DELETE_COMPANY + companyID);
        System.out.println("Company was successfully deleted");
    }

    /**
     * returns List of all the companies
     *
     * @return List of Company
     */
    @Override
    public List<Company> getAllCompanies() {
        List<Company> allCompanies = new ArrayList<>();

        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_ALL_COMPANIES);

        try {
            //adding new company to List from DB Table by resultSet
            while (resultSet.next()) {
                allCompanies.add(new Company(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allCompanies;
    }

    /**
     * returns company by company ID
     *
     * @param companyID int
     * @return Company
     */
    @Override
    public Company getOneCompany(int companyID) {
        //run query for results
        ResultSet resultSet = (ResultSet) DB_Utils.runQueryForResult(GET_COMPANY + companyID);
        Company company = null;

        try {
            //creating company by results from DB table
            while (resultSet.next()) {
                company = new Company(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return company;
    }
}
