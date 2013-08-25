package com.vilagmegvaltas.shisha;


import java.util.List;

import com.vilagmegvaltas.shisha.entities.Player;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.IntentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SessionSummary extends Activity {

	private Session s;
	private static int H = 1000*60*60;
	private static int M = 1000*60;
	private static int S = 1000;
	// private MessageAdapter m_adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session_summary);

		s = (Session) getIntent().getExtras().get("session");
		ListView lv = (ListView) findViewById(R.id.sessionlist);
		lv.setAdapter(new SummaryAdapter(this, R.layout.session_item, s.getPlayers()));

	}

	private class SummaryAdapter extends ArrayAdapter<Player> {

		private List<Player> players;

		public SummaryAdapter(Context context,
				int textViewResourceId, List<Player> objects) {
			super(context, textViewResourceId, objects);
			players = objects;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.session_item, null);
			}
			Player p = players.get(position);
			if (null != p) {
				TextView sessitem_name = (TextView) v.findViewById(R.id.sessitem_name);
				TextView sessitem_time = (TextView) v.findViewById(R.id.sessitem_time);
				sessitem_name.setText(p.getName());
				long sum = p.getSumPipeTime();
				int h = (int) (sum / (H));
				int m = (int) ((sum - h*H) / (M));
				int s = (int) ((sum - h*H - m*M) / (S));
				sessitem_time.setText(h + "h "+ m + "m " + s + "s");
			}

			return v;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent main = IntentManager.getStartSessionIntent(this);
			finish();
			startActivity(main);
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
			startActivity(new Intent(SessionSummary.this, Donate.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}