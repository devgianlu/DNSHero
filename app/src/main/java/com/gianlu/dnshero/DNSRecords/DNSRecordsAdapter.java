package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.List;

public abstract class DNSRecordsAdapter<E extends Domain.GeneralRecordEntry, D extends Domain.DNSRecordsArrayList.RelevantData, VH extends DNSRecordsAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final LayoutInflater inflater;
    private final List<D> authoritative;
    private final List<D> resolver;

    public DNSRecordsAdapter(Context context, Domain.DNSRecordsArrayList<E, D> authoritative, Domain.DNSRecordsArrayList<E, D> resolver) {
        this.authoritative = authoritative.createRelevantDataList();
        this.resolver = resolver.createRelevantDataList();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        D authoritative = this.authoritative.get(position);
        D resolver = this.resolver.get(position);

        holder.name.setText(authoritative.name);
        // holder.rtt.setHtml(R.string.rtt, Utils.formatRTT(authoritative.rtt));

        onBindViewHolder(holder, position, authoritative, resolver);
    }

    protected abstract void onBindViewHolder(VH holder, int position, D authoritative, D resolver);

    @Override
    public final int getItemCount() {
        if (authoritative.size() > resolver.size()) return resolver.size();
        else return authoritative.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(inflater.inflate(res, parent, false));

            name = itemView.findViewById(R.id.dnsRecordItem_name);
        }
    }
}
