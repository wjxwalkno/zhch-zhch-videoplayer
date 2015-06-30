package com.zhch.threedcinema.adapter;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import com.zhch.threedcinema.base.ImagesUrl;
import com.zhch.threedcinema.database.OnlineVideoDbHelper;
import com.zhch.threedcinema.database.VideoDetailData;
import com.zhch.threedcinema.database.VideoListData;
import com.zhch.threedcinema.ui.R;
import com.zhch.threedcinema.ui.movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * LruCache�����̷���:
 * ���Ǵӵ�һ�ν���Ӧ�õ�����¿�ʼ
 * 1 ����ͼƬ��Url��LruCache������ȡͼƬ.
 *   ��ͼƬ���ڻ�����,����ʾ��ͼƬ;������ʾĬ��ͼƬ
 * 2 ��Ϊ�ǵ�һ�ν���ý������Ի�ִ��:
 *   loadBitmaps(firstVisibleItem, visibleItemCount);
 *   ���Ǵ�loadBitmaps()������Ϊ�����,������������
 * 3 ���Դ�LruCache������ȡͼƬ.�������ʾ����,�������4
 * 4 ����һ���첽��������ͼƬ.������ɺ���ʾͼƬ,���ҽ�
 *   ��ͼƬ����LruCache������
 *   
 * ��ֹͣ����ʱ,�����loadBitmaps(firstVisibleItem, visibleItemCount)
 * �����������
 */
@SuppressLint("NewApi")
public class GridViewAdapter extends ArrayAdapter<String> {
	
	private GridView mGridView;
	//ͼƬ������
	private LruCache<String, Bitmap> mLruCache;
	//��¼�����������ػ�ȴ����ص�����
	private HashSet<DownloadBitmapAsyncTask> mDownloadBitmapAsyncTaskHashSet;
	//GridView�пɼ��ĵ�һ��ͼƬ���±�
	private int mFirstVisibleItem;
	//GridView�пɼ���ͼƬ������
	private int mVisibleItemCount;
	//��¼�Ƿ��ǵ�һ�ν���ý���
	private boolean isFirstEnterThisActivity = true;
	
	private String[] ImageURL;

	public GridViewAdapter(Context context, int textViewResourceId,String[] objects, GridView gridView) {
		super(context, textViewResourceId, objects);
		
		ImageURL=objects;
		
		
		mGridView = gridView;
		mGridView.setOnScrollListener(new ScrollListenerImpl());
		
		mDownloadBitmapAsyncTaskHashSet = new HashSet<DownloadBitmapAsyncTask>();
		
		// ��ȡӦ�ó����������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// ����ͼƬ�����СΪmaxMemory��1/6
		int cacheSize = maxMemory/6;
		
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String url = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_webpicture, null);
		} else {
			view = convertView;
		}
		ImageView imageView = (ImageView) view.findViewById(R.id.ItemImage);
//		TextView title=(TextView) view.findViewById(R.id.ItemTitle);
//		if(videolistdata!=null)
//		title.setText(videolistdata.find(position).getTitle());
		//Ϊ��ImageView����һ��Tag,��ֹͼƬ��λ
		imageView.setTag(url);
		//Ϊ��ImageView������ʾ��ͼƬ
		setImageForImageView(url, imageView);
		return view;
	}

	/**
	 * ΪImageView����ͼƬ(Image)
	 * 1 �ӻ����л�ȡͼƬ
	 * 2 ��ͼƬ���ڻ�������Ϊ������Ĭ��ͼƬ
	 */
	private void setImageForImageView(String imageUrl, ImageView imageView) {
		Bitmap bitmap = getBitmapFromLruCache(imageUrl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.preview);
		}
	}

	/**
	 * ��ͼƬ�洢��LruCache
	 */
	public void addBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * ��LruCache�����ȡͼƬ
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	

   /**
    * ΪGridView��item����ͼƬ
    * 
    * @param firstVisibleItem 
    * GridView�пɼ��ĵ�һ��ͼƬ���±�
    * 
    * @param visibleItemCount 
    * GridView�пɼ���ͼƬ������
    * 
    */
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				String imageUrl=ImageURL[i];//videolistdata.find(i).getThumb();//ImageUrl[i];// ImagesUrl.Urls[i];//= ImagesUrl.Urls[i];
			//	System.out.println(imageUrl);
				Bitmap bitmap = getBitmapFromLruCache(imageUrl);
				if (bitmap == null) {
					DownloadBitmapAsyncTask downloadBitmapAsyncTask = new DownloadBitmapAsyncTask();
					mDownloadBitmapAsyncTaskHashSet.add(downloadBitmapAsyncTask);
					downloadBitmapAsyncTask.execute(imageUrl);
				} else {
					//����Tag�ҵ���Ӧ��ImageView��ʾͼƬ
					ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ�������������ػ�ȴ����ص�����
	 */
	public void cancelAllTasks() {
		if (mDownloadBitmapAsyncTaskHashSet != null) {
			for (DownloadBitmapAsyncTask task : mDownloadBitmapAsyncTaskHashSet) {
				task.cancel(false);
			}
		}
	}
	
	
	private class ScrollListenerImpl implements OnScrollListener{
	    /**
	     * 
	     * ���ǵı�����ͨ��onScrollStateChanged��֪:ÿ��GridViewֹͣ����ʱ����ͼƬ
	     * ���Ǵ���һ���������:
	     * ����һ����Ӧ�õ�ʱ��,��ʱ��û�л�����Ļ�Ĳ������������onScrollStateChanged,��Ӧ�ü���ͼƬ.
	     * �����ڴ˴���һ������Ĵ���.
	     * ������:
	     * if (isFirstEnterThisActivity && visibleItemCount > 0) {
	     *      loadBitmaps(firstVisibleItem, visibleItemCount);
	     *      isFirstEnterThisActivity = false;
	     *    }
	     *    
	     * ------------------------------------------------------------
	     * 
	     * ����Ķ����������.
	     * ����������Ҫ���ϱ���:firstVisibleItem��visibleItemCount
	     * �Ӷ���������onScrollStateChanged()�жϵ�ֹͣ����ʱ����ͼƬ
	     * 
	     */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
			mFirstVisibleItem = firstVisibleItem;
			mVisibleItemCount = visibleItemCount;
			if (isFirstEnterThisActivity && visibleItemCount > 0) {
				loadBitmaps(firstVisibleItem, visibleItemCount);
				isFirstEnterThisActivity = false;
			}
		}
		
		/**
		 *  GridViewֹͣ����ʱ����ͼƬ
		 *  ���������ȡ�������������ػ��ߵȴ����ص�����
		 */
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
			} else {
				cancelAllTasks();
			}
		}
		
	}

	/**
	 * ����ͼƬ���첽����
	 */
	class DownloadBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
		private String imageUrl;
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			Bitmap bitmap = downloadBitmap(params[0]);
			if (bitmap != null) {
				//�������,���仺�浽LrcCache
				addBitmapToLruCache(params[0], bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			//������ɺ�,�ҵ����Ӧ��ImageView��ʾͼƬ
			ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			mDownloadBitmapAsyncTaskHashSet.remove(this);
		}
	}

	// ��ȡBitmap
	private Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(imageUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setConnectTimeout(50 * 1000);
//			httpURLConnection.setReadTimeout(100 * 1000);
			httpURLConnection.setDoInput(true);
//			httpURLConnection.setDoOutput(true);
			httpURLConnection.connect();
			bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return bitmap;
	}

}
