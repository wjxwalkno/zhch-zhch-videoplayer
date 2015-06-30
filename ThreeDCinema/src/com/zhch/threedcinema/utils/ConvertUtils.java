package com.zhch.threedcinema.utils;

import android.content.Context;

/**
 * 数据转换�?
 * 
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140
 * 
 */
public final class ConvertUtils {

	private static final float UNIT = 1000.0F;

	/**
	 * 毫秒转秒
	 * 
	 * @param time 毫秒
	 * @return
	 */
	public static float ms2s(long time) {
		return time / UNIT;
	}

	/**
	 * 微秒转秒
	 * 
	 * @param time 微秒
	 * @return
	 */
	public static float us2s(long time) {
		return time / UNIT / UNIT;
	}

	/**
	 * 纳秒转秒
	 * 
	 * @param time 纳秒
	 * @return
	 */
	public static float ns2s(long time) {
		return time / UNIT / UNIT / UNIT;
	}

	/**
	 * 转换字符串为boolean
	 * 
	 * @param str
	 * @return
	 */
	public static boolean toBoolean(String str) {
		return toBoolean(str, false);
	}

	/**
	 * 转换字符串为boolean
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static boolean toBoolean(String str, boolean def) {
		if (StringUtils.isEmpty(str))
			return def;
		if ("false".equalsIgnoreCase(str) || "0".equals(str))
			return false;
		else if ("true".equalsIgnoreCase(str) || "1".equals(str))
			return true;
		else
			return def;
	}

	/**
	 * 转换字符串为float
	 * 
	 * @param str
	 * @return
	 */
	public static float toFloat(String str) {
		return toFloat(str, 0F);
	}

	/**
	 * 转换字符串为float
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static float toFloat(String str, float def) {
		if (StringUtils.isEmpty(str))
			return def;
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为long
	 * 
	 * @param str
	 * @return
	 */
	public static long toLong(String str) {
		return toLong(str, 0L);
	}

	/**
	 * 转换字符串为long
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static long toLong(String str, long def) {
		if (StringUtils.isEmpty(str))
			return def;
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为short
	 * 
	 * @param str
	 * @return
	 */
	public static short toShort(String str) {
		return toShort(str, (short) 0);
	}

	/**
	 * 转换字符串为short
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static short toShort(String str, short def) {
		if (StringUtils.isEmpty(str))
			return def;
		try {
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为int
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt(String str) {
		return toInt(str, 0);
	}

	/**
	 * 转换字符串为int
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static int toInt(String str, int def) {
		if (StringUtils.isEmpty(str))
			return def;
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	public static String toString(Object o) {
		return toString(o, "");
	}

	public static String toString(Object o, String def) {
		if (o == null)
			return def;

		return o.toString();
	}

	public static int dipToPX(final Context ctx, int dip) {
		float scale = ctx.getResources().getDisplayMetrics().density;
		return (int) (dip / 1.5D * scale + 0.5D);
	}
}
