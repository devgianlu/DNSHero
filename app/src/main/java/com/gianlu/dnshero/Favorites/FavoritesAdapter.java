package com.gianlu.dnshero.Favorites;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<String> favorites;

    public FavoritesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
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
        String domain = favorites.get(position);
        holder.domain.setText(domain);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
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
