package com.jayurewards.tablet.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatHelper {
    public static String getDayString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE", Locale.getDefault());
        return fmt.format(date);
    }

    public static String getTimeString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return fmt.format(date);
    }
}
