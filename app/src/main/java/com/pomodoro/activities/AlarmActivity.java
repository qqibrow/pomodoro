package com.pomodoro.activities;

import com.pomodoro.data.Task;
import com.pomodoro.widgets.ProgressRingView;
import com.pomodoro.widgets.RingTimer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends ActionBarActivity {

	private final long SECOND = 1000; // 1000 miliseconds
	private final long MINUTE = 60 * SECOND;
	private final long START_TIME = 30 * SECOND;

	// All the views.
	private Button startBtn, stopBtn;
	private RingTimerWithRingtone timer;

	// Some hardcode string which could move to configuration file.
	private static final String CHUNK_FIVE = "fonts/Chunkfive.otf";

	private Ringtone r;


    // Extra info from intent
    public static final String POS = "com.pomodoro.activities.AlarmActivity.POS";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
        Intent intent = getIntent();
        Task t = (Task)intent.getParcelableExtra(AlarmActivity.POS);

		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        getSupportActionBar().setTitle(t.getName());
        initAllViews();
	}

	void initAllViews() {
		startBtn = (Button) this.findViewById(R.id.startBtn);
        stopBtn = (Button) this.findViewById(R.id.stopBtn);
        stopBtn.setEnabled(false);

		TextView timerText = (TextView) this.findViewById(R.id.timer);
		ProgressRingView ring = (ProgressRingView) this.findViewById(R.id.timerAnim);

		// Assign special font to button and timerText.
		Typeface font = Typeface.createFromAsset(getAssets(), CHUNK_FIVE);
		timerText.setTypeface(font);

        timer = new RingTimerWithRingtone(ring, timerText, START_TIME, SECOND);
        timer.init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	public void onClickStartBtn(View w) {
        timer.start();
        exchangeActivation(startBtn, stopBtn);
	}

    public void onClickStopBtn(View v) {
        timer.cancel();
        exchangeActivation(stopBtn, startBtn);
    }

    private void exchangeActivation(Button b1, Button b2) {
        b1.setEnabled(false);
        b2.setEnabled(true);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {

		}
		return super.onOptionsItemSelected(item);
	}


    private class RingTimerWithRingtone extends RingTimer {

        public RingTimerWithRingtone(ProgressRingView ringView, TextView textView,
                                     long millisInFuture, long countDownInterval) {
            super(ringView, textView, millisInFuture, countDownInterval);
        }

        @Override
        protected void reset() {
            super.reset();
            if(r.isPlaying())
                r.stop();
        }

        @Override
        protected void onFinish() {
            super.onFinish();
            // Send notification to users.
            //popupNotification();

            try {
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void popupNotification() {
        int mId = 17;
        // Set the default behavior to open existing activity.
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.tomato2)
                .setContentTitle("Pomodoro")
                .setContentText("Congrats! You got your Pomodoro!")
                .setContentIntent(pIntent).setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
