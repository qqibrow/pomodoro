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
        public void onFinish(final PomoTimer pomo);
    }

    private onFinishListener mOnFinishListener;

    public void setOnFinishListener(onFinishListener listener) {
        this.mOnFinishListener = listener;
    }

    private Ringtone ringtone;

    private long workTime = TimeUnit.MILLISECONDS.convert(25, TimeUnit.SECONDS);
    private long restTime = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS);

    private final long SECOND = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

    // Indicate whether pomo is working or is resting.
    boolean workMode;

    public PomoTimer(ProgressRingView ringView, TextView textView,
                     Ringtone r, long workTime, long restTime) {
        super(ringView, textView);
        this.ringtone = r;

        this.workTime = workTime;
        this.restTime = restTime;

        toWorkMode();
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
        setTimer(workTime, SECOND);
    }

    public final void toRestMode() {
        workMode = false;
        setTimer(restTime, SECOND);
    }
}