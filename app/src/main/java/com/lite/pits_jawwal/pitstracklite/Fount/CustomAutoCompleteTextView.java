package com.lite.pits_jawwal.pitstracklite.Fount;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Ruba-PITS on 9/28/2017.
 */

public class CustomAutoCompleteTextView extends AutoCompleteTextView {
    public CustomAutoCompleteTextView(Context context) {
        super(context);
        setFont();
    }
    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
