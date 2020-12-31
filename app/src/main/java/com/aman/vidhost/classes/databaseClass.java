package com.aman.vidhost.classes;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import java.time.temporal.ValueRange;
import android.database.Cursor;

public class databaseClass extends SQLiteOpenHelper
{

	public final static String DATABASE_NAME = "Download.db";    
	public final static String TABLE_NAME = "download_table"; 
	public static final String COL_1 = "ID"; 
	public static final String COL_2 = "NAME"; 
	public static final String COL_3 = "THUMB"; 
	public static final String COL_4 = "SITENAME";

	public databaseClass(Context context)
	{
		super(context,DATABASE_NAME,null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,THUMB TEXT,SITENAME TEXT)"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); 
		onCreate(db); 
	}

	public Boolean insterData(String name, String thumb,String sitename)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(COL_2,name);
		value.put(COL_3,thumb);
		value.put(COL_4,sitename);
		long result = db.insert(TABLE_NAME,null,value);
		if(result == -1){
			return false;
		}
		else
		{
			return true;
		}
	}
	public Cursor getData(String id)
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String query="SELECT * FROM " + TABLE_NAME + " WHERE ID='" + id + "'"; 
		Cursor  cursor = db.rawQuery(query, null); 
		return cursor; 
	} 

	public boolean updateData(String id, String name, String surname, String marks)
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues contentValues = new ContentValues(); 
		contentValues.put(COL_1, id); 
		contentValues.put(COL_2, name); 
		contentValues.put(COL_3, surname); 
		contentValues.put(COL_4, marks); 
		db.update(TABLE_NAME, contentValues, "ID=?", new String[]{id}); 
		return true; 
	} 
	public Integer deleteData(String id)
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		return db.delete(TABLE_NAME, "ID = ?", new String[]{id}); 
	} 

	public Cursor getAllData()
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null); 
		return res; 
	} 

}

