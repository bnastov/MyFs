package fr.um2.service;

import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import fr.um2.apicaller.Position;
import fr.um2.apicaller.ResponseApi;
import fr.um2.database.GeoLocationDBAdapteur;
import fr.um2.entities.GeoLocation;
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
				long now = Calendar.getInstance().getTimeInMillis();
				if (now - lasttime > TWO_MINUTE) {
					loc.getLatitude();
					loc.getLongitude();

					String Text = "My current location is: " + "Latitud = "
							+ loc.getLatitude() + "Longitud = "
							+ loc.getLongitude();

					Log.i(TAG, Text);

					OwerUser.getUser().updateGeo(loc.getLongitude(),
							loc.getLatitude());
					lasttime = now;
				}
			}
		};

		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);

		mHandler.postDelayed(traceTask, TWO_MINUTE);

	}

	private Handler mHandler = new Handler();
	private static final int TWO_MINUTE = 120000;

	private Runnable traceTask = new Runnable() {

		public void run() {
			for (Friend friend : OwerUser.getUser().getFriends()) {
				if(friend.isTracable()){
					ResponseApi<Position> repo = OwerUser.getUser().getGeoLocalizationOfFriend(friend.getPublictoken());
					if(repo.isOK()){
						GeoLocation f = new GeoLocation(OwerUser.getUser().getToken(),friend.getPublictoken(), repo
								.getResults().getLat(), repo.getResults().getLon(),
								repo.getResults().getTime());
						GeoLocationDBAdapteur base = new GeoLocationDBAdapteur(getApplicationContext());
						base.open();
						base.insertGeoLocation(f);
						base.close();
					}
				}
			}
			
			mHandler.postDelayed(traceTask, TWO_MINUTE);
		}
	};
}