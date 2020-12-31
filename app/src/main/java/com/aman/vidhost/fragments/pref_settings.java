package com.aman.vidhost.fragments;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.aman.vidhost.R;

public class pref_settings extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(Bundle p1, String p2) {
		
		setPreferencesFromResource(R.xml.settings,p2);

	}
	
    
    
    
}
