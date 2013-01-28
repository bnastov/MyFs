package fr.um2.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.um2.apicaller.ApiCaller;
import fr.um2.apicaller.Parser;
import fr.um2.apicaller.Position;
import fr.um2.apicaller.ResponseApi;

public class OwerUser extends AbstractUser {
	String token = "";

	ArrayList<Friend> friends = new ArrayList<Friend>();

	private OwerUser() {
	}

	static OwerUser instance = null;

	public static ResponseApi<Void> createUser(String pseudo, String password,
			String firstname, String lastname) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=createuser&pseudo=" + pseudo + "&password="
				+ password + "&firstname=" + firstname + "&lastname="
				+ lastname);

		ResponseApi<Void> api = new ResponseApi<Void>();
		Parser.parse(api, res, "what", "type", "info", "details");

		return api;

	}

	/**
	 * Ajouter un Friend
	 * 
	 * @param publictoken
	 *            public token of the other user
	 * @return {@link ResponseApi} of {@link Void}
	 */
	public ResponseApi<Void> addFriend(String publictoken) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=addfriend&token=" + this.token + "&friendtoken="
				+ publictoken);

		ResponseApi<Void> api = new ResponseApi<Void>();
		Parser.parse(api, res, "what", "type", "info", "details");

		return api;

	}

	/**
	 * Delete un Friend
	 * 
	 * @param publictoken
	 *            public token of the other user
	 * @return {@link ResponseApi} of {@link Void}
	 */
	public ResponseApi<Void> removeFriend(String publictoken) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=deletefriend&token=" + this.token + "&friendtoken="
				+ publictoken);

		ResponseApi<Void> api = new ResponseApi<Void>();
		Parser.parse(api, res, "what", "type", "info", "details");

		return api;
	}

	/**
	 * To login User with pseudo and password given.
	 * 
	 * @param pseudo
	 * @param password
	 * @return {@link ResponseApi}
	 */
	public static ResponseApi<OwerUser> loginUser(String pseudo, String password) {
		ResponseApi<OwerUser> response = new ResponseApi<OwerUser>();
		if (instance == null) {
			String res = ApiCaller.callSyncrone(urlserver
					+ "?action=Login&pseudo=" + ApiCaller.urlEncode(pseudo)
					+ "&password=" + ApiCaller.urlEncode(password));

			Parser.parse(response, res, "what", "type");

			if (response.isOK()) {
				instance = new OwerUser();
				Parser.parse(instance, res, "pseudo", "firstName", "lastName",
						"token", "publictoken", "age", "city", "imagelink",
						"number");

				response.setResults(instance);
			} else {
				response.setResults(null);
			}

		}
		return response;
	}

	/**
	 * To get The Singleton Class of User if he is connected, null else
	 * 
	 * @return {@link OwerUser}
	 */
	public static OwerUser getUser() {
		return instance;
	}

	public ArrayList<Friend> getFriends() {
		return friends;
	}

	/**
	 * To get Friend form the web <strong> WARNING :</strong> This Getter is
	 * used to get Friends From the WebService <br/>
	 * do not abuse
	 * 
	 * @return
	 */
	public ArrayList<Friend> getFriendsWeb() {
		friends.clear();
		findFriend();
		return friends;
	}

	/**
	 * This function is intern called when you try to get a friends
	 */
	protected void findFriend() {

		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=getfriends&token=" + token);
		ResponseApi<Void> rep = new ResponseApi<Void>();
		Parser.parse(rep, res, "what");
		try {
			if (rep.isOK()) {
				JSONObject f = Parser.getJsonObjectFromText(res);
				JSONArray frds;
				frds = f.getJSONArray("friends");

				for (int i = 0; i < frds.length(); i++) {
					Friend friend = new Friend();
					Parser.parse(friend, frds.getJSONObject(i), "pseudo",
							"firstName", "lastName", "publictoken", "city",
							"age", "imagelink", "number");
					
					Position p = new Position();
					Parser.parse(p, frds.getJSONObject(i));
					
					friend.setGeoloc(p);
					
					friends.add(friend);
				}
			}
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

	}

	/**
	 * To update user Position
	 * 
	 * @param lon
	 *            current Longitude of the user
	 * @param lat
	 *            current Latitude of the user
	 * @return
	 */
	public ResponseApi<Void> updateGeo(double lon, double lat) {
		String res = ApiCaller.callSyncrone(urlserver + "?action=update&lat="
				+ lat + "&lon=" + lon + "&token=" + token);
		ResponseApi<Void> rep = new ResponseApi<Void>();
		Parser.parse(rep, res, "what", "type", "info", "details");
		return rep;

	}

	/**
	 * To update user Visibility
	 * 
	 * @param value
	 * @return
	 */
	public ResponseApi<Void> updateVisible(Boolean value) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=update&visible=" + value.toString() + "&token="
				+ token);
		ResponseApi<Void> rep = new ResponseApi<Void>();
		Parser.parse(rep, res, "what", "type", "info", "details");
		return rep;

	}

	/**
	 * To update user informations
	 * 
	 * @return
	 */
	public ResponseApi<Void> updateProfile(String firstname, String lastname,
			String city, String age, String imagelink, String number) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=updateuser&firstname=" + firstname + "&lastname="
				+ lastname + "&imagelink=" + imagelink + "&age=" + age
				+ "&city=" + city + "&number=" + number + "&token=" + token);
		
		
		ResponseApi<Void> rep = new ResponseApi<Void>();
		Parser.parse(rep, res, "what", "type", "info", "details");
		if (rep.isOK()) {
			setFirstName(firstname);
			setLastName(lastname);
			setCity(city);
			setAge(age);
			setImagelink(imagelink);
			setNumber(number);
		}

		return rep;

	}

	/**
	 * Get localization of friend
	 * 
	 * @param tokenpublic
	 * @return {@link ResponseApi}
	 */
	public ResponseApi<Position> getGeoLocalizationOfFriend(String tokenpublic) {
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=getlocationfriend&token=" + token + "&friendtoken="
				+ tokenpublic);
		ResponseApi<Position> rep = new ResponseApi<Position>();
		Parser.parse(rep, res, "what", "type", "info", "details");
		Position pac = null;
		try {
			if (rep.isOK()) {
				JSONObject f = Parser.getJsonObjectFromText(res);
				JSONArray frds = f.getJSONArray("friends");
				pac = new Position();
				Parser.parse(pac, frds.getJSONObject(0));
				rep.setResults(pac);
			}
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

		return rep;
	}

	/**
	 * Search a regex in friend or public token
	 * 
	 * @param regex
	 * @return {@link ResponseApi} witth {@link ArrayList} of {@link OwerUser}
	 */
	public ResponseApi<ArrayList<Friend>> search(String regex) {

		ResponseApi<ArrayList<Friend>> ret = new ResponseApi<ArrayList<Friend>>();
		ArrayList<Friend> searchedFriends = new ArrayList<Friend>();
		String res = ApiCaller.callSyncrone(urlserver
				+ "?action=searchfriends&token=" + this.token + "&search="
				+ regex);

		Parser.parse(ret, res, "what", "type", "info", "details");
		try {
			if (ret.isOK()) {
				JSONObject f = Parser.getJsonObjectFromText(res);
				JSONArray frds = f.getJSONArray("friends");

				for (int i = 0; i < frds.length(); i++) {
					Friend friend = new Friend();
					Parser.parse(friend, frds.getJSONObject(i), "pseudo",
							"firstName", "lastName", "publictoken", "number",
							"age", "city", "imagelink");
					searchedFriends.add(friend);
				}
				ret.setResults(searchedFriends);
			}
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

		return ret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return super.toString() + "\n" + getFriends();
	}

}
