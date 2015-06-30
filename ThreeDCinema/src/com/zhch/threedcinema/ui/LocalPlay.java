package com.zhch.threedcinema.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.zhch.threedcinema.ui.R;
import com.zhch.threedcinema.base.ArrayAdapter;
import com.zhch.threedcinema.business.FileBusiness;
import com.zhch.threedcinema.utils.FileUtils;
import com.zhch.threedcinema.adapter.FileAdapter;

import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;
import com.zhch.threedcinema.play.VideoActivity;
import com.zhch.threedcinema.po.POMedia;
import com.zhch.threedcinema.service.MediaScannerService;
import com.zhch.threedcinema.service.MediaScannerService.IMediaScannerObserver;
import com.zhch.threedcinema.service.MediaScannerService.MediaScannerServiceBinder;
import com.zhch.threedcinema.ui.vitamio.LibsChecker;


public class LocalPlay  extends Activity implements Callback,IMediaScannerObserver{

	private FileAdapter mAdapter;
	private GridView gridview;  
	private MediaScannerService mMediaScannerService;
	private LruCache<String, Bitmap> mLruCache;
	private ServiceConnection mMediaScannerServiceConnection = new ServiceConnection() {


		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mMediaScannerService = null;
			
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mMediaScannerService = ((MediaScannerServiceBinder) service)
					.getService();
			mMediaScannerService.addObserver(LocalPlay.this);
		}

		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		

//        OPreference pref = new OPreference(this);
//        //�״����У�ɨ��SD��
//    //    if (pref.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
//            startService(new Intent(LocalPlay.this, MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));
//            pref.putBooleanAndCommit(OPlayerApplication.PREF_KEY_FIRST, false);
//    //    }
				
		setContentView(R.layout.localplay_activity);
		
		  
	//	mMediaScannerService.getApplicationContext().startService(new Intent(mMediaScannerService.getApplicationContext(), MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));

		
		new DataTask().execute();

		bindService(
				new Intent(LocalPlay.this,
						MediaScannerService.class),
				mMediaScannerServiceConnection, Context.BIND_AUTO_CREATE);
		
