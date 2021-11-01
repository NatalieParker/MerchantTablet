package com.jayurewards.tablet.helpers;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class UtilsHelper {

    public static double getScreenSizeInches() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);

//        return Math.sqrt(x + y) * dm.scaledDensity; // Multiple by scaled density for more accuracy?
        // REF: https://stackoverflow.com/questions/19155559/how-to-get-android-device-screen-size
    }

    public static boolean isScreenLarge() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return (Math.sqrt(x + y) >= 8.5);
    }
}
