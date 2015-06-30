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
		  
	        // 生成动态数组，并且转入数据  
	        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
	        for (int i = 0; i < 100; i++) {  
	            HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("ItemImage", R.drawable.preview);// 添加图像资源的ID  
	            lstImageItem.add(map);  
	        }  
	        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
	        SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释  
	                lstImageItem,// 数据来源  
	                R.layout.item_webpicture,// night_item的XML实现  
	                // 动态数组与ImageItem对应的子项  
	                new String[] { "ItemImage" },  
	                // ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	                new int[] { R.id.ItemImage });  
	        // 添加并且显示  
	        gridview.setAdapter(saImageItems);  
	        // 添加消息处理  
	        gridview.setOnItemClickListener(new ItemClickListener());  
	    }  
	  
	    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件  
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
