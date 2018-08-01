package com.stratagile.qlink.ui.activity.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.card.CardFragment;

public class PoolFragment extends CardFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View attachView = inflater.inflate(R.layout.fragment_pool, null, false);
        setView(attachView);
        return view;
    }


}
