package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.card.CardFragment;
import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.utils.TimeUtil;
import com.stratagile.qlink.view.TimeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PoolFragment extends CardFragment {
    @BindView(R.id.time_view)
    TimeView timeView;
    Unbinder unbinder;
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.tv_qlc_count)
    TextView tvQlcCount;
    private RankViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View attachView = inflater.inflate(R.layout.fragment_pool, null, false);
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
                        tvQlcCount.setText(active.getData().getActs().get(i).getActAmount() + "");
                        timeView.setRemainTime(TimeUtil.timeStamp(active.getData().getActs().get(i).getActEndDate()), TimeUtil.timeStamp(active.getData().getCurrentDate()));
                    }
                }
            }
        });
    }

    public static PoolFragment newInstance(String param) {
        PoolFragment fragment = new PoolFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        timeView.onFinish();
        super.onDestroyView();
        unbinder.unbind();
    }
}
