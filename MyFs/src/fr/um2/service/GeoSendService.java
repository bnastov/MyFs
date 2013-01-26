package fr.um2.service;

import fr.um2.apicaller.OwerUser;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GeoSendService extends Service {

	public static final String TAG = "My";

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
				loc.getLatitude();
				loc.getLongitude();

				String Text = "My current location is: " + "Latitud = "
						+ loc.getLatitude() + "Longitud = "
						+ loc.getLongitude();

				Log.i(TAG, Text);
				
				OwerUser.getUser().updateGeo(loc.getLongitude(), loc.getLatitude());
			}
		};

		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);

	}
}