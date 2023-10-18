package com.gianlu.dnshero;


import androidx.annotation.Nullable;

import com.gianlu.commonutils.analytics.AnalyticsApplication;

public class ThisApplication extends AnalyticsApplication {
    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Nullable
    @Override
    protected String getGithubProjectName() {
        return "DNSHero";
    }
}
