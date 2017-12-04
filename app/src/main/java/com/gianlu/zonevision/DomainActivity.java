package com.gianlu.zonevision;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gianlu.zonevision.NetIO.Domain;

public class DomainActivity extends AppCompatActivity {

    public static void startActivity(Context context, Domain domain) {
        context.startActivity(new Intent(context, DomainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("domain", domain));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Domain domain = (Domain) getIntent().getSerializableExtra("domain");
        if (domain == null) {
            onBackPressed();
            return;
        }

        setTitle(getString(R.string.appName_domain, domain.name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoadingActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
