package fr.um2.entities;

public class GeoLocation {

	String token;
	String publictoken;
	String pseudo;

	double lat;

	double lon;

	String time;

	public GeoLocation() {
		super();

	}

	public GeoLocation(String t, String pt,String ps, double lt, double lg, String ti) {
		token = t;
		publictoken = pt;
		lat = lt;
		lon = lg;
		time = ti;
		pseudo = ps;

	}

	@Override
	public String toString() {
		return token + "\n" + publictoken + "\n" + lat + "\n" + lon + "n"
				+ time;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
