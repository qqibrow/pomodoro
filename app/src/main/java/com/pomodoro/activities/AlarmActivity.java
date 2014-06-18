package com.pomodoro.activities;

import com.pomodoro.data.Task;
import com.pomodoro.widgets.RingView;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.Notification;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends ActionBarActivity {

	private final long SECOND = 1000; // 1000 miliseconds
	private final long MINUTE = 60 * SECOND;
	private final long START_TIME = 30 * SECOND;

	// All the views.
	private Button startBtn;
	private TextView timerText;
	private RingView ring;

	// All the data logic.
	private Boolean timerStarted = false;
	private myCountDownTimer timer;

	// Some hardcode string which could move to configuration file.
	private static final String CHUNK_FIVE = "fonts/Chunkfive.otf";

	private Ringtone r;


    // Extra info from intent
    public static final String POS = "com.pomodoro.activities.AlarmActivity.POS";
    private int TaskIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		initAllViews();
		initAllDataLogic();

        // extract intent.
        Intent intent = getIntent();
        Task t = (Task)intent.getParcelableExtra(AlarmActivity.POS);

		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(getApplicationContext(), notification);

		resetTimer();
        getSupportActionBar().setTitle(t.getName());
	}

	void initAllViews() {
		startBtn = (Button) this.findViewById(R.id.startBtn);
		timerText = (TextView) this.findViewById(R.id.timer);
		ring = (RingView) this.findViewById(R.id.timerAnim);
		
		// Assign special font to button and timerText.
		Typeface font = Typeface.createFromAsset(getAssets(), CHUNK_FIVE);
		timerText.setTypeface(font);
		startBtn.setTypeface(font);
		
		// (TODO)Put startBtn the the ring place regardless of the devices.

	}

	void initAllDataLogic() {
		timer = new myCountDownTimer(START_TIME, SECOND);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	public void onClickStartBtn(View w) {
		if (!timerStarted) {
			timer.start();
			timerStarted = true;
			startBtn.setText("STOP");
		} else {
			timer.cancel();
			resetTimer();
		}
	}

	private void resetTimer() {
		// Reset Text for timer.
		long leftMinutes = START_TIME / MINUTE;
		long leftSeconds = (START_TIME % MINUTE) / SECOND;
		String startText = String.format("%02d:%02d", leftMinutes, leftSeconds);
		timerText.setText(startText);

		// Reset the flag.
		timerStarted = false;

		// Reset button text.
		startBtn.setText("Start");

		// Clear animation for timer text.
		timerText.clearAnimation();
		ring.setPhase(0);

		// reset ringtone.
		if (r.isPlaying())
			r.stop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {

		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * TODO(lniu) Use builder to refactor myCountDownTimer so that it will
	 * encapsulate all the data change inside the class.
	 * 
	 * Like CounDOwnTimer.builder builder = new ... builder.setBtn();
	 * builder.setText(); myCountDownTimer timer = builder.build();
	 */
	private class myCountDownTimer extends CountDownTimer {

		public myCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			timerText.setText("00:00");
			ring.setPhase(1);

			// Start the shining animation for view.
			Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					AlarmActivity.this, R.anim.tween);
			timerText.startAnimation(myFadeInAnimation);

			// Start blaking animation for ring.

			// Send notification to users.
			int mId = 17;

			// Set the default behavior to open existing activity.
			Intent intent = new Intent(AlarmActivity.this, AlarmActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			PendingIntent pIntent = PendingIntent.getActivity(
					AlarmActivity.this, 0, intent, 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					AlarmActivity.this).setSmallIcon(R.drawable.tomato2)
					.setContentTitle("Pomodoro")
					.setContentText("Congrats! You got your Pomodoro!")
					.setContentIntent(pIntent).setAutoCancel(true);

			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, mBuilder.build());

			try {
				r.play();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onTick(long millisUntilFinished) {
			long leftMinutes = millisUntilFinished / MINUTE;
			long leftSeconds = (millisUntilFinished % MINUTE) / SECOND;
			timerText.setText(String.format("%02d:%02d", leftMinutes,
					leftSeconds));

			float phase = (float) (START_TIME - (millisUntilFinished / 1000) * 1000)
					/ START_TIME;
			ring.setPhase(phase);
		}

	}

}
