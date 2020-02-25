package com.lite.pits_jawwal.pitstracklite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by WIN on 9/17/2016.
 */
public class WebClient extends WebViewClient {
    private Activity _activity;
    private ProgressDialog progressDialog;
    private boolean isRedirected = false;
    public WebClient(Activity activity) {
        _activity = activity;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        isRedirected = true;
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        isRedirected = false;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        try {

        if (!isRedirected && progressDialog == null) {
            progressDialog = new ProgressDialog(_activity);
            progressDialog.setMessage(_activity.getResources().getString(R.string.loading));
            progressDialog.setIndeterminateDrawable(_activity.getResources().getDrawable(R.drawable.progress));
            progressDialog.show();
        }}catch (Exception e){}
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        try{
            isRedirected=true;
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
           Map inst=Map.getInstance();
           inst.show_location("1");
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        try{
        if (_activity == null || _activity.isFinishing()) {
            return;
        }
        if (Utils.isOnline(_activity.getApplicationContext())) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
            builder.setTitle("Please check your internet connection.")
                    .setCancelable(false)
                    .setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            _activity.finish();

                            Intent intent = new Intent(_activity.getApplicationContext(), MainActivity.class);
                            _activity.startActivity(intent);
                        }
                    })
                    .create().show();
        }
    }catch (Exception e){
        }
    }
}
