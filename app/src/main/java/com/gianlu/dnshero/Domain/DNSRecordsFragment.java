package com.gianlu.dnshero.Domain;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.dnshero.NetIO.Domain;

public class DNSRecordsFragment extends Fragment {

    public static DNSRecordsFragment getInstance(Context context, @StringRes int title, Domain.DNSRecords records) {
        DNSRecordsFragment fragment = new DNSRecordsFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(title));
        args.putSerializable("records", records);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
