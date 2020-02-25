package com.lite.pits_jawwal.pitstracklite.Fount;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ruba-PITS on 7/6/2017.
 */

public class CustomTextViewB extends TextView {
    public CustomTextViewB(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewB(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
