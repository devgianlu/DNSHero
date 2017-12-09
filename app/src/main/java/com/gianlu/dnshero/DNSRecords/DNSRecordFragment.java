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
import com.gianlu.dnshero.NetIO.DNSRecord;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.lang.reflect.InvocationTargetException;

public class DNSRecordFragment<E extends DNSRecord.Entry, A extends DNSRecordsAdapter<E, ? extends DNSRecordsAdapter.ViewHolder>> extends Fragment {

    @SuppressWarnings("unchecked")
    private static <A extends DNSRecordsAdapter<E, VH>,
            VH extends DNSRecordsAdapter.ViewHolder,
            F extends DNSRecordFragment<E, A>,
            E extends DNSRecord.Entry> F getInstance(Context context, @StringRes int title, Class<A> adapter, Domain.DNSRecordsArrayList<E> authoritative, Domain.DNSRecordsArrayList<E> resolver) {
        DNSRecordFragment<E, A> fragment = new DNSRecordFragment<>();
        Bundle args = new Bundle();
        args.putString("title", context.getString(title));
        args.putSerializable("adapterClass", adapter);
        args.putSerializable("authoritative", authoritative);
        args.putSerializable("resolver", resolver);
        fragment.setArguments(args);
        return (F) fragment;
    }

    public static DNSRecordFragment<DNSRecord.MXEntry, MXAdapter> getMXInstance(Context context, Domain domain) {
        return getInstance(context, R.string.mx, MXAdapter.class, domain.authoritative.mx, domain.resolver.mx);
    }

    public static DNSRecordFragment<DNSRecord.AEntry, AAdapter> getAInstance(Context context, Domain domain) {
        return getInstance(context, R.string.a, AAdapter.class, domain.authoritative.a, domain.resolver.a);
    }

    public static DNSRecordFragment<DNSRecord.AEntry, AAdapter> getAAAAInstance(Context context, Domain domain) {
        return getInstance(context, R.string.aaaa, AAdapter.class, domain.authoritative.aaaa, domain.resolver.aaaa);
    }

    public static DNSRecordFragment<DNSRecord.CNAMEEntry, CNAMEAdapter> getCNAMEInstance(Context context, Domain domain) {
        return getInstance(context, R.string.cname, CNAMEAdapter.class, domain.authoritative.cname, domain.resolver.cname);
    }

    public static DNSRecordFragment<DNSRecord.TXTEntry, TXTAdapter> getTXTInstance(Context context, Domain domain) {
        return getInstance(context, R.string.txt, TXTAdapter.class, domain.authoritative.txt, domain.resolver.txt);
    }

    public static DNSRecordFragment<DNSRecord.SOAEntry, SOAAdapter> getSOAInstance(Context context, Domain domain) {
        return getInstance(context, R.string.soa, SOAAdapter.class, domain.authoritative.soa, domain.resolver.soa);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = new RecyclerViewLayout(inflater);
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        Domain.DNSRecordsArrayList<E> authoritative;
        Domain.DNSRecordsArrayList<E> resolver;
        Class<A> adapterClass;
        if (getContext() == null || args == null
                || (adapterClass = (Class<A>) args.getSerializable("adapterClass")) == null
                || (authoritative = (Domain.DNSRecordsArrayList<E>) args.getSerializable("authoritative")) == null
                || (resolver = (Domain.DNSRecordsArrayList<E>) args.getSerializable("resolver")) == null) {

            layout.showMessage(R.string.failedLoading, true);
            return layout;
        }

        layout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (authoritative.hasSomethingRelevant()) {
            try {
                layout.loadListData(adapterClass.getConstructor(Context.class, Domain.DNSRecordsArrayList.class, Domain.DNSRecordsArrayList.class).newInstance(getContext(), authoritative, resolver));
            } catch (java.lang.InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                Logging.logMe(ex);
                layout.showMessage(R.string.failedLoading, true);
                return layout;
            }
        } else {
            layout.showMessage(R.string.noRecords, false);
        }

        return layout;
    }
}
