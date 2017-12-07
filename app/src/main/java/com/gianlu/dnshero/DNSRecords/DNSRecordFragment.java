package com.gianlu.dnshero.DNSRecords;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.Logging;
import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.lang.reflect.InvocationTargetException;

public class DNSRecordFragment<E extends Domain.GeneralRecordEntry, D extends Domain.DNSRecordsArrayList.RelevantData, A extends DNSRecordsAdapter<E, D, ? extends DNSRecordsAdapter.ViewHolder>> extends Fragment {

    @SuppressWarnings("unchecked")
    private static <A extends DNSRecordsAdapter<E, D, VH>,
            VH extends DNSRecordsAdapter.ViewHolder,
            D extends Domain.DNSRecordsArrayList.RelevantData,
            F extends DNSRecordFragment<E, D, A>,
            E extends Domain.GeneralRecordEntry> F getInstance(Context context, @StringRes int title, Class<A> adapter, Domain.DNSRecordsArrayList<E, D> authoritative, Domain.DNSRecordsArrayList<E, D> resolver) {
        DNSRecordFragment<E, D, A> fragment = new DNSRecordFragment<>();
        Bundle args = new Bundle();
        args.putString("title", context.getString(title));
        args.putSerializable("adapterClass", adapter);
        args.putSerializable("authoritative", authoritative);
        args.putSerializable("resolver", resolver);
        fragment.setArguments(args);
        return (F) fragment;
    }

    public static DNSRecordFragment<Domain.MXEntry, Domain.MXEntries.RelevantData, MXAdapter> getMXInstance(Context context, Domain domain) {
        return getInstance(context, R.string.mx, MXAdapter.class, domain.authoritative.mx, domain.resolver.mx);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = new RecyclerViewLayout(inflater);
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        Domain.DNSRecordsArrayList<E, D> authoritative;
        Domain.DNSRecordsArrayList<E, D> resolver;
        Class<A> adapterClass;
        if (getContext() == null || args == null
                || (adapterClass = (Class<A>) args.getSerializable("adapterClass")) == null
                || (authoritative = (Domain.DNSRecordsArrayList<E, D>) args.getSerializable("authoritative")) == null
                || (resolver = (Domain.DNSRecordsArrayList<E, D>) args.getSerializable("resolver")) == null) {

            layout.showMessage(R.string.failedLoading, true);
            return layout;
        }

        layout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        try {
            layout.loadListData(adapterClass.getConstructor(Context.class, Domain.DNSRecordsArrayList.class, Domain.DNSRecordsArrayList.class).newInstance(getContext(), authoritative, resolver));
        } catch (java.lang.InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Logging.logMe(ex);
            layout.showMessage(R.string.failedLoading, true);
            return layout;
        }

        return layout;
    }
}
