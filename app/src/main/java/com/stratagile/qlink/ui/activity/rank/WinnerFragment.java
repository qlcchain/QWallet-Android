package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.card.CardFragment;
import com.stratagile.qlink.entity.Active;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WinnerFragment extends CardFragment {
    Unbinder unbinder;
    @BindView(R.id.tv_winner)
    TextView tvWinner;
    private static final String ARG_TYPE = "arg_type";
    private RankViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View attachView = inflater.inflate(R.layout.fragment_winner, null, false);
        setView(attachView);
        unbinder = ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(RankViewModel.class);
        setListener();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setActId(getArguments().getString(ARG_TYPE));
        }
    }

    private void setListener() {
        viewModel.getActive().observe(this, new Observer<Active>() {
            @Override
            public void onChanged(@Nullable Active active) {
                for (int i = 0; i < active.getData().getActs().size(); i++) {
                    if (active.getData().getActs().get(i).getActId().equals(getActId())) {
                        tvWinner.setText(Html.fromHtml(AppConfig.getInstance().getResources().getString(R.string.winner_list_round_1_2300_qlc_prize_pool, active.getData().getActs().get(i).getActName(), (active.getData().getActs().get(i).getActAmount()))));
                    }
                }
            }
        });
    }

    public static WinnerFragment newInstance(String param) {
        WinnerFragment fragment = new WinnerFragment();
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
