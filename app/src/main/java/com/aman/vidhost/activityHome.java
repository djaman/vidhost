package com.aman.vidhost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.aman.vidhost.fragments.fragment_setting;
import com.aman.vidhost.fragments.fragment_home;
import android.support.v4.app.FragmentManager;
import com.aman.vidhost.fragments.fragment_download;
import com.aman.vidhost.classes.updateHandler;
import android.content.Intent;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class activityHome extends AppCompatActivity {
	final Fragment fragment1 = new fragment_home();
    final Fragment fragment2 = new fragment_download();
	
    final Fragment fragment3 = new fragment_setting();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

	int fragmentId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		updateHandler update = new updateHandler(this);
		
		if(getSupportActionBar() !=null){
			getSupportActionBar().hide();
		}
//		if (savedInstanceState != null) {
//			return;
//		}
		
			fragmentId = 1;
			fm.beginTransaction().add(R.id.content, fragment3, "3").hide(fragment3).commit();
			fm.beginTransaction().add(R.id.content, fragment2, "2").hide(fragment2).commit();
			fm.beginTransaction().add(R.id.content,fragment1, "1").commit();
			
		
		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					
					switch (item.getItemId()) {
						case R.id.home:
							if(fragmentId != 1){
								//Toast.makeText(getApplicationContext(),"new fragment set 1",Toast.LENGTH_LONG).show();
								fragmentId = 1;
								fm.beginTransaction().hide(active).show(fragment1).commit();
								active = fragment1;
							}
							break;
						case R.id.downloads:
							if(fragmentId != 2){
								//Toast.makeText(getApplicationContext(),"new fragment set 2",Toast.LENGTH_LONG).show();
								fragmentId = 2;
								fm.beginTransaction().hide(active).show(fragment2).commit();
								active = fragment2;
							}
							break;
						case R.id.settings:
							if(fragmentId != 3){
								//Toast.makeText(getApplicationContext(),"new fragment set 3",Toast.LENGTH_LONG).show();
								fragmentId = 3;
								fm.beginTransaction().hide(active).show(fragment3).commit();
								active = fragment3;
							}
							break;

					}
					
					return true;
				}
			});
	}

	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Close Vidhost?");
		alert.setCancelable(true);
		alert.setMessage("click YES to exit");
		alert.setNegativeButton("No", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					p1.dismiss();
				}
				
			
		});
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2) {
					
					finish();
				}
				
			
		});
		alert.show();
	}
	
    
}
