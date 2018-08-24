package com.stratagile.qlink.view;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.UIUtils;

import java.math.BigDecimal;

/**
 * Created by huzhipeng on 2018/1/16.
 */

public class TextMoveLayout extends ViewGroup {
    private TextView textView;
    private int max;

    private boolean isDot = false;

    private int currentProgress = 0;

    public boolean isDot() {
        return isDot;
    }

    public void setDot(boolean dot) {
        isDot = dot;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public TextMoveLayout(Context context) {
        super(context);
        init();
    }

    public TextMoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextMoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void reSetTextAfterOnMeasure() {
        setText(currentProgress);
    }

    private void init() {
        textView = new TextView(getContext());
        textView.setTextColor(getResources().getColor(R.color.mainColor));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getWidth(), 50);
        addView(textView, layoutParams);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setText(currentProgress);
            }
        });
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setText(int progress) {
        if (getChildCount() == 0) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(getWidth(), 50);
            addView(textView, layoutParams);
        }
//        KLog.i("布局的宽度为:" + getWidth());
        currentProgress = progress;
//        KLog.i((getWidth() * (progress / (float) max)));
        if (isDot) {
//            if (progress % 5 != 0) {
//                return;
//            } else {
            Double value = ((double) progress / 10);
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            textView.setText(bd + "");
//            }
        } else {
            textView.setText(progress + "");
        }
//        KLog.i("显示的文字为:" + textView.getText());
        int left = (int) (getWidth() * (progress / (float) max));
        int jianju = (int) getContext().getResources().getDimension(R.dimen.x60);
        int right = left + jianju;
        if (left < 0) {
            left = 0;
            right = jianju;
        }
        if (right >= getWidth()) {
            left = getWidth() - jianju;
        }
        textView.layout(left, 0, right, 80);
    }
}
