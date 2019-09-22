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
public class CNAMEAdapter extends DNSRecordsAdapter<DNSRecord.CNAMEEntry, CNAMEAdapter.ViewHolder> {

    public CNAMEAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.CNAMEEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.CNAMEEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DNSRecord.CNAMEEntry authoritative, DNSRecord.CNAMEEntry resolver) {
        holder.target.setText(authoritative.target);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final TextView target;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.header_cname);

            target = (TextView) header.getChildAt(0);
            Utils.clickToCopy(target);
        }
    }
}
