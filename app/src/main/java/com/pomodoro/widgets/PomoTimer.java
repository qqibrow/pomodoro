package com.pomodoro.widgets;

import android.media.Ringtone;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by lniu on 6/19/14.
 */

/*
    TODO(lniu) I don't need to extends Ringtimer. Is has a member of ringtimer a better idea?

 */
public class PomoTimer extends RingTimer {

    public interface onFinishListener {
        public void onFinish(PomoTimer pomo);
    }

    private onFinishListener mOnFinishListener;

    public void setOnFinishListener(onFinishListener listener) {
        this.mOnFinishListener = listener;
    }

    private Ringtone ringtone;

    private final long WORKING_TIME = TimeUnit.MILLISECONDS.convert(25, TimeUnit.SECONDS);
    private final long REST_TIME = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);

    private final long SECOND = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

    // Indicate whether pomo is working or is resting.
    boolean workMode;

    public PomoTimer(ProgressRingView ringView, TextView textView,
                     Ringtone r) {
        super(ringView, textView);
        this.ringtone = r;
        workMode = true;
        setTimer(WORKING_TIME, SECOND);
    }

    @Override
    protected void reset() {
        super.reset();
        if (ringtone.isPlaying())
            ringtone.stop();
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        try {
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mOnFinishListener != null) {
            mOnFinishListener.onFinish(this);
        }
    }

    public boolean isWorkMode() {
        return workMode;
    }

    public final void toWorkMode() {
        workMode = true;
        setTimer(WORKING_TIME, SECOND);
    }

    public final void toRestMode() {
        workMode = false;
        setTimer(REST_TIME, SECOND);
    }
}