package com.gianlu.dnshero.Domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.List;


public class DiagnosticsAdapter extends RecyclerView.Adapter<DiagnosticsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Domain.Diagnostic> diagnostics;

    public DiagnosticsAdapter(@NonNull Context context, List<Domain.Diagnostic> diagnostics) {
        this.inflater = LayoutInflater.from(context);
        this.diagnostics = diagnostics;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Domain.Diagnostic diagnostic = diagnostics.get(position);


        switch (diagnostic.status) {
            case PASSED:
                CommonUtils.setTextColor(holder.title, R.color.passed);
                break;
            case FAILED:
                CommonUtils.setTextColor(holder.title, R.color.failed);
                holder.recommendationImage.setImageResource(R.drawable.baseline_error_outline_24);
                break;
            default:
            case SKIPPED:
                CommonUtils.setTextColorFromAttr(holder.title, R.attr.colorOnSurface);
                holder.recommendationImage.setImageResource(R.drawable.outline_info_24);
                break;
            case INFO:
                CommonUtils.setTextColor(holder.title, R.color.info);
                holder.recommendationImage.setImageResource(R.drawable.outline_info_24);
                break;
        }

        holder.title.setText(diagnostic.name);
        holder.description.setText(diagnostic.description);
        holder.recommendationText.setText(diagnostic.recommendation);

        holder.toggle.setVisibility(diagnostic.recommendation != null ? View.VISIBLE : View.GONE);
        holder.toggle.setOnClickListener(v -> CommonUtils.handleCollapseClick(holder.toggle, holder.recommendation));

        CommonUtils.setRecyclerViewTopMargin(holder);
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
