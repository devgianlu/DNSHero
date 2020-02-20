package com.gianlu.dnshero.domain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.misc.SuperTextView;
import com.gianlu.dnshero.GlueView;
import com.gianlu.dnshero.R;
import com.gianlu.dnshero.Utils;
import com.gianlu.dnshero.api.Domain;

public class RootNameserverFragment extends Fragment {
    public static RootNameserverFragment getInstance(Context context, Domain domain) {
        RootNameserverFragment fragment = new RootNameserverFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.rootNameServer));
        args.putSerializable("root", domain.root);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView layout = (ScrollView) inflater.inflate(R.layout.fragment_root_nameserver, container, false);

        Bundle args = getArguments();
        Domain.RootNameserver root;
        if (getContext() == null || args == null || (root = (Domain.RootNameserver) args.getSerializable("root")) == null)
            return null;

        SuperTextView name = layout.findViewById(R.id.rootNsFragment_name);
        Utils.clickToCopy(name);
        name.setText(root.name);

        SuperTextView rtt = layout.findViewById(R.id.rootNsFragment_rtt);
        rtt.setHtml(R.string.rtt, Utils.formatRTT(root.rtt));

        final LinearLayout nameservers = layout.findViewById(R.id.rootNsFragment_nameservers);
        for (String nameserver : root.nameservers) {
            TextView text = (TextView) inflater.inflate(R.layout.item_secondary_text, nameservers, false);
            text.setText(nameserver);
            Utils.clickToCopy(text);

            nameservers.addView(text);
        }

        final ImageButton toggleNs = layout.findViewById(R.id.rootNsFragment_toggleNs);
        toggleNs.setOnClickListener(v -> CommonUtils.handleCollapseClick(toggleNs, nameservers));

        GlueView glue = layout.findViewById(R.id.rootNsFragment_glue);
        glue.setGlue(root.glue);

        return layout;
    }
}
