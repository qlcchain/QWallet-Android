package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.card.CardFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ComingSoonFragment extends CardFragment {
    Unbinder unbinder;
    private RankViewModel viewModel;
    private static final String ARG_TYPE = "arg_type";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View attachView = inflater.inflate(R.layout.fragment_coming_soon, null, false);
        setView(attachView);
        unbinder = ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(RankViewModel.class);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setActId(getArguments().getString(ARG_TYPE));
        }
    }

    public static ComingSoonFragment newInstance(String param) {
        ComingSoonFragment fragment = new ComingSoonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
