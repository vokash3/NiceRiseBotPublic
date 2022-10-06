package org.wain.Models;

import org.wain.Models.fromDB.Period;

public class PeriodFactory {

    public enum Periods {
        DEFAULT,
        HOURLY,
        FIFTEEN_MINUTES,
        HALF_HOURLY,
        DAILY;
    }

    public static Period getPeriod(Periods periodName) {
        switch (periodName) {
            case HALF_HOURLY:
                return new Period(2, "30M", 1_800_000);
            case FIFTEEN_MINUTES:
                return new Period(3, "15M", 900_000);
            case DAILY:
                return new Period(4, "1D", 86_400_000);
            default:
                return new Period(1, "1H", 3_600_000);
        }
    }
}
