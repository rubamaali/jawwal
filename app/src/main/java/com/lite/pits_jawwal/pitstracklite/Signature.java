package com.lite.pits_jawwal.pitstracklite;

import android.app.Activity;
import android.os.Bundle;

public class Signature extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SignatureMainLayout(this));
    }

}
