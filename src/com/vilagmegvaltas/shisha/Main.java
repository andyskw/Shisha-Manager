package com.vilagmegvaltas.shisha;



import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.IntentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Main extends Activity {
	/** Called when the activity is first created. */
	private Session s;
	private Chronometer cm;
	private TextView actPlayer;
	private Spinner sp;
	private TextView pleaseClick;
	LinearLayout ll;
	private MediaPlayer mp;
	Button end_session;
	Button pause;
	private long pauseTime;
	private boolean started = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		s = (Session) (getIntent().getExtras().get("session"));
		if (s == null) {
			s = new Session();
			s.setWarnTimeOut(60 * 1000 * 5);
			s.addPlayer("andy");
			s.addPlayer("flyerz");
		}
		mp = MediaPlayer.create(this, R.raw.timeout);
		cm = (Chronometer) findViewById(R.id.chronometer1);
		actPlayer = (TextView) findViewById(R.id.textView1);
		pleaseClick = (TextView) findViewById(R.id.pleaseClickTextView);
		end_session = (Button) findViewById(R.id.btn_endsession);
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
				cm.setKeepScreenOn(false);
				} else {
					if (started) {
					cm.setBase(SystemClock.elapsedRealtime() - (pauseTime - cm.getBase()));
					cm.setKeepScreenOn(true);
					cm.start();
					pauseTime = 0;
					pause.setText(R.string.pause);
					pleaseClick.setText("");
					}
				}
				
			}
		});
		
		ll = (LinearLayout) findViewById(R.id.main_llayout_host);
		ll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!started) {
					started = true;
					pleaseClick.setText("");
					cm.setBase(SystemClock.elapsedRealtime());
					cm.setKeepScreenOn(true);
					cm.start();
				} else {
					if (pauseTime == 0) {
					
						nextPlayer();
					}
				}
			}
		});
		cm.setOnChronometerTickListener(new OnChronometerTickListener() {
			public void onChronometerTick(Chronometer chronometer) {
				long time = SystemClock.elapsedRealtime() - cm.getBase();
				if (time > s.getWarnTimeOut()) {
					cm.setTextColor(Color.RED);
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
			cm.setBase(SystemClock.elapsedRealtime() - (pauseTime - cm.getBase()));
		}
		long time = SystemClock.elapsedRealtime() - cm.getBase();
		s.next(time);
		cm.stop();
		Intent statistics = IntentManager.getStatisticsIntent(this, s);
		finish();
		startActivity(statistics);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	//OMITTED!
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_donate:
			startActivity(new Intent(Main.this, Donate.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}