package com.zhch.threedcinema.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhch.threedcinema.info.OPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class SetActivity extends Activity implements Callback,SetFragmentLeft.Callbacks{
	private SetFragmentRight fragmentRight;

	private SetFragmentRight2 fragmentRight2;
	private SetFragmentRight3 fragmentRight3;
	private SetFragmentRight4 fragmentRight4;
	private SetFragmentRight5 fragmentRight5;
	private boolean fragment1=true;
	private boolean fragment2=false;
	private boolean fragment3=false;
	private boolean fragment4=false;
	private boolean fragment5=false;
	OPreference pre=null;
	
	
	public void onButtonSelected(Integer id){
		pre=new OPreference(SetActivity.this);
		fragment1=pre.getBoolean("fragment1");
		fragment2=pre.getBoolean("fragment2");
		fragment3=pre.getBoolean("fragment3");
		fragment4=pre.getBoolean("fragment4");
		fragment5=pre.getBoolean("fragment5");
		if(fragment1){
			getFragmentManager().beginTransaction().remove(fragmentRight);
		}else if(fragment2){
			getFragmentManager().beginTransaction().remove(fragmentRight2);
			
		}else if(fragment3){
			getFragmentManager().beginTransaction().remove(fragmentRight3);
			
		}else if(fragment4){
			getFragmentManager().beginTransaction().remove(fragmentRight4);
			
		}else if(fragment5){
			getFragmentManager().beginTransaction().remove(fragmentRight5);
			
		}
		
		if(1==id){
			fragmentRight= new SetFragmentRight();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight).commit();
//			fragment1=true;
//			fragment2=false;
//			fragment3=false;
//			fragment4=false;
//			fragment5=false;
			pre.putBooleanAndCommit("fragment1",true);
			pre.putBooleanAndCommit("fragment2",false);
			pre.putBooleanAndCommit("fragment3",false);
			pre.putBooleanAndCommit("fragment4",false);
			pre.putBooleanAndCommit("fragment5",false);
		}
		if(2==id){
			fragmentRight2= new SetFragmentRight2();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight2).commit();
			
//			fragment1=false;
//			fragment2=true;
//			fragment3=false;
//			fragment4=false;
//			fragment5=false;
			
			pre.putBooleanAndCommit("fragment1",false);
			pre.putBooleanAndCommit("fragment2",true);
			pre.putBooleanAndCommit("fragment3",false);
			pre.putBooleanAndCommit("fragment4",false);
			pre.putBooleanAndCommit("fragment5",false);
		}
		if(3==id){
			fragmentRight3= new SetFragmentRight3();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight3).commit();
//			fragment1=false;
//			fragment2=false;
//			fragment3=true;
//			fragment4=false;
//			fragment5=false;
			pre.putBooleanAndCommit("fragment1",false);
			pre.putBooleanAndCommit("fragment2",false);
			pre.putBooleanAndCommit("fragment3",true);
			pre.putBooleanAndCommit("fragment4",false);
			pre.putBooleanAndCommit("fragment5",false);
		
		}
		if(4==id){
			fragmentRight4= new SetFragmentRight4();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight4).commit();
//			fragment1=false;
//			fragment2=false;
//			fragment3=false;
//			fragment4=true;
//			fragment5=false;
			pre.putBooleanAndCommit("fragment1",false);
			pre.putBooleanAndCommit("fragment2",false);
			pre.putBooleanAndCommit("fragment3",false);
			pre.putBooleanAndCommit("fragment4",true);
			pre.putBooleanAndCommit("fragment5",false);
		}
		if(5==id){
			fragmentRight5= new SetFragmentRight5();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentRight5).commit();
//			fragment1=false;
//			fragment2=false;
//			fragment3=false;
//			fragment4=false;
//			fragment5=true;
			
			pre.putBooleanAndCommit("fragment1",false);
			pre.putBooleanAndCommit("fragment2",false);
			pre.putBooleanAndCommit("fragment3",false);
			pre.putBooleanAndCommit("fragment4",false);
			pre.putBooleanAndCommit("fragment5",true);
		
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_activity);
		fragmentRight= new SetFragmentRight();
		getFragmentManager().beginTransaction().add(R.id.set_content, fragmentRight).commit();
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
