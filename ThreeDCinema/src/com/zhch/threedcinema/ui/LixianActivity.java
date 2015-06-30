package com.zhch.threedcinema.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;
import com.zhch.threedcinema.service.MediaScannerService;
//import com.zhch.threedcinema.ui.MainActivity.lixianListener;
import com.zhch.threedcinema.ui.ShaixuanActivity.fanhuiListener;

public class LixianActivity extends Activity {
	
	private TabHost tabHost;
	private LayoutInflater inflater;
	private Intent intent1, intent2;
	
	private ImageView fanhui;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
//        OPreference pref = new OPreference(this);
//        //首次运行，扫描SD卡
//      //  if (pref.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
//        	getApplicationContext().startService(new Intent(this, MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));
//   //         pref.putBooleanAndCommit(OPlayerApplication.PREF_KEY_FIRST, false);
//        //}
//		mMediaScannerService.getApplicationContext().startService(new Intent(mMediaScannerService.getApplicationContext(), MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));

        setContentView(R.layout.xiazai_activity);
       
//        
//		intent1 = new Intent(LixianActivity.this, LocalPlay.class);
//		intent2 = new Intent(LixianActivity.this, DownLoadMovie.class);
//		
//
//		LayoutInflater inflater = LayoutInflater.from(LixianActivity.this);
//		View view1 = inflater.inflate(R.layout.tabitem7, null);
//		View view2 = inflater.inflate(R.layout.tabitem8, null);
//
//		 
//
//		// 创建TabHost
//		 
//		tabHost = getTabHost();
//		TabSpec tabSpec1 = tabHost.newTabSpec("tab1").setIndicator(view1)
//				.setContent(intent1);
//		tabHost.addTab(tabSpec1);
//		TabSpec tabSpec2 = tabHost.newTabSpec("tab2").setIndicator(view2)
//				.setContent(intent2);
//		tabHost.addTab(tabSpec2);
//	
//		
//		fanhui=(ImageView)findViewById(R.id.lixian_fanhui);
//		fanhui.setOnClickListener(new lixian_fanhuiListener());
//    }
//   
//    class lixian_fanhuiListener implements OnClickListener{
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			
//		}
    	
        fanhui=(ImageView)findViewById(R.id.xiazai_fanhui);
		fanhui.setOnClickListener(new fanhuiListener());
	}

	class fanhuiListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(LixianActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
}
