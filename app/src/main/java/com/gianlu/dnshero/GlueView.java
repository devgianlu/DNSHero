package com.gianlu.dnshero;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.SuperTextView;
import com.gianlu.dnshero.NetIO.Domain;

import java.util.List;

public class GlueView extends LinearLayout {
    private final int dp4;
    private final LayoutInflater inflater;

    public GlueView(Context context) {
        this(context, null, 0);
    }

    public GlueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflater = LayoutInflater.from(context);
        dp4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());
        setOrientation(VERTICAL);
    }

    public void setGlue(Domain.Glue glue) {
        removeAllViews();

        populateV4(glue.v4, R.string.glueV4);
        populateV4(glue.v6, R.string.glueV6);
    }

    private void populateV4(List<Domain.Glue.Entry> glue, @StringRes int titleRes) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_glue, this, false);

        TextView title = layout.findViewById(R.id.glueView_title);
        title.setText(titleRes);

        addView(layout);

        if (glue != null && !glue.isEmpty()) {
            final LinearLayout glues = layout.findViewById(R.id.glueView_glues);

            layout.findViewById(R.id.glueView_toggle).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.handleCollapseClick((ImageButton) v, glues);
                }
            });

            int secondary = ContextCompat.getColor(getContext(), android.R.color.secondary_text_light);
            int accent = ContextCompat.getColor(getContext(), R.color.colorAccent);

            boolean first = true;
            for (Domain.Glue.Entry entry : glue) {
                LinearLayout item = new LinearLayout(getContext());
                item.setOrientation(VERTICAL);

                SuperTextView name = new SuperTextView(getContext(), entry.name);
                name.setTextColor(secondary);
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                item.addView(name);

                SuperTextView address = new SuperTextView(getContext(), entry.address);
                address.setTextColor(accent);
                address.setTypeface(Typeface.DEFAULT_BOLD);
                item.addView(address);

                glues.addView(item);

                LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
                if (!first) params.topMargin = dp4;
                first = false;
            }
        } else {
            layout.addView(new SuperTextView(getContext(), getContext().getString(R.string.noGlueRecords), ContextCompat.getColor(getContext(), android.R.color.secondary_text_light)));
        }
    }
}
