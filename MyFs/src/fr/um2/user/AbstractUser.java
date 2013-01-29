package fr.um2.user;

import fr.um2.apicaller.Position;

public abstract class AbstractUser {
	protected static String urlserver = "http://findmyfriendwebservice.comlu.com/MyFriend/api.php";

	String firstName = "";
	String lastName = "";
	String publictoken = "";
	String pseudo = "";
	String age = "";
	String city = "";
	String imagelink = "";
	String number= "";
	Position geoloc;
	
	
	public int getAgeAsInteger(){
		return Integer.getInteger(age, 0);
	}
	
	
	

	public String getPublictoken() {
		return publictoken;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	

	public String getPseudo() {
		return pseudo;
	}

	public Position getGeoloc() {
		return geoloc;
	}

	public static String getUrlserver() {
		return urlserver;
	}

	

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getImagelink() {
		return imagelink;
	}

	public void setImagelink(String imageLink) {
		this.imagelink = imageLink;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public void setPublictoken(String publictoken) {
		this.publictoken = publictoken;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public void setGeoloc(Position geoloc) {
		this.geoloc = geoloc;
	}

	
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
	@Override
	public String toString() {
		return pseudo + " : " + firstName + " : " + lastName + " : "  + publictoken + "";
	}
}
