package com.zhch.threedcinema.ui;

import com.zhch.threedcinema.info.OPlayerApplication;
import com.zhch.threedcinema.info.OPreference;
import com.zhch.threedcinema.service.MediaScannerService;
import com.zhch.threedcinema.ui.SetFragmentLeft.ButtonClickListener;
import com.zhch.threedcinema.ui.SetFragmentLeft.Callbacks;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import android.widget.RadioGroup;


public class SetFragmentRight extends Fragment{
	private FileSelectActivity fragmentFilePath;
	private CheckBox soft=null;
	private CheckBox hw=null;
	private ImageView selectPath=null;
	private Callbacks mCallbacks;
	private EditText filePath=null;
	private boolean changeView=false;
	public static final int FILE_RESULT_CODE = 1;
	private boolean temp_hw=false;
	private boolean temp_sw=false;
	OPreference pre=null;
	public interface Callbacks{
		public void onFilePathSelected(Integer id);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.set_right_fragment, container, false);
		
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		pre=new OPreference(getActivity());
		
		soft=(CheckBox)getActivity().findViewById(R.id.dec_soft);
		hw=(CheckBox)getActivity().findViewById(R.id.dec_hw);
		selectPath=(ImageView)getActivity().findViewById(R.id.select_path);
		filePath = (EditText)getActivity().findViewById(R.id.path_txt);
		
		if (pre.getBoolean(OPlayerApplication.PREF_KEY_FIRST, true)) {
		  filePath.setText(pre.getString("FilePath",Environment.getExternalStorageDirectory().getPath()));
	    } else{
			filePath.setText(pre.getString("FilePath"));
	    }
		soft.setChecked(pre.getBoolean("software", false));
		hw.setChecked(pre.getBoolean("hardware", true));
		temp_sw=false;
		temp_hw=true;
		soft.setOnCheckedChangeListener(changelistener);
		hw.setOnCheckedChangeListener(changelistener); 
		selectPath.setOnClickListener(new SelectPathListener());
	}
	
	class SelectPathListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Intent intent = new Intent(getActivity(),MovieFileManager.class);
//			startActivityForResult(intent, FILE_RESULT_CODE);
			 fragmentFilePath= new FileSelectActivity();
			getFragmentManager().beginTransaction().replace(R.id.set_content, fragmentFilePath).commit();
			
		}
		
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if(FILE_RESULT_CODE == requestCode){
//			Bundle bundle = null;
//			if(data!=null&&(bundle=data.getExtras())!=null){
//				filePath.setText(""+bundle.getString("file"));
//			}
//		}
//	}



	private OnCheckedChangeListener changelistener= new OnCheckedChangeListener(){
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			switch(buttonView.getId())
			{
				case R.id.dec_hw:
					if(isChecked)  {
					  pre.putBooleanAndCommit("hardware", true);  
					  pre.putBooleanAndCommit("software", false);
					  soft.setChecked(false);
					  hw.setChecked(true);
					  temp_hw=true;
					  temp_sw=false;
					}
		               
		            break;
				case R.id.dec_soft:
					if(isChecked)  {
						  pre.putBooleanAndCommit("hardware", false);  
						  pre.putBooleanAndCommit("software", true);
						  hw.setChecked(false);
						  soft.setChecked(true);
						  temp_sw=true;
						  temp_hw=false;
						}
			              
			          break;
			    default:
			    	if(temp_hw){
			    		soft.setChecked(false);
						hw.setChecked(true);
			    	}else if(temp_sw){
			    		hw.setChecked(false);
						soft.setChecked(true);
			    	}
			    	break;
			}
			
		}
		
		
	} ;
	

}
