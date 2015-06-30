package com.zhch.threedcinema.info;

import com.zhch.threedcinema.utils.FileUtils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;



public class OPlayerApplication extends Application {

	private static OPlayerApplication mApplication;

	/** OPlayer SD卡缓存路�? */
	public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/threedcinema";
	/** 视频截图缓冲路径 */
	public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
	/** 首次扫描 */
	public static final String PREF_KEY_FIRST = "application_first";

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;

		init();
	}

	private void init() {
		//创建缓存目录
		FileUtils.createIfNoExists(OPLAYER_CACHE_BASE);
		FileUtils.createIfNoExists(OPLAYER_VIDEO_THUMB);
	}

	public static OPlayerApplication getApplication() {
		return mApplication;
	}

	public static Context getContext() {
		return mApplication;
	}

	/** �?�? */
	public void destory() {
		mApplication = null;
	}
}
