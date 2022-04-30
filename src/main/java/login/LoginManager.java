package login;

import exeptions.ErrorMsg;
import facade.AdminFacade;
import facade.ClientFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import utils.DateUtils;

import javax.security.auth.login.LoginException;

public class LoginManager {
    private static LoginManager instance = null;

    /**
     * constructor
     */
    private LoginManager() {
    }

    /**
     * gets instance if possible with double check
     * @return Login Manager
     */
    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    /**
     * makes login to the required type of Client facade
     * @param email String
     * @param password String
     * @param clientType String
     * @return Client Facade
     * @throws LoginException if entered invalid details
     */
    public ClientFacade login(String email, String password, ClientType clientType) throws LoginException {
        switch (clientType) {
            case Administrator:
                ClientFacade adminFacade = new AdminFacade();
                if (!adminFacade.login(email, password)) {
                    throw new LoginException(ErrorMsg.LOGIN.getMsg());
                }
                System.out.println(DateUtils.getLocalDateTime() +" "+ email + " was logged ");
                return adminFacade;

            case Company:
                ClientFacade companyFacade = new CompanyFacade();
                if (!companyFacade.login(email, password)) {
                    throw new LoginException(ErrorMsg.LOGIN.getMsg());
                }
                System.out.println(DateUtils.getLocalDateTime() +" "+ email + " was logged ");
                return companyFacade;

            case Customer:
                ClientFacade customerFacade = new CustomerFacade();
                if (!customerFacade.login(email, password)) {
                    throw new LoginException(ErrorMsg.LOGIN.getMsg());
                }
                System.out.println(DateUtils.getLocalDateTime() +" "+ email + " was logged ");
                return customerFacade;

            default:
                return null;
        }
    }
}
