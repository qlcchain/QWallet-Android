package com.stratagile.qlink.ui.activity.otc;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MarketListAdapter extends FragmentStatePagerAdapter {//FragmentPagerAdapter

    private ArrayList<Fragment> fragments = null;
    private Context context;

    public MarketListAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
//        Fragment fragment = new ColourFragment();
//        Bundle args = new Bundle();
//        args.putInt("title", arg0);
//        args.putSerializable("content",hotIssuesList.get(arg0));
//        fragment.setArguments(args);
//        return fragment;
        return fragments.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
