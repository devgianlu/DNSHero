package com.gianlu.dnshero.records;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.gianlu.commonutils.misc.SuperTextView;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.Utils;
import com.gianlu.dnshero.api.DNSRecord;
import com.gianlu.dnshero.api.Domain;

@Keep
public class SOAAdapter extends DNSRecordsAdapter<DNSRecord.SOAEntry, SOAAdapter.ViewHolder> {

    public SOAAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.SOAEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.SOAEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DNSRecord.SOAEntry authoritative, DNSRecord.SOAEntry resolver) {
        holder.email.setHtml(R.string.emailLabel, authoritative.getEmailAddress());
        Utils.clickToCopy(holder.email, authoritative.getEmailAddress());

        holder.primaryDns.setHtml(R.string.primaryDns, authoritative.mname);
        Utils.clickToCopy(holder.primaryDns, authoritative.name);

        holder.serial.setHtml(R.string.serial, authoritative.serial);
        Utils.clickToCopy(holder.serial, String.valueOf(authoritative.serial));

        holder.refresh.setHtml(R.string.refresh, authoritative.refresh);
        holder.retry.setHtml(R.string.soaRetry, authoritative.retry);
        holder.expire.setHtml(R.string.expire, authoritative.expire);
        holder.minimumTtl.setHtml(R.string.minimumTtl, authoritative.minimum_ttl);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final SuperTextView email;
        final SuperTextView primaryDns;
        final SuperTextView serial;
        final SuperTextView refresh;
        final SuperTextView retry;
        final SuperTextView expire;
        final SuperTextView minimumTtl;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.header_soa);

            email = header.findViewById(R.id.soaItem_email);
            primaryDns = header.findViewById(R.id.soaItem_primaryDns);
            serial = header.findViewById(R.id.soaItem_serial);
            refresh = header.findViewById(R.id.soaItem_refresh);
            retry = header.findViewById(R.id.soaItem_retry);
            expire = header.findViewById(R.id.soaItem_expire);
            minimumTtl = header.findViewById(R.id.soaItem_minimumTtl);
        }
    }
}
