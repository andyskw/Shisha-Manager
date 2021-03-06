package com.vilagmegvaltas.shisha.fragments;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vilagmegvaltas.shisha.R;
import com.vilagmegvaltas.shisha.SummaryActivity;

public class ShareFragment extends Fragment {
	private SocialAuthAdapter adapter;
	private CheckBox uploadImage;
	private EditText shareText;
	private ImageView shareimage;
	Bitmap shareImageBitmap;

	public static Fragment newInstance() {
		return new ShareFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_share, null);

		// Create Your Own Share Button
		Button share = (Button) v.findViewById(R.id.btn_share_share);
		uploadImage = (CheckBox) v.findViewById(R.id.cb_share_uploadimage);
		shareText = (EditText) v.findViewById(R.id.et_share_sharetext);
		shareimage = (ImageView) v.findViewById(R.id.iv_share_shareimage);
		
		// share.setBackgroundResource(R.drawable.button_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProviderName(Provider.FACEBOOK, "Facebook");
		adapter.addProviderName(Provider.TWITTER, "Twitter");
		// Enable Provider
		adapter.enable(share);

		uploadImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					shareimage.setImageBitmap(getChartImage());
					shareimage.setVisibility(View.VISIBLE);
				} else {
					shareimage.setVisibility(View.INVISIBLE);
				}

			}
		});
		return v;
	}

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			if (!uploadImage.isChecked()) {
				adapter.updateStatus(shareText.getText().toString()
						+ " - #shishamanager", new MessageListener(), false);
			} else {
				try {
					adapter.uploadImageAsync(shareText.getText().toString()
							+ " - #shishamanager", "shishaztunk.jpg",
							getChartImage(), 100, new MessageListener());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("ShareBar", error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareBar", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

	}

	private Bitmap getChartImage() {
		if (shareImageBitmap == null) {
			String tag = ((SummaryActivity) getActivity())
					.getChartFragmentIdentifier();
			ChartSummaryFragment f = (ChartSummaryFragment) getActivity()
					.getSupportFragmentManager().findFragmentByTag(tag);

			shareImageBitmap = f.getChartBitmap();
		}
		return shareImageBitmap;
	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {

		public void onExecute(Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(getActivity(), "Message posted",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity(), "Message not posted",
						Toast.LENGTH_LONG).show();
		}

		public void onError(SocialAuthError e) {

		}

		@Override
		public void onExecute(String arg0, Integer arg1) {
			// TODO Auto-generated method stub

		}
	}
}
