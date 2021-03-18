package com.jayurewards.tablet.helpers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Arrays;

public class LogHelper {

    public enum ErrorReportType {
        GENERAL,
        JSON,
        NETWORK,
        PARSING,
        IMAGE,
        VIDEO,
        NOTIFICATIONS,
        LOCATION,
        BEACON,
        FIREBASE
    }

    public static void serverError(String logTag, String errorMessage, int code, String severMessage) {
        String message = errorMessage + code + " - Server Message: " + severMessage;
        Log.e(logTag, "\n" + message);
        FirebaseCrashlytics.getInstance().log(message);
    }

    public static void errorReport(String tag, String message, Throwable t, ErrorReportType code) {
        Log.e(tag, code + ": \n" + message + "\n " + t);
        FirebaseCrashlytics.getInstance().log(message);
        FirebaseCrashlytics.getInstance().recordException(t);
    }

    public static void logReport(String tag, String message, ErrorReportType code) {
        Log.e(tag, code + ": \n" + message);
        FirebaseCrashlytics.getInstance().log(message);
    }


    /**
     * Print out intents
     */
    public static String printIntentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToString(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }


}
