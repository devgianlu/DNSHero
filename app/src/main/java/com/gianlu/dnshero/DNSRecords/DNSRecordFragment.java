package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.gianlu.dnshero.NetIO.Domain;

import java.util.ArrayList;

public abstract class DNSRecordFragment<E extends Domain.GeneralRecordEntry> extends Fragment {

    protected static <F extends DNSRecordFragment<E>, E extends Domain.GeneralRecordEntry> F prepareInstance(Context context, F instance, ArrayList<Domain.DNSRecord<E>> authoritative, ArrayList<Domain.DNSRecord<E>> resolver) {
        Bundle args = new Bundle();
        args.putString("title", context.getString(instance.getTitleRes()));
        args.putSerializable("authoritative", authoritative);
        args.putSerializable("resolver", resolver);
        instance.setArguments(args);
        return instance;
    }

    @StringRes
    protected abstract int getTitleRes();

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void onCreateView(@NonNull ViewGroup layout, ArrayList<Domain.DNSRecord<E>> authoritative, ArrayList<Domain.DNSRecord<E>> resolver);

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView layout = new ScrollView(getContext());
        inflater.inflate(getLayoutRes(), layout, false);

        Bundle args = getArguments();
        ArrayList<Domain.DNSRecord<E>> authoritative;
        ArrayList<Domain.DNSRecord<E>> resolver;
        if (args == null
                || (authoritative = (ArrayList<Domain.DNSRecord<E>>) args.getSerializable("authoritative")) == null
                || (resolver = (ArrayList<Domain.DNSRecord<E>>) args.getSerializable("resolver")) == null) {

            return null;
        }

        onCreateView(layout, authoritative, resolver);

        return layout;
    }
}
