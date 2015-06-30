package com.zhch.threedcinema.adapter;

import java.util.ArrayList;

import com.zhch.threedcinema.ui.R;
import com.zhch.threedcinema.po.PFile;
import com.zhch.threedcinema.utils.FileUtils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends CursorAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Character> mKey = new ArrayList<Character>();

	public FileAdapter(Context context, Cursor c) {
		super(context, c, false);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.item_localpicture, null);
	}

	@Override
	public void changeCursor(Cursor cursor) {
		initKeys(cursor);
	  super.changeCursor(cursor);
	}
	
	private void initKeys(Cursor c) {
		if (c != null && !c.isClosed()) {
			mKey.clear();
			while (c.moveToNext()) {
				String title_key = c.getString(2);
				mKey.add(title_key.charAt(0));
			}
		}
	}

	/** 鏍规嵁鎷奸煶鍚嶇О鑾峰彇浣嶇疆 */
	public int getPositionByName(char key) {
		return mKey.indexOf(key);
	}

	@Override
	public PFile getItem(int position) {
		if (getCursor().moveToPosition(position))
			return new PFile(getCursor());
		else
			return null;
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		final PFile f = new PFile(cursor);
		//影片名
		((TextView) convertView.findViewById(R.id.local_title)).setText(f.title);

		//获取影片大小
		String file_size;
		if (f.temp_file_size > 0) {
			file_size = FileUtils.showFileSize(f.temp_file_size) + " / " + FileUtils.showFileSize(f.file_size);
		} else {
			file_size = FileUtils.showFileSize(f.file_size);
		}
	//	((TextView) convertView.findViewById(R.id.local_title)).setText(f.title);
		//影片截图
	//	((ImageView) convertView.findViewById(R.id.local_image)).setImageBitmap(f.getThumb(context));
	}

}
