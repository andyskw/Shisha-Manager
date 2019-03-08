package com.vilagmegvaltas.shisha.fragments;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.R;
import com.vilagmegvaltas.shisha.entities.Player;
import com.vilagmegvaltas.shisha.entities.Session;

public class SessionSummaryFragment extends Fragment {
	private static Session session;
	private static int H = 1000 * 60 * 60;
	private static int M = 1000 * 60;
	private static int S = 1000;

	public SessionSummaryFragment() {

	}

	public static Fragment newInstance(Session s) {
		SessionSummaryFragment f = new SessionSummaryFragment();
		SessionSummaryFragment.session = s;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.session_summary, null);

		ListView lv = (ListView) v.findViewById(R.id.sessionlist);
		lv.setAdapter(new SummaryAdapter(getActivity(), R.layout.session_item,
				session.getPlayers()));
		// session summary bitmap creator not in use
		/*
		 * final LinearLayout ll = (LinearLayout) v
		 * .findViewById(R.id.session_summary_host);
		 * ll.getViewTreeObserver().addOnGlobalLayoutListener( new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() { View z = ll.getRootView();
		 * z.setDrawingCacheEnabled(true); z.buildDrawingCache(true); Bitmap x =
		 * z.getDrawingCache();
		 * 
		 * } });
		 */
		FlurryAgent.logEvent("SessionSummary viewed");

		return v;
	}

	private class SummaryAdapter extends ArrayAdapter<Player> {

		private List<Player> players;

		public SummaryAdapter(Context context, int textViewResourceId,
				List<Player> objects) {
			super(context, textViewResourceId, objects);
			players = objects;

		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = View.inflate(getContext(), R.layout.session_item, null);
			}
			Player p = players.get(position);
			if (null != p) {
				TextView sessitem_name = (TextView) v
						.findViewById(R.id.sessitem_name);
				TextView sessitem_time = (TextView) v
						.findViewById(R.id.sessitem_time);
				sessitem_name.setText(p.getName());
				long sum = p.getSumPipeTime();
				int h = (int) (sum / (H));
				int m = (int) ((sum - h * H) / (M));
				int s = (int) ((sum - h * H - m * M) / (S));
				sessitem_time.setText(h + "h " + m + "m " + s + "s");
			}

			return v;
		}

	}
}
