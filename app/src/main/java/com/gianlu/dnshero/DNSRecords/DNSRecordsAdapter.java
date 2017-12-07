package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gianlu.dnshero.NetIO.Domain;

import java.util.List;

public abstract class DNSRecordsAdapter<E extends Domain.GeneralRecordEntry, VH extends DNSRecordsAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final LayoutInflater inflater;
    private final List<Domain.DNSRecord<E>> authoritative;
    private final List<Domain.DNSRecord<E>> resolver;

    public DNSRecordsAdapter(Context context, List<Domain.DNSRecord<E>> authoritative, List<Domain.DNSRecord<E>> resolver) {
        this.authoritative = authoritative;
        this.resolver = resolver;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        Domain.DNSRecord<E> authoritative = this.authoritative.get(position);
        Domain.DNSRecord<E> resolver = this.resolver.get(position);
        onBindViewHolder(holder, position, authoritative, resolver);
    }

    protected abstract void onBindViewHolder(VH holder, int position, Domain.DNSRecord<E> authoritative, Domain.DNSRecord<E> resolver);

    @Override
    public final int getItemCount() {
        if (authoritative.size() > resolver.size()) return resolver.size();
        else return authoritative.size();
    }

    protected abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(inflater.inflate(res, parent, false));
        }
    }
}
