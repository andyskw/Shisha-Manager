package com.vilagmegvaltas.shisha;

import com.flurry.android.FlurryAgent;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class Statistics extends TabActivity {

	// Josh Clemm's tabhost setup is used here. :)
	
	Session s;
    TabHost mTabHost;
    
	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		Intent i = new Intent(this, UsageSummary.class);
		i.putExtra("session", s);
		setupTab(new TextView(this), "Statistics", R.drawable.statistics, IntentManager.getSessionSummaryIntent(this, s));
		setupTab(new TextView(this), "Usage time per round", R.drawable.graph, i);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        s = (Session) getIntent().getExtras().getSerializable("session");
        setupTabHost();
        
    }
        
    
	private void setupTab(final View view, final String tag, final int icon, final Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, icon);
        
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);

	}

	private static View createTabView(final Context context, final String text, final int icon) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		
		Drawable rajz = view.getContext().getResources().getDrawable(icon);
		
		ImageView iv = (ImageView) view.findViewById(R.id.tabsImage);
		iv.setImageDrawable(rajz);
		
		return view;
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
			startActivity(new Intent(Statistics.this, Donate.class));
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