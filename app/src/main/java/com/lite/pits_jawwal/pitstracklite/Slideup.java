package com.lite.pits_jawwal.pitstracklite;

/**
 * Created by Ruba-PITS on 7/8/2017.
 */


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;

public class Slideup implements OnTouchListener, AnimatorUpdateListener, AnimatorListener {
    private View view;
    private float touchableTop;
    private int autoSlideDuration = 300;
    private Slideup.SlideListener slideListener;
    private ValueAnimator valueAnimator;
    private float slideAnimationTo;
    private float startPositionY;
    private float viewStartPositionY;
    private boolean canSlide = true;
    private float density;
    private float lowerPosition;
    private float viewHeight;
    private boolean hiddenInit;

    public Slideup(final View view) {
        this.view = view;
        this.density = view.getResources().getDisplayMetrics().density;
        this.touchableTop = 300.0F * this.density;
        view.setOnTouchListener(this);
        view.setPivotY(0.0F);
        this.createAnimation();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if(Slideup.this.hiddenInit) {
                    Slideup.this.viewHeight = (float)view.getHeight();
                    Slideup.this.hideImmediately();
                }

                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public boolean isVisible() {
        return this.view.getVisibility() == View.VISIBLE;
    }

    public void setSlideListener(Slideup.SlideListener slideListener) {
        this.slideListener = slideListener;
    }

    public void setAutoSlideDuration(int autoSlideDuration) {
        this.autoSlideDuration = autoSlideDuration;
    }

    public float getAutoSlideDuration() {
        return (float)this.autoSlideDuration;
    }

    public void setTouchableTop(float touchableTop) {
        this.touchableTop = touchableTop * this.density;
    }

    public float getTouchableTop() {
        return this.touchableTop / this.density;
    }

    public boolean isAnimationRunning() {
        return this.valueAnimator != null && this.valueAnimator.isRunning();
    }

    public void animateIn() {
        this.slideAnimationTo = 0.0F;
        this.valueAnimator.setFloatValues(new float[]{this.viewHeight, this.slideAnimationTo});
        this.valueAnimator.start();
    }

    public void animateOut() {
        this.slideAnimationTo = (float)this.view.getHeight();
        this.valueAnimator.setFloatValues(new float[]{this.view.getTranslationY(), this.slideAnimationTo});
        this.valueAnimator.start();
    }

    public void hideImmediately() {
        if(this.view.getHeight() > 0) {
            this.view.setTranslationY(this.viewHeight);
            this.view.setVisibility(View.GONE);
            this.notifyVisibilityChanged(8);
        } else {
            this.hiddenInit = true;
        }

    }

    private void createAnimation() {
        this.valueAnimator = ValueAnimator.ofFloat(new float[0]);
        this.valueAnimator.setDuration((long)this.autoSlideDuration);
        this.valueAnimator.setInterpolator(new DecelerateInterpolator());
        this.valueAnimator.addUpdateListener(this);
        this.valueAnimator.addListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        float touchedArea = event.getRawY() - (float)this.view.getTop();
        if(this.isAnimationRunning()) {
            return false;
        } else {
            switch(event.getActionMasked()) {
                case 0:
                    this.viewHeight = (float)this.view.getHeight();
                    this.startPositionY = event.getRawY();
                    this.viewStartPositionY = this.view.getTranslationY();
                    if(this.touchableTop < touchedArea) {
                        this.canSlide = false;
                    }
                    break;
                case 1:
                    float slideAnimationFrom = this.view.getTranslationY();
                    boolean mustSlideUp = this.lowerPosition > event.getRawY();
                    boolean scrollableAreaConsumed = this.view.getTranslationY() > (float)(this.view.getHeight() / 5);
                    if(scrollableAreaConsumed && !mustSlideUp) {
                        this.slideAnimationTo = (float)this.view.getHeight();
                    } else {
                        this.slideAnimationTo = 0.0F;
                    }

                    this.valueAnimator.setFloatValues(new float[]{slideAnimationFrom, this.slideAnimationTo});
                    this.valueAnimator.start();
                    this.canSlide = true;
                    this.lowerPosition = 0.0F;
                    break;
                case 2:
                    float difference = event.getRawY() - this.startPositionY;
                    float moveTo = this.viewStartPositionY + difference;
                    float percents = moveTo * 100.0F / (float)this.view.getHeight();
                    if(moveTo > 0.0F && this.canSlide) {
                        this.notifyPercentChanged(percents);
                        this.view.setTranslationY(moveTo);
                    }

                    if(event.getRawY() > this.lowerPosition) {
                        this.lowerPosition = event.getRawY();
                    }
            }

            return true;
        }
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        float val = ((Float)animation.getAnimatedValue()).floatValue();
        this.view.setTranslationY(val);
        float percents = (this.view.getY() - (float)this.view.getTop()) * 100.0F / this.viewHeight;
        this.notifyPercentChanged(percents);
    }

    private void notifyPercentChanged(float percent) {
        if(this.slideListener != null) {
            this.slideListener.onSlideDown(percent);
        }

    }

    private void notifyVisibilityChanged(int visibility) {
        if(this.slideListener != null) {
            this.slideListener.onVisibilityChanged(visibility);
        }

    }

    public void onAnimationStart(Animator animator) {
        this.view.setVisibility(View.VISIBLE);
        this.notifyVisibilityChanged(0);
    }

    public void onAnimationEnd(Animator animator) {
        if(this.slideAnimationTo > 0.0F) {
            this.view.setVisibility(View.GONE);
            this.notifyVisibilityChanged(8);
        }

    }

    public void onAnimationCancel(Animator animator) {
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public interface SlideListener {
        void onSlideDown(float var1);

        void onVisibilityChanged(int var1);
    }
}

