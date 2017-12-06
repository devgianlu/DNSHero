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

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.SuperTextView;
import com.gianlu.dnshero.NetIO.Domain;

import java.util.ArrayList;

public class GlueView extends LinearLayout {
    private final LinearLayout glues;
    private final int dp4;

    public GlueView(Context context) {
        this(context, null, 0);
    }

    public GlueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dp4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());

        LayoutInflater.from(context).inflate(R.layout.view_glue, this, true);
        setOrientation(VERTICAL);

        glues = findViewById(R.id.glueView_glues);
        final ImageButton toggle = findViewById(R.id.glueView_toggle);
        toggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.handleCollapseClick(toggle, glues);
            }
        });
    }

    private void populate(ArrayList<Domain.Glue.Entry> glue, @StringRes int titleRes, boolean paddingTop) {
        if (glue != null && !glue.isEmpty()) {
            SuperTextView title = new SuperTextView(getContext(), getContext().getString(titleRes), ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
            title.setTypeface(Typeface.DEFAULT_BOLD);
            glues.addView(title);

            if (paddingTop) {
                LinearLayout.LayoutParams params = (LayoutParams) title.getLayoutParams();
                params.topMargin = dp4 * 2;
            }

            int secondary = ContextCompat.getColor(getContext(), android.R.color.secondary_text_light);

            boolean first = true;
            for (Domain.Glue.Entry entry : glue) {
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(VERTICAL);

                SuperTextView name = new SuperTextView(getContext(), R.string.name, entry.name);
                name.setTextColor(secondary);
                layout.addView(name);

                SuperTextView address = new SuperTextView(getContext(), R.string.address, entry.address);
                address.setTextColor(secondary);
                layout.addView(address);

                glues.addView(layout);

                LinearLayout.LayoutParams params = (LayoutParams) layout.getLayoutParams();
                params.setMarginStart(dp4);
                if (!first) params.topMargin = dp4;
                first = false;
            }
        }
    }

    public void setGlue(Domain.Glue glue) {
        glues.removeAllViews();

        populate(glue.v4, R.string.v4, false);
        populate(glue.v6, R.string.v6, true);

        if (glues.getChildCount() == 0)
            glues.addView(new SuperTextView(getContext(), getContext().getString(R.string.noGlueRecords), ContextCompat.getColor(getContext(), android.R.color.secondary_text_light)));
    }
}
