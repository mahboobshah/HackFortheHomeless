package org.hforh;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;

public class WorkAvailabilityActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_availability);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.work_availability, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnClickListener, com.google.android.gms.location.LocationListener{
		
		TextView where;
		TextView when;
		TextView type;
		TextView pay;
		
		TextView progress;
		
		String wheretext;
		String whentext;
		String typetext;
		String paytext;
		String givername;
		LinearLayout normal;
		LinearLayout mapView;
		View rootView;
		
		GoogleMap map;
		private SupportMapFragment mapFragment;
		private double destLat;
		private double destLong;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			 rootView = inflater.inflate(
					R.layout.fragment_work_availability, container, false);
			
			normal = (LinearLayout) rootView.findViewById(R.id.normalView);
			mapView = (LinearLayout) rootView.findViewById(R.id.MapView);
			
			TextView textView1 = (TextView) rootView.findViewById(R.id.ftv1);
			Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/caviardreamsbold.ttf");
			textView1.setTypeface(custom_font);
			TextView textView2 = (TextView) rootView.findViewById(R.id.ftv2);
			TextView textView3 = (TextView) rootView.findViewById(R.id.ftv3);
			TextView textView4 = (TextView) rootView.findViewById(R.id.ftv4);
			textView2.setTypeface(custom_font);
			textView3.setTypeface(custom_font);
			textView4.setTypeface(custom_font);
			
			
			where = (TextView) rootView.findViewById(R.id.where1);
			when = (TextView) rootView.findViewById(R.id.when1);
			type = (TextView) rootView.findViewById(R.id.type1);
			pay = (TextView) rootView.findViewById(R.id.pay1);
			
			progress = (TextView) rootView.findViewById(R.id.inProgress);
			progress.setTypeface(custom_font);
			Intent intent = getActivity().getIntent();
			where.setText(intent.getStringExtra("where"));
			when.setText(intent.getStringExtra("when"));
			pay.setText(intent.getStringExtra("pay"));
			type.setText(intent.getStringExtra("type"));
			
			givername = intent.getStringExtra("givername");
			
/*			MapView mapView1 = (MapView) rootView.findViewById(R.id.mapview1);

			mapView1.onCreate(savedInstanceState);
			mapView1.onResume(); //without this, map showed but was empty 

		    // Gets to GoogleMap from the MapView and does initialization stuff
		    map = mapView1.getMap(); 
		    map.getUiSettings().setMyLocationButtonEnabled(true);
		    map.setMyLocationEnabled(true);
		    try {
		        MapsInitializer.initialize(this.getActivity());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }


		    // Updates the location and zoom of the MapView
		    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(44.14, 14.2), 10);
		    map.animateCamera(cameraUpdate);*/
			
			mapFragment =
					(SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.googleMap);
			
			Geocoder coder = new Geocoder(getActivity());
			List<Address> address;
			try{
				address = coder.getFromLocationName(wheretext, 5);
				Address location1 = address.get(0);
				destLat = location1.getLatitude();
				destLong = location1.getLongitude();
			}catch (IOException e){
				
			}
			
			mapFragment.getMapAsync(new OnMapReadyCallback() {
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;
					map.setMyLocationEnabled(true);
					LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					String bestProvider = locationManager.getBestProvider(criteria, true);
					Location location = locationManager.getLastKnownLocation(bestProvider);
					//location.getAltitude();
					if (location != null) {
						
						onLocationChanged(location);
					}
					Log.i("Ranga", "Emochindia ra");
					//locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
				}
			});
			
			Button btn = (Button) rootView.findViewById(R.id.buttonA);
			btn.setTypeface(custom_font);
			btn.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button btn = (Button) v;
			if (btn.getTag().toString().equals("accept")){
				normal.setVisibility(View.GONE);
				mapView.setVisibility(View.VISIBLE);
				progress.setVisibility(View.VISIBLE);
				//mapFragment =
				//		(SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.googleMap);

				//View view = inflater.inflate(R.layout.activity_main, vg, false);
				//android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				//mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.googleMap);
				
				
				
				btn.setTag("done");
				btn.setText("Done! ");
				ParsePush push = new ParsePush();
				push.setChannel(givername); // Notice we use setChannels not setChannel
				JSONObject obj = null;
				try {
					obj = new JSONObject();
					//"{\"alert\": \"Need some work to be done!\",  \"uri\": \"com.example.scuhackforhomeless.WorkAvailabilityActivity\", \"action\":\"com.example.scuhackforhomeless.CUSTOM_NOTIFICATION\" }"
					obj.put("alert", "Your work is in progres!!");
					obj.put("action", "com.example.scuhackforhomeless.CUSTOM_NOTIFICATION");
					obj.put("status", "inprogress");
					obj.put("givername", givername);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//push.setMessage("hello");
				push.setData(obj);
				
				//
				//push.setMessage("Need some work to be done");
				try {
					push.send();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				push.sendInBackground(new SendCallback() {
					
					@Override
					public void done(ParseException arg0) {
						Toast.makeText(getActivity(), arg0==null?" no exception":"excp", Toast.LENGTH_LONG).show();
						
					}
				});
			}else {
				ParsePush push = new ParsePush();
				push.setChannel(givername); // Notice we use setChannels not setChannel
				JSONObject obj = null;
				try {
					obj = new JSONObject();
					//"{\"alert\": \"Need some work to be done!\",  \"uri\": \"com.example.scuhackforhomeless.WorkAvailabilityActivity\", \"action\":\"com.example.scuhackforhomeless.CUSTOM_NOTIFICATION\" }"
					obj.put("alert", "Hurrayy!!! Your work is done!!");
					obj.put("action", "com.example.scuhackforhomeless.CUSTOM_NOTIFICATION");
					obj.put("status", "done");
					int id = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("id", -1);
					obj.put("takerid", id);
					
					obj.put("givername", givername);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//push.setMessage("hello");
				push.setData(obj);
				
				//
				//push.setMessage("Need some work to be done");
				try {
					push.send();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				push.sendInBackground(new SendCallback() {
					
					@Override
					public void done(ParseException arg0) {
						//Toast.makeText(getActivity(), arg0==null?" no exception":"excp", Toast.LENGTH_LONG).show();
						
					}
				});
				
				getActivity().finish();
			}
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			TextView locationTv = (TextView) rootView.findViewById(R.id.latlongLocation);
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			final LatLng latLng = new LatLng(latitude, longitude);
			
			final LatLng desclatlng = new LatLng(destLat, destLong);
			/*final LatLng latLng1 = new LatLng(39.34714975, -123.9408934);
			final LatLng latLng2 = new LatLng(41.34714975, -126.9408934);*/
			
			Log.i("Lat", ""+latitude);
			Log.i("Lat", ""+longitude);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					map.addMarker(new MarkerOptions().position(latLng));
					map.addMarker(new MarkerOptions().position(desclatlng));
					//map.addMarker(new MarkerOptions().position(latLng2));
					map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					map.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
			}, 500);
			locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
		}

		private boolean isGooglePlayServicesAvailable() {
			Log.i("Ranga", "Emochindia ra moda ga is google 1");
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
			if (ConnectionResult.SUCCESS == status) {
				return true;
			} else {
				GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
				return false;
			}
		}
	}

}
