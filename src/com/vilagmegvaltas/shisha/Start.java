package com.vilagmegvaltas.shisha;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.utils.AppRater;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

public class Start extends Activity {
	LinearLayout ll;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		ll = (LinearLayout) findViewById(R.id.start_llayout_host);
		AppRater.app_launched(this);
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startSession();			
			}
		});
		
		
	}



	private void startSession() {

		Intent main = IntentManager.getStartSessionIntent(this);
		finish();
		startActivity(main);
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	AlertDialog.Builder adb= new AlertDialog.Builder(this);
	    	adb.setTitle(R.string.exit_app);
	    	adb.setMessage(R.string.areyousure_exit);
	    	adb.setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Start.this.finish();
					
				}
			}).setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});
	    	AlertDialog dialog = adb.create();
	    	dialog.show();
	    }
	
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_donate:
			startActivity(new Intent(Start.this, Donate.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, FlurryAPIKeyContainer.API_KEY);
	}

	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
}