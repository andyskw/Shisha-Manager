package com.vilagmegvaltas.shisha;

import java.io.ByteArrayOutputStream;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ShareActivity extends Activity {

	private SocialAuthAdapter adapter;
	private CheckBox uploadImage;
	private EditText shareText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		// Create Your Own Share Button
		Button share = (Button) findViewById(R.id.share);
		uploadImage = (CheckBox) findViewById(R.id.uploadImage);
		shareText = (EditText) findViewById(R.id.shareUserText);
		share.setText("Share");
		share.setTextColor(Color.WHITE);
		// share.setBackgroundResource(R.drawable.button_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.icon);

		// Enable Provider
		adapter.enable(share);
	}

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			if (!uploadImage.isChecked()) {
				adapter.updateStatus(shareText.getText().toString()
						+ " - #shishamanager", new MessageListener(), false);
			} else {
				try {
					Bitmap receivedImage = (Bitmap) getIntent().getExtras()
							.get("usage_summary_graph");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();

					receivedImage.compress(CompressFormat.JPEG, 70, bos);
					byte[] imageBytes = bos.toByteArray();
					Bitmap jpegImage = BitmapFactory.decodeByteArray(
							imageBytes, 0, imageBytes.length);
					adapter.uploadImageAsync(shareText.getText().toString()
							+ " - #shishamanager", "shishaztunk.jpg",
							jpegImage, 100, new MessageListener());
				} catch (Exception e) {
					// TODO Auto-generated catch block
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

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {

		public void onExecute(Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(ShareActivity.this, "Message posted", Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(ShareActivity.this, "Message not posted",
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
