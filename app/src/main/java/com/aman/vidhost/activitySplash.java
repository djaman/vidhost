package com.aman.vidhost;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.aman.vidhost.R;
import com.aman.vidhost.utils.fileUtils;
import com.aman.vidhost.utils.networkUtils;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.Snackbar;

public class activitySplash extends AppCompatActivity {
    fileUtils filesystem;
	networkUtils networkmanager;
	AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		/*Init Classes*/
		filesystem = new fileUtils(getApplication());
		networkmanager = new networkUtils(getApplicationContext());
		/********/
	    /** hide action bar for splash screen**/
		if(getSupportActionBar() != null){
			getSupportActionBar().hide();
		}
		/********************
	     ********Works*******
		 *check internal storage permission*
		 *check internet connection*
		 *check autostart permission*
		 ********************/
		if(networkmanager.checkSelfInternetConnection())
		{
			//Internet Is Ok 
			//djaman.toast("internet ok","HIGH");
			//startactivity();
			permissionHandler();
		}
		else
		{
			showsnake("No Connection",true);
		}
    }
	private void startactivity()
	{
		// TODO: Implement this method
		final Intent send = new Intent(getApplicationContext(),activityHome.class);
		//startActivity(send);
		final Handler h = new Handler();

		h.postDelayed(new Runnable() {

				@Override

				public void run() {

					//Do something after 1s   

					startActivity(send);
					finish();
				}

			}, 1000);
	}

	private void showsnake(String p0, boolean p1)
	{
		// TODO: Implement this method
		Snackbar snake = Snackbar.make(findViewById(R.id.main),"No Connection!",Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry", new View.OnClickListener() {

                @Override
                public void onClick(View p1)
                {
                    if(networkmanager.checkSelfInternetConnection())
					{
						//Internet Is Back
						//djaman.toast("internet backed","HIGH");
						permissionHandler();
					}
					else
					{
						showsnake("Please Turn On Internet",true);
					}
                }


            });
		snake.show();
	}
	

	private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_storage_permission, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it 
        final AlertDialog alertDialog = builder.create();
		alertDialog.setCancelable(false);
		TextView txt = dialogView.findViewById(R.id.text_dialog);
		txt.setText("To download facebook or instagram videos! give access to storage");
        alertDialog.show();

		Button btn = dialogView.findViewById(R.id.btn_dialog);

		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					requestPermission();
					alertDialog.dismiss();
				}
			});
    }

	private boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(activitySplash.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (result == PackageManager.PERMISSION_GRANTED) {
			return true;
		} else {
			return false;
		}
	}

	private void requestPermission() {

		ActivityCompat.requestPermissions(activitySplash.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

	}
	public void permissionHandler()
	{
		if(!checkPermission())
		{
			showCustomDialog();
		}
		else
		{
			startactivity();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//Log.e("value", "Permission Granted, Now you can use local drive .");
					startactivity();
				} else {
					//Log.e("value", "Permission Denied, You cannot use local drive .");
					permissionHandler();
				}
				break;
		}
	}
}
