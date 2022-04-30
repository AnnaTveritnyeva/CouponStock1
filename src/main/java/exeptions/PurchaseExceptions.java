package exeptions;


public class PurchaseExceptions extends Exception{
    public PurchaseExceptions(String message) {
        super(message);
    }

    /**
     *
     * @param errorMsg in what method happened
     * @param msg what happened
     */
    public PurchaseExceptions(ErrorMsg errorMsg, ErrorMsg msg) {
        super(errorMsg.getMsg()+msg.getMsg());
    }
}
