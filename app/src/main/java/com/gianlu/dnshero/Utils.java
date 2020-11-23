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

    public static float parseMs(@NonNull String str) {
        if (str.endsWith("ms") || str.endsWith("µs")) {
            String sub = str.substring(0, str.length() - 2);
            return Float.parseFloat(sub) / (str.endsWith("µs") ? 1000f : 1);
        } else {
            return Float.parseFloat(str);
        }
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
