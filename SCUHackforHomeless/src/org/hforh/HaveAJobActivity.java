package org.hforh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;

public class HaveAJobActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "VhaNEZE7Ido6Cl3Lmozv9463ZKDGiZiZ3YeQW301", "ICcnUMTEmqST67bLnAEjfoKbQSxWBj8OQecgez0J");


		setContentView(R.layout.activity_have_ajob);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.have_ajob, menu);
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
	public static class PlaceholderFragment extends Fragment  implements android.view.View.OnClickListener{

		Button btn;
		Button ratingbtn;
		Intent intent;
		String whentext;
		String wheretext;
		String paytext;
		String type = "";
		//Set<String> skillset = new HashSet<>();
		LinkedList<String> skillset = new LinkedList<>();


		ToggleButton labour ;
		ToggleButton HandyMan ;
		ToggleButton assistance ;
		ToggleButton moving ;
		JSONObject obj = null;

		EditText when;
		EditText where;
		EditText pay;
		EditText comments;
		TextView workStatus;
		LinearLayout normal;
		LinearLayout ratingPanel;
		
		RatingBar ratingbar;
		int takerid;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_have_ajob,
					container, false);

			TextView textView1 = (TextView) rootView.findViewById(R.id.htv1);
			Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/caviardreamsbold.ttf");
			textView1.setTypeface(custom_font);
			TextView textView2 = (TextView) rootView.findViewById(R.id.htv2);
			TextView textView3 = (TextView) rootView.findViewById(R.id.htv3);
			TextView textView4 = (TextView) rootView.findViewById(R.id.htv4);
			textView2.setTypeface(custom_font);
			textView3.setTypeface(custom_font);
			textView4.setTypeface(custom_font);
			
			btn = (Button) rootView.findViewById(R.id.submitAJob);
			btn.setTypeface(custom_font);
			
			ratingbtn = (Button)rootView.findViewById(R.id.submitRating);
			ratingbtn.setTypeface(custom_font);
			
			when = (EditText) rootView.findViewById(R.id.when);
			where= (EditText) rootView.findViewById(R.id.where);
			
			LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String bestProvider = locationManager.getBestProvider(criteria, true);
			Location location = locationManager.getLastKnownLocation(bestProvider);
			
			(new GetAddressTask(getActivity())).execute(location);
			//location.getAltitude();
			//where.setText("");
			
			
			pay = (EditText) rootView.findViewById(R.id.pay);
			
			comments = (EditText) rootView.findViewById(R.id.comments);
			normal = (LinearLayout) rootView.findViewById(R.id.normalPanel);
			ratingPanel = (LinearLayout) rootView.findViewById(R.id.ratingPanel);
			
			ratingbar = (RatingBar) rootView.findViewById(R.id.rating);

			workStatus = (TextView) rootView.findViewById(R.id.workStatus);
			String status;
			Intent intent = getActivity().getIntent();
			
			if(intent.hasExtra("status")){
				status = intent.getStringExtra("status");

				if(status.equals("inprogress")){
					workStatus.setVisibility(View.VISIBLE);
					btn.setText("Please Wait..");
					btn.setTag("wait");
					workStatus.setText("Work in Progres.. ");
				}else {
					if(status.equals("done")){
						//workStatus.setVisibility(View.VISIBLE);
						takerid = intent.getIntExtra("takerid", -1);
						normal.setVisibility(View.GONE);
						ratingPanel.setVisibility(View.VISIBLE);
						ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
							
							@Override
							public void onRatingChanged(RatingBar ratingBar, float rating,
									boolean fromUser) {
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(), rating+"", Toast.LENGTH_SHORT).show();
							}
						});
						btn.setText("New work ?");
						btn.setTag("new");
						workStatus.setText("Hurray!!! your Work Done!! ");
					}
				}
			}

			labour = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
			HandyMan = (ToggleButton) rootView.findViewById(R.id.toggleButton2);
			assistance = (ToggleButton) rootView.findViewById(R.id.toggleButton3);
			moving = (ToggleButton) rootView.findViewById(R.id.toggleButton4);
			
			labour.setTypeface(custom_font);
			HandyMan.setTypeface(custom_font);
			assistance.setTypeface(custom_font);
			moving.setTypeface(custom_font);

			//			EditText nameText = (EditText) rootView.findViewById(R.id.nameText);
			btn.setOnClickListener(this);
			ratingbtn.setOnClickListener(this);


			return rootView;
		}


		@Override
		public void onClick(View v) {
			Button b = (Button) v;
			if (b.getTag().equals("work")){
				skillset.clear();
				/*if(labour.isChecked())
				skillset.add(moving.getText().toString());*/
				if (HandyMan.isChecked()) {
					skillset.add(HandyMan.getText().toString().toLowerCase());
					type += HandyMan.getText().toString().toLowerCase()+", ";
				}
				if(assistance.isChecked()){
					skillset.add(assistance.getText().toString().toLowerCase());
					type += assistance.getText().toString().toLowerCase()+", ";
				}
				if(moving.isChecked()){
					skillset.add(moving.getText().toString().toLowerCase());
					type += moving.getText().toString().toLowerCase()+", ";
				}

				/*LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				String bestProvider = locationManager.getBestProvider(criteria, true);
				Location location = locationManager.getLastKnownLocation(bestProvider);
				//location.getAltitude();
				if (location != null) {
					
					onLocationChanged(location);
				}*/
				
				
				whentext = when.getText().toString();
				wheretext = where.getText().toString();
				paytext = pay.getText().toString();

				JSONObject obj = null;

				String giverName = getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
						.getString("givername", "");

				ParsePush.subscribeInBackground(giverName);


				Toast.makeText(getActivity(), skillset.size()+"", Toast.LENGTH_LONG).show();
				if (skillset != null)
				{
					ParsePush push = new ParsePush();
					push.setChannel("handyman"); // Notice we use setChannels not setChannel

					try {
						obj = new JSONObject();
						//"{\"alert\": \"Need some work to be done!\",  \"uri\": \"com.example.scuhackforhomeless.WorkAvailabilityActivity\", \"action\":\"com.example.scuhackforhomeless.CUSTOM_NOTIFICATION\" }"
						obj.put("alert", "Need some work to be done");
						obj.put("action", "com.example.scuhackforhomeless.CUSTOM_NOTIFICATION");
						obj.put("where", whentext);
						obj.put("when", wheretext);
						obj.put("pay", paytext);
						obj.put("status", "todo");
						obj.put("givername", giverName);
						obj.put("type", type.substring(0, type.length()-2));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//push.setMessage("hello");
					push.setData(obj);

					//
					//push.setMessage("Need some work to be done");
					/*try {
					push.send();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					push.sendInBackground(new SendCallback() {

						@Override
						public void done(ParseException arg0) {
							Toast.makeText(getActivity(), arg0==null?" no exception":"excp", Toast.LENGTH_LONG).show();

						}
					});
				}

			}else {
				if (b.getTag().equals("new")){
					btn.setText("Submit");
					btn.setTag("work");
					when.setText("");
					where.setText("");
					pay.setText("");

					normal.setVisibility(View.VISIBLE);
					ratingPanel.setVisibility(View.GONE);
					workStatus.setVisibility(View.GONE);
					labour.setChecked(false);
					moving.setChecked(false);
					assistance.setChecked(false);
					HandyMan.setChecked(false);
				}else if (b.getTag().equals("ratingsub")){
					DownloadWebPageTask task = new DownloadWebPageTask();
					task.execute(new String[] { "http://ec2-54-153-74-245.us-west-1.compute.amazonaws.com/rating" });
				}
			}
		}
		
		private class GetAddressTask extends AsyncTask<Location, Void, String>{
		      Context mContext;
		      public GetAddressTask(Context context) {
		         super();
		         mContext = context;
		      }

		      /*
		       * When the task finishes, onPostExecute() displays the address. 
		       */
		      @Override
		      protected void onPostExecute(String address) {
		         // Display the current address in the UI
		         where.setText(address);
		      }
		      @Override
		      protected String doInBackground(Location... params) {
		         Geocoder geocoder =
		         new Geocoder(mContext, Locale.getDefault());
		         // Get the current location from the input parameter list
		         Location loc = params[0];
		         // Create a list to contain the result address
		         List<Address> addresses = null;
		         try {
		            addresses = geocoder.getFromLocation(loc.getLatitude(),
		            loc.getLongitude(), 1);
		         } catch (IOException e1) {
		            Log.e("LocationSampleActivity", 
		            "IO Exception in getFromLocation()");
		            e1.printStackTrace();
		            return ("IO Exception trying to get address");
		         } catch (IllegalArgumentException e2) {
		            // Error message to post in the log
		            String errorString = "Illegal arguments " +
		            Double.toString(loc.getLatitude()) +
		            " , " +
		            Double.toString(loc.getLongitude()) +
		            " passed to address service";
		            Log.e("LocationSampleActivity", errorString);
		            e2.printStackTrace();
		            return errorString;
		         }
		         // If the reverse geocode returned an address
		         if (addresses != null && addresses.size() > 0) {
		            // Get the first address
		            Address address = addresses.get(0);
		            /*
		            * Format the first line of address (if available),
		            * city, and country name.
		            */
		            String addressText = String.format(
		            "%s, %s, %s",
		            // If there's a street address, add it
		            address.getMaxAddressLineIndex() > 0 ?
		            address.getAddressLine(0) : "",
		            // Locality is usually a city
		            address.getLocality(),
		            // The country of the address
		            address.getCountryName());
		            // Return the text
		            return addressText;
		         } else {
		            return "No address found";
		         }
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
					jsonObject.accumulate("id", takerid);
					jsonObject.accumulate("rating", ratingbar.getRating());
					jsonObject.accumulate("review", comments.getText().toString());

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
						//int id = jsonObj.getInt("id");

						if(status)
						{
							/*getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
							.putInt("id", id).commit();*/
							startNewActivity();
						}else Toast.makeText(getActivity(), "Error while updating rating", Toast.LENGTH_SHORT).show();
					}catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void startNewActivity() {
			//Toast.makeText(getActivity(), "Rating updated", Toast.LENGTH_SHORT).show();
			getActivity().finish();
			
		}
	}

}
