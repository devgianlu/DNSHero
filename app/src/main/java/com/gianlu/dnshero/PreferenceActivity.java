package com.gianlu.dnshero;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.gianlu.commonutils.Preferences.BasePreferenceActivity;
import com.gianlu.commonutils.Preferences.BasePreferenceFragment;
import com.gianlu.commonutils.Preferences.MaterialAboutPreferenceItem;
import com.gianlu.commonutils.Preferences.Prefs;
import com.yarolegovich.mp.MaterialStandardPreference;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PreferenceActivity extends BasePreferenceActivity {
    @NonNull
    @Override
    protected List<MaterialAboutPreferenceItem> getPreferencesItems() {
        return Collections.singletonList(new MaterialAboutPreferenceItem(R.string.favorites, R.drawable.baseline_favorite_24, FavoritesFragment.class));
    }

    @Override
    protected int getAppIconRes() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected boolean hasTutorial() {
        return false;
    }

    @Nullable
    @Override
    protected String getOpenSourceUrl() {
        return "https://github.com/devgianlu/DNSHero";
    }

    @Override
    protected boolean disablePayPalOnGooglePlay() {
        return false;
    }

    public static final class FavoritesFragment extends BasePreferenceFragment {

        private void showRemoveDialog(Context context) {
            final String[] entries = Prefs.getSet(PK.FAVORITES, new HashSet<String>()).toArray(new String[0]);
            final boolean[] checked = new boolean[entries.length];
            for (int i = 0; i < checked.length; i++) checked[i] = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.removeFavorite)
                    .setMultiChoiceItems(entries, checked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checked[which] = isChecked;
                        }
                    })
                    .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < checked.length; i++) {
                                if (checked[i]) Prefs.removeFromSet(PK.FAVORITES, entries[i]);
                            }

                            if (Prefs.isSetEmpty(PK.FAVORITES)) onBackPressed();
                        }
                    }).setNegativeButton(android.R.string.cancel, null);

            showDialog(builder);
        }

        @Override
        protected void buildPreferences(@NonNull final Context context) {
            if (!Prefs.isSetEmpty(PK.FAVORITES)) {
                final MaterialStandardPreference remove = new MaterialStandardPreference(context);
                remove.setTitle(R.string.removeFavorite);
                remove.setSummary(R.string.removeFavorite_summary);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRemoveDialog(context);
                    }
                });
                addPreference(remove);
            }

            MaterialStandardPreference clear = new MaterialStandardPreference(context);
            clear.setTitle(R.string.clearFavorites);
            clear.setSummary(R.string.clearFavorites_summary);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prefs.putSet(PK.FAVORITES, new HashSet<String>());
                    onBackPressed();
                }
            });
            addPreference(clear);
        }

        @Override
        public int getTitleRes() {
            return R.string.favorites;
        }
    }
}
