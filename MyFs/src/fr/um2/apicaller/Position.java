package fr.um2.apicaller;

public class Position implements Comparable<Position>{
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
	@Override
	public int compareTo(Position another) {
		double distance1 = Math.sqrt( lat*lat  + lon*lon );
		double distance2 = Math.sqrt( another.getLat() * another.getLat() + 
				another.getLon() * another.getLon());
		if(distance1 > distance2)
			return 1;
		if(distance1 < distance2)
			return -1;
			
		return 0;
	}
	
	public boolean equals(Position p) {	
		return lat == p.getLat() && lon == p.getLon();
	}
	
	
}
