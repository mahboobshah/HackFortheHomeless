package org.hforh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GiverProfile extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_giver_profile);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.giver_profile, menu);
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

		Button btn;
		Intent intent;
		String giverName;
		String age;
		String sex;
		String zip;
		Set<String> skillset = new HashSet<>();
		
		EditText nameText;
		EditText ageText;
		EditText sexText;
		EditText zipText;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_giver_profile,
					container, false);

			TextView textView1 = (TextView) rootView.findViewById(R.id.gtv1);
			Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/caviardreamsbold.ttf");
			textView1.setTypeface(custom_font);
			TextView textView2 = (TextView) rootView.findViewById(R.id.gtv2);
			TextView textView3 = (TextView) rootView.findViewById(R.id.gtv3);
			TextView textView4 = (TextView) rootView.findViewById(R.id.gtv4);
			textView2.setTypeface(custom_font);
			textView3.setTypeface(custom_font);
			textView4.setTypeface(custom_font);
			
			
			
			intent = new Intent(getActivity(), HaveAJobActivity.class);
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); */
			btn = (Button) rootView.findViewById(R.id.button1);
			btn.setTypeface(custom_font);
			nameText = (EditText) rootView.findViewById(R.id.giverName);
			ageText = (EditText) rootView.findViewById(R.id.giverAge);
			sexText = (EditText) rootView.findViewById(R.id.giverSex);
			zipText = (EditText) rootView.findViewById(R.id.giverZip);
			/*ToggleButton labour = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
			ToggleButton HandyMan = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
			ToggleButton assistance = (ToggleButton) rootView.findViewById(R.id.toggleButton3);
			ToggleButton moving = (ToggleButton) rootView.findViewById(R.id.toggleButton4);
			 */
			

			/*if(labour.isChecked())
				skillset.add(moving.getText().toString());
			if (HandyMan.isChecked())
				skillset.add(HandyMan.getText().toString());
			if(assistance.isChecked())
				skillset.add(assistance.getText().toString());
			if(moving.isChecked())
				skillset.add(moving.getText().toString());
			 */
			//			EditText nameText = (EditText) rootView.findViewById(R.id.nameText);
			btn.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			
			giverName = nameText.getText().toString();
			age = ageText.getText().toString();
			sex = sexText.getText().toString();
			zip = zipText.getText().toString();
			
			
			getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
			.putBoolean("isFirstRun", false)
			.putString("givername", giverName)
			.putString("giverage", age)
			.putString("giversex", sex)
			.putString("giverzip", zip)
			.commit();
			
			getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
			.putBoolean("isFirstRun", false).commit();

			//DownloadWebPageTask task = new DownloadWebPageTask();
			//task.execute(new String[] { "http://www.vogella.com" });

			startActivity(intent);

			getActivity().finish();

		}

		private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... url) {
				InputStream inputStream = null;
				String result = "";
				try {
					// 1. create HttpClient
					HttpClient httpclient = new DefaultHttpClient();

					// 2. make POST request to the given URL
					HttpPost httpPost = new HttpPost(url[0]);

					String json = "";

					// 3. build jsonObject
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("name", giverName);
					jsonObject.accumulate("gender", sex);
					jsonObject.accumulate("age", age);
					jsonObject.accumulate("zip", zip);

					// 4. convert JSONObject to JSON to String
					json = jsonObject.toString();

					// ** Alternative way to convert Person object to JSON string usin Jackson Lib 
					// ObjectMapper mapper = new ObjectMapper();
					// json = mapper.writeValueAsString(person); 

					// 5. set json to StringEntity
					StringEntity se = new StringEntity(json);

					// 6. set httpPost Entity
					httpPost.setEntity(se);

					// 7. Set some headers to inform server about the type of the content   
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");

					// 8. Execute POST request to the given URL
					HttpResponse httpResponse = httpclient.execute(httpPost);

					// 9. receive response as inputStream
					inputStream = httpResponse.getEntity().getContent();

					// 10. convert inputstream to string
					if(inputStream != null)
						result = convertInputStreamToString(inputStream);
					else
						result = "Did not work!";

				} catch (Exception e) {
					Log.d("InputStream", e.getLocalizedMessage());
				}

				// 11. return result
				return result;
			}

			private String convertInputStreamToString(InputStream inputStream) throws IOException{
				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
				String line = "";
				String result = "";
				while((line = bufferedReader.readLine()) != null)
					result += line;

				inputStream.close();
				return result;

			} 

			@Override
			protected void onPostExecute(String result) {
				//textView.setText(result);
			}
		}
	}

}
