package com.vilagmegvaltas.shisha;


import java.util.ArrayList;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

import android.app.Activity;
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

public class StartSession extends Activity {
	Button addUserButton;
	Button startSessionButton;
	ListView lv;
	TextView tv;
	TextView timeoutField;
	ArrayList<String> names = new ArrayList<String>();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_session);
		addUserButton = (Button) findViewById(R.id.adduserButton);
		startSessionButton = (Button) findViewById(R.id.startButton);
		lv = (ListView) findViewById(R.id.userList);
		tv = (TextView) findViewById(R.id.newUserName);
		timeoutField = (TextView) findViewById(R.id.timeoutField);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
		addUserButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				if (tv.getText() != null && tv.getText().length() > 0) {
					names.add(tv.getText().toString());
					tv.setText("");
					((ArrayAdapter<String>) lv.getAdapter())
							.notifyDataSetChanged();
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
								StartSession.this, s);
						finish();
						startActivity(i);
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.error_empty_user_list,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.error_no_timeout_set,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_donate:
			startActivity(new Intent(StartSession.this, Donate.class));
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