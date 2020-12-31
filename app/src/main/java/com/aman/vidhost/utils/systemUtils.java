package com.aman.vidhost.utils;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.aman.vidhost.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import android.util.Log;

public class systemUtils {
    
	public String isValidUrl(String url){
		String current = null;
		String regexFB= "(?:https?:\\/\\/)?(?:www\\.)?(?:facebook|fb|m\\.facebook)\\.(?:com|me).*";
		String regexIS = "(?:https?:\\/\\/)?(?:www\\.)?(?:instagram|m\\.instagram)\\.(?:com|me).*";
		
		Pattern patternFB = Pattern.compile(regexFB);
		Matcher matcherFB = patternFB.matcher(url);
		
		if(matcherFB.find()){
			current = "facebook";
		}

	   Pattern patternIS = Pattern.compile(regexIS);
	   Matcher matcherIS = patternIS.matcher(url);
	   
	   if(matcherIS.find()){
		   current = "instagram";
	   }
	   return current;
	}
	public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No App Found", Toast.LENGTH_LONG).show();
        }
    }
	public void deleteFromExternalStorage(String fileName) {
		String fullPath = "/mnt/sdcard/";
		try
		{
			File file = new File(fullPath, fileName);
			if(file.exists())
				file.delete();
		}
		catch (Exception e)
		{
			Log.e("App", "Exception while deleting file " + e.getMessage());
		}
	}
}
