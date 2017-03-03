package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.SearchHistory;
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
 * @description 搜素历史数据库管理
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-7-24 下午7:49:48
 *
 */
public class SearchHistoryDao {
	private static final String TAG = "SearchHistoryDb";
	private static final String TABLE_SEARCH_HISTORY = "kincai_search_history";
	private Context mContext;

	public SearchHistoryDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 保存搜素历史信息
	 * 
	 * @param list
	 * @return
	 */
	public long saveSearchInfo(List<SearchHistory> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		long row = 0;
		for (SearchHistory info : list) {

			ContentValues cv = new ContentValues();
			cv.put("content", info.getContent());

			row = db.insert(TABLE_SEARCH_HISTORY, null, cv);
		}
		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 得到所以历史记录
	 * 
	 * @return
	 */
	public List<SearchHistory> getSearchInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<SearchHistory> list = new ArrayList<SearchHistory>();
		String sql = "select * from " + TABLE_SEARCH_HISTORY;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环添加");
			while (cursor.moveToNext()) {
				SearchHistory searchInfo = new SearchHistory();
				searchInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				searchInfo.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				list.add(searchInfo);
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
	 * 得到搜素历史记录
	 * 
	 * @return
	 */
	public List<SearchHistory> getSearchInfoOne(String content) {
		LogTest.LogMsg(TAG + " 指定内容查询", "指定内容" + content);
		if (content != null) {
			SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
			List<SearchHistory> list = new ArrayList<SearchHistory>();
			String sql = "select * from " + TABLE_SEARCH_HISTORY
					+ " where content=" + "'" + content + "'";
			Cursor cursor = db.rawQuery(sql, null);
			LogTest.LogMsg(TAG + " 指定内容查询", "结果集数 " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					LogTest.LogMsg(TAG + " 指定内容查询", "" + "111111 循环添加");
					SearchHistory searchInfo = new SearchHistory();
					searchInfo
							.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					searchInfo.setContent((cursor.getString(cursor
							.getColumnIndex("content"))));
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
	 * 删除一条搜素历史
	 * 
	 * @param id
	 * @return
	 */
	public int deleteSearchInfo(String id) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "_id" + " = ?";
		String[] whereValue = { id };
		int row = db.delete(TABLE_SEARCH_HISTORY, where, whereValue);
		LogTest.LogMsg(TAG, "删除id为 " + id + " 用户信息");
		return row;
	}

	/**
	 * 删除全部搜素历史
	 * 
	 * @param id
	 * @return
	 */
	public int deleteSearchInfos() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		int row = db.delete(TABLE_SEARCH_HISTORY, null, null);
		LogTest.LogMsg(TAG, row+"删除所以 用户信息");
		
		return row;
	}

}
