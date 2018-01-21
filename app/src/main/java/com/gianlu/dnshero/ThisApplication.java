package com.gianlu.dnshero;


import com.gianlu.commonutils.Analytics.AnalyticsApplication;

public class ThisApplication extends AnalyticsApplication {
    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
