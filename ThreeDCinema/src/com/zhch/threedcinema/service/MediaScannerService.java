package com.zhch.threedcinema.service;



import io.vov.vitamio.ThumbnailUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Images;


import com.zhch.threedcinema.database.DbHelper;
import com.zhch.threedcinema.exception.Logger;
import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;

import com.zhch.threedcinema.po.POMedia;
import com.zhch.threedcinema.utils.ConvertUtils;
import com.zhch.threedcinema.utils.FileUtils;
import com.zhch.threedcinema.utils.PinyinUtils;
import com.zhch.threedcinema.utils.StringUtils;

/** 濯掍綋鎵弿 */
public class MediaScannerService extends Service implements Runnable {

	private static final String SERVICE_NAME = "com.zhch.threedcinema.service.MediaScannerService";
	/** 鎵弿鏂囦欢澶� */
	public static final String EXTRA_DIRECTORY = "scan_directory";
	/** 鎵弿鏂囦欢 */
	public static final String EXTRA_FILE_PATH = "scan_file";
	public static final String EXTRA_MIME_TYPE = "mimetype";

	public static final int SCAN_STATUS_NORMAL = -1;
	/** 寮�濮嬫壂鎻� */
	
	public static final int SCAN_STATUS_START = 0;
	/** 姝ｅ湪鎵弿 鎵弿鍒颁竴涓棰戞枃浠� */
	public static final int SCAN_STATUS_RUNNING = 1;
	/** 鎵弿瀹屾垚 */
	public static final int SCAN_STATUS_END = 2;
	/**  */
	private ArrayList<IMediaScannerObserver> observers = new ArrayList<IMediaScannerObserver>();
	private ConcurrentHashMap<String, String> mScanMap = new ConcurrentHashMap<String, String>();

	/** 褰撳墠鐘舵�� */
	private volatile int mServiceStatus = SCAN_STATUS_NORMAL;
	private DbHelper<POMedia> mDbHelper;
	private Map<String, Object> mDbWhere = new HashMap<String, Object>(2);
	private OPreference pre=null;
	@Override
	public void onCreate() {
		super.onCreate();

		mDbHelper = new DbHelper<POMedia>();
	}

	/** 鏄惁姝ｅ湪杩愯 */ 
	public static boolean isRunning() {
		ActivityManager manager = (ActivityManager) OPlayerApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (SERVICE_NAME.equals(service.service.getClassName()))
				return true;
		}
		return false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null)
			parseIntent(intent);

		return super.onStartCommand(intent, flags, startId);
	}

	/** 瑙ｆ瀽Intent */
	private void parseIntent(final Intent intent) {
		final Bundle arguments = intent.getExtras();
		if (arguments != null) {
			if (arguments.containsKey(EXTRA_DIRECTORY)) {
				String directory = arguments.getString(EXTRA_DIRECTORY);
				
				Logger.i("onStartCommand:" + directory);
				//鎵弿鏂囦欢澶�
				if (!mScanMap.containsKey(directory))
					mScanMap.put(directory, "");
			} else if (arguments.containsKey(EXTRA_FILE_PATH)) {
				//鍗曟枃浠�
				String filePath = arguments.getString(EXTRA_FILE_PATH);
				
				Logger.i("onStartCommand:" + filePath);
				if (!StringUtils.isEmpty(filePath)) {
					if (!mScanMap.containsKey(filePath))
						mScanMap.put(filePath, arguments.getString(EXTRA_MIME_TYPE));
					//					scanFile(filePath, arguments.getString(EXTRA_MIME_TYPE));
				}
			}
		}

		if (mServiceStatus == SCAN_STATUS_NORMAL || mServiceStatus == SCAN_STATUS_END) {
			new Thread(this).start();
			//scan();
		}
	}

	@Override
	public void run() {
		pre=new OPreference(this);
		scan();
	}

	/** 鎵弿 */
	private void scan() {
		//寮�濮嬫壂鎻�
		notifyObservers(SCAN_STATUS_START, null);

		while (mScanMap.keySet().size() > 0) {

			String path = "";
			for (String key : mScanMap.keySet()) {
				path = key;
			//	path=pre.getString("FilePath");
				
				break;
			}
			
			
			
			if (mScanMap.containsKey(path)) {
				String mimeType = mScanMap.get(path);
				if ("".equals(mimeType)) {
					scanDirectory(path);
				} else {
					scanFile(path, mimeType);
				}

				//鎵弿瀹屾垚涓�涓�
				mScanMap.remove(path);
			}

			//浠诲姟涔嬮棿姝囨伅涓�绉�
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logger.e(e);
			}
		}
 
		//鍏ㄩ儴鎵弿瀹屾垚
		notifyObservers(SCAN_STATUS_END, null);

		//@by wjx
		OPreference pref = new OPreference(this);
		if (pref.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true))
			pref.putBooleanAndCommit(OPlayerApplication.PREF_KEY_FIRST, false);

