package com.zhch.threedcinema.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zhch.threedcinema.database.DbHelper;
import com.zhch.threedcinema.info.OPreference;
import com.zhch.threedcinema.po.POMedia;
import com.zhch.threedcinema.service.MediaScannerService;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileSelectActivity extends ListFragment {
	OPreference pre=null;
	private SetFragmentRight fragmentRight;
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/sdcard";
	private String curPath = "/";
	private TextView mPath;
	private final static String TAG = "bb";
	private ImageView fileSelect=null;
	private EditText filePath=null;
	public static final int FILE_RESULT_CODE = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.file_select, container, false);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rootPath=Environment.getExternalStorageDirectory()+"";
		curPath=Environment.getExternalStorageDirectory()+"";
		mPath = (TextView)getActivity().findViewById(R.id.mPath);
//		filePathList=(ListView)getActivity().findViewById(R.id.list);
		Button buttonConfirm = (Button)getActivity().findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			 pre=new OPreference(getActivity());
			 pre.putStringAndCommit("FilePath", curPath);
				
//			 fragmentRight= new SetFragmentRight();
//			 getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight).commit();
			 
			 DbHelper<POMedia> mDbHelper;
				mDbHelper = new DbHelper<POMedia>();
				mDbHelper.removeAll(POMedia.class);
				getActivity().getApplicationContext().startService(new Intent(getActivity(), MediaScannerService.class).putExtra(MediaScannerService.EXTRA_DIRECTORY, pre.getString("FilePath")));//Environment.getExternalStorageDirectory().getAbsolutePath()));
			    
			 // ---show the dialog---
			    final ProgressDialog dialog = ProgressDialog.show(getActivity(),
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
			                
			                getActivity().finish();
			                Intent intent = new Intent(getActivity(), MainActivity.class);  
			                startActivity(intent); 
			                
			            } catch (InterruptedException e) {
			                // TODO: handle exception
			                e.printStackTrace();
			            }
			        }
			    }).start();
			 
			}
		});
		Button buttonCancle = (Button)getActivity().findViewById(R.id.buttonCancle);
		buttonCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentRight= new SetFragmentRight();
				 getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight).commit();
			}
		});
		getFileDir(rootPath);
		
	}
	
	private void getFileDir(String filePath) {
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();
		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}
		setListAdapter(new MovieFileAdapter(getActivity(), items, paths));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		File file = new File(paths.get(position));
		if (file.isDirectory()) {
			curPath = paths.get(position);
			System.out.println(curPath);
			getFileDir(paths.get(position));
		} else {
			
		}
	}

	
}
