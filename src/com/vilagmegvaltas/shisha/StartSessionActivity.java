package com.vilagmegvaltas.shisha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.AppRater;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

public class StartSessionActivity extends Activity {
	Button addUserButton;
	Button startSessionButton;
	ListView lv;
	TextView tv;
	TextView timeoutField;
	ArrayList<String> names = new ArrayList<String>();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startsession);
		AppRater.app_launched(this);
		addUserButton = (Button) findViewById(R.id.adduserButton);
		startSessionButton = (Button) findViewById(R.id.startButton);
		lv = (ListView) findViewById(R.id.userList);
		tv = (TextView) findViewById(R.id.newUserName);
		timeoutField = (TextView) findViewById(R.id.timeoutField);
		lv.setAdapter(new NameArrayAdapter(this,
				android.R.layout.simple_list_item_1, names));
		addUserButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				if (tv.getText() != null && tv.getText().length() > 0) {
					names.add(tv.getText().toString());
					tv.setText("");
					((NameArrayAdapter) lv.getAdapter()).notifyDataSetChanged();
				}

			}
		});
		startSessionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CharSequence cs = timeoutField.getText();
				if (cs.length() != 0) {
					if (!names.isEmpty()) {
						Session s = new Session();
						for (String name : names) {
							s.addPlayer(name);
						}
						s.setWarnTimeOut(Integer.parseInt(timeoutField
								.getText().toString()) * 1000);
						Intent i = IntentManager.getMainIntent(
								StartSessionActivity.this, s);
						Map<String, String> params = new HashMap<String, String>();
						params.put("user_count",
								String.valueOf(s.getPlayers().size()));
						params.put("warntimeout",
								String.valueOf(s.getWarnTimeOut()));
						FlurryAgent.logEvent("New Session", params);
						finish();
						startActivity(i);
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.error_empty_user_list,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.error_no_timeout_set, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
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
									StartSessionActivity.this.finish();

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
			return true;
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

	private class NameArrayAdapter extends ArrayAdapter<String> {

		public NameArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
}