package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.SuperTextView;
import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.SourceView;

import java.util.List;

public class MXAdapter extends DNSRecordsAdapter<DNSRecord.MXEntry, MXAdapter.ViewHolder> {
    private final int dp8;

    public MXAdapter(Context context, Domain.DNSRecordsArrayList<DNSRecord.MXEntry> authoritative, Domain.DNSRecordsArrayList<DNSRecord.MXEntry> resolver) {
        super(context, authoritative, resolver);

        dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int position, DNSRecord.MXEntry authoritative, List<DNSRecord<DNSRecord.MXEntry>> authoritativeSources, DNSRecord.MXEntry resolver, List<DNSRecord<DNSRecord.MXEntry>> resolverSources) {
        holder.exchange.setText(authoritative.exchange);
        holder.name.setHtml(R.string.name, authoritative.name);
        holder.preference.setText(String.valueOf(authoritative.preference));
        holder.ttl.setHtml(R.string.ttl, String.valueOf(authoritative.ttl));
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.handleCollapseClick(holder.toggle, holder.details);
            }
        });

        holder.sources.removeAllViews();

        boolean first = true;
        for (DNSRecord<DNSRecord.MXEntry> dns : authoritativeSources) {
            SourceView view = new SourceView(context, dns, true);
            holder.sources.addView(view);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.bottomMargin = dp8;

            if (first) params.topMargin = dp8;
            first = false;
        }

        for (DNSRecord<DNSRecord.MXEntry> dns : resolverSources) {
            SourceView view = new SourceView(context, dns, false);
            holder.sources.addView(view);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.bottomMargin = dp8;

            if (first) params.topMargin = dp8;
            first = false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    public class ViewHolder extends DNSRecordsAdapter.ViewHolder {
        final TextView exchange;
        final TextView preference;
        final SuperTextView name;
        final SuperTextView ttl;
        final ImageButton toggle;
        final LinearLayout details;
        final LinearLayout sources;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_mx);

            exchange = itemView.findViewById(R.id.mxItem_exchange);
            preference = itemView.findViewById(R.id.mxItem_preference);
            name = itemView.findViewById(R.id.mxItem_name);
            ttl = itemView.findViewById(R.id.mxItem_ttl);
            toggle = itemView.findViewById(R.id.mxItem_toggle);
            details = itemView.findViewById(R.id.mxItem_details);
            sources = itemView.findViewById(R.id.mxItem_sources);
        }
    }
}
