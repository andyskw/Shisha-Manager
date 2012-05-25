package hu.harmakhis.shisha;

import hu.harmakhis.shisha.utils.IntentManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Start extends Activity {
	LinearLayout ll;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		ll = (LinearLayout) findViewById(R.id.start_llayout_host);
		
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startSession();			
			}
		});
		
	}



	private void startSession() {

		Intent main = IntentManager.getStartSessionIntent(this);
		finish();
		startActivity(main);
	}
}