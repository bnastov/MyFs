package fr.um2.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import fr.um2.apicaller.Position;
import fr.um2.apicaller.ResponseApi;
import fr.um2.database.GeoLocationDBAdapteur;
import fr.um2.entities.GeoLocation;
import fr.um2.myfs.GoogleMapActivity;
import fr.um2.myfs.R;
import fr.um2.search.ResultActivity;
import fr.um2.user.Friend;
import fr.um2.user.OwerUser;

public class GeoSendService extends Service {

	public static final String TAG = "My";
	public static long lasttime = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service Destroy");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		
		

		Log.i(TAG, "Service Start");
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener mlocListener = new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location loc) {
				/*long now = Calendar.getInstance().getTimeInMillis();
				if (now - lasttime > TWO_MINUTE) {//*/
					loc.getLatitude();
					loc.getLongitude();

					String Text = "My current location is: " + "Latitud = "
							+ loc.getLatitude() + "Longitud = "
							+ loc.getLongitude();

					Log.i(TAG, Text);

					OwerUser.getUser().updateGeo(loc.getLongitude(),
							loc.getLatitude());
					//lasttime = now;
				//}
			}
		};

		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);

		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				for (Friend friend : OwerUser.getUser().getFriends()) {
					if (friend.isTracable()) {
						ResponseApi<Position> repo = OwerUser
								.getUser()
								.getGeoLocalizationOfFriend(friend.getPublictoken());
						if (repo.isOK()) {
							
							friend.setGeoloc(repo.getResults());
							
							GeoLocation f = new GeoLocation(OwerUser.getUser()
									.getToken(), friend.getPublictoken(),
									friend.getPseudo(), repo.getResults().getLat(),
									repo.getResults().getLon(), repo.getResults()
											.getTime());
							GeoLocationDBAdapteur base = new GeoLocationDBAdapteur(
									getApplicationContext());
							base.open();
							base.insertGeoLocation(f);
							base.close();
							
							showNotification(friend);
						}
					}
				}
	
			}
		}, TWO_MINUTE, TWO_MINUTE);
	}
	
	Timer timer = new Timer();

	private static final int TWO_MINUTE = 2000;

	
	public void showNotification(Friend f){
		Log.i(TAG, "notification");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Tracing of \n" + f.getPseudo())
				.setContentText(f.getFirstName() + ","+f.getLastName());
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, GoogleMapActivity.class);
 
		f.setVisibleInMap(true);
		//resultIntent.putExtra("onePoint", f);
		// The stack builder object will contain an artificial back stack
		// for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out
		// of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(GoogleMapActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
				0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());

	}
}