package fr.um2.apicaller;

public class Position {
	double lat;
	double lon;
	String time;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public Position() {
	
	}
	
	@Override
	public String toString() {
		return lat+" "+lon+" "+time;
	}
	
	
}
