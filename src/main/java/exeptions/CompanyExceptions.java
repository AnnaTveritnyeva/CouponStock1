package exeptions;

public class CompanyExceptions extends Exception {

    public CompanyExceptions(String message) {
        super(message);
    }

    public CompanyExceptions(ErrorMsg errorMsg) {
        super(errorMsg.getMsg());
    }

    /**
     *
     * @param errorMsg in what method happened
     * @param msg what happened
     */
    public CompanyExceptions(ErrorMsg errorMsg, ErrorMsg msg) {
        super(errorMsg.getMsg()+msg.getMsg());
    }
}
