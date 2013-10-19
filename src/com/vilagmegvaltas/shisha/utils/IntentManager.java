package com.vilagmegvaltas.shisha.utils;

import com.vilagmegvaltas.shisha.Main;
import com.vilagmegvaltas.shisha.SessionSummary;
import com.vilagmegvaltas.shisha.Share;
import com.vilagmegvaltas.shisha.StartSession;
import com.vilagmegvaltas.shisha.Statistics;
import com.vilagmegvaltas.shisha.entities.Session;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;


public class IntentManager {

	public static Intent getSessionSummaryIntent(Activity host, Session s) {
		Intent i =new Intent().setClass(host, SessionSummary.class);
		i.putExtra("session", s);
		return i;
	}
	
	public static Intent getStatisticsIntent(Activity host, Session s) {
		Intent i =new Intent().setClass(host, Statistics.class);
		i.putExtra("session", s);
		return i;
	}
	
	@Deprecated
	public static Intent getMainIntent(Activity host) {
		Intent i =new Intent().setClass(host, Main.class);
		return i;
	}
	
	public static Intent getStartSessionIntent(Activity host) {
		Intent i =new Intent().setClass(host, StartSession.class);
		return i;
	}

	public static Intent getMainIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, Main.class);
		i.putExtra("session", s);
		return i;
		
	}
	
	public static Intent getShareIntent(Activity host, Session s) {
		Intent i = new Intent().setClass(host, Share.class);
		i.putExtra("session", s);
		return i;
	}
}