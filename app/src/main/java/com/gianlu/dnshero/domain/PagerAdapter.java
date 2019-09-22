package com.gianlu.dnshero.domain;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final Fragment[] fragments;

    public PagerAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Bundle bundle = fragments[position].getArguments();
        if (bundle != null) return bundle.getString("title");
        else return null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
