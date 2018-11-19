package com.gianlu.dnshero.Domain;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class DiagnosticsAdapter extends RecyclerView.Adapter<DiagnosticsAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Domain.Diagnostic> diagnostics;

    public DiagnosticsAdapter(@NonNull Context context, List<Domain.Diagnostic> diagnostics) {
        this.context = context;
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

        int textColor;
        switch (diagnostic.status) {
            case PASSED:
                textColor = ContextCompat.getColor(context, R.color.passed);
                break;
            case FAILED:
                textColor = ContextCompat.getColor(context, R.color.failed);
                holder.recommendationImage.setImageResource(R.drawable.baseline_error_outline_24);
                break;
            default:
            case SKIPPED:
                textColor = ContextCompat.getColor(context, R.color.white);
                holder.recommendationImage.setImageResource(R.drawable.outline_info_24);
                break;
            case INFO:
                textColor = ContextCompat.getColor(context, R.color.info);
                holder.recommendationImage.setImageResource(R.drawable.outline_info_24);
                break;
        }

        holder.title.setTextColor(textColor);

        holder.title.setText(diagnostic.name);
        holder.description.setText(diagnostic.description);
        holder.recommendationText.setText(diagnostic.recommendation);

        holder.toggle.setVisibility(diagnostic.recommendation != null ? View.VISIBLE : View.GONE);
        holder.toggle.setOnClickListener(v -> CommonUtils.handleCollapseClick(holder.toggle, holder.recommendation));

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
