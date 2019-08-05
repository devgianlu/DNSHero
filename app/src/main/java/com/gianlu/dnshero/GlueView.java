package com.gianlu.dnshero;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.gianlu.commonutils.CommonUtils;
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

        populate(glue.v4, R.string.glueV4);
        populate(glue.v6, R.string.glueV6);
    }

    private void populate(List<Domain.Glue.Entry> glue, @StringRes int titleRes) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_glue, this, false);

        TextView title = layout.findViewById(R.id.glueView_title);
        title.setText(titleRes);

        addView(layout);

        final LinearLayout glues = layout.findViewById(R.id.glueView_glues);
        layout.findViewById(R.id.glueView_toggle).setOnClickListener(v -> CommonUtils.handleCollapseClick((ImageButton) v, glues));

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (glue != null && !glue.isEmpty()) {
            boolean first = true;
            for (Domain.Glue.Entry entry : glue) {
                LinearLayout item = (LinearLayout) inflater.inflate(R.layout.item_glue, this, false);
                glues.addView(item);

                TextView name = item.findViewById(R.id.glueItem_name);
                name.setText(entry.name);

                TextView address = item.findViewById(R.id.glueItem_address);
                address.setText(entry.address);
                Utils.clickToCopy(address);

                LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
                if (!first) params.topMargin = dp4;
                first = false;
            }
        } else {
            TextView text = (TextView) inflater.inflate(R.layout.item_secondary_text, glues, false);
            text.setText(R.string.noGlueRecords);
            glues.addView(text);
        }
    }
}
