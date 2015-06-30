package com.zhch.threedcinema.database;

import com.zhch.threedcinema.po.VideoList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoListData {
	
	private OnlineVideoDbHelper vldbHelper;
//	private  String newname=null;
	
	public VideoListData(Context context){
		this.vldbHelper=new OnlineVideoDbHelper(context);
	}

 
//	public String setdbname(String name){
//		newname=name;
//		return newname;
//	}
    /** 
     * save a student to the database 
     */  
	public void save(VideoList vl) {  
        SQLiteDatabase database =vldbHelper.getWritableDatabase();  
        database.execSQL("insert into videolist(id,videoid,catid,title,thumb,description,inputtime,updatetime,diqu,fenlei,zimu,nianfen) values (?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{vl.getId(),vl.getVideoid(),vl.getCatid(),vl.getTitle(),
        					vl.getThumb(),vl.getDescription(),vl.getInputtime(),vl.getUpdatetime(),vl.getDiqu(),vl.getFenlei(),vl.getZimu(),vl.getNianfen()});  
    }  
    /** 
     * updata a student to the database 
     */  
    public void update(VideoList vl) {  
        SQLiteDatabase database = vldbHelper.getWritableDatabase();  
        database.execSQL("update videolist set videoid=?,catid=?,title=?,thumb=?,description=?,inputtime=?,updatetime=?,diqu=?,fenlei=?,zimu=?,nianfen=? where id=?", new Object[]{vl.getVideoid(),vl.getCatid(),vl.getTitle(),
				vl.getThumb(),vl.getDescription(),vl.getInputtime(),vl.getUpdatetime(),vl.getDiqu(),vl.getFenlei(),vl.getZimu(),vl.getNianfen(),vl.getId()}); 
    }  
    
    public void clean (){  
    	SQLiteDatabase database = vldbHelper.getWritableDatabase();  
    	database.execSQL("DROP TABLE IF EXISTS videolist");  
    	database.execSQL("create table videolist(id integer primary key autoincrement, videoid Integer(20),catid Integer(20), title varchar(255), thumb varchar(255),  description varchar(255), inputtime varchar(20), updatetime varchar(20), diqu varchar(200), fenlei varchar(20), zimu varchar(20), nianfen varchar(20)) ");
    	
    }  
    /** 
     * find a person from the database according to the id 
     */  
    public VideoList find(Integer id) {  
        SQLiteDatabase database = vldbHelper.getReadableDatabase();  
        Cursor cursor = database.rawQuery("select * from videolist where id=?", new String[]{id.toString()});  
        VideoList vl = null;  
        if(cursor.moveToFirst()) {  
            Integer vlid = cursor.getInt(cursor.getColumnIndex("id")); 
            Integer videoid =cursor.getInt(cursor.getColumnIndex("videoid")); 
            Integer catid = cursor.getInt(cursor.getColumnIndex("catid")); 
            Integer inputtime = cursor.getInt(cursor.getColumnIndex("inputtime")); 
            Integer updatetime= cursor.getInt(cursor.getColumnIndex("updatetime")); 
            String title=cursor.getString(cursor.getColumnIndex("title"));  
            String thumb=cursor.getString(cursor.getColumnIndex("thumb")); 
            String description=cursor.getString(cursor.getColumnIndex("description")); 
            String diqu=cursor.getString(cursor.getColumnIndex("diqu")); 
            String fenlei=cursor.getString(cursor.getColumnIndex("fenlei")); 
            String zimu=cursor.getString(cursor.getColumnIndex("zimu")); 
            String nianfen=cursor.getString(cursor.getColumnIndex("nianfen")); 
            
            
            vl = new VideoList();  
            vl.setId(vlid);
            vl.setVideoid(videoid);
            vl.setCatid(catid);
            vl.setTitle(title);
            vl.setThumb(thumb);
            vl.setDescription(description);
            vl.setInputtime(inputtime);
            vl.setUpdatetime(updatetime);
            vl.setDiqu(diqu);
            vl.setFenlei(fenlei);
            vl.setZimu(zimu);
            vl.setNianfen(nianfen);
            
            
        }  
        
        return vl; 
        
    }  
       
    /** 
     * 获取数据总数 
     * @return 
     */  
    public long getCount(){  
        SQLiteDatabase db=vldbHelper.getReadableDatabase();  
        Cursor cursor =db.rawQuery("select count(*) from videolist", null);  
        cursor.moveToFirst();  
        long reslut=cursor.getLong(0);  
        return reslut;  
    }
      
}