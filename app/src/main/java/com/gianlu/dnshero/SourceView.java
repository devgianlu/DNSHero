package com.gianlu.dnshero;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gianlu.commonutils.SuperTextView;
import com.gianlu.dnshero.NetIO.DNSRecord;

@SuppressLint("ViewConstructor")
public class SourceView extends LinearLayout {

    public SourceView(Context context, DNSRecord<?> dns, boolean authoritative) {
        super(context);
        setOrientation(VERTICAL);

        inflate(getContext(), R.layout.view_source, this);

        TextView source = findViewById(R.id.sourceView_source);
        source.setText(dns.source);

        SuperTextView badge = findViewById(R.id.sourceView_badge);
        badge.setTypeface("fonts/Roboto-Black.ttf");
        badge.setText(getContext().getString(authoritative ? R.string.authoritativeServer : R.string.publicResolver));

        SuperTextView rtt = findViewById(R.id.sourceView_rtt);
        rtt.setHtml(R.string.rttWhite, Utils.formatRTT(dns.rtt));
    }
}
