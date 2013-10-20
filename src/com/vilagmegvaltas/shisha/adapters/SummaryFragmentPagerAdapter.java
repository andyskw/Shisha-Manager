package com.vilagmegvaltas.shisha.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vilagmegvaltas.shisha.R;
import com.vilagmegvaltas.shisha.entities.Session;
import com.vilagmegvaltas.shisha.fragments.ShareFragment;
import com.vilagmegvaltas.shisha.fragments.SessionSummaryFragment;
import com.vilagmegvaltas.shisha.fragments.ChartSummaryFragment;

public class SummaryFragmentPagerAdapter extends FragmentPagerAdapter {
	private String[] indicators;
	private Session s;

	public SummaryFragmentPagerAdapter(FragmentManager fm, Context ctx,
			Session s) {
		super(fm);
		this.s = s;
		indicators = ctx.getResources().getStringArray(
				R.array.titlePageIndicatorTitles);
	}

	@Override
	public Fragment getItem(int count) {
		switch (count) {
		case 0:
			return SessionSummaryFragment.newInstance(s);
		case 1:
			return ChartSummaryFragment.newInstance(s);
		case 2:
			return ShareFragment.newInstance();
		default:
			return SessionSummaryFragment.newInstance(s);
		}
	}

	@Override
	public int getCount() {
		return indicators.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return indicators[position];
	}

}
