package com.stratagile.qlink.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.socks.library.KLog;
import com.stratagile.qlink.R;

/**
 * Created by huzhipeng on 2018/3/30.
 */

public class QlinkSeekBar extends LinearLayout {

    private SeekBar seekBar;
    private TextMoveLayout value;

    public int getDefaultMax() {
        return defaultMax;
    }

    public void setDefaultMax(int defaultMax) {
        this.defaultMax = defaultMax;
    }

    private int defaultMax;
    private int defaultProgress;
    private boolean isDot;

    public boolean isDot() {
        return isDot;
    }

    public void setDot(boolean dot) {
        isDot = dot;
    }

    public void setProgress(int progress) {
        KLog.i("设置的progress为:" + progress);
        seekBar.setProgress(progress);
        value.setText(progress);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public QlinkSeekBar(Context context) {
        super(context);
    }

    public QlinkSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public QlinkSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(@Nullable AttributeSet attrs) {
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs, R.styleable.QlinkSeekBar);
        defaultMax = typedArray.getInteger(R.styleable.QlinkSeekBar_max, 10);
        defaultProgress = typedArray.getInteger(R.styleable.QlinkSeekBar_progress, 1);
        isDot = typedArray.getBoolean(R.styleable.QlinkSeekBar_dot, false);
        typedArray.recycle();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qlink_seekbar_layout, this, true);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setMax(defaultMax);
        seekBar.setProgress(defaultProgress);
        value = view.findViewById(R.id.value);
        value.setDot(isDot);
        ImageView reduce = view.findViewById(R.id.reduce);
        ImageView increase = view.findViewById(R.id.increse);
        reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() == 0) {
                    return;
                }
                seekBar.setProgress(seekBar.getProgress() - 1);
            }
        });
        increase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() == seekBar.getMax()) {
                    return;
                }
                seekBar.setProgress(seekBar.getProgress() + 1);
            }
        });
        value.setMax(seekBar.getMax());
        value.setText(seekBar.getProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
