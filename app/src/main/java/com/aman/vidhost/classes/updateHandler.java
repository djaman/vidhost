package com.aman.vidhost.classes;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.aman.vidhost.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;

  public class updateHandler {
    Context context;
	ProgressDialog pd;
    public updateHandler(Context context,ProgressDialog pd){
		this.context = context;
		new JsonTask().execute("https://api.npoint.io/647fc6d006274163fdf1");
		this.pd = pd;
	}
	public updateHandler(Context context){
		this.context = context;
		new JsonTask().execute("https://api.npoint.io/647fc6d006274163fdf1");
		pd = new ProgressDialog(context);
	}
	public String getAppVersion() {
		String versionCode = "1.0";

		try {
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {}

		return versionCode;
	}
    private class JsonTask extends AsyncTask<String, String, JSONObject> {

		protected void onPreExecute() {
			super.onPreExecute();

			
		}

		protected JSONObject doInBackground(String... params) {


			HttpURLConnection connection = null;
			BufferedReader reader = null;

			try {
				URL url = new URL(params[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();


				InputStream stream = connection.getInputStream();

				reader = new BufferedReader(new InputStreamReader(stream));

				StringBuffer buffer = new StringBuffer();
				String line = "";

				while ((line = reader.readLine()) != null) {
					buffer.append(line+"\n");
					Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-) 

				}

				try {
					return new JSONObject(buffer.toString());
				} catch (JSONException e) {}


			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(final JSONObject result) {
			super.onPostExecute(result);
			if (pd.isShowing()){
				pd.dismiss();
			}
			if(result != null)
			{
				try {
					String serverVersion = result.getString("currentVersion");
					String appversion = getAppVersion();
					Log.d("appVersion",""+Float.parseFloat(appversion));
					Log.d("serverVersion",""+serverVersion);
					if(Float.parseFloat(appversion) < Float.parseFloat(serverVersion)){
						Log.d("update","availbel");

						final Dialog dialog = new Dialog(context);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setCancelable(true);
						dialog.setContentView(R.layout.update_dialog);
						dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

						TextView submit  = (TextView) dialog.findViewById(R.id.update);
						TextView cancel  = (TextView) dialog.findViewById(R.id.cancel);
						TextView desc  = (TextView) dialog.findViewById(R.id.desc);
						TextView tophead  = (TextView) dialog.findViewById(R.id.updatehead);

						desc.setText(result.getString("desc"));

						tophead.setText("Update Found v"+serverVersion);
						submit.setOnClickListener(new View.OnClickListener(){

								@Override
								public void onClick(View p1) {
									Toast.makeText(context,"Uninstall it before installing update...",Toast.LENGTH_LONG).show();
									String url = "https://djaman-motihari.business.site/";
									try {
										url = result.getString("apkUrl");
									} catch (JSONException e) {}
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setData(Uri.parse(url)); 
									context.startActivity(i); 
								}

							});
						cancel.setOnClickListener(new View.OnClickListener(){

								@Override
								public void onClick(View p1) {
									dialog.dismiss();
								}
								
							
						});
						dialog.show();

					}
					else
					{
						Toast.makeText(context,"No Update Found",Toast.LENGTH_LONG).show();
					}
				} catch (JSONException ex) {
					Log.e("App", "Failure", ex);
				}
			}
		}
	}
}
