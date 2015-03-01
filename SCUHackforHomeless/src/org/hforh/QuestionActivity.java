package org.hforh;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class QuestionActivity extends Activity {
	String result=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		final Question question = (Question) getIntent().getSerializableExtra(
				"question");
		String QUESTION_TEXT_URL = "http://sfbay.craigslist.org/reply/sfo/rid/"+question.getId();
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				                
				try {                    
					URL url = new URL(params[0]);
					//InputStream in = url.openStream();  
					//BufferedReader br = new BufferedReader(in);
					BufferedReader br = new BufferedReader(
					        new InputStreamReader(url.openStream()));
					String inputLine;
			        while ((inputLine = br.readLine()) != null)
			        {    //Log.i("immer",inputLine);
			        	Pattern pattern = Pattern.compile("(class=\"mailapp\">)(.*?)(</a>)");
			            Matcher matcher = pattern.matcher(inputLine);
			            
			            if(matcher.find())
			            {
			            	
			        	result=matcher.group(2);
			        	Log.i("immer",result);
			            }
			        }	
			        br.close();                    

				} catch (IOException e) {
					Log.e(getClass().toString(), e.getMessage());
				}
				return result;
			}
			@Override
			protected void onPostExecute(final String string) {
				WebView webView = (WebView) findViewById(R.id.webView);
				webView.setInitialScale(1);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
				webView.setScrollbarFadingEnabled(false);
				webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				webView.loadUrl(question.getUrl());		

				findViewById(R.id.sendEmail).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String address = string;
						address = "sravan2j@gmail.com";   //this line is just for testing
						String subject = question.getTitle();
						String profile = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
								.getString("profile", "");
						String emailtext = "Hello, I found this advertisement in the below link \n" + question.getUrl() + "\n\nI am interested in this work. Please find my profile details below,\n"+profile;

						final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

						emailIntent.setType("plain/text");

						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ address});

						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);

						startActivity(Intent.createChooser(emailIntent, "Send mail..."));

					}
				});
				/*ArrayAdapter<Question> adapter = new ArrayAdapter<Question>(
						MainActivity.this, R.layout.questiontext_item, quiz.getQuestions()) {
					@Override
					public View getView(int position, View v, ViewGroup parent) {

						LayoutInflater inflater = LayoutInflater.from(getContext());
						Question q = getItem(position);                                
						if (v == null) v = inflater.inflate(R.layout.questiontext_item, null);                                
						TextView textView = (TextView) v.findViewById(R.id.questionText);
						textView.setText(q.getTitle());                    
						Typeface custom_font = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/oxygenregular.ttf");
						textView.setTypeface(custom_font);
						TextView textView1 = (TextView) v.findViewById(R.id.dateTime);
						textView1.setText(q.getDateTime());
						custom_font = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/caviardreamsbold.ttf");
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
						Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
						intent.putExtra("question", ques);
						startActivity(intent);

					}});*/
				
				
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












/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
QuestionActivity.this, R.layout.choicetext_item, question.getChoices());;

ListView listView = (ListView) findViewById(R.id.choices);
listView.setAdapter(adapter);
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view,
int position, long id) {
boolean correct = question.isCorrect(position);
Log.i("hw0333",question.getResult());
Toast toast = Toast.makeText(getApplicationContext(), (correct ? "Correct Choice. Good job!" : "Wrong Choice!"), Toast.LENGTH_SHORT);
toast.show();
Intent i = getIntent();
i.putExtra("question", question);
i.putExtra("correct", correct);
setResult(RESULT_OK, i);
finish();
Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
intent.putExtra("question", question);
intent.putExtra("choice", position);
startActivity(intent);
}});
/*
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:paddingBottom="@dimen/activity_vertical_margin"
android:paddingLeft="@dimen/activity_horizontal_margin"
android:paddingRight="@dimen/activity_horizontal_margin"
android:paddingTop="@dimen/activity_vertical_margin"
tools:context=".MainActivity" >

<TextView
    android:id="@+id/questionTextHeader"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignParentLeft="true"
    android:layout_alignParentTop="true" />

<ListView
    android:id="@+id/choices"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#7FADAB"
    android:layout_alignParentLeft="true"
    android:layout_below="@id/questionTextHeader" >
</ListView>

</RelativeLayout>
//textView.setTextColor(ColorStateList.valueOf(qColor.get(position)));

                                /*if (q.getResult()=="wrong")
                                {
                                    Log.i("hw03-color",(String) q.getText());                                    
                                    textView.setTextColor(Color.RED);


                                }

                                else if (q.getResult()=="right")
                                {
                                    Log.i("hw03-color",(String) q.getText());
                                    textView.setTextColor(Color.GREEN);

                                }*/
