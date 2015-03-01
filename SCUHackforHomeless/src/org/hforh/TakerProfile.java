package org.hforh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ToggleButton;

import com.parse.Parse;
import com.parse.ParsePush;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TakerProfile extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "VhaNEZE7Ido6Cl3Lmozv9463ZKDGiZiZ3YeQW301", "ICcnUMTEmqST67bLnAEjfoKbQSxWBj8OQecgez0J");
		setContentView(R.layout.activity_taker_profile);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.taker_profile, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnClickListener {

		Button btn;
		Intent intent;
		String takerName;
		String age;
		String sex;
		Set<String> skillset = new HashSet<>();
		String skills = "";


		ToggleButton labour ;
		ToggleButton HandyMan ;
		ToggleButton assistance ;
		ToggleButton moving ;

		EditText nameText ;
		EditText ageText;
		EditText sexText;


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_taker_profile,
					container, false);
			
			TextView textView1 = (TextView) rootView.findViewById(R.id.tv1);
			Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/caviardreamsbold.ttf");
			textView1.setTypeface(custom_font);
			TextView textView2 = (TextView) rootView.findViewById(R.id.tv2);
			TextView textView3 = (TextView) rootView.findViewById(R.id.tv3);
			TextView textView4 = (TextView) rootView.findViewById(R.id.tv4);
			textView2.setTypeface(custom_font);
			textView3.setTypeface(custom_font);
			textView4.setTypeface(custom_font);
			
			
			intent = new Intent(getActivity(), DashBoard.class);
			btn = (Button) rootView.findViewById(R.id.button1);
			nameText = (EditText) rootView.findViewById(R.id.nameText);
			ageText = (EditText) rootView.findViewById(R.id.ageText);
			sexText = (EditText) rootView.findViewById(R.id.sexText);

			labour = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
			HandyMan = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
			assistance = (ToggleButton) rootView.findViewById(R.id.toggleButton3);
			moving = (ToggleButton) rootView.findViewById(R.id.toggleButton4);
			labour.setTypeface(custom_font);
			HandyMan.setTypeface(custom_font);
			assistance.setTypeface(custom_font);
			btn.setTypeface(custom_font);



			//			EditText nameText = (EditText) rootView.findViewById(R.id.nameText);
			btn.setOnClickListener(this);
			
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			if(labour.isChecked()){
				skillset.add(labour.getText().toString());
				skills +=labour.getText().toString()+",";
			}
			if (HandyMan.isChecked()){
				skillset.add(HandyMan.getText().toString());
				skills +=HandyMan.getText().toString()+",";
			}
			if(assistance.isChecked()){
				skillset.add(assistance.getText().toString());
				skills +=assistance.getText().toString()+",";
			}
			if(moving.isChecked()){
				skillset.add(moving.getText().toString());
				skills +=moving.getText().toString()+",";
			}

			takerName = nameText.getText().toString();
			age = ageText.getText().toString();
			sex = sexText.getText().toString();

			skills =skills.substring(0,skills.length()-1);



			DownloadWebPageTask task = new DownloadWebPageTask();
			task.execute(new String[] { "http://ec2-54-153-74-245.us-west-1.compute.amazonaws.com/registration" });


			//Toast.makeText(getActivity(), skillset.size()+"", Toast.LENGTH_LONG).show();
			if (skillset != null)
			{
				Iterator<String> it = skillset.iterator();
				while (it.hasNext())
					ParsePush.subscribeInBackground(it.next().toLowerCase().trim());
			}


		}

		private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... url) {
				InputStream inputStream = null;
				HttpEntity httpEntity = null;
				String result = "";
				try {
					// 1. create HttpClient
					HttpClient httpclient = new DefaultHttpClient();

					// 2. make POST request to the given URL
					HttpPost httpPost = new HttpPost(url[0]);

					String json = "";

					// 3. build jsonObject
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("name", takerName);
					jsonObject.accumulate("gender", sex);
					jsonObject.accumulate("age", age);
					jsonObject.accumulate("skillset", skills);

					// 4. convert JSONObject to JSON to String
					json = jsonObject.toString();

					// ** Alternative way to convert Person object to JSON string usin Jackson Lib 
					// ObjectMapper mapper = new ObjectMapper();
					// json = mapper.writeValueAsString(person); 
					Log.i("json ", json);

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
					//inputStream = httpResponse.getEntity().getContent();
					httpEntity = httpResponse.getEntity();
					result = EntityUtils.toString(httpEntity);

					/*// 10. convert inputstream to string
					if(inputStream != null)
						result = convertInputStreamToString(inputStream);
					else
						result = "Did not work!";*/

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
				Log.d("Response: ", "> " + result);

				if (result != null) {
					try {
						JSONObject jsonObj = new JSONObject(result);

						boolean status = jsonObj.getBoolean("status");
						int id = jsonObj.getInt("id");

						if(status)
						{
							getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
							.putInt("id", id).commit();
							startNewActivity();
						}
					}catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void startNewActivity() {
			// TODO Auto-generated method stub

			getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
			.putBoolean("isFirstRun", false)
			.putString("takername", takerName)
			.putString("takerage", age)
			.putString("takersex", sex)
			.putStringSet("skillset", skillset).commit();

			getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
			.putBoolean("isFirstRun", false).commit();
			
			startActivity(intent);
		}

	}
}
