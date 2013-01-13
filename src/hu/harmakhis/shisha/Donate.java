package hu.harmakhis.shisha;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Donate extends Activity {
	private static final String PAYPAL_LINK = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=TY47JH5LK3AT8&lc=HU&item_name=Shisha%20Manager%20Donation&item_number=evogochi_donate&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted";
	private Button btn_paypal_donate;

	@Override
	protected void onStart() {
		super.onStart();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donate_screen);
		setUIReferences();
		setUIListeners();
	}

	private void setUIListeners() {
		btn_paypal_donate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPayPalDonate();

			}
		});

	}

	private void setUIReferences() {
		btn_paypal_donate = (Button) findViewById(R.id.btn_donate_paypal);

	}

	private void startPayPalDonate() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(PAYPAL_LINK));
		startActivity(i);

	}

}
