package com.zhch.threedcinema.ui;

import com.zhch.threedcinema.info.OPreference;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SetFragmentRight3 extends Fragment{

	private TextView softVersion=null;
	private OPreference pref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.set_right_fragment3, container, false);
		
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pref=new OPreference(getActivity());
		softVersion=(TextView)getActivity().findViewById(R.id.soft_version);
		softVersion.setText("��ǰ�汾��Ϊv"+pref.getString("APPVersion")+"����Ϊ���°汾���������");
	}
	

}