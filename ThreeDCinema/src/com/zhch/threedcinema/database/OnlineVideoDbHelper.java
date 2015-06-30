package com.zhch.threedcinema.database;

import java.sql.SQLException;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class OnlineVideoDbHelper extends SQLiteOpenHelper{

	private static final int VERSION=1;
	private static final String DATABASENAME="3dcinema.db";
	private static OnlineVideoDbHelper mInstance = null;
		
	public OnlineVideoDbHelper(Context context,String name,CursorFactory factory,int version){
		
		super(context,name,factory,version);
	}
	public OnlineVideoDbHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public OnlineVideoDbHelper(Context context,String name,int version){
		this(context,name,null,version);
	}
	public OnlineVideoDbHelper(Context context){
		this(context,DATABASENAME,null,VERSION);
	}
	   
	@Override
	public void onCreate(SQLiteDatabase db) { 
		// TODO Auto-generated method stub
		
			db.execSQL("create table videolist(id integer primary key autoincrement,videoid Integer(20), catid Integer(20), title varchar(255), thumb varchar(255),  description varchar(255), inputtime varchar(20), updatetime varchar(20), diqu varchar(200), fenlei varchar(20), zimu varchar(20), nianfen varchar(20)) ");
			db.execSQL("create table videodetail(id integer primary key autoincrement,videoid Integer(20), zhuyan varchar(200), daoyan varchar(200), yuyan varchar(200), content varchar(200), dydz1 varchar(200), dydz2 varchar(20), dydz3 varchar(200)) ");
		
		
	//	db.execSQL("create table fstudentdata(studentid integer primary key autoincrement, tablename varchar(20), name varchar(20), classes Integer(20), holidays Integer(20),  pay Integer(20), signok varchar(20), allclasses Integer(20), datachange varchar(20), signtime varchar(2000)) ");
		
	} 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
