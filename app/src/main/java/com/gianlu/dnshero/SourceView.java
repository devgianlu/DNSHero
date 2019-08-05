package com.gianlu.dnshero;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gianlu.commonutils.CasualViews.SuperTextView;
import com.gianlu.commonutils.FontsManager;
import com.gianlu.dnshero.NetIO.DNSRecord;

public class SourceView extends LinearLayout {
    private final SuperTextView badge;
    private final TextView source;
    private final SuperTextView rtt;

    public SourceView(Context context, DNSRecord<?> dns, boolean authoritative) {
        this(context, null, 0);
        setSource(dns, authoritative);
    }

    public SourceView(Context context) {
        this(context, null, 0);
    }

    public SourceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SourceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.view_source, this);

        source = findViewById(R.id.sourceView_source);
        rtt = findViewById(R.id.sourceView_rtt);
        badge = findViewById(R.id.sourceView_badge);
        badge.setTypeface(FontsManager.ROBOTO_BLACK);
    }

    public void setSource(DNSRecord<?> dns, boolean authoritative) {
        source.setText(dns.source);
        Utils.clickToCopy(source);

        badge.setText(getContext().getString(authoritative ? R.string.authoritativeServer : R.string.publicResolver));
        rtt.setHtml(R.string.rtt, Utils.formatRTT(dns.rtt));
    }
}
