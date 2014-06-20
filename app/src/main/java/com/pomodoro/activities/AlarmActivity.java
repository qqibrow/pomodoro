package com.pomodoro.activities;

import com.pomodoro.data.Task;
import com.pomodoro.widgets.PomoTimer;
import com.pomodoro.widgets.ProgressRingView;

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

//	private final long WORKING_TIME = 25 * MINUTE;
//    private final long REST_TIME = 5 * MINUTE;

	// All the views.
	private PomoTimer timer;

	// Some hardcode string which could move to configuration file.
	private static final String CHUNK_FIVE = "fonts/Chunkfive.otf";

    private Task task;

    // Extra info from intent
    public static final String POS = "com.pomodoro.activities.AlarmActivity.POS";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

        // Get passed-in task.
        Intent intent = getIntent();
        task = intent.getParcelableExtra(AlarmActivity.POS);

        // Setup timer with ringtone.
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        setUpRingTimerWithRingtone(r);

        getSupportActionBar().setTitle(task.getName());
        setUpButtons();
	}

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(AlarmActivity.POS, task);
        setResult(MainActivity.REQUEST_CODE, intent);
        if (getParent() == null) {
            setResult(MainActivity.REQUEST_CODE, intent);
        } else {
            getParent().setResult(MainActivity.REQUEST_CODE, intent);
        }
        super.onBackPressed();
    }


    private void setUpRingTimerWithRingtone(Ringtone ringtone) {
        TextView timerText = (TextView) this.findViewById(R.id.timer);
        ProgressRingView ring = (ProgressRingView) this.findViewById(R.id.timerAnim);

        // Assign special font to button and timerText.
        Typeface font = Typeface.createFromAsset(getAssets(), CHUNK_FIVE);
        timerText.setTypeface(font);

        timer = new PomoTimer(ring, timerText, ringtone);
        timer.init();
    }

    private void setUpButtons() {
        final Button startBtn = (Button) this.findViewById(R.id.startBtn);
        final Button stopBtn = (Button) this.findViewById(R.id.stopBtn);
        stopBtn.setEnabled(false);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
                exchangeActivation(startBtn, stopBtn);
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                exchangeActivation(stopBtn, startBtn);
            }
        });

        timer.setOnFinishListener(new PomoTimer.onFinishListener() {

            @Override
            public void onFinish(PomoTimer pomo) {
                exchangeActivation(stopBtn, startBtn);
                if(pomo.isWorkMode()) {
                    task.advance();
                    pomo.toRestMode();
                } else {
                    pomo.toWorkMode();
                }

            }
        });
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
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
