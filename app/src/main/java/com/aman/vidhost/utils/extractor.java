package com.aman.vidhost.utils;
import android.content.Context;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import android.os.StrictMode;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.content.ClipData;
import android.content.ClipboardManager;
import java.net.URLConnection;
import java.io.InputStream;
import android.widget.Toast;
import java.io.File;
import android.os.Environment;
import java.io.FileWriter;
import java.util.Map;
import java.util.HashMap;

public class extractor {
    Context context;
	
	public extractor(Context context){
		this.context = context;
	}
	private static final String USER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.47 Safari/537.36";
    public Map extractFacebook(String url) {
		try {
			String html = getSourceFormUrl(url);
			Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
			String sdvideo = "null";
			String hdvideo = "null";
			String title = "null";
			String image = "null";
			//Sd Link 
			Pattern sdlink = Pattern.compile("sd_src\\:\\\"([^\\\"]+)\\\"");
			Pattern sdlink_noRateLimit = Pattern.compile("sd_src_no_ratelimit\\:\\\"([^\\\"]+)\\\"");
			
			Matcher sdlink_match = sdlink.matcher(html);
			Matcher sdlink_noRateLimit_matcher = sdlink_noRateLimit.matcher(html);
			
			if(sdlink_match.find()){
				//Toast.makeText(context, "Link => "+sdlink_match.group(1), Toast.LENGTH_LONG).show();
				Log.d("url",sdlink_match.group());
				sdvideo = sdlink_match.group(1);
			}
			else if(sdlink_noRateLimit_matcher.find()) {
				//Toast.makeText(context,"Faild",Toast.LENGTH_SHORT).show();
				Log.d("sd_no_ratemilit",sdlink_noRateLimit_matcher.group());
				sdvideo = sdlink_noRateLimit_matcher.group(1);
			}
			else 
			{
				sdvideo = "null";
			}
			
			
			//Hd Link
			Pattern hdlink = Pattern.compile("hd_src\\:\\\"([^\\\"]+)\\\"");
			Pattern hdlink_noRateLimit = Pattern.compile("hd_src_no_ratelimit\\:\\\"([^\\\"]+)\\\"");
			
			Matcher hdlink_matcher = hdlink.matcher(html);
			Matcher hdlink_noRateLimit_matcher = hdlink_noRateLimit.matcher(html);
			
			if(hdlink_matcher.find()){
				Log.d("HdLink",hdlink_matcher.group(1));
				hdvideo = hdlink_matcher.group(1);
			}
			else if(hdlink_noRateLimit_matcher.find()){
				Log.d("hdlink_norateLimit",hdlink_noRateLimit_matcher.group(1));
				hdvideo = hdlink_noRateLimit_matcher.group(1);
			}
			else {
				hdvideo = "null";
			}
			
			Pattern metaTAGTitle = Pattern.compile("<title id=\\\"pageTitle\\\">([^\\\"]+)</title>");
			Matcher metaTAGTitleMatcher = metaTAGTitle.matcher(html);

			
			if(metaTAGTitleMatcher.find())
			{
			
				title = metaTAGTitleMatcher.group(1);
			}
			else
			{
				title = "N/A";
			}

			Pattern img  = Pattern.compile("<meta property=\\\"og\\:image\\\" content=\\\"([^\\\"]+)\" />");
			Matcher img_matcher = img.matcher(html);
			
			if(img_matcher.find()){
				String imgbit  = img_matcher.group(1);
				image = imgbit.replace("amp;","");
				
			}
			else
			{
				image = "null";
			}
			generateNoteOnSD(context,"title.txt","Hd=>"+title+" Sd=>"+image+"");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("sdlink", sdvideo);
			map.put("hdlink", hdvideo);
			map.put("title",title);
			map.put("image",image);
// etc

			map.get("name"); // returns "demo"
			return map;
			
		} catch (IOException e) {
			return null;
		}
		
	}
	public Map extractInstagram(String url){
		try {
			String html = getSourceFormUrl(url);
			String sdvideo = "null";
			String hdvideo = "null";
			String title = "null";
			String image = "null";

			String pattern = "<meta property=\\\"og:video\\\"\\ content=\\\"([^\\\"]+)\\\" \\/>";

			String name = "<title>(.*?)</title>";

			String img = "<meta property=\\\"og:image\\\"\\ content=\\\"([^\\\"]+)\\\" \\/>";

			Pattern link_pattern= Pattern.compile(pattern);

			Matcher link_matcher = link_pattern.matcher(html);
			
			if(link_matcher.find()){
				Log.d("matched","true =>"+link_matcher.group(1));
				hdvideo = link_matcher.group(1);
			}
			Pattern name_pattern  = Pattern.compile(name);
			
			Matcher name_matcher  = name_pattern.matcher(html);
			
			if(name_matcher.find()){
				Log.d("matched","true =>"+name_matcher.group(1));
				title  = name_matcher.group(1);
			}
			
			Pattern img_pattern = Pattern.compile(img);
			
			Matcher img_matcher = img_pattern.matcher(html);
			
			if(img_matcher.find()){
				Log.d("matched","true =>"+img_matcher.group(1));
				image = img_matcher.group(1);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("sdlink", "null");
			map.put("hdlink", hdvideo);
			map.put("title",title);
			map.put("image",image);
			
			return map;
		} catch (IOException e) {
			return null;
		}
		
		
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
	public void generateNoteOnSD(Context context, String sFileName, String sBody) {
    try {
        File root = new File(Environment.getExternalStorageDirectory(), "vidhost/.cache/log/");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, sFileName);
        FileWriter writer = new FileWriter(gpxfile);
        writer.append(sBody);
        writer.flush();
        writer.close();
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
