package com.stratagile.qlink.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;

public class ActiveDataYValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return DefiUtil.INSTANCE.parseValue(value + "", false);
    }
}
