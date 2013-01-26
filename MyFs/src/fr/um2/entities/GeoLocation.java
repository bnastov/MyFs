package fr.um2.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "GeoLocation")
public class GeoLocation extends Model {
	@Column(name = "publictoken")
	String publictoken;

	@Column(name = "lat")
	double lat;

	@Column(name = "lon")
	double lon;

	@Column(name = "time")
	String time;

	public GeoLocation() {
		super();

	}

	public GeoLocation(String pt, double lt, double lg, String ti) {
		super();
		publictoken = pt;
		lat = lt;
		lon = lg;
		time = ti;

	}

	@Override
	public String toString() {
		return publictoken + "\n" + lat + "\n" + lon + "n" + time;
	}

	public static GeoLocation getRandom() {
		return new Select().from(GeoLocation.class).orderBy("RANDOM()")
				.executeSingle();
	}

	public String getPublictoken() {
		return publictoken;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getTime() {
		return time;
	}

	public void setPublictoken(String publictoken) {
		this.publictoken = publictoken;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