		 gridview = (GridView) findViewById(R.id.localplay_gridview);  
		  
//	        // ���ɶ�̬���飬����ת������  
//	        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
//	        for (int i = 0; i < 100; i++) {  
//	            HashMap<String, Object> map = new HashMap<String, Object>();  
//	            map.put("ItemImage", R.drawable.preview);// ���ͼ����Դ��ID  
//	            lstImageItem.add(map);  
//	        }  
//	        // ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ  
//	        SimpleAdapter saImageItems = new SimpleAdapter(this, // ûʲô����  
//	                lstImageItem,// ������Դ  
//	                R.layout.item_localpicture,// night_item��XMLʵ��  
//	                // ��̬������ImageItem��Ӧ������  
//	                new String[] { "ItemImage" },  
//	                // ImageItem��XML�ļ������һ��ImageView,����TextView ID  
//	                new int[] { R.id.ItemImage });  
//	        // ��Ӳ�����ʾ  
//	        gridview.setAdapter(saImageItems);  
	        // �����Ϣ����  
	        gridview.setOnItemClickListener(new ItemClickListener());  
	    }  

		    @Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}
	
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
		
			unbindService(mMediaScannerServiceConnection);
			super.onDestroy();
		}

		// ��AdapterView������(���������߼���)���򷵻ص�Item�����¼�  
	    class ItemClickListener implements OnItemClickListener {  
	    	
	        @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
	        	final POMedia f =  mAdapter.getItem(position);
	        	
	     //   	System.out.println("------------"+f.title+"--------"+f.path);
		    		
	    		Intent intent = new Intent(LocalPlay.this, VideoActivity.class);
	    		intent.setData(Uri.parse(f.path));
	    		// intent.putExtra("path", f.path);
	    		intent.putExtra("displayName", f.title);
	    		startActivity(intent);
				
			}

			
	    }  
	    private class DataTask extends AsyncTask<Void, Void, List<POMedia>> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
		//		mLoadingLayout.setVisibility(View.VISIBLE);
		//		mListView.setVisibility(View.GONE);
			}

			@Override
			protected List<POMedia> doInBackground(Void... params) {
				return FileBusiness.getAllSortFiles(); //���vitamioɨ����ļ�
			}

			@Override
			protected void onPostExecute(List<POMedia> result) {
				super.onPostExecute(result);

				mAdapter = new FileAdapter(LocalPlay.this, result);
				gridview.setAdapter(mAdapter);

		//		mLoadingLayout.setVisibility(View.GONE);
		//		mListView.setVisibility(View.VISIBLE);
			}
		}
		private class FileAdapter extends ArrayAdapter<POMedia> {

			private HashMap<String, POMedia> maps = new HashMap<String, POMedia>();

			public FileAdapter(Context ctx, List<POMedia> l) {
				super(ctx, l);
				maps.clear();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final POMedia f = getItem(position);
				if (convertView == null) {
					final LayoutInflater mInflater = LocalPlay.this
							.getLayoutInflater();
					convertView = mInflater.inflate(R.layout.item_localpicture,
							null);
				}
				((TextView) convertView.findViewById(R.id.local_title)).setText(f.title);
			
				Bitmap bitmap = null;
				bitmap = getVideoThumbnail(f.title, 240, 120, Thumbnails.MINI_KIND);
				((ImageView) convertView.findViewById(R.id.local_image)).setImageBitmap(bitmap);
				// ��ʾ�ļ���С
				String file_size;
				if (f.temp_file_size > 0) {
					file_size = FileUtils.showFileSize(f.temp_file_size) + " / "
							+ FileUtils.showFileSize(f.file_size);
				} else {
					file_size = FileUtils.showFileSize(f.file_size);
				}
			
//				((TextView) convertView.findViewById(R.id.file_size))
//						.setText(file_size);

//				// ��ʾ���ȱ�
//				final ImageView status = (ImageView) convertView
//						.findViewById(R.id.status);
//				if (f.status > -1) {
//					int resStauts = getStatusImage(f.status);
//					if (resStauts > 0) {
//						if (status.getVisibility() != View.VISIBLE)
//							status.setVisibility(View.VISIBLE);
//						status.setImageResource(resStauts);
//					}
//				} else {
//					if (status.getVisibility() != View.GONE)
//						status.setVisibility(View.GONE);
//				}
				return convertView;
			}

			public void add(POMedia item, String url) {
				super.add(item);
				if (!maps.containsKey(url))
					maps.put(url, item);
			}

			public void delete(int position) {
				synchronized (mLock) {
					mObjects.remove(position);
				}
				notifyDataSetChanged();
			}

			public POMedia getItem(String url) {
				return maps.get(url);
			}
		}

		
	    /**
		 * 
		 * @param flag
		 *            0 ��ʼɨ�� 1 ����ɨ�� 2 ɨ�����
		 * @param file
		 *            ɨ�赽����Ƶ�ļ�
		 */
		@Override
		public void update(int flag, POMedia media) {
			// Logger.i(flag + " " + media.path);
			switch (flag) {
			case MediaScannerService.SCAN_STATUS_START:

				break;
			case MediaScannerService.SCAN_STATUS_END:// ɨ�����
//				if (mProgress != null)
//					mProgress.setVisibility(View.GONE);
				new DataTask().execute();
				break;
			case MediaScannerService.SCAN_STATUS_RUNNING:// ɨ��һ���ļ�
				if (mAdapter != null && media != null) {
					mAdapter.add(media);
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	/** 
	  * ��ȡ��Ƶ����ͼ 
      * @param videoPath 
	  * @param width 
	  * @param height 
	  * @param kind 
      * @return 
      */  
	private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){  
		videoPath=Environment.getExternalStorageDirectory()+"/threedcinema/thumb/"+videoPath+".jpg";
		System.out.println(videoPath);
		File mFile=new File(videoPath);
        //�����ļ�����
       
        Bitmap bitmap=BitmapFactory.decodeFile(videoPath);
         return bitmap;
       
	}  
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

