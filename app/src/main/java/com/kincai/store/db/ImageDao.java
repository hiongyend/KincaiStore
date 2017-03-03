package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.ProImagePathInfo;
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
 * @description 图片数据库管理
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-7-24 下午7:49:58
 *
 */
public class ImageDao {
	private static final String TABLE_IMAGE = "kincai_image";
	private static final String TAG = "ImageDao";
	private Context mContext;

	public ImageDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 图片信息
	 * 
	 * @param list
	 * @return
	 */
	public long saveImagePathInfo(List<ProImagePathInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		long row = 0;
		for (ProImagePathInfo info : list) {

			ContentValues cv = new ContentValues();
			cv.put("pId", info.getpId());
			cv.put("albumPath", info.getAlbumPath());
			row = db.insert(TABLE_IMAGE, null, cv);

		}

		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<ProImagePathInfo> getImagePathInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<ProImagePathInfo> list = new ArrayList<ProImagePathInfo>();
		String sql = "select * from " + TABLE_IMAGE;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ProImagePathInfo proImagePathInfo = new ProImagePathInfo();
				proImagePathInfo.setId(cursor.getInt(cursor
						.getColumnIndex("_id")));
				proImagePathInfo.setpId(cursor.getInt(cursor
						.getColumnIndex("pId")));
				proImagePathInfo.setAlbumPath(cursor.getString(cursor
						.getColumnIndex("albumPath")));
				list.add(proImagePathInfo);
			}
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 图片路径 ", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public List<ProImagePathInfo> getImagePathInfoOne(String pId) {
		LogTest.LogMsg(TAG + " 指定图片路径 ", "指定图片路径 " + pId);
		if (pId != null) {
			SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
			List<ProImagePathInfo> list = new ArrayList<ProImagePathInfo>();
			String sql = "select * from " + TABLE_IMAGE + " where pId="
					+ "'" + pId + "'";
			Cursor cursor = db.rawQuery(sql, null);
			LogTest.LogMsg(TAG + " 指定图片路径 ", "结果集数 " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					LogTest.LogMsg(TAG + " 指定图片路径 ", "" + "111111 循环添加");
					ProImagePathInfo proImagePathInfo = new ProImagePathInfo();
					proImagePathInfo.setId(cursor.getInt(cursor
							.getColumnIndex("_id")));

					proImagePathInfo.setpId(cursor.getInt(cursor
							.getColumnIndex("pId")));
					proImagePathInfo.setAlbumPath(cursor.getString(cursor
							.getColumnIndex("albumPath")));
					list.add(proImagePathInfo);
					LogTest.LogMsg(TAG + " 指定图片路径 ", "" + "111111 循环结束");
				}
				cursor.close();
				return list;
			} else {
				LogTest.LogMsg(TAG + " 指定图片路径 ", "" + "查询不到");
				cursor.close();
				return null;
			}

		} else {
			LogTest.LogMsg(TAG + " 指定图片路径 ", "" + "指定的用户名都为空 叫我查个屁");
			return null;
		}

	}

	/**
	 * 修改用户信息
	 * 
	 * @param userId
	 * @param user
	 *            用户名或者性别或密码
	 *//*
	public int upDateUser(String userId, String flag, String userInfo) {
		LogTest.LogMsg("ererere", userId + "");
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "user" + " = ?";
		String[] whereValue = { userId };
		ContentValues cv = new ContentValues();
		if ("sex".equals(flag)) {
			cv.put("sex", userInfo);

		} else if ("username".equals(flag)) {
			cv.put("username", userInfo);

		} else if ("password".equals(flag)) {
			cv.put("password", userInfo);
		}

		LogTest.LogMsg(TAG, "修改用户信息：" + flag);
		int row = db.update(TABLE_IMAGE, cv, where, whereValue);
		return row;
	}*/

	/**
	 * 
	 * @param Id
	 * @return
	 */
	public int delete(int Id) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "user" + " = ?";
		String[] whereValue = { Integer.toString(Id) };
		int row = db.delete(TABLE_IMAGE, where, whereValue);
		LogTest.LogMsg(TAG, "删除id为 " + Id + " 信息");
		return row;
	}

}
