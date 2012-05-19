package hu.harmakhis.shisha.utils;

import hu.harmakhis.shisha.Main;
import hu.harmakhis.shisha.SessionSummary;
import hu.harmakhis.shisha.StartSession;
import hu.harmakhis.shisha.Statistics;
import hu.harmakhis.shisha.entities.Session;
import android.app.Activity;
import android.content.Intent;


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
	
	public static Intent getMainIntent(Activity host) {
		Intent i =new Intent().setClass(host, Main.class);
		return i;
	}
	
	public static Intent getStartSessionIntent(Activity host) {
		Intent i =new Intent().setClass(host, StartSession.class);
		return i;
	}
}