package com.gianlu.dnshero;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.Toaster;
import com.gianlu.dnshero.Favorites.FavoritesAdapter;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.NetIO.ZoneVisionAPI;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadingActivity extends AppCompatActivity implements ZoneVisionAPI.OnSearch, FavoritesAdapter.Listener {
    private TextInputLayout domain;
    private ProgressBar loading;
    private LinearLayout form;
    private RecyclerView favorites;

    @Override
    protected void onResume() {
        super.onResume();
        favorites.setAdapter(new FavoritesAdapter(this, this));
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
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoadingActivity.this, PreferenceActivity.class));
            }
        });

        favorites = findViewById(R.id.loading_favorites);
        favorites.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        favorites.setAdapter(new FavoritesAdapter(this, this));

        final ImageButton search = findViewById(R.id.loading_search);
        form = findViewById(R.id.loading_form);
        loading = findViewById(R.id.loading_loading);
        domain = findViewById(R.id.loading_domain);
        CommonUtils.getEditText(domain).setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search.performClick();
                    return true;
                }

                return false;
            }
        });
        CommonUtils.getEditText(domain).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                domain.setErrorEnabled(false);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("ConstantConditions")
            public void onClick(View v) {
                String query = CommonUtils.getText(domain);
                if (!query.isEmpty()) {
                    loading.setVisibility(View.VISIBLE);
                    form.setVisibility(View.GONE);
                    favorites.setVisibility(View.GONE);

                    ZoneVisionAPI.get().search(query, LoadingActivity.this);

                    ThisApplication.sendAnalytics(Utils.ACTION_SEARCH);
                }
            }
        });

        Uri sharedUri = getIntent().getData();
        if (sharedUri != null) {
            String fragment = sharedUri.getFragment();
            if (fragment != null) {
                loading.setVisibility(View.VISIBLE);
                form.setVisibility(View.GONE);
                favorites.setVisibility(View.GONE);

                ZoneVisionAPI.get().search(fragment.substring(1), this);

                ThisApplication.sendAnalytics(Utils.ACTION_SEARCH_INTENT);
            }
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
        favorites.setVisibility(View.VISIBLE);
    }

    @Override
    public void onApiException(@NonNull ZoneVisionAPI.ApiException ex) {
        loading.setVisibility(View.GONE);
        form.setVisibility(View.VISIBLE);
        domain.setError(ex.getMessage());
        favorites.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDomainSelected(@NonNull String domain) {
        loading.setVisibility(View.VISIBLE);
        form.setVisibility(View.GONE);
        favorites.setVisibility(View.GONE);

        ZoneVisionAPI.get().search(domain, this);
    }
}
