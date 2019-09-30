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
public class TXTAdapter extends DNSRecordsAdapter<DNSRecord.TXTEntry, TXTAdapter.ViewHolder> {

    public TXTAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.TXTEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.TXTEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DNSRecord.TXTEntry authoritative, DNSRecord.TXTEntry resolver) {
        holder.text.setText(authoritative.text.get(0));
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final TextView text;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.header_txt);

            text = (TextView) header.getChildAt(0);
            Utils.clickToCopy(text);
        }
    }
}
