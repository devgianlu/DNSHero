package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.view.ViewGroup;

import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

public class MXAdapter extends DNSRecordsAdapter<Domain.MXEntry, Domain.MXEntries.RelevantData, MXAdapter.ViewHolder> {

    public MXAdapter(Context context, Domain.DNSRecordsArrayList<Domain.MXEntry, Domain.MXEntries.RelevantData> authoritative, Domain.DNSRecordsArrayList<Domain.MXEntry, Domain.MXEntries.RelevantData> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Domain.MXEntries.RelevantData authoritative, Domain.MXEntries.RelevantData resolver) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mx);
        }
    }
}
