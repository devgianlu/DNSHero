package com.gianlu.dnshero.records;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.gianlu.dnshero.R;
import com.gianlu.dnshero.Utils;
import com.gianlu.dnshero.api.DNSRecord;
import com.gianlu.dnshero.api.Domain;

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
            Utils.clickToCopy(exchange);

            preference = header.findViewById(R.id.mxHeader_preference);
        }
    }
}
