package com.stratagile.qlink.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;

import org.w3c.dom.Text;

/**
 * Created by huzhipeng on 2018/1/16.
 */

public class WiFiInfoView extends LinearLayout {
    private CharSequence infoTitle;
    private CharSequence infoContent;
    private CharSequence infoHint;
    private TextView title;
    private EditText content;
    private boolean infoEnable;

    public CharSequence getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(CharSequence infoTitle) {
        this.infoTitle = infoTitle;
        title.setText(infoTitle);
    }

    public CharSequence getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(CharSequence infoContent) {
        this.infoContent = infoContent;
        content.setText(infoContent);
    }

    public WiFiInfoView(Context context) {
        super(context);
    }

    public WiFiInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WiFiInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs, R.styleable.wifiinfoview);
        infoTitle = typedArray.getText(R.styleable.wifiinfoview_infotitle);
        infoContent = typedArray.getText(R.styleable.wifiinfoview_infocontent);
        infoHint = typedArray.getText(R.styleable.wifiinfoview_infohint);
        infoEnable = typedArray.getBoolean(R.styleable.wifiinfoview_infoenable, false);
        typedArray.recycle();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.wifi_infoview, this, true);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        content.setHint(infoHint);
        title.setText(infoTitle);
        content.setEnabled(infoEnable);
        content.setText(infoContent);
    }

    public CharSequence getText() {
        return content.getText().toString().trim();
    }

    public EditText getContent() {
        return content;
    }
}
