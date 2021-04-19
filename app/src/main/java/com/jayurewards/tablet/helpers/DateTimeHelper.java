package com.jayurewards.tablet.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper {
    public static String getDayString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE", Locale.getDefault());
        return fmt.format(date);
    }

    public static String getTimeString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return fmt.format(date);
    }

    public static Date parseDateStringToDate(String dateString) {
        SimpleDateFormat fmt;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fmt = new SimpleDateFormat(GlobalConstants.MYSQL_DATE_FORMAT, Locale.getDefault());
        } else {
            fmt = new SimpleDateFormat(GlobalConstants.MYSQL_DATE_FORMAT_LEGACY, Locale.getDefault());
        }

        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static long getDateDifferenceSeconds(String dateString) {
        Date currentDateTime = Calendar.getInstance().getTime();
        Date date = new Date();

        SimpleDateFormat fmt;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fmt = new SimpleDateFormat(GlobalConstants.MYSQL_DATE_FORMAT, Locale.getDefault());
        } else {
            fmt = new SimpleDateFormat(GlobalConstants.MYSQL_DATE_FORMAT_LEGACY, Locale.getDefault());
        }
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateDiff = currentDateTime.getTime() -  date.getTime();
        return dateDiff / 1000;
    }

    public static String getDateInPast(int days) {
        if (days > 1 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now().minusDays(days);
            return date.toString();
        }

        Date currentDateTime = Calendar.getInstance().getTime();
        long daysInMilli = days * 24 * 3600 * 1000L;

        Date dateBefore = new Date(currentDateTime.getTime() - daysInMilli);

        SimpleDateFormat fmt = new SimpleDateFormat(GlobalConstants.MYSQL_CUSTOM_DATE_FORMAT, Locale.getDefault());
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        return fmt.format(dateBefore);
    }

    public static String dateDifferenceString(int timeLeft) {
        long hours = timeLeft / 3600;
        long minutes = timeLeft / 60 % 60;
        long seconds = timeLeft % 60;

        String time;

        if (timeLeft == 3600) {
            time = String.format(Locale.getDefault(), "%2d hour", hours);
        } else if (timeLeft >= 3599) {
            time = String.format(Locale.getDefault(), "%2d hours", hours);
        } else if (timeLeft == 60){
            time = String.format(Locale.getDefault(), "%2d minute", minutes);
        } else if (timeLeft >= 59){
            time = String.format(Locale.getDefault(), "%2d minutes", minutes);
        } else {
            time = String.format(Locale.getDefault(), "%2d seconds", seconds);
        }

        return time;
    }
}
