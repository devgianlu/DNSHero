package com.gianlu.dnshero.api;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.gianlu.commonutils.lifecycle.LifecycleAwareHandler;
import com.gianlu.commonutils.lifecycle.LifecycleAwareRunnable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ZoneVisionAPI {
    private static final String BASE_URL = "https://api.zone.vision/";
    private static ZoneVisionAPI instance;
    private final ExecutorService executorService;
    private final OkHttpClient client;
    private final LifecycleAwareHandler handler;

    private ZoneVisionAPI() {
        executorService = Executors.newSingleThreadExecutor();
        client = new OkHttpClient();
        handler = new LifecycleAwareHandler(new Handler(Looper.getMainLooper()));
    }

    @NonNull
    public static ZoneVisionAPI get() {
        if (instance == null) instance = new ZoneVisionAPI();
        return instance;
    }

    public void search(@NonNull String query, @Nullable Activity activity, @NonNull OnSearch listener) {
        executorService.execute(new LifecycleAwareRunnable(handler, activity == null ? listener : activity) {
            @Override
            public void run() {
                try (final Response resp = client.newCall(new Request.Builder()
                        .get().url(BASE_URL + query.trim()).build()).execute()) {

                    JSONObject json = null;
                    ResponseBody body = resp.body();
                    if (body != null) json = new JSONObject(body.string());

                    if (json != null && resp.code() >= 200 && resp.code() < 300) {
                        Domain domain = new Domain(json);
                        post(() -> listener.onDone(domain));
                    } else if (json != null && (resp.code() == 400 || resp.code() == 500)) {
                        String error = json.getString("error");
                        throw new ApiException(error);
                    } else {
                        post(() -> listener.onException(new StatusCodeException(resp)));
                    }
                } catch (IOException | JSONException ex) {
                    post(() -> listener.onException(ex));
                } catch (ApiException ex) {
                    post(() -> listener.onApiException(ex));
                }
            }
        });
    }

    @UiThread
    public interface OnSearch {
        void onDone(@NonNull Domain domain);

        void onException(@NonNull Exception ex);

        void onApiException(@NonNull ApiException ex);
    }

    public static class StatusCodeException extends Exception {
        StatusCodeException(Response resp) {
            super(resp.code() + ": " + resp.message());
        }
    }

    public static class ApiException extends Exception {
        ApiException(String message) {
            super(message);
        }
    }
}
