package com.gianlu.dnshero;

import com.gianlu.commonutils.Toaster;

import java.text.DecimalFormat;

public final class Utils {

    public static String formatRTT(float rtt) {
        return new DecimalFormat("#.000").format(rtt) + "ms";
    }

    public static final class Messages {

        public static final Toaster.Message FAILED_SEARCH = new Toaster.Message(R.string.searchFailed, true);
    }
}
