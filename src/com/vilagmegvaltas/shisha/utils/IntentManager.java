package com.vilagmegvaltas.shisha.utils;

import android.app.Activity;
import android.content.Intent;

import com.vilagmegvaltas.shisha.RunningSessionActivity;
import com.vilagmegvaltas.shisha.SessionSummary;
import com.vilagmegvaltas.shisha.ShareActivity;
import com.vilagmegvaltas.shisha.StartSessionActivity;
import com.vilagmegvaltas.shisha.SummaryActivity;
import com.vilagmegvaltas.shisha.entities.Session;

public class IntentManager {

	public static Intent getSessionSummaryIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, SessionSummary.class);
		i.putExtra("session", s);
		return i;
	}

	public static Intent getStatisticsIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, SummaryActivity.class);
		i.putExtra("session", s);
		return i;
	}

	@Deprecated
	public static Intent getMainIntent(Activity host) {
		Intent i = new Intent().setClass(host, RunningSessionActivity.class);
		return i;
	}

	public static Intent getStartSessionIntent(Activity host) {
		Intent i = new Intent().setClass(host, StartSessionActivity.class);
		return i;
	}

	public static Intent getMainIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, RunningSessionActivity.class);
		i.putExtra("session", s);
		return i;

	}

	public static Intent getShareIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, ShareActivity.class);
		i.putExtra("session", s);
		return i;
	}
}