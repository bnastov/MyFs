package fr.um2.myfs;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.um2.utils.MyMarker;

public class GoogleMapActivity extends MapActivity {

	MapView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = (MapView) findViewById(R.id.mapView);
		map.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = map.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_launcher);
		MyMarker itemizedoverlay = new MyMarker(drawable, this);
		//lat:36.752175,lng:3.042026
		GeoPoint point = new GeoPoint(36752175, 3042026);
		
		map.getController().setCenter(point);
		map.getController().setZoom(12);
		
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
				"I'm in Mexico City!");

		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		
		
		
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
