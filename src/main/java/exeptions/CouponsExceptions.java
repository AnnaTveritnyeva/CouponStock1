package exeptions;

public class CouponsExceptions extends Exception {
    public CouponsExceptions(String message) {
        super(message);
    }

    /**
     *
     * @param errorMsg in what method happened
     * @param msg what happened
     */
    public CouponsExceptions(ErrorMsg errorMsg, ErrorMsg msg) {
        super(errorMsg.getMsg()+msg.getMsg());
    }
}
