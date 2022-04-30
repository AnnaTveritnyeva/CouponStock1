package dao;

import beans.Company;

import java.util.List;

public interface CompaniesDAO {

    /**
     * Checks if the company exists by email and password
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    boolean isCompanyExist(String email, String password);

    /**
     * Adding company
     *
     * @param company Company
     */
    void addCompany(Company company);

    /**
     * updates company details by id and name (that cannot be changed)
     *
     * @param company Company
     */
    void updateCompany(Company company);

    /**
     * deleting company by company ID
     *
     * @param companyID int
     */
    void deleteCompany(int companyID);

    /**
     * returns List of all the companies
     *
     * @return List of Company
     */
    List<Company> getAllCompanies();

    /**
     * returns company by company ID
     *
     * @param companyID int
     * @return Company
     */
    Company getOneCompany(int companyID);
}
