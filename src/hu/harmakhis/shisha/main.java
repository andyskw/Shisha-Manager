package hu.harmakhis.shisha;

import java.io.IOException;

import hu.harmakhis.shisha.entities.Session;
import hu.harmakhis.shisha.utils.IntentManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;

public class main extends Activity {
    /** Called when the activity is first created. */
	private Session s;
	private Chronometer cm;
	private TextView actPlayer;
	LinearLayout ll;
	private MediaPlayer mp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        s = new Session();
        s.setWarnTimeOut(60*1000*5);
        s.addPlayer("andy");
        s.addPlayer("flyerz");
        mp = MediaPlayer.create(this, R.raw.timeout);
        cm = (Chronometer) findViewById(R.id.chronometer1);
        actPlayer= (TextView) findViewById(R.id.textView1);
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				endSession();
				
			}
		});
        ll = (LinearLayout) findViewById(R.id.main_llayout_host);
        ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextPlayer();
			}
		});
        cm.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				long time = SystemClock.elapsedRealtime()-cm.getBase();
				if (time > s.getWarnTimeOut()) {
					cm.setTextColor(Color.RED);
					if (!mp.isPlaying()) mp.start();
				}		
			}
		});
        cm.start();
    }
    
    private void nextPlayer() {
    	long time = SystemClock.elapsedRealtime()-cm.getBase();
		cm.setTextColor(Color.WHITE);
		actPlayer.setText(s.next(time).getName());
		cm.setBase(SystemClock.elapsedRealtime());
    }
    
    
    private void endSession() {
    	long time = SystemClock.elapsedRealtime()-cm.getBase();
    	s.next(time);
    	cm.stop();
    	Intent statistics = IntentManager.getStatisticsIntent(this, s);
    	startActivity(statistics);
    }
}