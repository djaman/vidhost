package com.aman.vidhost.adapter;

import android.arch.core.BuildConfig;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.aman.vidhost.R;
import com.aman.vidhost.player;
import com.aman.vidhost.utils.fileUtils;
import java.io.File;
import java.util.List;
import android.os.Build;
import android.content.ContentValues;
import android.provider.MediaStore;
import android.content.ContentResolver;
import com.aman.vidhost.utils.systemUtils;
import android.os.Environment;
import android.os.StrictMode;
import com.aman.vidhost.classes.databaseClass;
import java.util.ArrayList;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
	
    Context context;
	ArrayList<String> id;
	ArrayList<String> name;
	ArrayList<String> img;
	ArrayList<String> sitename;
    databaseClass database;
	public MyAdapter(ArrayList<String>  id,ArrayList<String> name, ArrayList<String> img, ArrayList<String> sitename) {
		this.id = id;
		this.name = name;
		this.img = img;
		this.sitename = sitename;
		
	}
	
	

	
	@Override
	public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		
		View view = inflater.inflate(R.layout.cardViewDesign,parent,false);
		context = parent.getContext();
		database = new databaseClass(context);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
		
		holder.textView.setText(name.get(position));
		String filePath = "/sdcard/vidhost/.cache/thumb/"+name.get(position);
		fileUtils fileutils = new fileUtils(context);
		final String vidpath = "/sdcard/vidhost/"+name.get(position)+".mp4";
		
		if(fileutils.isExistFile(filePath))
		{
			Bitmap bmp = BitmapFactory.decodeFile(filePath);
			//ImageView img;
			holder.img.setImageBitmap(bmp);
		}
		else
		{
			holder.img.setImageResource(R.drawable.ic_main);
		}

		
		holder.share.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					File f = new File(vidpath);
					//Uri  uri = Uri.fromFile(new File(vidpath));
//				systemUtils utils = new systemUtils();
//					File file = new File(Environment.getExternalStorageDirectory(),"VidHost/"+name[position]+".mp4");
//				utils.shareVideo(context,file.getPath());
					StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
					StrictMode.setVmPolicy(builder.build());
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("video/mp4");
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(vidpath)));
					context.startActivity(Intent.createChooser(intent, "share"));
        	}
				
			
		});
		holder.play.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
				}
				
			
		});
		holder.delete.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					
					deleteAlert();
				}

				private void deleteAlert() {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setMessage("Are you sure you want delete it");
					alert.setTitle("Delete alert");
					alert.setCancelable(false);
					
					alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2) {
								database.deleteData(id.get(position));
								systemUtils utils = new systemUtils();
								utils.deleteFromExternalStorage("/vidhost/"+name.get(position)+".mp4");

								name.remove(position);
								id.remove(position);
								img.remove(position);
								sitename.remove(position);

								notifyItemRemoved(position);
							}
							
						
					});
					alert.setNegativeButton("no", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2) {
							     p1.cancel();
							}
							
						
					});
					alert.show();
				}
				
			
		});
		holder.textView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					
					Toast.makeText(context,name.get(position),Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vidpath));
//					intent.setDataAndType(Uri.parse(vidpath), "video/mp4");
//					context.startActivity(intent);
					Intent i = new Intent(context, player.class);   
					//String strName = null;
					i.putExtra("uri", vidpath);
					
					context.startActivity(i);
				}
				
		
		});
		
	}

	@Override
	public int getItemCount() {
		
		return name.size();
	}
	
    
	public static int length(List<String> list) {
		if (list.size() == 0) {
			return 0;
		}
		return list.get(0).length() + length(list.subList(1, list.size()));
	}
    

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
	public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
		public ImageView img;
		public ImageView share;
		public ImageView play;
		public ImageView delete;
        public MyViewHolder(@Nullable View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt_employee_name);
			
			img = (ImageView) itemView.findViewById(R.id.vimg);
			
			share  = (ImageView) itemView.findViewById(R.id.share);
			
			play = (ImageView) itemView.findViewById(R.id.play);
			
			delete = (ImageView) itemView.findViewById(R.id.delete);
			
        }
    }
	
}
