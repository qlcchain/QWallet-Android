package com.stratagile.qlink.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.socks.library.KLog;

import java.math.BigDecimal;

public class TimeValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return TimeUtil.getActiveDataTime(new BigDecimal(value).longValue());
    }
}
