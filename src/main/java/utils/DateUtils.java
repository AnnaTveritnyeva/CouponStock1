package utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    /**
     * converting LocalDate to sql Date
     *
     * @param localDate LocalDate
     * @return sql Date
     */
    public static Date localDateToSqlDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static LocalDate sqlDateToLocalDate(Date date) {
        return date.toLocalDate();
    }

    /**
     * random LocalDate for starting coupon
     *
     * @return LocalDate
     */
    public static LocalDate getStartDate() {
        return LocalDate.now().minusDays((int) (Math.random() * 14) + 1);
    }

    /**
     * random expiration Date of the coupon
     *
     * @return LocalDate
     */
    public static LocalDate getEndDate() {
        return LocalDate.now().plusDays((int) (Math.random() * 14) + 1);
    }

    /**
     * Date as we used to see in Israel
     *
     * @param localDate LocalDate
     * @return String
     */
    public static String beautifyLocalDate(LocalDate localDate) {
        return String.format("%02d/%02d/%04d",
                localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }

    /**
     * Date and time as we used to see it with time
     *
     * @param localDate LocalDate
     * @return String
     */
    public static String beautifyDateTime(LocalDateTime localDate) {
        return String.format("%02d/%02d/%04d %02d:%02d:%02d",
                localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear(),
                localDate.getHour(), localDate.getMinute(), localDate.getSecond()
        );
    }

    /**
     * The time now as we used to see it in Israel
     *
     * @return String
     */
    public static String getLocalDateTime() {
        return "[" + beautifyDateTime(LocalDateTime.now()) + "]";
    }
}
