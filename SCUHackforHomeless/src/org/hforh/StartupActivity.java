package org.hforh;

import java.util.Iterator;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.Parse;
import com.parse.ParsePush;

public class StartupActivity extends ActionBarActivity {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "VhaNEZE7Ido6Cl3Lmozv9463ZKDGiZiZ3YeQW301", "ICcnUMTEmqST67bLnAEjfoKbQSxWBj8OQecgez0J");


		setContentView(R.layout.activity_startup);


		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}

		Boolean isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
				.getBoolean("isFirstRun", true);
		
		Log.i("called", isFirstRun+"");

		if (isFirstRun) {
			//show start activity
			
			startActivity(new Intent(StartupActivity.this, MainActivity.class));
			//Toast.makeText(StartupActivity.this, "First Run", Toast.LENGTH_LONG)
			//       .show();
		}else {
			String giverName = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
					.getString("givername", "");
			
			String takerName = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
					.getString("takername", "");
			
			if (!giverName.equals("")){
				Intent intent = new Intent(StartupActivity.this, HaveAJobActivity.class);
				/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); */
				
				startActivity(intent);
				
				
				

			}
			else if (!takerName.equals("")) {
				Intent intent = new Intent(StartupActivity.this, DashBoard.class);
				/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); */
				
				startActivity(intent);

				Set<String> skillSet = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
						.getStringSet("skillset", null);

				if (skillSet != null)
				{
					Iterator<String> it = skillSet.iterator();
					while (it.hasNext())
						ParsePush.subscribeInBackground(it.next().toLowerCase().trim());
				}
				
				
				


			}

		}
		finish();


		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_startup,
					container, false);
			return rootView;
		}
	}

}
