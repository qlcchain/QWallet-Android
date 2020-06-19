package com.stratagile.qlink.utils;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;

import org.xml.sax.XMLReader;

public class SizeLabel implements Html.TagHandler {
    private int size;
    private int startIndex = 0;
    private int stopIndex = 0;
    public SizeLabel(int size) {
        this.size = size;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        KLog.i(tag);
        if(tag.toLowerCase().equals("size")) {
            if(opening){
                startIndex = output.length();
            }else{
                stopIndex = output.length();
                KLog.i(startIndex);
                KLog.i(stopIndex);
                KLog.i(output.toString());
                if (!"Rating".equals(output.toString())) {
                    output.setSpan(new AbsoluteSizeSpan(13, true), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    output.setSpan(new AbsoluteSizeSpan(9, true), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
