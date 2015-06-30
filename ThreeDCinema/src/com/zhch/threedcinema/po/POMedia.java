package com.zhch.threedcinema.po;

import io.vov.vitamio.provider.MediaStore.Video;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 视频PO�?
 * 
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140
 * 
 */
@DatabaseTable(tableName = "media")
public class POMedia {
	@DatabaseField(generatedId = true)
	public long _id;
	/** 视频标题 */
	@DatabaseField
	public String title;
	/** 视频标题拼音 */
	@DatabaseField
	public String title_key;
	/** 视频路径 */
	@DatabaseField
	public String path;
	/** �?后一次访问时�? */
	@DatabaseField
	public long last_access_time;
	/** �?后一次修改时�? */
	@DatabaseField
	public long last_modify_time;
	/** 视频时长 */
	@DatabaseField
	public int duration;
	/** 视频播放进度 */
	@DatabaseField
	public int position;
	/** 视频缩略图路�? */
	@DatabaseField
	public String thumb_path;
	/** 文件大小 */
	@DatabaseField
	public long file_size;
	/** 视频宽度 */
	@DatabaseField
	public int width;
	/** 视频高度 */
	@DatabaseField
	public int height;
	/** MIME类型 */
	public String mime_type;
	/** 0 本地视频 1 网络视频 */
	public int type = 0;

	/** 文件状�??0 - 10 分别代表 下载 0-100% */
	public int status = -1;
	/** 文件临时大小 用于下载 */
	public long temp_file_size = -1L;

	public POMedia() {

	}

	public POMedia(File f) {
		title = f.getName();
		path = f.getAbsolutePath();
		last_modify_time = f.lastModified();
		
		file_size = f.length();
	}

	public POMedia(String path, String mimeType) {
		this(new File(path));
		this.mime_type = mimeType;
	}
	public Bitmap getThumb(Context ctx) {
//		return null;
		return Video.Thumbnails.getThumbnail(ctx.getApplicationContext(), ctx.getContentResolver(), _id, Video.Thumbnails.MICRO_KIND, null);
	}
}
