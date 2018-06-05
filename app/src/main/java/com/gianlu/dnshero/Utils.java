package com.gianlu.dnshero;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;

public final class Utils {
    public static final String ACTION_SEARCH = "search_domain";
    public static final String ACTION_SEARCH_INTENT = "search_domain_intent";

    @NonNull
    public static String formatRTT(float rtt) {
        return new DecimalFormat("#.000").format(rtt) + "ms";
    }

    public static float parseMs(String str) {
        float value = Float.parseFloat(str.substring(0, str.length() - 2));
        if (str.endsWith("µs")) return value / 1000f; // microseconds
        else return value; // milliseconds
    }
}
