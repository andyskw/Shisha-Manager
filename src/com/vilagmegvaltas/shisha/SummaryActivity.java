package com.vilagmegvaltas.shisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.flurry.android.FlurryAgent;
import com.viewpagerindicator.TitlePageIndicator;
import com.vilagmegvaltas.shisha.adapters.SummaryFragmentPagerAdapter;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.utils.DonateNotifier;
import com.vilagmegvaltas.shisha.utils.FlurryAPIKeyContainer;
import com.vilagmegvaltas.shisha.utils.IntentManager;

public class SummaryActivity extends FragmentActivity {
	ViewPager pager;
	TitlePageIndicator indicator;
	Session s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		setUIReferences();
		s = (Session) getIntent().getExtras().getSerializable("session");

		SummaryFragmentPagerAdapter adaper = new SummaryFragmentPagerAdapter(
				getSupportFragmentManager(), SummaryActivity.this, s);
		pager.setAdapter(adaper);
		indicator.setViewPager(pager);

		DonateNotifier.app_launched(this);
	}

	private void setUIReferences() {
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent main = IntentManager.getStartSessionIntent(this);
			finish();
			startActivity(main);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
