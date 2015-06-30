package com.zhch.threedcinema.po;

import io.vov.vitamio.provider.MediaStore.Video;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

public final class PFile {
	
	public long _id;
	/** 视频标题 */
	public String title;
	/** 视频标题拼音 */
	public String title_pinyin;
	/** 视频路径 */
	public String path;
	/** �?后一次访问时�? */
	public long last_access_time;
	/** 视频时长 */
	public int duration;
	/** 视频播放进度 */
	public int position;
	/** 视频缩略�? */
	public String thumb;
	/** 文件大小 */
	public long file_size;
	/** 文件状�??0 - 10 分别代表 下载 0-100% */
	public int status = -1;
	/** 文件临时大小 用于下载 */
	public long temp_file_size = -1L;
	/** 视频宽度 */
	public int width;
	/** 视频高度 */
	public int height;

	public PFile() {

	}

	public PFile(Cursor c) {
		//Video.Media._ID, Video.Media.TITLE, Video.Media.TITLE_KEY, Video.Media.SIZE, Video.Media.DURATION, Video.Media.DATA, Video.Media.WIDTH, Video.Media.HEIGHT
		_id = c.getLong(0);
		title = c.getString(1);
		title_pinyin = c.getString(2);
		file_size = c.getLong(3);
		duration = c.getInt(4);
		path = c.getString(5);
		width = c.getInt(6);
		height = c.getInt(7);
	}

	/** 获取缩略�? */
	public Bitmap getThumb(Context ctx) {
//		return null;
		return Video.Thumbnails.getThumbnail(ctx.getApplicationContext(), ctx.getContentResolver(), _id, Video.Thumbnails.MICRO_KIND, null);
	}
}
