package com.aman.vidhost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;
import android.net.Uri;
import android.app.ProgressDialog;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.media.MediaPlayer;
import android.content.Context;

public class player extends AppCompatActivity {
	VideoView vv;
	MediaController mc;
	ProgressDialog progDailog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
	    String newString;
		vv = (VideoView) findViewById(R.id.vv);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if(extras == null) {
				newString= null;
			} else {
				newString= extras.getString("uri");
				mc = new MediaController(this);
				play(newString);
			}
		} else {
			newString= (String) savedInstanceState.getSerializable("uri");
			mc = new MediaController(this);
			play(newString);
		}
	}
    private void play(String url)
	{
		//String vdouri = getIntent().getStringExtra("url");
		Uri uri = Uri.parse(url);
		vv.setVideoURI(uri);
		vv.setMediaController(mc);
		//vv.setZOrderOnTop(true);
		mc.setAnchorView(mc);
		
		vv.start();
		progDailog = ProgressDialog.show(this, "Please wait ...", "Connecting To Facebook...", true);

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					progDailog.dismiss();
				}
			});
		vv.setOnErrorListener(new MediaPlayer.OnErrorListener(){

				@Override
				public boolean onError(MediaPlayer p1, int p2, int p3) {
					finish();
					return false;
				
				}
				
			
		});
		//End of play
	}
    
    
}
