package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;

import java.util.List;

public abstract class DNSRecordsAdapter<E extends DNSRecord.Entry, VH extends DNSRecordsAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Context context;
    protected final Domain.DNSRecordsArrayList<E> authoritative;
    protected final Domain.DNSRecordsArrayList<E> resolver;
    private final LayoutInflater inflater;
    private final List<E> relevantAuthoritative;
    private final List<E> relevantResolver;

    public DNSRecordsAdapter(Context context, Domain.DNSRecordsArrayList<E> authoritative, Domain.DNSRecordsArrayList<E> resolver) {
        this.context = context;
        this.relevantAuthoritative = authoritative.createRelevantDataList();
        this.authoritative = authoritative;
        this.relevantResolver = resolver.createRelevantDataList();
        this.inflater = LayoutInflater.from(context);
        this.resolver = resolver;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        E authoritative = relevantAuthoritative.get(position);
        E resolver = relevantResolver.get(position);
        onBindViewHolder(holder, position, authoritative, this.authoritative.listRecordsThatHas(authoritative), resolver, this.resolver.listRecordsThatHas(resolver));

        CommonUtils.setRecyclerViewTopMargin(context, holder);
    }

    protected abstract void onBindViewHolder(VH holder, int position, E authoritative, List<DNSRecord<E>> authoritativeSources, E resolver, List<DNSRecord<E>> resolverSources);

    @Override
    public final int getItemCount() {
        if (authoritative.size() > resolver.size()) return resolver.size();
        else return authoritative.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(inflater.inflate(res, parent, false));
        }
    }
}
