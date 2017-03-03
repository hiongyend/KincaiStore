package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.SearchItem;
import com.kincai.store.utils.LogTest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 搜素条目数据库管理
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-7-24 下午7:49:34
 *
 */
public class SearchItemDao {
	private static final String TAG = "SearchItemDb";
	private static final String TABLE_SEARCH_ITEM = "kincai_search_item";
	private Context mContext;

	public SearchItemDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 保存搜素信息
	 * 
	 * @param list
	 * @return
	 */
	public long saveSearchInfo(List<SearchItem> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		long row = 0;
		for (SearchItem info : list) {

			ContentValues cv = new ContentValues();
			cv.put("item", info.getItem());
			row = db.insert(TABLE_SEARCH_ITEM, null, cv);
		}
		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 得到全部记录
	 * 
	 * @return
	 */
	public List<SearchItem> getSearchItemInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<SearchItem> list = new ArrayList<SearchItem>();
		String sql = "select * from " + TABLE_SEARCH_ITEM;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环添加");
			while (cursor.moveToNext()) {
				SearchItem searchItem = new SearchItem();
				searchItem.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				searchItem.setItem(cursor.getString(cursor
						.getColumnIndex("item")));
				list.add(searchItem);
			}
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环结束");
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}
	/**
	 * 得到全部模糊记录
	 * 
	 * @return
	 */
	public List<SearchItem> getFuzzyQueryInfo(String item) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<SearchItem> list = new ArrayList<SearchItem>();
		String sql = "select * from " + TABLE_SEARCH_ITEM +" where item like '" +item+ "%'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环添加");
			while (cursor.moveToNext()) {
				SearchItem searchItem = new SearchItem();
				searchItem.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				searchItem.setItem(cursor.getString(cursor
						.getColumnIndex("item")));
				list.add(searchItem);
			}
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环结束");
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到一条记录
	 * 
	 * @return
	 */
	public List<SearchItem> getSearchItemOne(String item) {
		
		if (item != null) {
			LogTest.LogMsg(TAG + " 指定内容查询", "指定内容" + item);
			SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
			List<SearchItem> list = new ArrayList<SearchItem>();
			String sql = "select * from " + TABLE_SEARCH_ITEM
					+ " where item=" + "'" + item + "'";
			Cursor cursor = db.rawQuery(sql, null);
			LogTest.LogMsg(TAG + " 指定内容查询", "结果集数 " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环添加");
					SearchItem searchInfo = new SearchItem();
					searchInfo
							.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					searchInfo.setItem(cursor.getString(cursor
							.getColumnIndex("item")));
					list.add(searchInfo);
					LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环结束");
				}
				cursor.close();
				return list;
			} else {
				LogTest.LogMsg(TAG + " 指定内容查询", "" + "查询不到");
				cursor.close();
				return null;
			}

		} else {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "指定的内容都为空 叫我查个屁");
			return null;
		}

	}

	/**
	 * 修改历史信息
	 * 
	 */
	/*
	 * public int upDateUser(String userId, String flag, String userInfo) {
	 * LogTest.LogMsg("ererere", userId+""); SQLiteDatabase db =
	 * DatabaseOpenHelper.getInstance(mContext); String where = "user" + " = ?";
	 * String[] whereValue = { userId}; ContentValues cv = new ContentValues();
	 * if("sex".equals(flag)) { cv.put("sex", userInfo); } else if
	 * ("username".equals(flag)) { cv.put("username", userInfo);
	 * 
	 * } else if ("password".equals(flag)) { cv.put("password", userInfo); }
	 * 
	 * LogTest.LogMsg("UserDb", "修改用户信息："+flag); int row =
	 * db.update(TABLE_SEARCH_HISTORY, cv, where, whereValue); return row; }
	 */

	/**
	 * 删除一条搜素item
	 * 
	 * @param id
	 * @return
	 */
	public int deleteSearchItem(String id) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "_id" + " = ?";
		String[] whereValue = { id };
		int row = db.delete(TABLE_SEARCH_ITEM, where, whereValue);
		LogTest.LogMsg(TAG, "删除id为 " + id + " 用户信息");
		return row;
	}

	/**
	 * 删除全部
	 * 
	 * @param id
	 * @return
	 */
	public int deleteSearchitems() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		int row = db.delete(TABLE_SEARCH_ITEM, null, null);
		LogTest.LogMsg(TAG, "删除所以 用户信息");
		return row;
	}

}
