package digital.erben.various;

import java.time.*;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("RedundantExplicitChronoField")
public class DateTimeExample {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();

        LocalDate.of(2015, 02, 20);
        LocalDate.parse("2015-02-20");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        // gleichbedeutend mit
        LocalDate tomorrowWithChronoUnit = LocalDate.now().plus(1, ChronoUnit.DAYS);

        DayOfWeek sunday = LocalDate.parse("2024-01-13").getDayOfWeek();
        int twelve = LocalDate.parse("2024-12-02").getDayOfMonth();

        boolean leapYear = LocalDate.now().isLeapYear();

        boolean notBefore = LocalDate
            .parse("2016-06-12")
            .isBefore(LocalDate.parse("2016-06-11"));

        boolean isAfter = LocalDate
            .parse("2016-06-12")
            .isAfter(LocalDate.parse("2016-06-11"));

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime.of(2023, Month.FEBRUARY, 20, 06, 30);
        LocalDateTime.parse("2015-02-20T06:30:00");

        ZonedDateTime zonedDateTime = ZonedDateTime.of(
            LocalDateTime.of(2023, 01, 01, 10, 10),
            ZoneId.of("Europe/Paris")
        );

        ZonedDateTime zonedDateTimePortugal = zonedDateTime.withZoneSameInstant(
            ZoneId.of("Europe/Lisbon")
        );
        System.out.println(zonedDateTimePortugal.toLocalDateTime().getHour());
        // 9

        ZonedDateTime.parse("2015-05-03T10:15:30+01:00[Europe/Paris]");

        ZoneOffset offset = ZoneOffset.of("+02:00");

        OffsetDateTime offSetByTwo = OffsetDateTime.of(localDateTime, offset);
    }
}
