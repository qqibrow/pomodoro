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
    private myCountDownTimer timer;

    private Animation onFinishedAnimation = null;

    private static final long SECOND = 1000; // 1000 miliseconds
    private static final long MINUTE = 60 * SECOND;


    private long timeInterval;

    public RingTimer(ProgressRingView ringView, TextView textView) {
        this.ringView = ringView;
        this.textView = textView;
        this.timer = new myCountDownTimer(MINUTE, SECOND);
        timeInterval = SECOND;
    }

    public RingTimer(ProgressRingView ringView, TextView textView,
              long millisInFuture, long countDownInterval) {
        this.ringView = ringView;
        this.textView = textView;
        this.timer = new myCountDownTimer(millisInFuture, countDownInterval);
        timeInterval = millisInFuture;
    }

    public final void start() {
        timer.start();
    }

    public final void setTimer( long millisInFuture, long countDownInterval) {
        this.timer = new myCountDownTimer(millisInFuture, countDownInterval);
        timeInterval = millisInFuture;
        this.init();
    }

    public final void cancel() {
        timer.cancel();
        performReset();
    }

    private void performReset() {
        mCalled = false;
        reset();
        if(!mCalled)
            throw new UnsupportedOperationException("super.reset() has not been called.");

    }

    protected void reset() {
        // set timer back to init string.
        long leftMinutes = timeInterval / MINUTE;
		long leftSeconds = (timeInterval % MINUTE) / SECOND;
		String startText = String.format("%02d:%02d", leftMinutes, leftSeconds);
        this.textView.setText(startText);

        // clear animation of timer text if necessary.
        if(this.onFinishedAnimation != null)
            textView.clearAnimation();

        // reset ring.
        ringView.setPhase(ProgressRingView.START);
        mCalled = true;
    }

    //    private void resetTimer() {
//		// Reset Text for timer.
//		long leftMinutes = timeInterval / MINUTE;
//		long leftSeconds = (timeInterval % MINUTE) / SECOND;
//		String startText = String.format("%02d:%02d", leftMinutes, leftSeconds);
//		timerText.setText(startText);
//
//		// Reset the flag.
//		timerStarted = false;
//
//		// Clear animation for timer text.
//		timerText.clearAnimation();
//		ring.setPhase(ProgressRingView.START);
//
//		// reset ringtone.
//		if (r.isPlaying())
//			r.stop();
//	}

    public final void init() {
        performReset();
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

        float phase = (float) (timeInterval - (millisUntilFinished / SECOND) * SECOND)
                / timeInterval;
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
