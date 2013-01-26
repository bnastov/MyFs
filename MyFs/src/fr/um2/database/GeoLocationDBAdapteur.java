package fr.um2.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import fr.um2.entities.GeoLocation;

public class GeoLocationDBAdapteur {
	private static final int BASE_VERSION = 1;
	private static final String BASE_NOM = "geolocation.db";
	
	private static final String TABLE_GEO_LOCATION = "table_geo_location";
	
	private static final String COLONNE_ID = "id";
	public static final int COLONNE_ID_ID = 0;
	
	private static final String COLONNE_PUBLIC_TOKEN = "public_token";
	public static final int COLONNE_PUBLIC_TOEKN_ID = 1;
	
	private static final String COLONNE_LATITUDE = "latitude";
	public static final int COLONNE_LATITUDE_ID = 2;
	
	private static final String COLONNE_LONGITUDE = "longitude";	
	public static final int COLONNE_LONGITUDE_ID = 3;
	
	private static final String COLONNE_TIME = "time";
	public static final int COLONNE_TIME_ID = 4;
	
	/**
	 * The creation database query
	 */
	private static final String REQUETE_CREATION_BD = 
			"create table " + TABLE_GEO_LOCATION + " (" + 
				COLONNE_ID 				+ " integer primary key autoincrement, " + 
				COLONNE_PUBLIC_TOKEN 	+ " text not null, " + 
				COLONNE_LATITUDE 		+ " double not null, " +
				COLONNE_LONGITUDE 		+ " double not null," +
				COLONNE_TIME 			+ " text not null);";
	
	/**
	 * This instance will be manipulated inside the adapter class
	 */
	private SQLiteDatabase maBaseDonnees;
	private MaBaseOpenHelper baseHelper; 
	
	//////////////////////////////////////////////////////////////////////
	
	public GeoLocationDBAdapteur(Context ctx){
		baseHelper = new MaBaseOpenHelper(ctx, BASE_NOM, null, BASE_VERSION);
	}
	
	public SQLiteDatabase open(){
		maBaseDonnees = baseHelper.getWritableDatabase();
		return maBaseDonnees;
	}
	
	public void close(){
		maBaseDonnees.close();
	}
	
	/**
	 * Insert {@link GeoLocation} into the database
	 * @param geo the instance to be inserted
	 */
	public long insertGeoLocation(GeoLocation geo) {
		ContentValues valeurs = new ContentValues();
		
		valeurs.put(COLONNE_PUBLIC_TOKEN, 	geo.getPublictoken());
		valeurs.put(COLONNE_LATITUDE, 		geo.getLat());
		valeurs.put(COLONNE_LONGITUDE, 		geo.getLon());
		valeurs.put(COLONNE_TIME, 			geo.getTime());
		
		return maBaseDonnees.insert(TABLE_GEO_LOCATION, null, valeurs);		
	}
	
	/**
	 * Get an geo location in function  of a public token
	 * @param public_token the public token
	 * @return the query result
	 */
	public ArrayList<GeoLocation> getGeo(String public_token) {
		Cursor c = maBaseDonnees.query(TABLE_GEO_LOCATION, new String[]{
				COLONNE_ID, COLONNE_PUBLIC_TOKEN, COLONNE_LATITUDE, COLONNE_LONGITUDE, COLONNE_TIME}, null, null, null, 
				COLONNE_PUBLIC_TOKEN + " LIKE " + public_token, null);
		return cursorToGeoLocation(c);
	}
	
	/**
	 * Transforms a cursor to an object of type {@link GeoLocation}
	 * @param c the cursor
	 * @return the object of type {@link GeoLocation}
	 */
	private ArrayList<GeoLocation> cursorToGeoLocation(Cursor c) {
		//If nothing found
		if(c.getCount() == 0)
			return null;
		
		ArrayList<GeoLocation> result = new ArrayList<GeoLocation>();
		String pt,ti;
		Double la,lo;
		c.moveToFirst();
		while(c.moveToNext()){
			pt = c.getString(COLONNE_PUBLIC_TOEKN_ID);
			la = c.getDouble(COLONNE_LATITUDE_ID);
			lo = c.getDouble(COLONNE_LONGITUDE_ID);
			ti = c.getString(COLONNE_TIME_ID);
			result.add(new GeoLocation(pt, la, lo, ti));		
		}
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////

	public class MaBaseOpenHelper extends SQLiteOpenHelper{
		
		public MaBaseOpenHelper(Context ctx, String nom, CursorFactory cursorfactory, int version){
			super(ctx, nom, cursorfactory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(REQUETE_CREATION_BD);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table " + TABLE_GEO_LOCATION + ";");
			onCreate(db);
		}
		
	}
}
