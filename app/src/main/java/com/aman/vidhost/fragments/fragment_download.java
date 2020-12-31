package com.aman.vidhost.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aman.vidhost.R;
import java.io.File;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import com.aman.vidhost.adapter.MyAdapter;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import com.aman.vidhost.classes.databaseClass;
import android.support.v4.widget.SwipeRefreshLayout;

public class fragment_download extends Fragment{
    
    
    private View rootView;
	private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
	SwipeRefreshLayout swipe;
	databaseClass db;
	String path = Environment.getExternalStorageDirectory().toString()+"/vidhost"; 
	//private List<String> myList;
    public fragment_download() {}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

		
		if (rootView == null) {

			rootView = inflater.inflate(R.layout.fragment_download, container, false);

			// Initialise your layout here

		} else {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
		db = new databaseClass(getActivity());
		
		Cursor res=db.getAllData(); 
		final int size = res.getCount();
//		String[] id = new String[size];
//		String[] name = new String[size];
//		String[] thumb = new String[size];
//		String[] sitename = new String[size];
		ArrayList id = new ArrayList<String>();
		ArrayList name = new ArrayList<String>();
		ArrayList thumb = new ArrayList<String>();
		ArrayList sitename = new ArrayList<String>();
		if (res.moveToFirst()) {
			int i = 0;
			do {
//		    buffer.append("Id:" + res.getString(0) + "\n"); 
//			buffer.append("Name :" + res.getString(1) + "\n\n"); 
//     		buffer.append("Surname :" + res.getString(2) + "\n\n"); 
//      		buffer.append("Marks :" + res.getString(3) + "\n\n"); 
//				id[i] = res.getString(0);
//				name[i] = res.getString(1);
//				thumb[i] = res.getString(2);
//				sitename[i] = res.getString(3);
				id.add(i,res.getString(0));
				name.add(i,res.getString(1));
				thumb.add(i,res.getString(2));
				sitename.add(i,res.getString(3));
				i++;
			} while (res.moveToNext());
		}
		//showMessage("Data", buffer.toString());

//	
//		File file = new File(Environment.getExternalStorageDirectory(), "Pictures");
//		File[] pictures = file.listFiles();
//		...
//		for (...)
//		{
//			log.e("FILE:", pictures[i].getAbsolutePath());
//		}
		
		recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
		swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
		
		recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

		
//		String[] mydataset = {"aman","goulu","vipul","aman","aman","goulu","aman","goulu","vipul","aman","aman","goulu"};
//        // specify an adapter (see also next example)
//		myList = new ArrayList<String>();   
//
//		String root_sd = Environment.getExternalStorageDirectory().toString();
//		file = new File( root_sd + "/Vidhost" ) ;       
//		File list[] = file.listFiles();
//
//		for( int i=0; i< list.length; i++)
//		{
//			myList.add( list[i].getName() );
//		}
		
        mAdapter = new MyAdapter(id,name,thumb,sitename);
        recyclerView.setAdapter(mAdapter);
		
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh() {
					
					Cursor res=db.getAllData(); 
					final int size = res.getCount();
					ArrayList id = new ArrayList<String>();
		ArrayList name = new ArrayList<String>();
		ArrayList thumb = new ArrayList<String>();
		ArrayList sitename = new ArrayList<String>();
		if (res.moveToFirst()) {
			int i = 0;
			do {
//		    buffer.append("Id:" + res.getString(0) + "\n"); 
//			buffer.append("Name :" + res.getString(1) + "\n\n"); 
//     		buffer.append("Surname :" + res.getString(2) + "\n\n"); 
//      		buffer.append("Marks :" + res.getString(3) + "\n\n"); 
//				id[i] = res.getString(0);
//				name[i] = res.getString(1);
//				thumb[i] = res.getString(2);
//				sitename[i] = res.getString(3);
				id.add(i,res.getString(0));
				name.add(i,res.getString(1));
				thumb.add(i,res.getString(2));
				sitename.add(i,res.getString(3));
				i++;
			} while (res.moveToNext());
					}
					mAdapter = new MyAdapter(id,name,thumb,sitename);
					recyclerView.setAdapter(mAdapter);
					
					swipe.setRefreshing(false);
				}
				
			
		});
		
		return rootView;
    }

}
