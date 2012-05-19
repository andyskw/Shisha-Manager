package hu.harmakhis.shisha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class StartSession extends Activity {
	Button addUserButton;
	ListView lv;
	String[] names = {"Andy", "Joey"};
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_session);
		addUserButton = (Button) findViewById(R.id.adduserButton);
		lv = (ListView) findViewById(R.id.userList);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));
		
	}

}