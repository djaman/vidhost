package com.aman.vidhost.fragments;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aman.vidhost.R;
import com.aman.vidhost.classes.okloaderClass;
import com.aman.vidhost.utils.networkUtils;
import com.github.ybq.android.spinkit.SpinKitView;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import android.graphics.Color;
import android.widget.Toast;
import android.content.Intent;
import com.aman.vidhost.player;
import android.util.Log;

public class BottomSheetFragment extends BottomSheetDialogFragment{

	private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
	networkUtils network;
	
    public static BottomSheetFragment newInstance(String image, String name, String hdlink,String sdlink) {
		BottomSheetFragment  obj = new BottomSheetFragment();
		Bundle args = new Bundle();
		args.putString("image", image);
		args.putString("title",name);
		args.putString("hdlink",hdlink);
		args.putString("sdlink",sdlink);
		obj.setArguments(args);
        return obj;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
		
	}

	
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottomsheet, container,
                false);
		//((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);

// Setting timeout globally for the download network requests:
		
		network = new networkUtils(getActivity());
        // get the views and attach the listener
		final String img = getArguments().getString("image");
		final String title = getArguments().getString("title");
		final String hdlink = getArguments().getString("hdlink");
		final String sdlink = getArguments().getString("sdlink");
		
		Log.d("hd",""+hdlink.getClass());
		Log.d("sd",""+sdlink.getClass());
		Log.d("hd",""+hdlink);
		Log.d("sd",""+sdlink);
		//setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
		
		ImageView imageview = (ImageView) view.findViewById(R.id.iv_logo);
		TextView titleview = (TextView) view.findViewById(R.id.title);
		TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
		RelativeLayout cl_sd = (RelativeLayout) view.findViewById(R.id.cl_sd);
		RelativeLayout cl_hd = (RelativeLayout) view.findViewById(R.id.cl_hd);
		RelativeLayout cl_play = (RelativeLayout) view.findViewById(R.id.cl_play);
		final TextView sd_size = (TextView) view.findViewById(R.id.sd_size);
		
		final TextView hd_size = (TextView) view.findViewById(R.id.hd_size);
		
		final SpinKitView spinHd = (SpinKitView) view.findViewById(R.id.hd_spin_kit);
		final SpinKitView spinSd = (SpinKitView) view.findViewById(R.id.sd_spin_kit);
		
		new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					
					
					//spinSd.setVisibility(View.GONE);
						//spinHd.setVisibility(View.GONE);
					if(!sdlink.equals("null")){
						//Getting SD Link
						Long sd = Long.valueOf(network.getSize(sdlink));
						sd_size.setText(network.readableFileSize(sd));
						
					}
					if(!hdlink.equals("null")){
						//Getting HD Link
						Long hd = Long.valueOf(network.getSize(hdlink));
						hd_size.setText(network.readableFileSize(hd));
						
					}
				}
			}, 500);
		
		imageview.setImageBitmap(network.getBitmapFromURL(img));
		
		if(!title.equals("1")){
			titleview.setText(title);
		}
		if(sdlink.equals("null")){
			Log.d("sdnull","true");
			//Disible Sd Button
			cl_sd.setVisibility(View.GONE);
			
		}
		if(hdlink.equals("null")){
			//Disible HD Button
			cl_hd.setVisibility(View.GONE);
		}
		cancel.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					dismiss();
				}
				
			
		});
		cl_play.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					if(!sdlink.equals("null")){
						Intent i = new Intent(getActivity(), player.class);   
						//String strName = null;
						i.putExtra("uri", sdlink);

						getActivity().startActivity(i);
					}else if(!hdlink.equals("null")){
						Intent i = new Intent(getActivity(), player.class);   
						//String strName = null;
						i.putExtra("uri", hdlink);

						getActivity().startActivity(i);
					}
					else{
						Toast.makeText(getActivity(),"No Link Found!",Toast.LENGTH_LONG).show();
					}
				}
				
			
		});
		cl_sd.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					String dirPath = android.os.Environment.getExternalStorageDirectory()+"/vidhost";
					/*downloadClass dl = new downloadClass(getActivity());
					dl.startDownload(img,sdlink,dirPath,title);*/
					okloaderClass okdl  =new okloaderClass(getActivity());
					okdl.startLoad(img,title,sdlink,"demo");
					}
		});
		
		//int notificationId = Unique_Integer_Number;
		cl_hd.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1) {
					
					String dirPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"downloads";
					okloaderClass okdl  =new okloaderClass(getActivity());
					okdl.startLoad(img,title,hdlink,"demo");
					/*downloadClass dl = new downloadClass(getActivity());
					dl.startDownload(img,hdlink,dirPath,title);*/
				}
				
			
		});
        return view;
		
    }
	
	;
	
}


