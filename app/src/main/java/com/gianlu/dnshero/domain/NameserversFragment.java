package com.gianlu.dnshero.domain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gianlu.commonutils.misc.RecyclerMessageView;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.api.Domain;

import java.util.ArrayList;

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
        RecyclerMessageView layout = new RecyclerMessageView(requireContext());
        layout.disableSwipeRefresh();

        Bundle args = getArguments();
        ArrayList<Domain.NS> authoritative;
        if (getContext() == null || args == null || (authoritative = (ArrayList<Domain.NS>) args.getSerializable("authoritative")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        layout.linearLayoutManager(RecyclerView.VERTICAL, false);
        layout.loadListData(new NSAdapter(getContext(), authoritative));

        return layout;
    }
}
