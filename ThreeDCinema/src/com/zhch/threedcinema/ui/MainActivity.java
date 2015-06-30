package com.zhch.threedcinema.ui;



import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.zhch.threedcinema.database.DbHelper;
import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;
import com.zhch.threedcinema.po.POMedia;
import com.zhch.threedcinema.service.MediaScannerService;
import com.zhch.threedcinema.ui.vitamio.LibsChecker;
import com.zhch.threedcinema.update.UpdataInfo;
import com.zhch.threedcinema.update.UpdataInfoParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	
	private TabHost tabHost;
	private LayoutInflater inflater;
	private Intent intent1, intent2, intent3, intent4, intent5, intent6;
	
	private ImageView lixian;
	private ImageView zuijinguankan;
	private ImageView sousuo;
	private ImageView saixuan;
	private ImageView fresh;
	private OPreference pref;
	


	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private UpdataInfo info;
	private String localVersion;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!LibsChecker.checkVitamioLibs(this, R.string.init_decoders))
            return;
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        pref = new OPreference(this);
       
		pref.putBooleanAndCommit("fragment1",true);
		pref.putBooleanAndCommit("fragment2",false);
		pref.putBooleanAndCommit("fragment3",false);
		pref.putBooleanAndCommit("fragment4",false);
		pref.putBooleanAndCommit("fragment5",false);
        //首次运行，扫描SD卡
        if (pref.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
        	getApplicationContext().startService(new Intent(this, MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, Environment.getExternalStorageDirectory().getAbsolutePath()));
            pref.putBooleanAndCommit(OPlayerApplication.PREF_KEY_FIRST, false);
        } 
        	
        setContentView(R.layout.activity_main);
        
           
		intent1 = new Intent(MainActivity.this, SetActivity.class);
		intent2 = new Intent(MainActivity.this, LocalPlay.class);
		intent3 = new Intent(MainActivity.this, movie.class);
		intent4 = new Intent(MainActivity.this, tuijian.class);
	//	intent5 = new Intent(MainActivity.this, tuijian.class);
	//	intent6 = new Intent(MainActivity.this, yanshi.class);

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View view1 = inflater.inflate(R.layout.tabitem1, null);
		View view2 = inflater.inflate(R.layout.tabitem7, null);
		View view3 = inflater.inflate(R.layout.tabitem3, null);
		View view4 = inflater.inflate(R.layout.tabitem2, null);
	//	View view5 = inflater.inflate(R.layout.tabitem5, null);
	//	View view6 = inflater.inflate(R.layout.tabitem6, null);
		 
 
		// 创建TabHost
		 
		tabHost = getTabHost();
		TabSpec tabSpec1 = tabHost.newTabSpec("tab1").setIndicator(view1)
				.setContent(intent1);
		tabHost.addTab(tabSpec1);
		TabSpec tabSpec2 = tabHost.newTabSpec("tab2").setIndicator(view2)
				.setContent(intent2);
		tabHost.addTab(tabSpec2);
		TabSpec tabSpec3 = tabHost.newTabSpec("tab3").setIndicator(view3)
				.setContent(intent3);
		tabHost.addTab(tabSpec3);
		TabSpec tabSpec4 = tabHost.newTabSpec("tab4").setIndicator(view4)
				.setContent(intent4);
		tabHost.addTab(tabSpec4);
