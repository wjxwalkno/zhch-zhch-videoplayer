package com.zhch.threedcinema.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zhch.threedcinema.adapter.GridViewAdapter;
import com.zhch.threedcinema.base.ImagesUrl;
import com.zhch.threedcinema.database.OnlineVideoDbHelper;
import com.zhch.threedcinema.database.VideoDetailData;
import com.zhch.threedcinema.database.VideoListData;
import com.zhch.threedcinema.po.VideoDetail;
import com.zhch.threedcinema.po.VideoList;
import com.zhch.threedcinema.transf.JSONParser;
import com.zhch.threedcinema.utils.ImageUtils;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class movie extends Activity implements Callback{

private GridView gridview;  
//url to download the data
private static String url_down="http://www.3dxsj.com/phpcms/plugin/api-test.php";
private static String url_down2="http://www.3dxsj.com/phpcms/plugin/api-test2.php";
private ProgressDialog pDialog=null;
//wjx the json is received
private String tablename="videodetail";
public  String[] Urls;
private String movieTitle="";
private JSONArray array=null;
JSONParser jsonParser = new JSONParser();
SimpleAdapter saImageItems;
//wjx creat the Sqlite
private OnlineVideoDbHelper StdbOpenHelper=null;
private VideoListData videolistdata=null;
private VideoDetailData videodetaildata=null;
static final String[] strdiQu={"����","�ڵ�","��̨","�պ�","����","Ӣ��","�¹�","����","ӡ��","���","Ų��","����","Խ��","����","�Ű�","ϣ��","����","�ݿ�","̩��","����","����","�Ϸ�","�����","����"};
static final String[] strfenLei={"����","����","ϲ��","����","�ƻ�","����","�ֲ�","����","ħ��","ս��","�ﰸ","���","����","����","��¼","����"};
static final Integer[] arraynianfen={0,1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015};
private GridViewAdapter mGridViewAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_activity);
		gridview = (GridView) findViewById(R.id.gridview); 
		//wjx creat the Sqlite
		StdbOpenHelper = new OnlineVideoDbHelper(movie.this);  		
		StdbOpenHelper.getWritableDatabase();  //��һ�ε��ø÷������������ݿ� 
		videolistdata=new VideoListData(movie.this);
		videodetaildata=new VideoDetailData(movie.this);
	 
		 videolistdata.clean();
		 videodetaildata.clean();				 
		 new Down().execute(); 
		
		  
