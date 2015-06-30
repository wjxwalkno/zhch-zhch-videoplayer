package com.zhch.threedcinema.ui;

import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SetFragmentLeft extends Fragment{

	private Callbacks mCallbacks;
	private ImageView softset;
	private ImageView cleanset;
	private ImageView updateset;
	private ImageView helpset;
	private ImageView aboutset;
	private OPreference pre;
	public interface Callbacks{
		public void onButtonSelected(Integer id);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mCallbacks=(Callbacks)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pre=new OPreference(getActivity());
		pre.putBooleanAndCommit("fragment1",true);
		pre.putBooleanAndCommit("fragment2",false);
		pre.putBooleanAndCommit("fragment3",false);
		pre.putBooleanAndCommit("fragment4",false);
		pre.putBooleanAndCommit("fragment5",false);
		
		if (pre.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
			  pre.putStringAndCommit("FilePath",Environment.getExternalStorageDirectory().getPath());
		    }
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.set_left_fragment, container, false);
		
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		softset=(ImageView)getActivity().findViewById(R.id.softSet);
		cleanset=(ImageView)getActivity().findViewById(R.id.cleanSet);
		updateset=(ImageView)getActivity().findViewById(R.id.updateSet);
		helpset=(ImageView)getActivity().findViewById(R.id.helpSet);
		aboutset=(ImageView)getActivity().findViewById(R.id.aboutSet);
		ButtonClickListener clickListener = new ButtonClickListener();   
		softset.setOnClickListener(clickListener);  
		cleanset.setOnClickListener(clickListener);  
		updateset.setOnClickListener(clickListener);
		helpset.setOnClickListener(clickListener);  
		aboutset.setOnClickListener(clickListener);  
		
	    
	}
	 /** °´Å¥µÄ¼àÌýÆ÷ */  
    class ButtonClickListener implements OnClickListener   
    {  
        public void onClick(View v)   
        {  
            ImageView button = (ImageView) v;   
            if(button == softset)   
                mCallbacks.onButtonSelected(1);  
            if(button == cleanset)   
                mCallbacks.onButtonSelected(2);  
            if(button == updateset)   
                mCallbacks.onButtonSelected(3);  
            if(button == helpset)   
                mCallbacks.onButtonSelected(4);  
            if(button == aboutset)   
                mCallbacks.onButtonSelected(5);  
        }  
    }  
	

}
