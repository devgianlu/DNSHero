package com.gianlu.dnshero.DNSRecords.MX;

import android.content.Context;
import android.view.ViewGroup;

import com.gianlu.dnshero.DNSRecords.DNSRecordsAdapter;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.List;

public class MXAdapter extends DNSRecordsAdapter<Domain.MXEntry, MXAdapter.ViewHolder> {

    public MXAdapter(Context context, List<Domain.DNSRecord<Domain.MXEntry>> authoritative, List<Domain.DNSRecord<Domain.MXEntry>> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Domain.DNSRecord<Domain.MXEntry> authoritative, Domain.DNSRecord<Domain.MXEntry> resolver) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {

        public ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mx);
        }
    }
}
