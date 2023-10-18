package com.gianlu.dnshero;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.gianlu.commonutils.preferences.BasePreferenceActivity;
import com.gianlu.commonutils.preferences.BasePreferenceFragment;
import com.gianlu.commonutils.preferences.MaterialAboutPreferenceItem;
import com.gianlu.commonutils.preferences.Prefs;
import com.yarolegovich.mp.MaterialCheckboxPreference;
import com.yarolegovich.mp.MaterialStandardPreference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PreferenceActivity extends BasePreferenceActivity {
    @NonNull
    @Override
    protected List<MaterialAboutPreferenceItem> getPreferencesItems() {
        return Arrays.asList(new MaterialAboutPreferenceItem(R.string.general, R.drawable.baseline_settings_24, GeneralFragment.class),
                new MaterialAboutPreferenceItem(R.string.favorites, R.drawable.baseline_favorite_24, FavoritesFragment.class));
    }

    @Override
    protected int getAppIconRes() {
        return R.mipmap.ic_launcher_round;
    }

    @Override
    protected boolean hasTutorial() {
        return false;
    }

    @Nullable
    @Override
    protected String getGithubProjectName() {
        return "DNSHero";
    }

    @Nullable
    @Override
    protected String getOpenSourceUrl() {
        return "https://github.com/devgianlu/DNSHero";
    }

    @Override
    protected boolean disableOtherDonationsOnGooglePlay() {
        return true;
    }

    public static class GeneralFragment extends BasePreferenceFragment {

        @Override
        protected void buildPreferences(@NonNull Context context) {
            MaterialCheckboxPreference nightMode = new MaterialCheckboxPreference.Builder(context)
                    .defaultValue(PK.NIGHT_MODE.fallback())
                    .key(PK.NIGHT_MODE.key())
                    .build();
            nightMode.setTitle(R.string.prefs_nightMode);
            nightMode.setSummary(R.string.prefs_nightMode_summary);
            addPreference(nightMode);
        }

        @Override
        public int getTitleRes() {
            return R.string.general;
        }
    }

    public static final class FavoritesFragment extends BasePreferenceFragment {

        private void showRemoveDialog(Context context) {
            final String[] entries = Prefs.getSet(PK.FAVORITES, new HashSet<>()).toArray(new String[0]);
            final boolean[] checked = new boolean[entries.length];

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.removeFavorite)
                    .setMultiChoiceItems(entries, checked, (dialog, which, isChecked) -> checked[which] = isChecked)
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        for (int i = 0; i < checked.length; i++) {
                            if (checked[i]) Prefs.removeFromSet(PK.FAVORITES, entries[i]);
                        }

                        if (Prefs.isSetEmpty(PK.FAVORITES)) onBackPressed();
                    }).setNegativeButton(android.R.string.cancel, null);

            showDialog(builder);
        }

        @Override
        protected void buildPreferences(@NonNull Context context) {
            if (!Prefs.isSetEmpty(PK.FAVORITES)) {
                final MaterialStandardPreference remove = new MaterialStandardPreference(context);
                remove.setTitle(R.string.removeFavorite);
                remove.setSummary(R.string.removeFavorite_summary);
                remove.setOnClickListener(v -> showRemoveDialog(context));
                addPreference(remove);
            }

            MaterialStandardPreference clear = new MaterialStandardPreference(context);
            clear.setTitle(R.string.clearFavorites);
            clear.setSummary(R.string.clearFavorites_summary);
            clear.setOnClickListener(v -> {
                Prefs.putSet(PK.FAVORITES, new HashSet<>());
                onBackPressed();
            });
            addPreference(clear);
        }

        @Override
        public int getTitleRes() {
            return R.string.favorites;
        }
    }
}
