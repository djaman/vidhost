package com.aman.vidhost.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aman.vidhost.R;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.aman.vidhost.utils.systemUtils;
import com.aman.vidhost.utils.extractor;
import java.io.IOException;
import java.util.Map;
import android.app.ProgressDialog;
import android.util.Log;
import android.os.Handler;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Intent;


public class fragment_home extends Fragment {

    private View rootView;

    public fragment_home() {}
    
	Button downloadButton;
	Button pasteButton;
    EditText url;
	systemUtils system;
	extractor extractor;
	ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		
		
		if (rootView == null) {

			rootView = inflater.inflate(R.layout.fragment_home, container, false);

			// Initialise your layout here
			
			
			
		} else {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
		
		system = new systemUtils();
		extractor = new extractor(getActivity());
		Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);
		mTitle.setText(toolbar.getTitle());

		activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

		activity.getSupportActionBar().setElevation(3.0f);

		downloadButton  = (Button) rootView.findViewById(R.id.btndownload);
		pasteButton = (Button) rootView.findViewById(R.id.btnpaste);
		url = (EditText) rootView.findViewById(R.id.input);
		final Intent intent = getActivity().getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
			Log.println(Log.ASSERT,"shareablTextExtra",intent.getStringExtra("android.intent.extra.TEXT"));
			//handlervideo();
			//fragment_home fragment = new fragment_home();
			//((fragment_home) fragment).handlervideo(intent.getStringExtra("android.intent.extra.TEXT"));
			
			startProgress();
			//final String url  = getInputValue();
			new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
						handlervideo(intent.getStringExtra("android.intent.extra.TEXT"));

                    }
				}, 1000);
			
		}
		pasteButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View p1){
				ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
				final String charSequence = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
				if(!clipboard.equals("")){
					url.setText(charSequence);
					startProgress();
					new Handler().postDelayed(new Runnable(){

							@Override
							public void run() {
								handlervideo(charSequence);
							}
							
						
					},1000);
					
				}
			}
		});
		downloadButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					startProgress();
					final String url  = getInputValue();
					new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
						handlervideo(url);
						
                    }
                    }, 1000);
					
		//addPhotoBottomDialogFragment.setCancelable(false);
		
				}

				

			});

		
		return rootView;
		
    }
	private String getInputValue() {
		String link = url.getText().toString();
		return link;
	}
	
	public void handlervideo(String url){
		Log.d("url",""+url);
		if(system.isValidUrl(url) == "facebook"){
			Map data =  extractor.extractFacebook(url);
			String image = (String) data.get("image");

			String title = (String) data.get("title");
			
			String hdlink = (String) data.get("hdlink");
			
			String sdlink = (String) data.get("sdlink");
			
			
			if(image.equals("null")){
				image = "https://i.picsum.photos/id/384/536/354.jpg?hmac=MCKw0mm4RrI3IrF4QicN8divENQ0QthnQp9PVjCGblo";

			}
			if(title.equals("null")){
				title = "1";
			}
			if(!hdlink.equals("null") && !sdlink.equals("null")){
			BottomSheetFragment addPhotoBottomDialogFragment =
				BottomSheetFragment.newInstance(image,title,hdlink,sdlink);
			addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
											  "add_photo_dialog_fragment");
			
											  
		   addPhotoBottomDialogFragment.setCancelable(false);
		   
				//addPhotoBottomDialogFragment.setImage(image);
			//Toast.makeText(getActivity(), "Facebook", Toast.LENGTH_LONG).show();
			stopProgress();
			}
			else{
				stopProgress();
				Toast.makeText(getActivity(),"Video Not Found!",Toast.LENGTH_LONG).show();
			}
		}
		else if(system.isValidUrl(url) == "instagram"){
			Map data = extractor.extractInstagram(url);
			//Map data =  extractor.extractFacebook(url);
			String image = (String) data.get("image");

			String title = (String) data.get("title");
			
			String hdlink = (String) data.get("hdlink");
			
			String sdlink = (String) data.get("sdlink");
			
			
			if(image.equals("null")){
				image = "https://i.picsum.photos/id/384/536/354.jpg?hmac=MCKw0mm4RrI3IrF4QicN8divENQ0QthnQp9PVjCGblo";

			}
			if(title.equals("null")){
				title = "1";
			}
			
			if(!hdlink.equals("null")){
				BottomSheetFragment addPhotoBottomDialogFragment =
					BottomSheetFragment.newInstance(image,title,hdlink,sdlink);
				addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
												  "add_photo_dialog_fragment");


				addPhotoBottomDialogFragment.setCancelable(false);
				stopProgress();
			}
			else{
				
				Toast.makeText(getActivity(),"Video Not Found",Toast.LENGTH_LONG).show();
				stopProgress();
			}
			
			
			}
		else
		{
			if(system.isValidUrl(url) == null){
				Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_LONG).show();
				//hideProgress();
			stopProgress();
		}
			
		}
	}
	public ProgressDialog myprogress;
	public void startProgress()
	{
		Log.d("progress","Progress Started");
		myprogress = new ProgressDialog(getActivity());
		myprogress.setMessage("Cheking Url...");
		myprogress.setCancelable(false);
		myprogress.show();

	}

	public void stopProgress()
	{
		Log.d("progress","Progress Stop");
		if(myprogress.isShowing())
		{
			myprogress.dismiss();
		}

	}
	
	
}
