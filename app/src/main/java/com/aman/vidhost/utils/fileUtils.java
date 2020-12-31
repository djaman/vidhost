package com.aman.vidhost.utils;
import android.content.Context;
import java.io.File;

public class fileUtils {
	Context context;
    public fileUtils(Context context){
		this.context = context;
	}
	public boolean checkSelfStoragePermission(){
		
		return false;
	}
	public static boolean isExistFile(String path) {
		File file = new File(path);
		return file.exists();
	}
}
