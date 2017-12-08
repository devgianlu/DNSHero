package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.view.ViewGroup;

import com.gianlu.commonutils.SuperTextView;
import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;


public class SOAAdapter extends DNSRecordsAdapter<DNSRecord.SOAEntry, SOAAdapter.ViewHolder> {

    public SOAAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.SOAEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.SOAEntry> resolver) {
        super(context, authoritative, resolver);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DNSRecord.SOAEntry authoritative, DNSRecord.SOAEntry resolver) {
        holder.email.setHtml(R.string.emailLabel, authoritative.getEmailAddress());
        holder.primaryDns.setHtml(R.string.primaryDns, authoritative.mname);
        holder.serial.setHtml(R.string.serial, authoritative.serial);
        holder.refresh.setHtml(R.string.refresh, authoritative.refresh);
        holder.retry.setHtml(R.string.retry, authoritative.retry);
        holder.expire.setHtml(R.string.expire, authoritative.expire);
        holder.minimumTtl.setHtml(R.string.minimumTttl, authoritative.minimum_ttl);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
