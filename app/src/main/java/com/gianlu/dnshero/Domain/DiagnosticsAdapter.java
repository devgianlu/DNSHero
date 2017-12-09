package com.gianlu.dnshero.Domain;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.List;


public class DiagnosticsAdapter extends RecyclerView.Adapter<DiagnosticsAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Domain.Diagnostic> diagnostics;

    public DiagnosticsAdapter(Context context, List<Domain.Diagnostic> diagnostics) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.diagnostics = diagnostics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Domain.Diagnostic diagnostic = diagnostics.get(position);

        int color;
        int textColor;
        int textSecondaryColor;
        switch (diagnostic.status) {
            case PASSED:
                color = ContextCompat.getColor(context, R.color.passed);
                textColor = textSecondaryColor = ContextCompat.getColor(context, android.R.color.primary_text_dark);
                holder.toggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
                holder.toggle.setAlpha(1f);
                break;
            case FAILED:
                color = ContextCompat.getColor(context, R.color.failed);
                textColor = textSecondaryColor = ContextCompat.getColor(context, android.R.color.primary_text_dark);
                holder.toggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
                holder.toggle.setAlpha(1f);
                holder.recommendationImage.setImageResource(R.drawable.ic_error_white_48dp);
                holder.recommendationImage.setAlpha(1f);
                break;
            default:
            case SKIPPED:
                color = ContextCompat.getColor(context, R.color.white);
                textColor = ContextCompat.getColor(context, android.R.color.primary_text_light);
                textSecondaryColor = ContextCompat.getColor(context, android.R.color.secondary_text_light);
                holder.toggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                holder.toggle.setAlpha(.54f);
                holder.recommendationImage.setImageResource(R.drawable.ic_info_black_48dp);
                holder.recommendationImage.setAlpha(.54f);
                break;
            case INFO:
                color = ContextCompat.getColor(context, R.color.info);
                textColor = textSecondaryColor = ContextCompat.getColor(context, android.R.color.primary_text_dark);
                holder.toggle.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
                holder.toggle.setAlpha(1f);
                holder.recommendationImage.setImageResource(R.drawable.ic_info_white_48dp);
                holder.recommendationImage.setAlpha(1f);
                break;
        }

        holder.card.setCardBackgroundColor(color);
        holder.title.setTextColor(textColor);
        holder.title.setText(diagnostic.name);
        holder.description.setTextColor(textSecondaryColor);
        holder.description.setText(diagnostic.description);

        holder.recommendationText.setTextColor(textSecondaryColor);
        holder.recommendationText.setText(diagnostic.recommendation);

        holder.toggle.setVisibility(diagnostic.recommendation != null ? View.VISIBLE : View.GONE);
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.animateCollapsingArrowBellows(holder.toggle, CommonUtils.isExpanded(holder.recommendation));

                if (CommonUtils.isExpanded(holder.recommendation))
                    CommonUtils.collapse(holder.recommendation);
                else
                    CommonUtils.expand(holder.recommendation);
            }
        });

        CommonUtils.setRecyclerViewTopMargin(context, holder);
    }

    @Override
    public int getItemCount() {
        return diagnostics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CardView card;
        final TextView title;
        final TextView description;
        final ImageButton toggle;
        final LinearLayout recommendation;
        final ImageView recommendationImage;
        final TextView recommendationText;

        ViewHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.card_diagnostic, parent, false));

            card = (CardView) itemView;
            title = itemView.findViewById(R.id.diagnosticCard_title);
            description = itemView.findViewById(R.id.diagnosticCard_description);
            toggle = itemView.findViewById(R.id.diagnosticCard_toggle);
            recommendation = itemView.findViewById(R.id.diagnosticCard_recommendation);
            recommendationImage = (ImageView) recommendation.getChildAt(0);
            recommendationText = (TextView) recommendation.getChildAt(1);
        }
    }
}
