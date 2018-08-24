package com.stratagile.qlink.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

/**
 * Created by huzhipeng on 2018/1/11.
 */

public class TextSpaceView extends AppCompatTextView {
    private float spacing = Spacing.NORMAL;
    private CharSequence originalText = "";


    public TextSpaceView(Context context) {
        super(context);
    }

    public TextSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSpacing(4f);
    }

    public TextSpaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSpacing(4f);
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    private void applySpacing() {
        if (this == null || this.originalText == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i + 1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((spacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

    public class Spacing {
        public final static float NORMAL = 4f;
    }
}