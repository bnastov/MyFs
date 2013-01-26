package fr.um2.apicaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Asynchrony Call for Any Web Service
 * @author bibouh
 *
 */
public class ApiCaller extends AsyncTask<String, Integer, String> {

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		Log.d("Calller", "getASCIIContentFromEntity");
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	@Override
	protected String doInBackground(String... params) {
		Log.d("Calller", "doInBackground");
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		String req = "";
		if (params.length > 0) {
			req = params[0];
		}

		Log.d("Req is ",req);
		
		HttpGet httpGet = new HttpGet(req);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return "{\"what\":\"error\", \"type\" : \""+e.getLocalizedMessage()+"\"}";
		}
		return text;
	}

	/**
	 * To encode String given (Security Reason)
	 * @param str
	 * @return {@link String} encoded on UTF-8
	 */
	public static String urlEncode(String str) {
		String res = "";
		try {
			res = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return res;
	}

	/**
	 * To call an URL given in parameter and return the result
	 * @param url
	 * @return {@link String} the result of call
	 */
	public static String callSyncrone(String url) {
		ApiCaller caller = new ApiCaller();
		caller.execute(url);
		String res = null;
		try {
			res = caller.get();
		} catch (InterruptedException e) {
			Log.e(e.getClass().getSimpleName(),e.getLocalizedMessage());
		} catch (ExecutionException e) {
			Log.e(e.getClass().getSimpleName(),e.getLocalizedMessage());
		}

		Log.d("My", res);

		return res;
	}
	
	
	/**
	 * To  get Image from URL
	 * @param src the url of image
	 * @return {@link Bitmap}
	 */
	public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}