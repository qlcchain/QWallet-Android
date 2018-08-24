package com.stratagile.qlink.card;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.ui.activity.rank.ComingSoonFragment;
import com.stratagile.qlink.ui.activity.rank.PoolFragment;
import com.stratagile.qlink.ui.activity.rank.WinnerFragment;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> mFragments;
    private float mBaseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, Active active) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;

        for (int i = 0; i < active.getData().getActs().size(); i++) {
            switch (active.getData().getActs().get(i).getActStatus()) {
                case "START":
                    addCardFragment(PoolFragment.newInstance(active.getData().getActs().get(i).getActId()));
                    break;
                case "NEW":
                    addCardFragment(ComingSoonFragment.newInstance(active.getData().getActs().get(i).getActId()));
                    break;
                case "END":
                    addCardFragment(WinnerFragment.newInstance(active.getData().getActs().get(i).getActId()));
                    break;
                case "PRIZED":
                    addCardFragment(WinnerFragment.newInstance(active.getData().getActs().get(i).getActId()));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        mFragments.add(fragment);
    }

}
