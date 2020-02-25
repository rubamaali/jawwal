package com.lite.pits_jawwal.pitstracklite.Fount;

/**
 * Created by Ruba-PITS on 7/6/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewL extends TextView {
    public CustomTextViewL(Context context) {
        super(context);
        setFont();
    }
    public CustomTextViewL(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextViewL(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
