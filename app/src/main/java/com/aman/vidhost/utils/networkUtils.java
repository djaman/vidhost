package com.aman.vidhost.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import java.io.IOException;
import android.widget.Toast;
import java.io.File;
import android.app.NotificationManager;
import android.app.Notification;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class networkUtils {
	private static final String USER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.47 Safari/537.36";
	
	Context context;
    public networkUtils(Context context){
		this.context = context;
		
		
	}
	public boolean checkSelfInternetConnection(){
		ConnectivityManager connec =
			(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
	}
	public static Bitmap getBitmapFromURL(String src) {
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT > 8) 
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
			StrictMode.setThreadPolicy(policy);
			//your codes here

		}
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
	public String getSize(String url1){
		URL url = null;
		try {  
			url = new URL(url1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int length = connection.getContentLength();
		String size = "" + String.valueOf(length);
		return size;
	}
	public static String readableFileSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	
	public String getSourceFormUrl(String url) throws IOException {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		// Build and set timeout values for the request.
		URLConnection connection = (new URL(url)).openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setRequestProperty("User-agent",USER_AGENT);
		connection.connect();

		// Read and store the result line by line then return the entire string.
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder html = new StringBuilder();
		for (String line; (line = reader.readLine()) != null; ) {
			html.append(line);
		}
		in.close();

		return html.toString();
	}
}
