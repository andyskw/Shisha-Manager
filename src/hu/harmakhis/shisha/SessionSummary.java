package hu.harmakhis.shisha;

import hu.harmakhis.shisha.charts.PlayerChart;
import hu.harmakhis.shisha.entities.Player;
import hu.harmakhis.shisha.entities.Session;

import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SessionSummary extends Activity {

	private Session s;

	// private MessageAdapter m_adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session_summary);

		s = (Session) getIntent().getExtras().get("session");
		ListView lv = (ListView) findViewById(R.id.sessionlist);
		lv.setAdapter(new SummaryAdapter(this, R.layout.session_item, s.getPlayers()));
		PlayerChart pc = new PlayerChart();
		pc.setSession(s);
		Intent i = pc.execute(this);
		startActivity(i);

		
		
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
				sessitem_time.setText(new Long(p.getSumPipeTime()).toString());
			}

			return v;
		}

	}

}