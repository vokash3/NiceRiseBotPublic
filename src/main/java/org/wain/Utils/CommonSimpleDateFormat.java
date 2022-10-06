package org.wain.Utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CommonSimpleDateFormat {

    public static SimpleDateFormat format;

    static {
        format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
    }

}
