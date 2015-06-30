package com.zhch.threedcinema.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhch.threedcinema.ui.movie.ItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class yanshi extends Activity implements Callback{
	private GridView gridview; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_activity);
		
		 gridview = (GridView) findViewById(R.id.gridview);  
		  
	        // ���ɶ�̬���飬����ת������  
	        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
	        for (int i = 0; i < 100; i++) {  
	            HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("ItemImage", R.drawable.preview);// ���ͼ����Դ��ID  
	            lstImageItem.add(map);  
	        }  
	        // ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ  
	        SimpleAdapter saImageItems = new SimpleAdapter(this, // ûʲô����  
	                lstImageItem,// ������Դ  
	                R.layout.item_webpicture,// night_item��XMLʵ��  
	                // ��̬������ImageItem��Ӧ������  
	                new String[] { "ItemImage" },  
	                // ImageItem��XML�ļ������һ��ImageView,����TextView ID  
	                new int[] { R.id.ItemImage });  
	        // ��Ӳ�����ʾ  
	        gridview.setAdapter(saImageItems);  
	        // �����Ϣ����  
	        gridview.setOnItemClickListener(new ItemClickListener());  
	    }  
	  
	    // ��AdapterView������(���������߼���)���򷵻ص�Item�����¼�  
	    class ItemClickListener implements OnItemClickListener {  
	    	
	        @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

			
	    }  

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
