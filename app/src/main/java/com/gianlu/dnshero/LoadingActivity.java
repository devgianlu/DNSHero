package com.gianlu.dnshero;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.Dialogs.ActivityWithDialog;
import com.gianlu.commonutils.Toaster;
import com.gianlu.dnshero.Favorites.FavoritesAdapter;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.NetIO.ZoneVisionAPI;
import com.google.android.material.textfield.TextInputLayout;

public class LoadingActivity extends ActivityWithDialog implements ZoneVisionAPI.OnSearch, FavoritesAdapter.Listener {
    private TextInputLayout domain;
    private ProgressBar loading;
    private LinearLayout form;
    private RecyclerView favorites;
    private TextView favoritesLabel;

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        FavoritesAdapter adapter = new FavoritesAdapter(this, this);
        favorites.setAdapter(adapter);
        if (adapter.getItemCount() == 0) favoritesLabel.setVisibility(View.GONE);
        else favoritesLabel.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button preferences = findViewById(R.id.loading_preferences);
        preferences.setOnClickListener(v -> startActivity(new Intent(LoadingActivity.this, PreferenceActivity.class)));

        favoritesLabel = findViewById(R.id.loading_favoritesLabel);
        favorites = findViewById(R.id.loading_favorites);
        favorites.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        loadFavorites();

        form = findViewById(R.id.loading_form);
        loading = findViewById(R.id.loading_loading);
        domain = findViewById(R.id.loading_domain);
        CommonUtils.clearErrorOnEdit(domain);
        CommonUtils.getEditText(domain).setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                search();
                return true;
            }

            return false;
        });

        domain.setEndIconOnClickListener(view -> search());

        Uri sharedUri = getIntent().getData();
        if (sharedUri != null) {
            String fragment = sharedUri.getFragment();
            if (fragment != null) {
                loading.setVisibility(View.VISIBLE);
                form.setVisibility(View.GONE);

                ZoneVisionAPI.get().search(fragment.substring(1), null, this);

                ThisApplication.sendAnalytics(Utils.ACTION_SEARCH_INTENT);
            }
        }
    }

    private void search() {
        String query = CommonUtils.getText(domain);
        if (query.equals(".")) {
            loading.setVisibility(View.GONE);
            form.setVisibility(View.VISIBLE);
            domain.setError("Root domain isn't supported.");
        } else if (!query.isEmpty()) {
            loading.setVisibility(View.VISIBLE);
            form.setVisibility(View.GONE);

            ZoneVisionAPI.get().search(query, null, this);

            ThisApplication.sendAnalytics(Utils.ACTION_SEARCH);
        }
    }

    @Override
    public void onDone(@NonNull Domain domain) {
        DomainActivity.startActivity(this, domain);
    }

    @Override
    public void onException(@NonNull Exception ex) {
        Toaster.with(this).message(R.string.searchFailed).ex(ex).show();

        loading.setVisibility(View.GONE);
        form.setVisibility(View.VISIBLE);
        domain.setError(ex.getMessage());
    }

    @Override
    public void onApiException(@NonNull ZoneVisionAPI.ApiException ex) {
        loading.setVisibility(View.GONE);
        form.setVisibility(View.VISIBLE);
        domain.setError(ex.getMessage());
    }

    @Override
    public void onDomainSelected(@NonNull String domainStr) {
        loading.setVisibility(View.VISIBLE);
        form.setVisibility(View.GONE);

        ZoneVisionAPI.get().search(domainStr, null, this);
    }
}
