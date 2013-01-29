package fr.um2.apicaller;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import fr.um2.user.AbstractUser;

import android.util.Log;

public class Parser {
	/**
	 * To parse any object JAVA for the fields given
	 * 
	 * @param render
	 * @param jsonText
	 * @param fields
	 */
	public static void parse(Object render, String jsonText, String... fields) {
		parse(render, getJsonObjectFromText(jsonText), fields);
	}

	/**
	 * To parse any object JAVA for the attribute given
	 * 
	 * @param render
	 * @param obj
	 * @param fields
	 */
	public static void parse(Object render, JSONObject obj, String... fields) {
		try {
			for (String f : fields) {
				Log.d("parse : " + render.getClass().getSimpleName(),
						"On Field : " + f);
				if (obj.has(f)) {
					Field f1;
					
					if (render instanceof AbstractUser && !f.equals("token") && !f.equals("visible")) {
						f1 = render.getClass().getSuperclass()
								.getDeclaredField(f);
					} else {
						f1 = render.getClass()
								.getDeclaredField(f);
					}
					f1.setAccessible(true);
					f1.set(render, obj.getString(f));
					f1.setAccessible(false);
				} else {
					Log.d("WARNNING : " + f, "Does NOT EXIST");
				}
			}
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		} catch (NoSuchFieldException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}
	}

	/**
	 * Parse Postion
	 * 
	 * @param render
	 * @param obj
	 * @param fields
	 */
	public static void parse(Position render, JSONObject obj) {
		try {
			render.setLat(obj.getDouble("lat"));
			render.setLon(obj.getDouble("lon"));
			render.setTime(obj.getString("time"));
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}
	}

	public static JSONObject getJsonObjectFromText(String jsonText) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(jsonText);
		} catch (JSONException e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}
		return obj;
	}

}
