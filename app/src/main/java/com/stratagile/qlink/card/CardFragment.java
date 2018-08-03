package com.stratagile.qlink.card;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.stratagile.qlink.R;
import com.stratagile.qlink.ui.activity.rank.RankViewModel;

public class CardFragment extends Fragment {
    private String actId = "";

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    private CardView mCardView;

    private FrameLayout cardRootView;

    private RankViewModel rankViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        cardRootView = view.findViewById(R.id.card_root_view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rankViewModel = ViewModelProviders.of(getActivity()).get(RankViewModel.class);
    }

    public CardView getCardView() {
        return mCardView;
    }

    public void setView(View view) {
        cardRootView.addView(view);
    }

    public RankViewModel getViewModel() {
        return rankViewModel;
    }


}
