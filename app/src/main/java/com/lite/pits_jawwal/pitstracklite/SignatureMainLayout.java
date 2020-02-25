package com.lite.pits_jawwal.pitstracklite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SignatureMainLayout extends LinearLayout implements OnClickListener {

    LinearLayout buttonsLayout;
    SignatureView signatureView;

    public SignatureMainLayout(Context context) {
        super(context);

        this.setOrientation(LinearLayout.VERTICAL);

        this.buttonsLayout = this.buttonsLayout();
        this.signatureView = new SignatureView(context);

        // add the buttons and signature views
        this.addView(this.buttonsLayout);
        this.addView(signatureView);

    }

    private LinearLayout buttonsLayout() {

        // create the UI programatically
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        Button saveBtn = new Button(this.getContext());
        Button clearBtn = new Button(this.getContext());
        Button back = new Button(this.getContext());

        // set orientation
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#99c961"));

        // set texts, tags and OnClickListener
        saveBtn.setText("موافق");
        saveBtn.setTag("Save");
        saveBtn.setBackgroundColor(Color.parseColor("#99c961"));
        saveBtn.setTextColor(Color.parseColor("#ffffff"));
        saveBtn.setOnClickListener(this);

        clearBtn.setText("حذف");
        clearBtn.setTag("Clear");
        clearBtn.setBackgroundColor(Color.parseColor("#99c961"));
        clearBtn.setTextColor(Color.parseColor("#ffffff"));
        clearBtn.setOnClickListener(this);

        back.setText("رجوع");
        back.setTag("back");
        back.setBackgroundColor(Color.parseColor("#99c961"));
        back.setTextColor(Color.parseColor("#ffffff"));
        back.setOnClickListener(this);
        back.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        back.setGravity(Gravity.LEFT);
        back.setPadding(20,35,0,0);

        linearLayout.addView(saveBtn);
        linearLayout.addView(clearBtn);
        linearLayout.addView(back);

        // return the whoe layout
        return linearLayout;
    }

    // the on click listener of 'save' and 'clear' buttons
    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().trim();

        // save the signature
        if (tag.equalsIgnoreCase("save")) {
//            Violation violation=Violation.getViolation();
//            violation.show_sign(this.signatureView.getSignature());
            Replay_Report violation=Replay_Report.getReplay_report();
            violation.setImage(this.signatureView.getSignature());
//            Toast.makeText(this.getContext(), "Signature saved.", Toast.LENGTH_LONG).show();
            Activity activity = (Activity)getContext();
            activity.finish();
           // this.saveImage(this.signatureView.getSignature());
        }
        else  if (tag.equalsIgnoreCase("Clear"))  {
            this.signatureView.clearSignature();
        } else  if (tag.equalsIgnoreCase("back"))  {
            Activity activity = (Activity)getContext();
            activity.finish();
        }

    }

    /**
     * save the signature to an sd card directory
     * @param signature bitmap
     */
    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        String fname = "signature.png";

        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }

        try {

            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Replay_Report violation=Replay_Report.getReplay_report();
            violation.setImage(signature);
            Toast.makeText(this.getContext(), "Signature saved.", Toast.LENGTH_LONG).show();
            Activity activity = (Activity)getContext();
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The View where the signature will be drawn
     */
    private class SignatureView extends View {

        // set the stroke width
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public SignatureView(Context context) {

            super(context);

            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);

            // set the bg color as white
            this.setBackgroundColor(Color.WHITE);

            // width and height should cover the screen
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        }

        /**
         * Get signature
         *
         * @return
         */
        protected Bitmap getSignature() {

            Bitmap signatureBitmap = null;

            // set the signature bitmap
            if (signatureBitmap == null) {
                signatureBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
            }

            // important for saving signature
            final Canvas canvas = new Canvas(signatureBitmap);
            this.draw(canvas);

            return signatureBitmap;
        }

        /**
         * clear signature canvas
         */
        private void clearSignature() {
            path.reset();
            this.invalidate();
        }

        // all touch events during the drawing
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(this.path, this.paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    path.moveTo(eventX, eventY);

                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);

                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:

                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }

}