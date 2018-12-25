import java.time.LocalDateTime;
import java.time.Month;

public class Check {

    public static void main(String[] args) {

        LocalDateTime lt1 = LocalDateTime.of(2018, Month.MARCH, 17, 0, 0, 0, 0);
        Deposit d1 = new Deposit(1, lt1, 12, 1000, 15, true);

        LocalDateTime currentDay = LocalDateTime.of(2018, Month.JUNE, 25, 0, 0, 0);

        if (d1.isClosed(currentDay))
            System.out.println("Closed");
        else
            System.out.println("Open");

        System.out.println(d1.getWidthrraw(currentDay));

        d1.Refill(200, currentDay);

        d1.Refill(900, currentDay.plusMonths(1));

        LocalDateTime nextDay = LocalDateTime.of(2019, Month.JANUARY, 26, 0, 0, 0);

        System.out.println(d1.getWidthrraw(nextDay));
    }

}
