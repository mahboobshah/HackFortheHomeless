package org.hforh;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
// You must change these imports to yours

public class CustomNotificationReceiver extends BroadcastReceiver {

	NotificationCompat.Builder mBuilder;
	Intent resultIntent;
	int mNotificationId = 001;
	Uri notifySound;

	String alert; // This is the message string that send from push console
	String where;
	String when;
	String pay;
	String type;
	String giverName;

	String status ;
	int takerid;

	@Override
	public void onReceive(Context context, Intent intent) {

		//Get JSON data and put them into variables
		try {

			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data")); 
			status = json.getString("status").toString();

			if (status.equals("todo")){

				alert = json.getString("alert").toString();
				where = json.getString("where").toString();
				when = json.getString("when").toString();
				pay = json.getString("pay").toString();
				type = json.getString("type").toString();
				giverName = json.getString("givername").toString();
			}
			else {
				if (status.equals("done")){

					alert = json.getString("alert").toString();
					takerid = json.getInt("takerid");

				}else if (status.equals("inprogress")){

					alert = json.getString("alert").toString();

				}
			}

		} catch (JSONException e) {

		}

		//You can specify sound
		notifySound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher); //You can change your icon
		mBuilder.setContentText(alert);
		mBuilder.setContentTitle("Hack for Homeless");
		mBuilder.setSound(notifySound);
		mBuilder.setAutoCancel(true);

		if(status.equals("todo")) {

			// this is the activity that we will send the user, change this to anything you want
			resultIntent = new Intent(context, WorkAvailabilityActivity.class);
			resultIntent.putExtra("where", where);
			resultIntent.putExtra("when", when);
			resultIntent.putExtra("pay", pay);
			resultIntent.putExtra("type", type);
			resultIntent.putExtra("givername", giverName);
		} else {
			if(status.equals("inprogress")) {
			resultIntent = new Intent(context, HaveAJobActivity.class);
			resultIntent.putExtra("status", "inprogress");
			}else {
				resultIntent = new Intent(context, HaveAJobActivity.class);
				resultIntent.putExtra("status", "done");
				resultIntent.putExtra("takerid", takerid);
			}
			
		}
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 
		//startActivity(intent);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		notificationManager.notify(mNotificationId, mBuilder.build());

	}

}