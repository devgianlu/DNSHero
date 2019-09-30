package com.gianlu.dnshero;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gianlu.commonutils.ui.Toaster;

import java.text.DecimalFormat;

public final class Utils {
    public static final String ACTION_SEARCH = "search_domain";
    public static final String ACTION_SEARCH_INTENT = "search_domain_intent";

    @NonNull
    public static String formatRTT(float rtt) {
        return new DecimalFormat("#.000").format(rtt) + "ms";
    }

    public static float parseMs(String str) {
        float value = Float.parseFloat(str.substring(0, str.length() - 2));
        if (str.endsWith("µs")) return value / 1000f; // microseconds
        else return value; // milliseconds
    }

    public static void clickToCopy(@NonNull TextView view) {
        view.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard == null) return;
            clipboard.setPrimaryClip(ClipData.newPlainText("", view.getText().toString()));
            Toaster.with(view.getContext()).message(R.string.copiedToClipboard).show();
        });
    }

    public static void clickToCopy(@NonNull TextView view, @NonNull String text) {
        view.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard == null) return;
            clipboard.setPrimaryClip(ClipData.newPlainText("", text));
            Toaster.with(view.getContext()).message(R.string.copiedToClipboard).show();
        });
    }
}
