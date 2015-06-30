package com.zhch.threedcinema.database;

import com.zhch.threedcinema.po.VideoDetail;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoDetailData  {
	
	private OnlineVideoDbHelper stdbOpenHelper;
//	private  String newname=null;
	
	public VideoDetailData(Context context){
		
		this.stdbOpenHelper=new OnlineVideoDbHelper(context);
	}

 
//	public String setdbname(String name){
//		newname=name;
//		return newname;
//	}
    /** 
     * save a student to the database 
     */  
    public void save(VideoDetail VideoDetail) {  
    	
        SQLiteDatabase database =stdbOpenHelper.getWritableDatabase();  
        database.execSQL("insert into videodetail(id,zhuyan,daoyan,yuyan,content,dydz1,dydz2,dydz3) values (?,?,?,?,?,?,?,?)", new Object[]{VideoDetail.getId(),VideoDetail.getZhuyan(),
        		VideoDetail.getDaoyan(),VideoDetail.getYuyan(),VideoDetail.getContent(),VideoDetail.getDydz1(),VideoDetail.getDydz2(),VideoDetail.getDydz3()});  
    }  
      
  
    /** 
     * updata a student to the database 
     */  
    public void update(VideoDetail VideoDetail) {  
        SQLiteDatabase database = stdbOpenHelper.getWritableDatabase();  
        database.execSQL("update videodetail set zhuyan=?,daoyan=?,yuyan=?,content=?,dydz1=?,dydz2=?,dydz3 where id=?", new Object[]{VideoDetail.getZhuyan(),
        		VideoDetail.getDaoyan(),VideoDetail.getYuyan(),VideoDetail.getContent(),VideoDetail.getDydz1(),VideoDetail.getDydz2(),VideoDetail.getDydz3(),VideoDetail.getId()});  
    }  
          
//    /** 
//     * delete a person from the database according to the id 
//     */  
//    public void delete(Integer id) {  
//        SQLiteDatabase database = stdbOpenHelper.getWritableDatabase();  
//        database.execSQL("delete from videodetail where studentid=?", new Object[]{id.toString()});  
//    }  
//    /** 
//     * 清空表中的数据 
//     */  
//    public void clean (){  
//    	SQLiteDatabase database = stdbOpenHelper.getWritableDatabase();
//    	database.execSQL("DROP TABLE IF EXISTS videodetail");  
//    	
//    }  
    public void clean (){  
    	SQLiteDatabase database = stdbOpenHelper.getWritableDatabase();  
    	database.execSQL("DROP TABLE IF EXISTS videodetail");  
    	database.execSQL("create table videodetail(id integer primary key autoincrement, zhuyan varchar(200), daoyan varchar(200), yuyan varchar(200), content varchar(200), dydz1 varchar(200), dydz2 varchar(20), dydz3 varchar(200)) ");

    }   
    /** 
     * find a person from the database according to the id 
     */  
    public VideoDetail find(Integer id) {  
        SQLiteDatabase database = stdbOpenHelper.getReadableDatabase();  
        Cursor cursor = database.rawQuery("select * from videodetail where id=?", new String[]{id.toString()});  
        VideoDetail VideoDetail = null;  
        if(cursor.moveToFirst()) {  
            Integer vdid = cursor.getInt(cursor.getColumnIndex("id"));  
            String zhuyan=cursor.getString(cursor.getColumnIndex("zhuyan"));  
            String daoyan = cursor.getString(cursor.getColumnIndex("daoyan"));  
            String yuyan = cursor.getString(cursor.getColumnIndex("yuyan"));  
            String content = cursor.getString(cursor.getColumnIndex("content"));  
            String dydz1 = cursor.getString(cursor.getColumnIndex("dydz1"));  
            String dydz2 = cursor.getString(cursor.getColumnIndex("dydz2"));  
            String dydz3 = cursor.getString(cursor.getColumnIndex("dydz3"));  
            VideoDetail = new VideoDetail(); 
            VideoDetail.setId(vdid);
            VideoDetail.setZhuyan(zhuyan);
            VideoDetail.setDaoyan(daoyan);
            VideoDetail.setYuyan(yuyan);
            VideoDetail.setContent(content);
            VideoDetail.setDydz1(dydz1);
            VideoDetail.setDydz2(dydz2);
            VideoDetail.setDydz3(dydz3);
  
            
        }  
        
        return VideoDetail; 
        
    }  
    
    
    /** 
     * 获取数据总数 
     * @return 
     */  
    public long getCount(){  
        SQLiteDatabase db=stdbOpenHelper.getReadableDatabase();  
        Cursor cursor =db.rawQuery("select count(*) from videodetail", null);  
        cursor.moveToFirst();  
        long reslut=cursor.getLong(0);  
        return reslut;  
    }
      
}
