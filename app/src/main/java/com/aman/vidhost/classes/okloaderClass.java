package com.aman.vidhost.classes;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.aman.vidhost.R;
import com.aman.vidhost.utils.networkUtils;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;
import java.util.Date;
import android.widget.Toast;
import android.os.Handler;
import android.util.Log;
import java.io.File;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.FileOutputStream;

public class okloaderClass {
	Context context;
	private DownloadManager downloadManager;
	NotificationManager notificationManager;
	NotificationCompat.Builder mBuilder;
	databaseClass db;
	networkUtils network;
    public okloaderClass(Context context){
		this.context = context;
		downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
		notificationManager  = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		db= new databaseClass(context);  
	}
	public void startLoad(final String img,final String filename, String url,String parentFile){
		
		final int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
		String channelId = "channel-01";
		String channelName = "Download SD Quality";
		int importance = NotificationManager.IMPORTANCE_HIGH;
		final String saveFilename = filename.replaceAll("[\\\\/:*?\"<>|]","");
		final DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url)
			.setPath("/sdcard/vidhost/"+saveFilename+".mp4")
			.build();
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationChannel mChannel = new NotificationChannel(
				channelId, channelName, importance);
			notificationManager.createNotificationChannel(mChannel);
		}

		mBuilder = new NotificationCompat.Builder(context, channelId)
			.setSmallIcon(R.drawable.ic_download)
			.setContentTitle(filename)
			.setProgress(100,0,false)
			.setLargeIcon(network.getBitmapFromURL(img))
			.setContentText("djaman motihari");

		
//set download callback.
		downloadInfo.setDownloadListener(new DownloadListener() {

				@Override
				public void onStart() {
					notificationManager.notify(notificationId, mBuilder.build());

					
				}

				@Override
				public void onWaited() {
					mBuilder.setContentText("Preaparing Download").setProgress(0,0,false);
					notificationManager.notify(notificationId,mBuilder.build());
					
				}

				@Override
				public void onPaused() {
				}

				@Override
				public void onDownloading(long progress, long size) {
					
					long progressPercent = progress * 100 / size;
					Log.d("percent",""+progressPercent);
					mBuilder.setProgress(100,(int) progressPercent,false);
					notificationManager.notify(notificationId, mBuilder.build());

					
				}

				@Override
				public void onRemoved() {
				}

				@Override
				public void onDownloadSuccess() {
					
					
					createDirectoryAndSaveFile(network.getBitmapFromURL(img),saveFilename);
					boolean isInserted=db.insterData(saveFilename, "vidhost/.cache/thumb/"+saveFilename,
					
					"null"); 
					if (isInserted == true) {
						//Toast.makeText(context, "Data Inserted", Toast.LENGTH_LONG).show(); 
					}
					else {

						Toast.makeText(context, "Data could not be Inserted", Toast.LENGTH_LONG).show(); 
					} 
					Toast.makeText(context,"Download Complete",Toast.LENGTH_LONG).show();
					new Handler().postDelayed(new Runnable(){

							@Override
							public void run() {
								mBuilder.setContentText("Download Complete").setProgress(0,0,false);
								notificationManager.notify(notificationId,mBuilder.build());
							}
						}, 1000);
				}

				@Override
				public void onDownloadFailed(DownloadException p1) {
					Toast.makeText(context,"Download Error",Toast.LENGTH_SHORT).show();
					mBuilder.setContentText("Faild To Download! Try again").setProgress(0,0,false);
					notificationManager.notify(notificationId,mBuilder.build());
					
					
				}
				

				
			});

//submit download info to download manager.
		downloadManager.download(downloadInfo);
	}
	
	private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

		File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

		if (!direct.exists()) {
			File wallpaperDirectory = new File("/sdcard/vidhost/.cache/thumb/");
			wallpaperDirectory.mkdirs();
		}

		File file = new File("/sdcard/vidhost/.cache/thumb/", fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
