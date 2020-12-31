package com.aman.vidhost;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.net.Uri;

public class aboutActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("About Dev");
		
		ImageView twitter = findViewById(R.id.twitter);
		ImageView facebook = findViewById(R.id.facebook);
		ImageView github = findViewById(R.id.github);
		
		twitter.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					String url = "https://mobile.twitter.com/a_l_p9572";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
				
			
		});
		facebook.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					String url = "https://www.facebook.com/profile.php?id=100009615993554";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}


			});
		github.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					String url = "https://github.com/djaman";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}


			});
    }
    
}
