package fr.um2.myfs;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.um2.user.Friend;
import fr.um2.user.OwerUser;
import fr.um2.utils.MyMarker;

public class GoogleMapActivity extends MapActivity {

	public static String onePoint = "OnePoint";

	MapView map;
	int index = -1;
	List<GeoPoint> points = new ArrayList<GeoPoint>();
	Friend notificationFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = (MapView) findViewById(R.id.mapView);
		map.setBuiltInZoomControls(true);

		if (this.getIntent().getExtras() != null) {
			notificationFriend = (Friend) getIntent().getExtras().get(onePoint);

			getOverlayfromGeolocation(notificationFriend);
		} else {
			getOverlayfromFriend();
		}
	}

	private void getOverlayfromGeolocation(Friend friend) {
		index = -1;
		List<Overlay> mapOverlays = map.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_launcher);

		MyMarker itemizedoverlay = new MyMarker(drawable, this);

		GeoPoint point = new GeoPoint(
				(int) (friend.getGeoloc().getLat() * 1000000), (int) (friend
						.getGeoloc().getLon() * 1000000));

		points.add(point);

		if (index < 0) {
			index = 0;
			map.getController().setCenter(point);
			map.getController().setZoom(17);
		}

		OverlayItem overlayitem = new OverlayItem(point, friend.getPseudo(),
				friend.getNumber());

		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);

	}

	private void getOverlayfromFriend() {
		index = -1;
		List<Overlay> mapOverlays = map.getOverlays();
		for (Friend friend : OwerUser.getUser().getFriends()) {
			if (friend.isVisibleInMap()) {

				Drawable drawable = this.getResources().getDrawable(
						R.drawable.ic_launcher);

				MyMarker itemizedoverlay = new MyMarker(drawable, this);

				GeoPoint point = new GeoPoint((int) (friend.getGeoloc()
						.getLat() * 1000000), (int) (friend.getGeoloc()
						.getLon() * 1000000));

				points.add(point);

				if (index < 0) {
					index = 0;
					map.getController().setCenter(point);
					map.getController().setZoom(17);
				}

				OverlayItem overlayitem = new OverlayItem(point,
						friend.getPseudo(), friend.getNumber());

				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);

			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_menu_nextuser:
			if (index > -1) {
				index = (index + 1) % map.getOverlays().size();
				map.getController().setCenter(points.get(index));
			}
			break;

		case R.id.map_menu_beforeuser:
			if (index > -1) {
				if (index == 0) {
					index = map.getOverlays().size();
				}
				index = (index - 1) % map.getOverlays().size();
				map.getController().setCenter(points.get(index));
			}
			break;

		case R.id.map_menu_refresh:
			OwerUser.getUser().getFriendsWeb();
			getOverlayfromFriend();
			break;
		case R.id.map_menu_quit:
			this.finish();
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
