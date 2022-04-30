package facade;

import dao.CompaniesDAO;
import dao.CouponsDAO;
import dao.CustomersDAO;

public abstract class ClientFacade {
    //attributes:
    protected CompaniesDAO companiesDAO;
    protected CustomersDAO customersDAO;
    protected CouponsDAO couponsDAO;

    //method:
    public boolean login(String email, String password) {
        return false;
    }
}
