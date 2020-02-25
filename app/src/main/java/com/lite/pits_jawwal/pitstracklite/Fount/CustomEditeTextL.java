package com.lite.pits_jawwal.pitstracklite.Fount;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Ruba-PITS on 7/6/2017.
 */

public class CustomEditeTextL extends EditText {
    public CustomEditeTextL(Context context) {
        super(context);
        setFont();
    }
    public CustomEditeTextL(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomEditeTextL(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
