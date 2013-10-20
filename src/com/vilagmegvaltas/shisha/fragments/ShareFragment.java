package com.vilagmegvaltas.shisha.fragments;

import com.vilagmegvaltas.shisha.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShareFragment extends Fragment {

	public static Fragment newInstance() {
		return new ShareFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_share, null);
		return v;
	}

}
