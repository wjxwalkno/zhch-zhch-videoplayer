package com.zhch.threedcinema.ui;

//@by wjx
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.zhch.threedcinema.ui.R;
import com.zhch.threedcinema.play.VideoActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoDetailActivity extends Activity{

	private String thumb=null;
	private String zhuyan="暂无";
	private String daoyan="暂无";
	private Integer nianfen=0;
	private String diqu="暂无";
	private String yuyan="暂无";
	private String juqing="暂无";
	private String video_url=null;
	private String video_title="暂无";
	private boolean isHwdec=false;
	
	private TextView txt_zhuyan=null;
	private TextView txt_daoyan=null;
	private TextView txt_nianfen=null;
	private TextView txt_diqu=null;
	private TextView txt_yuyan=null;
	private TextView txt_juqing=null;
	private ImageView image_thumb=null;
	private ProgressDialog mDialog;
	private ImageView btn_play=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.videodetail_pager);
		Intent intent_vd=getIntent();
		thumb=intent_vd.getStringExtra("thumbnail");
		zhuyan=intent_vd.getStringExtra("zhuyan");
		daoyan=intent_vd.getStringExtra("daoyan");
		diqu=intent_vd.getStringExtra("diqu");
		juqing=intent_vd.getStringExtra("content");
		nianfen=intent_vd.getIntExtra("inputtime",0);
		video_url=intent_vd.getStringExtra("url");
		video_title=intent_vd.getStringExtra("title");
		
		txt_zhuyan=(TextView)findViewById(R.id.videopager_inputzhuyan);
		image_thumb=(ImageView)findViewById(R.id.videopager_thumbnail);
		txt_daoyan=(TextView)findViewById(R.id.videopager_inputdaoyan);
		txt_yuyan=(TextView)findViewById(R.id.videopager_inputyuyan);
		txt_diqu=(TextView)findViewById(R.id.videopager_inputdiqu);
		txt_nianfen=(TextView)findViewById(R.id.videopager_inputnianfen);
		txt_juqing=(TextView)findViewById(R.id.videopager_inputcontent);
		btn_play=(ImageView)findViewById(R.id.videopager_display);
		btn_play.setOnClickListener(new playClickListener());
		
		mDialog = new ProgressDialog(this);
        mDialog.setTitle("请稍等");
        mDialog.setMessage("正在加载...");
      
		txt_zhuyan.setText(zhuyan);
		txt_daoyan.setText(daoyan);
		txt_yuyan.setText(yuyan);
		txt_diqu.setText(diqu);
		txt_nianfen.setText(nianfen+" ");
		txt_juqing.setText(juqing);
		new ImageAsynTask().execute();
		
	}   
	  
	class playClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(VideoDetailActivity.this, VideoActivity.class);
			String tempURL=".wmv";		
			tempURL="mmsh://58.59.8.109:8085/test/sh.wmv";//"mmsh://58.59.8.109:8085/test/sbs_720p_1070kbps.wmv";//"rtsp://58.59.8.109:8554/sbs_720p_2000kbps.mpg";//"rtsp://58.59.8.109:8554/sbs_720p_2000kbps.mpg";//"http://202.201.14.186/flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4?wsiphost=local";	//"rtsp://58.59.8.109:8554/sbs_1080p_1360kbps.mkv";//"mms://58.59.8.109:8085/test/sbs_720p_407kbps.wmv";			
			intent.setData(Uri.parse(tempURL));
			// intent.putExtra("path", item.url);
			intent.putExtra(
					"displayName",
					video_title);
			intent.putExtra("hwCodec", isHwdec);
			startActivity(intent);
		}
		
	}
	 private class ImageAsynTask extends AsyncTask<Void, Void, Drawable> {
		  
		          @Override
		          protected Drawable doInBackground (Void... params) {
		              String url = thumb;
		              return loadImages(url);
		        }
		          
		          @Override
		          protected void onPostExecute (Drawable result) {
		              super.onPostExecute(result);
		              mDialog.dismiss();
		              image_thumb.setImageDrawable(result);
		          }
		          
		          @Override
		          protected void onPreExecute () {
		              super.onPreExecute();
		              mDialog.show();
		          }
		      }
		      
		      @Override
		      protected void onDestroy () {
		          super.onDestroy();
		          mDialog.dismiss();
		      }
		     
		      public Drawable loadImages(String url) { 
		          try {
		              return Drawable.createFromStream((InputStream)(new URL(url)).openStream(), "test");
		          }
		          catch (IOException e) {
		             e.printStackTrace();
		          }
		          return null;
		      }
		  }


