package org.hforh;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnClickListener{
		
		Button taker;
		Button giver;
		
		Intent takerIntent;
		Intent giverIntent;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			TextView textView = (TextView) rootView.findViewById(R.id.title);
			            
			Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/caviardreamsbold.ttf");
			textView.setTypeface(custom_font);
			
			
			taker = (Button) rootView.findViewById(R.id.taker);
			taker.setTypeface(custom_font);
			
			giver = (Button) rootView.findViewById(R.id.giver);
			giver.setTypeface(custom_font);
			
			takerIntent = new Intent(getActivity(), TakerProfile.class);
			giverIntent = new Intent(getActivity(), GiverProfile.class);
			
			taker.setOnClickListener(this);
			giver.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.taker)
				startActivity(takerIntent);
			if(arg0.getId() == R.id.giver)
				startActivity(giverIntent);
			
			getActivity().finish();
			
		}
	}

}