//		pref.putBooleanAndCommit(OPlayerApplication.PREF_KEY_FIRST,false);
		//鍋滄鏈嶅姟
		stopSelf();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			for (IMediaScannerObserver s : observers) {
				if (s != null) {
					s.update(msg.what, (POMedia) msg.obj);
				}
			}
		}
	};

	/** 鎵弿鏂囦欢 */
	private void scanFile(String path, String mimeType) {
		save(new POMedia(path, mimeType));
	}

	/** 鎵弿鏂囦欢澶� */
	private void scanDirectory(String path) {
		eachAllMedias(new File(path));
		
	}

	/** 閫掑綊鏌ユ壘瑙嗛 */
	private void eachAllMedias(File f) {
		if (f != null && f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				for (File file : f.listFiles()) {
					//					Logger.i(f.getAbsolutePath());
					if (file.isDirectory()) {
						//蹇界暐.寮�澶寸殑鏂囦欢澶�
						if (!file.getAbsolutePath().startsWith("."))
							eachAllMedias(file);
					} else if (file.exists() && file.canRead() && FileUtils.isVideo(file)) {
						save(new POMedia(file));
					}
				}
			}
		}
	}

	/**
	 * 淇濆瓨鍏ュ簱
	 * 
	 * @throws FileNotFoundException
	 */
	private void save(POMedia media) {
		mDbWhere.put("path", media.path);
		mDbWhere.put("last_modify_time", media.last_modify_time);
		
		//妫�娴�
		if (!mDbHelper.exists(media, mDbWhere)) {
			try {
				if (media.title != null && media.title.length() > 0)
					media.title_key = PinyinUtils.chineneToSpell(media.title.charAt(0) + "");
			} catch (Exception ex) {
				Logger.e(ex);
			}
			media.last_access_time = System.currentTimeMillis();
 
			//鎻愬彇缂╃暐鍥�
			extractThumbnail(media);

			media.mime_type = FileUtils.getMimeType(media.path);

			//鍏ュ簱
			mDbHelper.create(media);

			//鎵弿鍒颁竴涓�
			notifyObservers(SCAN_STATUS_RUNNING, media);
		}
	}
	
	/** 鎻愬彇鐢熸垚缂╃暐鍥� */
	private void extractThumbnail(POMedia media) {

				
		final Context ctx = OPlayerApplication.getContext();
		//		ThumbnailUtils.
		Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(this,media.path, Images.Thumbnails.MINI_KIND);
		try {
			if (bitmap == null) {
				//缂╃暐鍥惧垱寤哄け璐�
				bitmap = Bitmap.createBitmap(ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_WIDTH, ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_HEIGHT, Bitmap.Config.RGB_565);
			}

			media.width = bitmap.getWidth();
			media.height = bitmap.getHeight();
 
			//缂╃暐鍥�
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, ConvertUtils.dipToPX(ctx, ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL_WIDTH), ConvertUtils.dipToPX(ctx, ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL_HEIGHT), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			if (bitmap != null) {
				//灏嗙缉鐣ュ浘瀛樺埌瑙嗛褰撳墠璺緞
				File thum = new File(OPlayerApplication.OPLAYER_VIDEO_THUMB, media.title+".jpg");//UUID.randomUUID().toString());
				media.thumb_path = thum.getAbsolutePath();
		//		System.out.println("+++++++++++++++++++++++++++++++++++++");
				//thum.createNewFile();
				FileOutputStream iStream = new FileOutputStream(thum);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 85, iStream);
				iStream.close();
			}

			//鍏ュ簱

		} catch (Exception ex) {
			Logger.e(ex);
		} finally {
			if (bitmap != null)
				bitmap.recycle();

		}
	}

	// ~~~ 鐘舵�佹敼鍙� 

	/** 閫氱煡鐘舵�佹敼鍙� */
	private void notifyObservers(int flag, POMedia media) {
		mHandler.sendMessage(mHandler.obtainMessage(flag, media));
	}

	/** 澧炲姞瑙傚療鑰� */
	public void addObserver(IMediaScannerObserver s) {
		synchronized (this) {
			if (!observers.contains(s)) {
				observers.add(s);
			}
		}
	}

	/** 鍒犻櫎瑙傚療鑰� */
	public synchronized void deleteObserver(IMediaScannerObserver s) {
		observers.remove(s);
	}

	/** 鍒犻櫎鎵�鏈夎瀵熻�� */
	public synchronized void deleteObservers() {
		observers.clear();
	}

	public interface IMediaScannerObserver {
		/**
		 * 
		 * @param flag 0 寮�濮嬫壂鎻� 1 姝ｅ湪鎵弿 2 鎵弿瀹屾垚
		 * @param file 鎵弿鍒扮殑瑙嗛鏂囦欢
		 */
		public void update(int flag, POMedia media);
	}

	// ~~~ Binder 

	private final MediaScannerServiceBinder mBinder = new MediaScannerServiceBinder();

	public class MediaScannerServiceBinder extends Binder {
		public MediaScannerService getService() {
			return MediaScannerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}
