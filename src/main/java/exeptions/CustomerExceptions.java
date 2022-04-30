package exeptions;

public class CustomerExceptions extends Exception {
    public CustomerExceptions(String message) {
        super(message);
    }

    public CustomerExceptions(ErrorMsg errorMsg) {
        super((errorMsg.getMsg()));
    }

    /**
     *
     * @param errorMsg in what method happened
     * @param msg what happened
     */
    public CustomerExceptions(ErrorMsg errorMsg, ErrorMsg msg) {
        super(errorMsg.getMsg()+msg.getMsg());
    }
}
