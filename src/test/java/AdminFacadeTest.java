import MockData.MockData;
import beans.Company;
import beans.Customer;

import static org.junit.jupiter.api.Assertions.*;

import db_utils.DB_Manager;
import exeptions.CompanyExceptions;
import exeptions.CustomerExceptions;
import facade.AdminFacade;
import org.junit.jupiter.api.*;
import utils.ArtUtils;
import utils.TablePrinter;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminFacadeTest {
    private List<Company> companies;
    private List<Customer> customers;
    private Company companyToUpdate;
    private AdminFacade adminFacade;
    private Customer customerToUpdate;

    @BeforeAll
    public void setData() {
        try {
            DB_Manager.dropDataBase();
        } catch (SQLException e) {
            e.getMessage();
        }
        DB_Manager.createDataBase();
        DB_Manager.createAllTables();

        companies = Arrays.asList(
                MockData.mcDonaldS,
                MockData.johnBryce,
                MockData.mojo,
                MockData.elAl,
                MockData.apple
        );

        customers = Arrays.asList(
                MockData.anna,
                MockData.tal,
                MockData.zeev,
                MockData.yair,
                MockData.tomer
        );

        adminFacade = new AdminFacade();

        companyToUpdate = companies.get(4);
        customerToUpdate = customers.get(4);

        System.out.println(ArtUtils.ADMIN_FACADE);
    }

    @Test
    @Order(1)
    public void addCompanyTest() {
        assertAll(() -> companies.forEach(company -> {
            try {
                adminFacade.addCompany(company);
            } catch (CompanyExceptions e) {
                e.getMessage();
            }
        }));
    }

    @Test
    @Order(2)
    public void addExistingCompanyTest() {
        assertThrows(CompanyExceptions.class, () -> adminFacade.addCompany(companies.get(4)));
    }

    @Test
    @Order(3)
    public void getAllCompaniesTest() {
        assertDoesNotThrow(() -> TablePrinter.print(adminFacade.getAllCompanies()));
    }

    @Test
    @Order(4)
    public void updateCompanyTest()  {
        String newEmail = "my@email.com";
        companyToUpdate.setEmail(newEmail);

        assertDoesNotThrow(() -> adminFacade.updateCompany(companyToUpdate));
        assertEquals(newEmail, assertDoesNotThrow(() -> adminFacade.getOneCompany(5).getEmail()));
    }

    @Test
    @Order(5)
    public void updateCompanyIdTest() {
        companyToUpdate.setId(150);
        assertThrows(CompanyExceptions.class, () -> adminFacade.updateCompany(companyToUpdate));
    }

    @Test
    @Order(6)
    public void deleteCompanyTest() {
        assertDoesNotThrow(()-> adminFacade.deleteCompany(5));
        assertThrows(CompanyExceptions.class,()-> adminFacade.getOneCompany(5));
    }

    @Test
    @Order(7)
    public void deleteNonExistingCompanyTest() {
        assertThrows(CompanyExceptions.class, () -> adminFacade.deleteCompany(150));
    }

    @Test
    @Order(8)
    public void getOneCompanyTest() {
        assertDoesNotThrow(()->assertInstanceOf(Company.class, adminFacade.getOneCompany(2)));
    }

    @Test
    @Order(9)
    public void getNonExistingCompanyTest() {
        assertThrows(CompanyExceptions.class, () -> adminFacade.getOneCompany(150));
    }

    @Test
    @Order(10)
    public void addCustomerTest() {
        customers.forEach(customer -> assertDoesNotThrow(() ->
                adminFacade.addCustomer(customer)));
    }

    @Test
    @Order(11)
    public void addExistingCustomerTest() {
        assertThrows(CustomerExceptions.class, () -> adminFacade.addCustomer(customers.get(4)));
    }

    @Test
    @Order(12)
    public void getAllCustomersTest() {
        assertDoesNotThrow(() -> TablePrinter.print(adminFacade.getAllCustomers()));
    }

    @Test
    @Order(13)
    public void updateCustomerTest()  {
        String newEmail = "my@email.com";
        customerToUpdate.setEmail(newEmail);

        assertDoesNotThrow(() -> adminFacade.updateCustomer(customerToUpdate));
        assertEquals(newEmail, assertDoesNotThrow(() -> adminFacade.getOneCustomer(5).getEmail()));
    }

    @Test
    @Order(14)
    public void updateCustomerIdTest() {
        customerToUpdate.setId(150);
        assertThrows(CustomerExceptions.class, () -> adminFacade.updateCustomer(customerToUpdate));
    }

    @Test
    @Order(15)
    public void deleteCustomerTest() {
        assertDoesNotThrow(()-> adminFacade.deleteCustomer(5));
        assertThrows(CustomerExceptions.class,()-> adminFacade.getOneCustomer(5));
    }

    @Test
    @Order(16)
    public void deleteNonExistingCustomerTest() {
        assertThrows(CustomerExceptions.class, () -> adminFacade.deleteCustomer(150));
    }

    @Test
    @Order(17)
    public void getOneCustomerTest() {
        assertDoesNotThrow(()->assertInstanceOf(Customer.class, adminFacade.getOneCustomer(2)));
    }

    @Test
    @Order(18)
    public void getNonExistingCustomerTest() {
        assertThrows(CustomerExceptions.class, () -> adminFacade.getOneCustomer(150));
    }
}
