package com.stratagile.qlink.utils;

import android.support.v4.view.ViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

public class ViewPagerHelper {
    public ViewPagerHelper() {

    }

    public static void bind(final MagicIndicator magicIndicator, ViewPager viewPager, int count) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position % count, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position % count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}
