package com.gianlu.dnshero.NetIO;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ZoneVisionAPI {
    private static final String BASE_URL = "http://api.zone.vision/";
    private static ZoneVisionAPI instance;
    private final ExecutorService executorService;
    private final HttpClient client;
    private final Handler handler;

    private ZoneVisionAPI() {
        executorService = Executors.newSingleThreadExecutor();
        client = HttpClients.createDefault();
        handler = new Handler(Looper.getMainLooper());
    }

    public static ZoneVisionAPI get() {
        if (instance == null) instance = new ZoneVisionAPI();
        return instance;
    }

    public void search(@NonNull final String query, final ISearch listener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse resp = client.execute(new HttpGet(new URI(BASE_URL + query.trim())));

                    JSONObject json = null;
                    HttpEntity entity = resp.getEntity();
                    if (entity != null) json = new JSONObject(EntityUtils.toString(entity, Charset.forName("UTF-8")));

                    final StatusLine sl = resp.getStatusLine();
                    int code = sl.getStatusCode();
                    if (json != null && code >= HttpStatus.SC_OK && code < HttpStatus.SC_MULTIPLE_CHOICES) {
                        final Domain domain = new Domain(json);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) listener.onDone(domain);
                            }
                        });
                    } else if (json != null && (code == HttpStatus.SC_BAD_REQUEST || code == HttpStatus.SC_INTERNAL_SERVER_ERROR)) {
                        String error = json.getString("error");
                        throw new ApiException(error);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onException(new StatusCodeException(sl));
                            }
                        });
                    }
                } catch (IOException | JSONException | URISyntaxException ex) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onException(ex);
                        }
                    });
                } catch (final ApiException ex) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onApiException(ex);
                        }
                    });
                }
            }
        });
    }

    public interface ISearch {
        void onDone(Domain domain);

        void onException(Exception ex);

        void onApiException(ApiException ex);
    }

    public static class StatusCodeException extends Exception {
        StatusCodeException(StatusLine sl) {
            super(sl.getStatusCode() + ": " + sl.getReasonPhrase());
        }
    }

    public static class ApiException extends Exception {
        ApiException(String message) {
            super(message);
        }
    }
}
