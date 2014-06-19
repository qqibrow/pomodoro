package com.pomodoro.widgets;

import android.media.Ringtone;
import android.widget.TextView;

/**
 * Created by lniu on 6/19/14.
 */
public class RingTimerWithRingtone extends RingTimer {

    private Ringtone ringtone;
    public RingTimerWithRingtone(ProgressRingView ringView, TextView textView,
                                 long millisInFuture, long countDownInterval,
                                 Ringtone r) {
        super(ringView, textView, millisInFuture, countDownInterval);
        this.ringtone = r;
    }

    @Override
    protected void reset() {
        super.reset();
        if(ringtone.isPlaying())
            ringtone.stop();
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        // Send notification to users.
        //popupNotification();

        try {
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}