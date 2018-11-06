package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class MXAdapter extends DNSRecordsAdapter<DNSRecord.MXEntry, MXAdapter.ViewHolder> {

    public MXAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.MXEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.MXEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int position, DNSRecord.MXEntry authoritative, DNSRecord.MXEntry resolver) {
        holder.exchange.setText(authoritative.exchange);
        holder.preference.setText(String.valueOf(authoritative.preference));
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final TextView exchange;
        final TextView preference;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.header_mx);

            exchange = header.findViewById(R.id.mxHeader_exchange);
            preference = header.findViewById(R.id.mxHeader_preference);
        }
    }
}