//	        // ���ɶ�̬���飬����ת������  
//	        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
//	        for (int i = 0; i < 100; i++) {  
//	            HashMap<String, Object> map = new HashMap<String, Object>();  
//	            map.put("ItemImage", R.drawable.preview);// ���ͼ����Դ��ID  
//	          //  map.put("ItemTitle", movieTitle);
//	            lstImageItem.add(map);  
//	        }  
//	        // ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ  
//	        saImageItems = new SimpleAdapter(this, // ûʲô����  
//	                lstImageItem,// ������Դ  
//	                R.layout.item_webpicture,// night_item��XMLʵ��  
//	                // ��̬������ImageItem��Ӧ������  
//	                new String[] { "ItemImage" ,"ItemTitle"},  
//	                // ImageItem��XML�ļ������һ��ImageView,����TextView ID  
//	                new int[] { R.id.ItemImage ,R.id.ItemTitle});  
//	        // ��Ӳ�����ʾ  
//	        gridview.setAdapter(saImageItems);  
//	        // �����Ϣ����  
	        gridview.setOnItemClickListener(new ItemClickListener());  
    }  
	private void init(){
		
		mGridViewAdapter = new GridViewAdapter(movie.this, 0, Urls, gridview);
		gridview.setAdapter(mGridViewAdapter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mGridViewAdapter.cancelAllTasks();
	}


	/** 
	 * wjx Background Async Task to Load all data by making HTTP Request 
	 * */  
	class Down extends AsyncTask<String, String, String> {  

	     /** 
	      * Before starting background thread Show Progress Dialog 
	      * */  
	     @Override  
	     protected void onPreExecute() {  
	         super.onPreExecute();  
	         pDialog = new ProgressDialog(movie.this);  
	         pDialog.setMessage("��������������Դ������");  
	         pDialog.setIndeterminate(false);  
	         pDialog.setCancelable(true);  
	         pDialog.show();  
	     }  
	
	     /** 
	      * getting All students from url 
	      * */  
	     protected String doInBackground(String... args) {  
	         // Building Parameters  
	         List<NameValuePair> params = new ArrayList<NameValuePair>();  
	       
	         params.add(new BasicNameValuePair("videoList", tablename));
	    
	         // getting JSON string from URL  
	         JSONObject json = jsonParser.makeHttpRequest(url_down, "GET", params);  
	
	         // Check your log cat for JSON reponse  
	//         Log.d("All Products: ", json.toString());  
	
	         try {  
	             // Checking for SUCCESS TAG  
	         
	             int success = 1;  
	
	             VideoList videolist=new VideoList();
	             VideoDetail videodetail=new VideoDetail();
	             if (success == 1) {  
	               // looping through All Products  
	             ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
	             JSONArray jsonObj = json.getJSONArray("videoList"); 
	             Urls=new String[jsonObj.length()];
	               for (int i = 0; i < jsonObj.length(); i++) {  
	                   JSONObject videoObject = jsonObj.getJSONObject(i);  
	//
	//                 //  Integer vdid = student.getInt("id");
	                   Integer vlid=i;
	                   Integer vlvideoid=videoObject.getInt("id");
	                   String vltitle=videoObject.getString("title");
	                   Integer vlcatid=videoObject.getInt("catid");
	                   String vlthumb=videoObject.getString("thumb");
	                   Urls[i]=vlthumb;
	                //   System.out.println(Urls[i]);
	                   String vldescription=videoObject.getString("description");
	                   Integer vlinputtime=videoObject.getInt("inputtime");
	                   Integer vlupdatetime=videoObject.getInt("updatetime");
	                   String vldiqu=videoObject.getString("diqu");
	                   String vlfenlei=videoObject.getString("fenlei");
	                   String vlzimu=videoObject.getString("zimu");
	                   String vlnianfen=videoObject.getString("nianfen");
	              //     System.out.println(vltitle);
	
	                   videolist.setId(vlid);
	                   videolist.setVideoid(vlvideoid);
	                   videolist.setCatid(vlcatid);
	                   videolist.setTitle(vltitle);
	                   videolist.setThumb(vlthumb);
	                   videolist.setDescription(vldescription);
	                   videolist.setInputtime(vlinputtime);
	                   videolist.setUpdatetime(vlupdatetime);
	                   videolist.setDiqu(vldiqu);
	                   videolist.setFenlei(vlfenlei);
	                   videolist.setZimu(vlzimu);
	                   videolist.setNianfen(vlnianfen);
	                   
	                   
	
	                   videolistdata.save(videolist);
	                   
//	                   HashMap<String, Object> map = new HashMap<String, Object>();  
//	    	           // map.put("ItemImage", R.drawable.preview);// ���ͼ����Դ��ID  
//	    	           map.put("ItemTitle", vltitle);
//	    	           lstImageItem.add(map);  
	    	           
	              
	              }  
//	               // ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ  
//	   	          saImageItems = new SimpleAdapter(movie.this, // ûʲô����  
//	   	                lstImageItem,// ������Դ  
//	   	                R.layout.item_webpicture,// night_item��XMLʵ��  
//	   	                // ��̬������ImageItem��Ӧ������  
//	   	                new String[] { "ItemImage" ,"ItemTitle"},  
//	   	                // ImageItem��XML�ļ������һ��ImageView,����TextView ID  
//	   	                new int[] { R.id.ItemImage ,R.id.ItemTitle});  
//	               gridview.setAdapter(saImageItems);
	               
	               tablename="video";
	               params.add(new BasicNameValuePair("video", tablename));
	               
	               // getting JSON string from URL  
	               json = jsonParser.makeHttpRequest(url_down2, "GET", params);  
	               jsonObj = json.getJSONArray("video");  
	               
	//             // looping through All Products  
	             for (int i = 0; i < jsonObj.length(); i++) {  
	                 JSONObject videoObject = jsonObj.getJSONObject(i);  
	
	                 Integer vdid = videoObject.getInt("id");
	                 String zhuyan=videoObject.getString("zhuyan");  
	                 String daoyan =videoObject.getString("daoyan");  
	                 String yuyan =videoObject.getString("yuyan");  
	                 String content ="���ޣ�";   // student.getString("content");  
	                 String dydz1 =videoObject.getString("dydz1");  
	                 String dydz2 =videoObject.getString("dydz2");  
	                 String dydz3 = videoObject.getString("dydz3");  
	                 
	
	                 //����ѧԱ����Ϣ��ӵ��������ݿ���       
	                 videodetail.setId(vdid);
	                 videodetail.setZhuyan(zhuyan);
	                 videodetail.setDaoyan(daoyan);
	                 videodetail.setYuyan(yuyan);
	                 videodetail.setContent(content);
	                 videodetail.setDydz1(dydz1);
	                 videodetail.setDydz2(dydz2);
	                 videodetail.setDydz3(dydz3);
	 		   	    videodetaildata.save(videodetail); 
	
	              }  
	             }
	//             mGridViewAdapter = new GridViewAdapter(movie.this, 0, Urls, gridview);      
	//             gridview.setAdapter(mGridViewAdapter);
	             String message ="���سɹ�!";//json.getString(TAG_MESSAGE);//student1.getString("name");//json.getString(TAG_MESSAGE);
	             
	            // mAdapter.notifyDataSetChanged();
	             return message;
	         } catch (Exception e) {  
	             e.printStackTrace();  
	             return "����ʧ�ܣ�";  
	         }  
	//String message="0";
	//return  message;
	         
	     }  
	
	     /** 
	      * After completing background task Dismiss the progress dialog 
	      * **/  
	     protected void onPostExecute(String message) {  
	         // dismiss the dialog after getting all products  
	         pDialog.dismiss();  
	         // updating UI from Background Thread  
	         Toast.makeText(getApplicationContext(), message, 8000).show(); 
	         if(message.equals("���سɹ�!")){
	        	 init();
	         }
	        
	        
	     }  

 }  
 
	//����ͼƬ���زο�������Դhttps://code.csdn.net/snippets/158574
	
	    // ��AdapterView������(���������߼���)���򷵻ص�Item�����¼�  
	    class ItemClickListener implements OnItemClickListener {  
	    	
	        @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
	        	
	        	VideoList item =new VideoList();
	        	item=videolistdata.find(position);
	        	Intent intent_video;
				intent_video = new Intent(movie.this, VideoDetailActivity.class);
				VideoDetail vd1 = new VideoDetail();
				if(videodetaildata.find(item.getVideoid())==null){
					intent_video.putExtra("zhuyan", "����");
					intent_video.putExtra("yuyan", "����");
					intent_video.putExtra("daoyan", "����");
					
				}else{
					vd1=videodetaildata.find(item.getVideoid());	
					intent_video.putExtra("zhuyan", vd1.getZhuyan());
					intent_video.putExtra("yuyan", vd1.getYuyan());
					intent_video.putExtra("daoyan", vd1.getDaoyan());
					intent_video.putExtra("url", vd1.getDydz1());
				}	
				intent_video.putExtra("title",item.getTitle());
				intent_video.putExtra("thumbnail", item.getThumb());			
				intent_video.putExtra("inputtime",item.getInputtime());
				intent_video.putExtra("diqu",ReturnDiqu(item.getDiqu()));				
				intent_video.putExtra("content",item.getDescription());
				startActivity(intent_video);
				
			}

			
	    }  
	    
		public	String ReturnDiqu(String ID){
			int i = Integer.parseInt(ID);
			String Rdiqu=strdiQu[i];
			
			return Rdiqu;
			
			}
		public	String ReturnFenlei(String ID){
			int i = Integer.parseInt(ID);	
			String Rfenlie=strfenLei[i];
			return Rfenlie;		
			}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
