package com.gianlu.dnshero.Favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.commonutils.FontsManager;
import com.gianlu.commonutils.Preferences.Prefs;
import com.gianlu.dnshero.PK;
import com.gianlu.dnshero.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Listener listener;
    private final List<String> favorites;

    public FavoritesAdapter(Context context, Listener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.favorites = new ArrayList<>(Prefs.getSet(PK.FAVORITES, new HashSet<String>()));

        Collections.sort(favorites);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String domain = favorites.get(position);
        holder.domain.setText(domain);

        holder.domain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onDomainSelected(domain);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public interface Listener {
        void onDomainSelected(@NonNull String domain);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView domain;

        public ViewHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.item_favorite, parent, false));
            domain = (TextView) itemView;

            FontsManager.set(domain, FontsManager.ROBOTO_LIGHT);
        }
    }
}
