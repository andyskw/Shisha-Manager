package hu.harmakhis.shisha;


import hu.harmakhis.shisha.utils.IntentManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	@Override
	public void onBackPressed() {
	
		super.onBackPressed();
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	//OMITTED!
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
			startActivity(new Intent(Start.this, Donate.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}