//		TabSpec tabSpec5 = tabHost.newTabSpec("tab5").setIndicator(view5)
//				.setContent(intent5);
//		tabHost.addTab(tabSpec5);
//		TabSpec tabSpec6 = tabHost.newTabSpec("tab6").setIndicator(view6)
//				.setContent(intent6);
//		tabHost.addTab(tabSpec6);

		lixian=(ImageView)findViewById(R.id.main_lixian);
		lixian.setOnClickListener(new lixianListener());
		zuijinguankan=(ImageView)findViewById(R.id.main_zuijinguankan);
		zuijinguankan.setOnClickListener(new zuijinguankanListener());
		saixuan=(ImageView)findViewById(R.id.main_saixuan);
		saixuan.setOnClickListener(new saixuanListener());
		sousuo=(ImageView)findViewById(R.id.main_sousuo);
		sousuo.setOnClickListener(new sousuoListener());
		fresh=(ImageView)findViewById(R.id.main_fresh);
		fresh.setOnClickListener(new freshListener());
		
		try {
			localVersion =getVersionName();
			System.out.println(localVersion);
			pref.putStringAndCommit("APPVersion", localVersion);
			CheckVersionTask cv = new CheckVersionTask();
			new Thread(cv).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
	/***
	 * 功能： 软件新版本下载
	 * 引用： http://www.cnblogs.com/hxsyl/archive/2014/05/14/3727291.html
	 * @author wjx
	 *
	 */  
    private String getVersionName() throws Exception {
		//getPackageName()是你当前类的包名，0代表是获取版本信息  
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	} 
	public class CheckVersionTask implements Runnable {
		InputStream is;
		public void run() {
			try {
				String path = getResources().getString(R.string.url_server);
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET"); 
                int responseCode = conn.getResponseCode(); 
                if (responseCode == 200) { 
                    // 从服务器获得一个输入流 
                	is = conn.getInputStream(); 
                } 
				info = UpdataInfoParser.getUpdataInfo(is);
				if (info.getVersion().equals(localVersion)) {
					Log.i(TAG, "版本号相同");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
					// LoginMain();
				} else {
					Log.i(TAG, "版本号不相同 ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
//				Toast.makeText(getApplicationContext(), "最新版本",
//						Toast.LENGTH_SHORT).show();
				break;
			case UPDATA_CLIENT:
				 //对话框通知用户升级程序   
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//服务器超时   
	            Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show(); 
				break;
			case DOWN_ERROR:
				//下载apk失败  
	            Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show(); 
				break;
			}
		}
	};
    
	/* 
	 *  
	 * 弹出对话框通知用户更新程序  
	 *  
	 * 弹出对话框的步骤： 
	 *  1.创建alertDialog的builder.   
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
	 *  3.通过builder 创建一个对话框 
	 *  4.对话框show()出来   
	 */  
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		 //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	} 
	/* 
	 * 从服务器中下载APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new  ProgressDialog(this);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);  
	                sleep(3000);  
	                installApk(file);  
	                pd.dismiss(); //结束掉进度条对话框  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg); 
	                pd.dismiss(); 
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
	  
	//安装apk   
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //执行动作  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    startActivity(intent);  
	}  

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
 	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
 
	class freshListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		    DbHelper<POMedia> mDbHelper;
			mDbHelper = new DbHelper<POMedia>();
			mDbHelper.removeAll(POMedia.class);
		    getApplicationContext().startService(new Intent(MainActivity.this, MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, pref.getString("FilePath")));//Environment.getExternalStorageDirectory().getAbsolutePath()));
		    
		 // ---show the dialog---
		    final ProgressDialog dialog = ProgressDialog.show(MainActivity.this,
		            "正在加载视频文件", "请稍等......", true);//创建一个进度对话框
		    new Thread(new Runnable() {//使用Runnable代码块创建了一个Thread线程
		        @Override
		        public void run() {//run()方法中的代码将在一个单独的线程中执行
		            // TODO Auto-generated method stub
		            try {
		                // ---simulate doing something lengthy---
		                Thread.sleep(10000);//模拟一个耗时5秒的操作		                		        		
		                 
		                // ---dismiss the dialog---
		                dialog.dismiss();//5秒钟后，调用dismiss方法关闭进度对话框
		                
		                finish();  
		                Intent intent = new Intent(MainActivity.this, MainActivity.class);  
		                startActivity(intent); 
		                
		            } catch (InterruptedException e) {
		                // TODO: handle exception
		                e.printStackTrace();
		            }
		        }
		    }).start();
		}
    	
    }
    class lixianListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_lixian=new Intent(MainActivity.this,LixianActivity.class);
			startActivity(intent_lixian);
		}
    	
    }
    class zuijinguankanListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_lixian=new Intent(MainActivity.this,HistoryActivity.class);
			startActivity(intent_lixian);
		}
    	
    }
    class sousuoListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_lixian=new Intent(MainActivity.this,sousuo.class);
			startActivity(intent_lixian);
		}
    	
    }
    class saixuanListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_lixian=new Intent(MainActivity.this,ShaixuanActivity.class);
			startActivity(intent_lixian);
		}
    	
    }
}
