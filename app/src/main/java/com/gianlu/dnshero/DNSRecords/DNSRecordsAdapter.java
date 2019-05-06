package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.CasualViews.SuperTextView;
import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.SourceView;

import java.util.List;

public abstract class DNSRecordsAdapter<E extends DNSRecord.Entry, VH extends DNSRecordsAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Domain.DNSRecordsArrayList<E> authoritative;
    protected final Domain.DNSRecordsArrayList<E> resolver;
    private final LayoutInflater inflater;
    private final List<E> relevantAuthoritative;
    private final List<E> relevantResolver;
    private final int dp8;

    DNSRecordsAdapter(Context context, Domain.DNSRecordsArrayList<E> authoritative, Domain.DNSRecordsArrayList<E> resolver) {
        this.relevantAuthoritative = authoritative.createRelevantDataList();
        this.authoritative = authoritative;
        this.relevantResolver = resolver.createRelevantDataList();
        this.inflater = LayoutInflater.from(context);
        this.resolver = resolver;
        this.dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        E authoritative = relevantAuthoritative.get(position);
        E resolver = relevantResolver.get(position);

        holder.name.setHtml(R.string.name, authoritative.name);
        holder.ttl.setHtml(R.string.ttl, authoritative.ttl);
        holder.toggle.setOnClickListener(v -> CommonUtils.handleCollapseClick(holder.toggle, holder.details));

        holder.sources.removeAllViews();

        boolean first = true;
        for (DNSRecord<E> dns : this.authoritative.listRecordsThatHas(authoritative)) {
            SourceView view = new SourceView(holder.itemView.getContext(), dns, true);
            holder.sources.addView(view);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.bottomMargin = dp8;

            if (first) params.topMargin = dp8;
            first = false;
        }

        for (DNSRecord<E> dns : this.resolver.listRecordsThatHas(resolver)) {
            SourceView view = new SourceView(holder.itemView.getContext(), dns, false);
            holder.sources.addView(view);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.bottomMargin = dp8;

            if (first) params.topMargin = dp8;
            first = false;
        }

        onBindViewHolder(holder, position, authoritative, resolver);

        CommonUtils.setRecyclerViewTopMargin(holder);
    }

    protected abstract void onBindViewHolder(VH holder, int position, E authoritative, E resolver);

    @Override
    public final int getItemCount() {
        if (relevantAuthoritative.size() > relevantResolver.size()) return relevantResolver.size();
        else return relevantAuthoritative.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public final SuperTextView name;
        final SuperTextView ttl;
        final ImageButton toggle;
        final LinearLayout details;
        final LinearLayout sources;
        final FrameLayout header;

        ViewHolder(ViewGroup parent, @LayoutRes int headerRes) {
            super(inflater.inflate(R.layout.item_dns_record, parent, false));

            name = itemView.findViewById(R.id.dnsRecordItem_name);
            ttl = itemView.findViewById(R.id.dnsRecordItem_ttl);
            toggle = itemView.findViewById(R.id.dnsRecordItem_toggle);
            details = itemView.findViewById(R.id.dnsRecordItem_details);
            sources = itemView.findViewById(R.id.dnsRecordItem_sources);

            header = itemView.findViewById(R.id.dnsRecordItem_header);
            inflater.inflate(headerRes, header, true);
        }
    }
}
