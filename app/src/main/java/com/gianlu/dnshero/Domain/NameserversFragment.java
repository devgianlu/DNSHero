package com.gianlu.dnshero.Domain;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.ArrayList;

public class NameserversFragment extends Fragment {

    public static NameserversFragment getInstance(Context context, Domain domain) {
        NameserversFragment fragment = new NameserversFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.nameservers));
        args.putSerializable("authoritative", domain.authoritative.ns);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = new RecyclerViewLayout(inflater);
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        ArrayList<Domain.NS> authoritative;
        if (getContext() == null || args == null || (authoritative = (ArrayList<Domain.NS>) args.getSerializable("authoritative")) == null) {
            layout.showMessage(R.string.failedLoading, true);
            return layout;
        }

        layout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        layout.loadListData(new NSAdapter(getContext(), authoritative));

        return layout;
    }
}
