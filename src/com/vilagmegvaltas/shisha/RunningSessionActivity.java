package com.vilagmegvaltas.shisha;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

public class RunningSessionActivity extends Activity {
	/** Called when the activity is first created. */
	private Session session;
	private Chronometer cm_chronoMeter;
	private TextView tv_userName;
	private TextView tv_pleaseClick;
	RelativeLayout ll;
	private MediaPlayer mp_mediaPlayer;
	Button btn_endSession;
	Button btn_pause;
	private long pauseTime;
	private boolean started = false;
	KeyguardManager keyGuardManager;
	KeyguardLock keyGuardLock;
	ProgressBar pb_userprogress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runningsession);
		session = (Session) (getIntent().getExtras().get("session"));
		if (session == null) {
			session = new Session();
			session.setWarnTimeOut(60 * 1000 * 5);
			session.addPlayer("andy");
			session.addPlayer("flyerz");
		}
		keyGuardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		keyGuardLock = keyGuardManager.newKeyguardLock("Shisha");
		mp_mediaPlayer = MediaPlayer.create(this, R.raw.timeout);
		cm_chronoMeter = (Chronometer) findViewById(R.id.cm_runningsession_chronometer);
		tv_userName = (TextView) findViewById(R.id.tv_runningsession_username);
		tv_pleaseClick = (TextView) findViewById(R.id.tv_runningsession_pleaseclick_descr);
		btn_endSession = (Button) findViewById(R.id.btn_runningsession_endsession);
		pb_userprogress = (ProgressBar) findViewById(R.id.pb_runningsession_userprogress);
		pb_userprogress.setMax(session.getWarnTimeOut() + 1000);
		pb_userprogress.setProgress(0);
		btn_endSession.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				endSession();

			}
		});
		btn_pause = (Button) findViewById(R.id.btn_runningsession_pause);
		btn_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pauseTime == 0 && started) {
					pauseTime = SystemClock.elapsedRealtime();
					cm_chronoMeter.stop();
					btn_pause.setText(R.string.resume);
					tv_pleaseClick.setText(R.string.paused_infotext);
				} else {
					if (started) {
						cm_chronoMeter.setBase(SystemClock.elapsedRealtime()
								- (pauseTime - cm_chronoMeter.getBase()));
						cm_chronoMeter.start();
						pauseTime = 0;
						btn_pause.setText(R.string.pause);
						tv_pleaseClick.setText("");
					}
				}

			}
		});

		ll = (RelativeLayout) findViewById(R.id.rl_runningsession_host);
		ll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				pb_userprogress.setProgress(0);
				if (!started) {
					started = true;
					tv_pleaseClick.setText("");
					cm_chronoMeter.setBase(SystemClock.elapsedRealtime());
					cm_chronoMeter.start();
				} else {
					if (pauseTime == 0) {

						nextPlayer();
						cm_chronoMeter.setKeepScreenOn(false);

					}
				}
			}
		});
		cm_chronoMeter.setOnChronometerTickListener(new OnChronometerTickListener() {
			public void onChronometerTick(Chronometer chronometer) {
				long time = SystemClock.elapsedRealtime() - cm_chronoMeter.getBase();
				Log.d("tag", "time:" + time);
				pb_userprogress.setProgress( time < 1000 ? 0 : (int) (time + (1000 - time % 1000)));
				Log.d("tag", pb_userprogress.getProgress() + "");
				if (time > session.getWarnTimeOut()) {
					cm_chronoMeter.setTextColor(Color.RED);
					keyGuardLock.disableKeyguard();
					WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE))
							.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
									| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
					screenLock.acquire();
					screenLock.release();
					cm_chronoMeter.setKeepScreenOn(true);

					if (!mp_mediaPlayer.isPlaying())
						mp_mediaPlayer.start();
				}
			}
		});

		tv_userName.setText(session.getInitialPlayer().getName());
		cm_chronoMeter.setBase(SystemClock.elapsedRealtime());
		cm_chronoMeter.stop();
	}

	private void nextPlayer() {
		long time = SystemClock.elapsedRealtime() - cm_chronoMeter.getBase();
		cm_chronoMeter.setTextColor(Color.WHITE);
		tv_userName.setText(session.next(time).getName());
		cm_chronoMeter.setBase(SystemClock.elapsedRealtime());
	}

	private void endSession() {
		if (pauseTime != 0) {
			cm_chronoMeter.setBase(SystemClock.elapsedRealtime()
					- (pauseTime - cm_chronoMeter.getBase()));
		}
		long time = SystemClock.elapsedRealtime() - cm_chronoMeter.getBase();
		session.next(time);
		cm_chronoMeter.stop();
		Intent statistics = IntentManager.getStatisticsIntent(this, session);
		finish();
		startActivity(statistics);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// OMITTED!
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
			nextPlayer();

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryAPIKeyContainer.API_KEY);
	}

	@Override
	protected void onStop() {
		keyGuardLock.reenableKeyguard();
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}