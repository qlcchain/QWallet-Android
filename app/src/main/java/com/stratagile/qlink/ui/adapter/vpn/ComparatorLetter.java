package com.stratagile.qlink.ui.adapter.vpn;

import com.stratagile.qlink.entity.CountryEntity;

import java.util.Comparator;
import java.util.Locale;


public class ComparatorLetter implements Comparator<CountryEntity> {

    @Override
    public int compare(CountryEntity l, CountryEntity r) {
        if (l == null || r == null) {
            return 0;
        }

        String lhsSortLetters = l.getName().substring(0, 1).toUpperCase(Locale.ENGLISH);
        String rhsSortLetters = r.getName().substring(0, 1).toUpperCase(Locale.ENGLISH);
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}