package com.aman.vidhost.fragments;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aman.vidhost.R;
import com.aman.vidhost.classes.updateHandler;
import com.aman.vidhost.utils.networkUtils;
import android.os.Handler;
import com.aman.vidhost.aboutActivity;
import android.webkit.WebSettings;


public class fragment_setting extends Fragment{
    ProgressDialog pd;
    private View rootView;
	networkUtils network;
    public fragment_setting() {}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

		if (rootView == null) {

			rootView = inflater.inflate(R.layout.fragment_setting, container, false);

			// Initialise your layout here

			
		} else {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}

		
//		getActivity().getSupportFragmentManager()
//			.beginTransaction()
//			.replace(R.id.pref_setting, new pref_settings())
//			.commit();

		
		RelativeLayout rate  = (RelativeLayout) rootView.findViewById(R.id.rate);
		RelativeLayout feedback  = (RelativeLayout) rootView.findViewById(R.id.feedback);
		RelativeLayout privacy = (RelativeLayout) rootView.findViewById(R.id.privacy);
		RelativeLayout about = (RelativeLayout) rootView.findViewById(R.id.about);
		RelativeLayout update = (RelativeLayout) rootView.findViewById(R.id.update);
		about.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					startActivity(new Intent(getActivity(),aboutActivity.class));
				}
				
			
		});
		update.setOnClickListener(new View.OnClickListener(){
				
				@Override
				public void onClick(View p1) {
					network = new networkUtils(getActivity());
					pd = new ProgressDialog(getActivity());
					pd.setMessage("Please wait");
					pd.setCancelable(false);
					pd.show();
					
					new Handler().postDelayed(new Runnable(){

							@Override
							public void run() {
								updateHandler update = new updateHandler(getActivity(),pd);
								
							}
						}, 1000);
					
					
					
				}
				
			
		});
		
		feedback.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					
					String deviceInfo="Device Info:";
					deviceInfo += "\n Android Version: " +android.os.Build.VERSION.CODENAME;
					deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
					deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
					deviceInfo += "\n Device: " + android.os.Build.DEVICE;
					deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
					Intent intent = new Intent(Intent.ACTION_SEND);
					String[] recipients = {"webmasterdjaman@gmail.com"};//Add multiple recipients here
					intent.putExtra(Intent.EXTRA_EMAIL, recipients);
					intent.putExtra(Intent.EXTRA_SUBJECT, "VidHost Feedback"); //Add Mail Subject
					intent.putExtra(Intent.EXTRA_TEXT, deviceInfo+"\n");//Add mail body
					//intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");//Add CC emailid's if any
					//intent.putExtra(Intent.EXTRA_BCC, "mailbcc@gmail.com");//Add BCC email id if any
					intent.setType("text/html");
					intent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
					startActivity(Intent.createChooser(intent, "Send mail"));
				}
				
			
		});
		
		privacy.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()); 
					//alert.setTitle("Title here");

					WebView wv = new WebView(getActivity());
					WebSettings setting = wv.getSettings();
					setting.setJavaScriptEnabled(true);
					wv.loadUrl("https://sites.google.com/view/vidhost-policy/home");
					wv.setWebViewClient(new WebViewClient() {
							@Override
							public boolean shouldOverrideUrlLoading(WebView view, String url) {
								view.loadUrl(url);

								return true;
							}
						});

					alert.setView(wv);
					alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
					alert.show();
				}
				
			
		});
		rate.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					this.showRatingBox();
				}

				private void showRatingBox() {
					final Dialog dialog = new Dialog(getActivity());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setCancelable(true);
					dialog.setContentView(R.layout.rating_box);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

					TextView submit  = (TextView) dialog.findViewById(R.id.submit);
					
					submit.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View p1) {
								Toast.makeText(getActivity(),"Thanks For Rating...",Toast.LENGTH_LONG).show();
								dialog.dismiss();
							}
							
					});
					dialog.show();
				}
//			
		});
		
		return rootView;
		
		
    }
    
	
	
	
	}

