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
	private Session s;
	private Chronometer cm;
	private TextView actPlayer;
	private Spinner sp;
	private TextView pleaseClick;
	RelativeLayout ll;
	private MediaPlayer mp;
	Button end_session;
	Button pause;
	private long pauseTime;
	private boolean started = false;
	KeyguardManager km;
	KeyguardLock newKeyguardLock;
	ProgressBar pb_userprogress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_runningsession);
		s = (Session) (getIntent().getExtras().get("session"));
		if (s == null) {
			s = new Session();
			s.setWarnTimeOut(60 * 1000 * 5);
			s.addPlayer("andy");
			s.addPlayer("flyerz");
		}
		km = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		newKeyguardLock = km.newKeyguardLock("Shisha");
		mp = MediaPlayer.create(this, R.raw.timeout);
		cm = (Chronometer) findViewById(R.id.chronometer1);
		actPlayer = (TextView) findViewById(R.id.textView1);
		pleaseClick = (TextView) findViewById(R.id.pleaseClickTextView);
		end_session = (Button) findViewById(R.id.btn_endsession);
		pb_userprogress = (ProgressBar) findViewById(R.id.pb_userprogress);
		pb_userprogress.setMax(s.getWarnTimeOut() + 1000);
		pb_userprogress.setProgress(0);
		end_session.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				endSession();

			}
		});
		pause = (Button) findViewById(R.id.btn_pause);
		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pauseTime == 0 && started) {
					pauseTime = SystemClock.elapsedRealtime();
					cm.stop();
					pause.setText(R.string.resume);
					pleaseClick.setText(R.string.paused_infotext);
				} else {
					if (started) {
						cm.setBase(SystemClock.elapsedRealtime()
								- (pauseTime - cm.getBase()));
						cm.start();
						pauseTime = 0;
						pause.setText(R.string.pause);
						pleaseClick.setText("");
					}
				}

			}
		});

		ll = (RelativeLayout) findViewById(R.id.main_llayout_host);
		ll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				pb_userprogress.setProgress(0);
				if (!started) {
					started = true;
					pleaseClick.setText("");
					cm.setBase(SystemClock.elapsedRealtime());
					cm.start();
				} else {
					if (pauseTime == 0) {

						nextPlayer();
						cm.setKeepScreenOn(false);

					}
				}
			}
		});
		cm.setOnChronometerTickListener(new OnChronometerTickListener() {
			public void onChronometerTick(Chronometer chronometer) {
				long time = SystemClock.elapsedRealtime() - cm.getBase();
				Log.d("tag", "time:" + time);
				pb_userprogress.setProgress( time < 1000 ? 0 : (int) (time + (1000 - time % 1000)));
				Log.d("tag", pb_userprogress.getProgress() + "");
				if (time > s.getWarnTimeOut()) {
					cm.setTextColor(Color.RED);
					newKeyguardLock.disableKeyguard();
					WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE))
							.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
									| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
					screenLock.acquire();
					screenLock.release();
					cm.setKeepScreenOn(true);

					if (!mp.isPlaying())
						mp.start();
				}
			}
		});

		actPlayer.setText(s.getInitialPlayer().getName());
		cm.setBase(SystemClock.elapsedRealtime());
		cm.stop();
	}

	private void nextPlayer() {
		long time = SystemClock.elapsedRealtime() - cm.getBase();
		cm.setTextColor(Color.WHITE);
		actPlayer.setText(s.next(time).getName());
		cm.setBase(SystemClock.elapsedRealtime());
	}

	private void endSession() {
		if (pauseTime != 0) {
			cm.setBase(SystemClock.elapsedRealtime()
					- (pauseTime - cm.getBase()));
		}
		long time = SystemClock.elapsedRealtime() - cm.getBase();
		s.next(time);
		cm.stop();
		Intent statistics = IntentManager.getStatisticsIntent(this, s);
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
		newKeyguardLock.reenableKeyguard();
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}