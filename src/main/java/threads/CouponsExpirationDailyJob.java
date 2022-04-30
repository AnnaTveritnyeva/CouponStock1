package threads;

import dao.CouponsDAO;
import db_utils.DB_Utils;
import dbdao.CouponsDBDAO;
import lombok.Data;

@Data
public class CouponsExpirationDailyJob implements Runnable {
    private CouponsDAO couponsDAO = new CouponsDBDAO();
    private Boolean quit = false;

    private final String DAILY_JOB;

    /**
     * initializes daily job DB query
     */
    public CouponsExpirationDailyJob() {
        DAILY_JOB = "DELETE FROM luxury_coupons.coupons WHERE end_date < CURDATE()";
    }

    /**
     * runs the daily job until it's being stopped
     */
    @Override
    public void run() {
        //works while wasn't stopped or caught an exception
        while (!quit) {
            DB_Utils.runQuery(DAILY_JOB);

            try {
                //works every 24 hours
                Thread.sleep(1000 * 60 * 60 * 24);
            } catch (InterruptedException e) {
                this.quit = true;
                System.out.println("Daily job was stopped");
            }
        }
    }

    /**
     * stops the daily job before it tries to do another round of deleting
     */
    public void stopThread() {
        this.quit = true;
    }
}
