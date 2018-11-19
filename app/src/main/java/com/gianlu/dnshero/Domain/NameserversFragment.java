package com.gianlu.dnshero.Domain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.dnshero.NetIO.Domain;
import com.gianlu.dnshero.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NameserversFragment extends Fragment {

    @NonNull
    public static NameserversFragment getInstance(@NonNull Context context, @NonNull Domain domain) {
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
        RecyclerViewLayout layout = new RecyclerViewLayout(requireContext());
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        ArrayList<Domain.NS> authoritative;
        if (getContext() == null || args == null || (authoritative = (ArrayList<Domain.NS>) args.getSerializable("authoritative")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        layout.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        layout.loadListData(new NSAdapter(getContext(), authoritative));

        return layout;
    }
}
