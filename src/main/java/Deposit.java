import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

import static java.time.temporal.ChronoUnit.DAYS;

public class Deposit implements Serializable {

    private final static double TAXES = 0.196;
//    private final static double TAXES = 0.0;

    private LocalDateTime openDate;
    private LocalDateTime expirationDate;
    private double amount;
    private double percent;
    private double[] prognosis;

    static final long ONE_HOUR = 3600L;

    private boolean isLeapYear(LocalDateTime date) {
        boolean result = date.getYear() % 4 == 0;
        if (date.getYear() % 100 == 0)
            result = false;
        if (date.getYear() % 100 == 0)
            result = true;
        return result;
    }

    public static long daysBetween(LocalDateTime d1, LocalDateTime d2) {
        return DAYS.between(d1, d2);
    }

    public static long secondsBetween(LocalDateTime d1, LocalDateTime d2) {
        return Duration.between(d1, d2).getSeconds();
    }

    public Deposit(LocalDateTime openDate, int months, double amount, double percent) {
        this.openDate = openDate.plusDays(1);
        this.expirationDate = this.openDate.plusMonths(months);
        this.amount = amount;
        this.percent = percent / 100.0;
        prognosis = new double[months];
        float total = (float) (amount * this.percent);

        for (int i=0; i<months;i++) {
            int days = ( (openDate.plusMonths(i)).getMonth().length(isLeapYear(openDate.plusMonths(i))));
            prognosis[i] = Math.floor( (total * days / 365 * ( 1 - TAXES ) ) * 100) / 100.0;
        }
    }

    public double getWidthrawed() {
        double summ = 0.0;
        int m = monthElapsed();
        for (int i=0; i<m; i++) {
            summ += prognosis[i];
        }
        return summ;
    }

    public double getNextWidthrraw() {
        return prognosis[monthElapsed()];
    }

    public double getNextWidthrraw(LocalDateTime date) {
        int m = monthElapsed(date);
        if (m < 0)
            return 0.0;
        return prognosis[m];
    }

    public double getAmount() {
        return amount;
    }

    public double getWidthrraw(LocalDateTime date) {
        int m = monthElapsed(date);
        if (m <= 0)
            return 0.0;
        return prognosis[m-1];
    }

    private int monthElapsed() {
        if (LocalDateTime.now().isAfter(expirationDate))
            return -2;
        return (int) ChronoUnit.MONTHS.between(openDate, LocalDateTime.now());
    }

    private int monthElapsed(LocalDateTime date) {
        if (date.isAfter(expirationDate))
            return -2;
        return (int) ChronoUnit.MONTHS.between(openDate, date);
    }

    public double getAccurued() {
        int m = monthElapsed();
        if (m < 0) return 0.0;
        long sec = secondsBetween(openDate.plusMonths(m), LocalDateTime.now());
        int days = ( (openDate.plusMonths(m)).getMonth().length(isLeapYear(openDate.plusMonths(m))));
        long secondsInCurrentMonth  =  days * 24 * 3600;
        return prognosis[m] * sec / secondsInCurrentMonth;
    }

    @Override
    public String toString() {
        return "Amount: " + this.amount + " Open date: " + this.openDate + " Expiration date: " + this.expirationDate;
    }

    public double getAccurued(LocalDateTime date) {
        int m = monthElapsed(date);
        if (m < 0) return 0.0;
        if (date.equals(expirationDate))
            return 0.0;
        long sec = secondsBetween(openDate.plusMonths(m), date);
        int days = ( (openDate.plusMonths(m)).getMonth().length(isLeapYear(openDate.plusMonths(m))));
        long secondsInCurrentMonth  =  days * 24 * 3600;
        return prognosis[m] * sec / secondsInCurrentMonth;
    }

    public LocalDateTime getNextWidthrawDate() {
        int monthElapsed =  monthElapsed() + 1;
        if (LocalDateTime.now().isAfter(expirationDate))
            return null;
        return openDate.plusMonths(monthElapsed);
    }

    public LocalDateTime getNextWidthrawDate(LocalDateTime date) {
        LocalDateTime d = date.plusDays(-1);
        int monthElapsed =  monthElapsed(d) + 1;
        if (d.isAfter(expirationDate))
            return null;
        return openDate.plusMonths(monthElapsed);
    }

    public boolean isClosed() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public boolean isClosed(LocalDateTime date) {
        return date.isAfter(expirationDate);
    }

    public boolean isDayOfWidthraw(LocalDateTime date) {
        return date.equals(getNextWidthrawDate(date));
    }

    public static double getALL(LocalDateTime date, LinkedList<Deposit> list) {
        double summ = 0.0;
        for (Deposit d: list) {
            if (d.isClosed(date))
                list.remove(d);
            else {
                summ += d.amount + d.getAccurued(date);
            }
        }
        return  summ;
    }
}
