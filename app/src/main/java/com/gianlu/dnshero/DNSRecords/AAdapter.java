package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

public class AAdapter extends DNSRecordsAdapter<DNSRecord.AEntry, AAdapter.ViewHolder> {

    public AAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.AEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.AEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int position, DNSRecord.AEntry authoritative, DNSRecord.AEntry resolver) {
        holder.address.setText(authoritative.address);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final TextView address;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.header_a);

            address = (TextView) header.getChildAt(0);
        }
    }
}
