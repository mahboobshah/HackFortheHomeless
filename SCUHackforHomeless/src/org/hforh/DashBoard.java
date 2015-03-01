package org.hforh;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DashBoard extends Activity {
	int corrcnt = 0;
	int totalcount = 0;
	int quesnumb=-1;

	Question ques;
	Quiz result;
	private static final String QUESTION_TEXT_URL = "http://ec2-54-153-86-208.us-west-1.compute.amazonaws.com/data.xml";
	protected static final int GET_CHOICE_RESULT = 0;      

	ArrayList<Integer> qColor = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		new AsyncTask<String, Void, Quiz>() {
			@Override
			protected Quiz doInBackground(String... params) {
				
				
				int id = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getInt("id", -1);
				if (id!=-1)
				{
					HttpEntity httpEntity = null;
					String output = "";
					try {
						// 1. create HttpClient
						HttpClient httpclient = new DefaultHttpClient();

						// 2. make POST request to the given URL
						HttpPost httpPost = new HttpPost("http://ec2-54-153-74-245.us-west-1.compute.amazonaws.com/profile");

						String json = "";

						// 3. build jsonObject
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("id", id);

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
						output = EntityUtils.toString(httpEntity);

						Log.i("outresult",output);
						getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
						.putString("profile", output).commit();
					}
					catch (Exception e) {
						Log.d("InputStream", e.getLocalizedMessage());
					}
				}

				
				
				
				
				
				
				
				
				
				
				
				
				
				
				result = new Quiz();                
				try {                    
					URL url = new URL(params[0]);
					InputStream in = url.openStream();  

					result.readFromXml(in);                      
					in.close();                    

				} catch (IOException e) {
					Log.e(getClass().toString(), e.getMessage());
				}
				return result;
			}
			@Override
			protected void onPostExecute(final Quiz quiz) {

				ArrayAdapter<Question> adapter = new ArrayAdapter<Question>(
						DashBoard.this, R.layout.questiontext_item, quiz.getQuestions()) {
					@Override
					public View getView(int position, View v, ViewGroup parent) {

						LayoutInflater inflater = LayoutInflater.from(getContext());
						Question q = getItem(position);                                
						if (v == null) v = inflater.inflate(R.layout.questiontext_item, null);                                
						TextView textView = (TextView) v.findViewById(R.id.questionText);
						textView.setText(q.getTitle());                    
						Typeface custom_font = Typeface.createFromAsset(DashBoard.this.getAssets(), "fonts/oxygenregular.ttf");
						textView.setTypeface(custom_font);
						TextView textView1 = (TextView) v.findViewById(R.id.dateTime);
						textView1.setText(q.getDateTime());
						custom_font = Typeface.createFromAsset(DashBoard.this.getAssets(), "fonts/caviardreamsbold.ttf");
						textView1.setTypeface(custom_font);
						return v;                                
					}                            
				};

				ListView listView = (ListView) findViewById(R.id.questions);
				listView.setAdapter(adapter);

				TextView t0= (TextView) findViewById(R.id.tview1);
				totalcount = quiz.getQuestions().size();
				t0.setText("Score : "+corrcnt+ " out of " + totalcount);

				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						quesnumb = position;                        
						ques = quiz.getQuestions().get(position);                        
						Intent intent = new Intent(DashBoard.this, QuestionActivity.class);
						intent.putExtra("question", ques);
						startActivity(intent);

					}});
			}
		}.execute(QUESTION_TEXT_URL);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

