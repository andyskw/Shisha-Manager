package com.vilagmegvaltas.shisha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.utils.AppRater;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

public class SplashScreenActivity extends Activity {
	RelativeLayout rl_host;
	private static final long SPLASH_TIMEOUT = 2000;
	private boolean isTapped = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		rl_host = (RelativeLayout) findViewById(R.id.rl_splashscreen_host);
		rl_host.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isTapped = true;
				startSession();
			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isTapped)
					startSession();
			}
		}, SPLASH_TIMEOUT);

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(R.string.exit_app);
			adb.setMessage(R.string.areyousure_exit);
			adb.setCancelable(false)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SplashScreenActivity.this.finish();

								}
							})
					.setNegativeButton(R.string.nope,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							});
			AlertDialog dialog = adb.create();
			dialog.show();
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
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}