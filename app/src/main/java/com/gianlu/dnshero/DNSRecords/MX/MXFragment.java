package com.gianlu.dnshero.DNSRecords.MX;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.gianlu.dnshero.DNSRecords.DNSRecordFragment;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.ArrayList;

public class MXFragment extends DNSRecordFragment<Domain.MXEntry> {

    public static MXFragment getInstance(Context context, Domain domain) {
        return prepareInstance(context, new MXFragment(), domain.authoritative.mx, domain.resolver.mx);
    }

    @Override
    protected int getTitleRes() {
        return R.string.mx;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mx;
    }

    @Override
    protected void onCreateView(@NonNull ViewGroup layout, ArrayList<Domain.DNSRecord<Domain.MXEntry>> authoritative, ArrayList<Domain.DNSRecord<Domain.MXEntry>> resolver) {

    }
}
