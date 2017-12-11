package com.gianlu.dnshero;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gianlu.commonutils.Toaster;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.NetIO.ZoneVisionAPI;

public class LoadingActivity extends AppCompatActivity implements ZoneVisionAPI.ISearch {
    private TextInputLayout domain;
    private ProgressBar loading;
    private LinearLayout form;

    @SuppressWarnings("ConstantConditions")
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

        final ImageButton search = findViewById(R.id.loading_search);
        form = findViewById(R.id.loading_form);
        loading = findViewById(R.id.loading_loading);
        domain = findViewById(R.id.loading_domain);
        domain.getEditText().setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search.performClick();
                    return true;
                }

                return false;
            }
        });
        domain.getEditText().addTextChangedListener(new TextWatcher() {
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
                String query = domain.getEditText().getText().toString();
                if (!query.isEmpty()) {
                    loading.setVisibility(View.VISIBLE);
                    form.setVisibility(View.GONE);

                    ZoneVisionAPI.get().search(query, LoadingActivity.this);

                    ThisApplication.sendAnalytics(LoadingActivity.this, Utils.ACTION_SEARCH);
                }
            }
        });

        Uri sharedUri = getIntent().getData();
        if (sharedUri != null) {
            String fragment = sharedUri.getFragment();
            if (fragment != null) {
                loading.setVisibility(View.VISIBLE);
                form.setVisibility(View.GONE);

                ZoneVisionAPI.get().search(fragment.substring(1), LoadingActivity.this);

                ThisApplication.sendAnalytics(LoadingActivity.this, Utils.ACTION_SEARCH_INTENT);
            }
        }
    }

    @Override
    public void onDone(Domain domain) {
        DomainActivity.startActivity(this, domain);
    }

    @Override
    public void onException(Exception ex) {
        Toaster.show(this, Utils.Messages.FAILED_SEARCH, ex);
    }

    @Override
    public void onApiException(ZoneVisionAPI.ApiException ex) {
        loading.setVisibility(View.GONE);
        form.setVisibility(View.VISIBLE);
        domain.setError(ex.getMessage());
    }
}
