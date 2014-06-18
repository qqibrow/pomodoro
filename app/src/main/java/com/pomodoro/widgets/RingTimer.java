package com.pomodoro.widgets;

import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by lniu on 6/18/14.
 * RingTimer is a Widget with a text shown the counterclock
 */
public class RingTimer {
    private final ProgressRingView ringView;
    private final TextView textView;
    private final myCountDownTimer timer;

    private Animation onFinishedAnimation = null;

    private static final long SECOND = 1000; // 1000 miliseconds
    private static final long MINUTE = 60 * SECOND;
    private static final long START_TIME = 30 * SECOND;

    RingTimer(ProgressRingView ringView, TextView textView,
              long millisInFuture, long countDownInterval) {
        this.ringView = ringView;
        this.textView = textView;
        this.timer = new myCountDownTimer(millisInFuture, countDownInterval);
    }

    final void start() {
        timer.start();
    }

    final void cancel() {
        timer.cancel();
    }

    boolean mCalled;

    protected void onFinish() {
        textView.setText("00:00");
        if(onFinishedAnimation != null)
            textView.startAnimation(onFinishedAnimation);
        ringView.setPhase(ProgressRingView.FINISHED);
        mCalled = true;
    }

    protected void onTick(long millisUntilFinished) {
        long leftMinutes = millisUntilFinished / MINUTE;
        long leftSeconds = (millisUntilFinished % MINUTE) / SECOND;
        textView.setText(String.format("%02d:%02d", leftMinutes,
                leftSeconds));

        float phase = (float) (START_TIME - (millisUntilFinished / 1000) * 1000)
                / START_TIME;
        ringView.setPhase(phase);
        mCalled = true;
    }

    public void setFinishedAnimation(Animation anim) {
        this.onFinishedAnimation = anim;
    }

    private class myCountDownTimer extends CountDownTimer {

        public myCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mCalled = false;
            RingTimer.this.onFinish();
            if(!mCalled) {
                throw new UnsupportedOperationException("super.onFinish() has not been called.");
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCalled = false;
            RingTimer.this.onTick(millisUntilFinished);
            if(!mCalled) {
                throw new UnsupportedOperationException("super.onTick() has not been called.");
            }
        }

    }
}
