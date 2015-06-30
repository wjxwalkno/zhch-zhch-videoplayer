package com.zhch.threedcinema.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhch.threedcinema.exception.Logger;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DbHelper<T> {

	/** 新增�?条记�? */
	public int create(T po) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(po.getClass());
			return dao.create(po);
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}

	public boolean exists(T po, Map<String, Object> where) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(po.getClass());
			if (dao.queryForFieldValues(where).size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return false;
	}

	public int createIfNotExists(T po, Map<String, Object> where) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(po.getClass());
			if (dao.queryForFieldValues(where).size() < 1) {
				return dao.create(po);
			}
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}

	/** 查询�?条记�? */
	public List<T> queryForEq(Class<T> c, String fieldName, Object value) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(c);
			return dao.queryForEq(fieldName, value);
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return new ArrayList<T>();
	}

	/** 删除�?条记�? */
	public int remove(T po) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(po.getClass());
			return dao.delete(po);
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}
	public int removeAll(Class<T> c) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(c);
			return dao.delete(dao.queryForAll());
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}
	/**
	 * 根据特定条件更新特定字段
	 * 
	 * @param c
	 * @param values
	 * @param columnName where字段
	 * @param value where�?
	 * @return
	 */
	public int update(Class<T> c, ContentValues values, String columnName, Object value) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(c);
			UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
			updateBuilder.where().eq(columnName, value);
			for (String key : values.keySet()) {
				updateBuilder.updateColumnValue(key, values.get(key));
			}
			return updateBuilder.update();
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}

	/** 更新�?条记�? */
	public int update(T po) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {

			Dao dao = db.getDao(po.getClass());
			return dao.update(po);
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}

	/** 查询�?有记�? */
	public List<T> queryForAll(Class<T> c) {
		SQLiteHelperOrm db = new SQLiteHelperOrm();
		try {
			Dao dao = db.getDao(c);
			return dao.queryForAll();
		} catch (SQLException e) {
			Logger.e(e);
		} finally {
			if (db != null)
				db.close();
		}
		return new ArrayList<T>();
	}
}
