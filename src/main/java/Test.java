import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.ListIterator;

public class Test {

    private static final int MONTHES = 12;
    private static final int CUT = 100;
    private static final double percent = 18.0;
    private static final double ADD = 1490;
    private static final int DAYS = 600;

    public static double upperTo(double amount) {
        return  (Math.floor(amount/CUT) + 1) * CUT;
    }

    static public void customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        System.out.println(output);
    }

    public static void main(String[] args) {
        //        LocalDateTime currentDay = LocalDateTime.of(2018, Month.JUNE, 24, 0, 0, 0);
        LocalDateTime currentDay = LocalDateTime.of(2018, Month.DECEMBER, 24, 0, 0, 0);
        int days = 1;
        long ID = 1;
        double free = 0.0;

        LinkedList<Deposit> activeDeposites = new LinkedList<>();

        LocalDateTime lt1 = LocalDateTime.of(2018, Month.FEBRUARY, 17, 0, 0, 0, 0);
        LocalDateTime lt2 = LocalDateTime.of(2018, Month.MARCH, 18, 0, 0, 0, 0);
        LocalDateTime lt3 = LocalDateTime.of(2018, Month.APRIL, 19, 0, 0, 0, 0);
        LocalDateTime lt4 = LocalDateTime.of(2018, Month.MAY, 20, 0, 0, 0, 0);
        LocalDateTime lt5 = LocalDateTime.of(2018, Month.JUNE, 17, 0, 0, 0, 0);
        LocalDateTime lt6 = LocalDateTime.of(2018, Month.JULY, 17, 0, 0, 0, 0);
        LocalDateTime lt7 = LocalDateTime.of(2018, Month.AUGUST, 17, 0, 0, 0, 0);
        LocalDateTime lt8 = LocalDateTime.of(2018, Month.OCTOBER, 17, 0, 0, 0, 0);
        LocalDateTime lt9 = LocalDateTime.of(2018, Month.DECEMBER, 16, 0, 0, 0, 0);
        Deposit d1 = new Deposit(ID++, lt1, 12, 1000, 15);
        Deposit d2 = new Deposit(ID++, lt2, 12, 2000, 15);
        Deposit d3 = new Deposit(ID++, lt3, 12, 1500, 16);
        Deposit d4 = new Deposit(ID++, lt4, 12, 2000, 16);
        Deposit d5 = new Deposit(ID++, lt5, 12, 2000, 16);
        Deposit d6 = new Deposit(ID++, lt6, 12, 2000, 16);
        Deposit d7 = new Deposit(ID++, lt7, 12, 2000, 16);
        Deposit d8 = new Deposit(ID++, lt8, 12, 1600, 16);
        Deposit d9 = new Deposit(ID++, lt9, 12, 2100, 18, true);

        activeDeposites.addFirst(d1);
        activeDeposites.addFirst(d2);
        activeDeposites.addFirst(d3);
        activeDeposites.addFirst(d4);
        activeDeposites.addFirst(d5);
        activeDeposites.addFirst(d6);
        activeDeposites.addFirst(d7);
        activeDeposites.addFirst(d8);
        activeDeposites.addFirst(d9);

        double earned = 0.0;

        /** STRATEGY #1 Open as faster as possible */
/*

        //while (Deposit.getALL(currentDay.plusDays(days), activeDeposites) < 1000000) {
        while (days < DAYS) {
            for (Deposit d: activeDeposites) {
                if (d.isDayOfWidthraw(currentDay)) {
//                    System.out.println(d.getWidthrraw(currentDay) + "\t" + currentDay);
                    free += d.getWidthrraw(currentDay);
                    earned += d.getWidthrraw(currentDay);
//                    System.out.println("Free summ: " + free);
                }
                if (d.isClosed(currentDay)) {
                    free += d.getAmount();
                    activeDeposites.remove(d);
//                    System.out.println("Deposit was closed");
                }
            }
            if (currentDay.getDayOfMonth() == 17) {
                free += ADD;
            }
//            if (Deposit.getALL(currentDay, activeDeposites) < 1000000)
            if ( upperTo(free) >= 1000) {
                Deposit newDeposit = new Deposit(ID++, currentDay, MONTHES, upperTo(free), percent);

//                System.out.println(newDeposit);
//                System.out.println("\n" + currentDay);

                free = 0.0;
                activeDeposites.addFirst(newDeposit);
            }
//            System.out.println("\n" + currentDay);
            currentDay = currentDay.plusDays(1);
            days++;
        }
//*/

        /** STRATEGY #2 Wait for widthraw of all active deposites */
/*
        int closed = 0;
        while (days < DAYS) {
            for (Deposit d: activeDeposites) {
                if (d.isDayOfWidthraw(currentDay)) {
                    free += d.getWidthrraw(currentDay);
                    earned += d.getWidthrraw(currentDay);
                    closed++;
                }
                if (d.isClosed(currentDay)) {
                    free += d.getAmount();
                    activeDeposites.remove(d);
//                    d.setActive(false);
                }
            }
            if (currentDay.getDayOfMonth() == 14) {
                free += ADD;
            }
            if (closed >= activeDeposites.size() && free > 1000) {
                closed = 0;
                Deposit newDeposit = new Deposit(ID++, currentDay, MONTHES, upperTo(free), percent);
                //System.out.println(newDeposit);
                free = 0.0;
                activeDeposites.addFirst(newDeposit);
            }
            currentDay = currentDay.plusDays(1);
            days++;
        }
//*/

        /** STRATEGY #3 with replenishment deposites */

//*

        while (days < DAYS) {
            for (Deposit d: activeDeposites) {
                if (d.isDayOfWidthraw(currentDay)) {
                    free += d.getWidthrraw(currentDay);
                    earned += d.getWidthrraw(currentDay);
                }
                if (d.isClosed(currentDay)) {
                    free += d.getAmount();
                    activeDeposites.remove(d);
                }
            }
            if (currentDay.getDayOfMonth() == 17) {
                free += ADD;
            }

            if (free > 0.0)
            for (Deposit iter: activeDeposites) {
                if (iter.isReplenishmental()) {
                    double f = free;
                    System.out.print("Before refill " + free + " ");
                    free = iter.Refill(free, currentDay);
                    System.out.println("Refill " + iter.getID() + " on " + (f - free) + " Date: " + currentDay.getDayOfMonth() + "/" + currentDay.getMonthValue());
                    System.out.println("After refill " + free);

                }
                if (free <= 0) break;
            }

            if ( upperTo(free) >= 1000) {
                Deposit newDeposit = new Deposit(ID++, currentDay, MONTHES, upperTo(free), percent, true);
                System.out.println("Open" + newDeposit);
                free = 0.0;
                activeDeposites.addFirst(newDeposit);
            }
            currentDay = currentDay.plusDays(1);
            days++;
        }
//*/

        System.out.println("Active deposites: " + activeDeposites.size());
        System.out.print("In active:  ");
        customFormat("###,###.##", Deposit.getALL(currentDay, activeDeposites));
        System.out.print("Total earned: ");
        customFormat("###,###.##", earned);

    }

}
