package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.CateInfo;
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
 * @description 分类数据库管理
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-7-24 下午7:50:51
 *
 */
public class CateDao {
	private static final String TABLE_CATE = "kincai_cate";
	private static final String TAG = "CaterDao";
	private Context mContext;

	public CateDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 保存分类信息
	 * 
	 * @param list
	 * @return
	 */
	public long saveUserInfo(List<CateInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		long row = 0;
		for (CateInfo info : list) {
			
			ContentValues cv = new ContentValues();
			cv.put("cateId", info.getId());
			cv.put("cName", info.getcName());
			
			row = db.insert(TABLE_CATE, null, cv);

		}

		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 得到所有分类
	 * 
	 * @return
	 */
	public List<CateInfo> getCateInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<CateInfo> list = new ArrayList<CateInfo>();
		String sql = "select * from " + TABLE_CATE;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				CateInfo cateInfo = new CateInfo();
				cateInfo.setId(cursor.getInt(cursor.getColumnIndex("cateId")));
				cateInfo.setcName(cursor.getString(cursor.getColumnIndex("cName")));
				list.add(cateInfo);
			}
			
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG+" 指定分类查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到指定分离
	 * 
	 * @return
	 */
	public List<CateInfo> getUserInfoOne(String username) {
		LogTest.LogMsg(TAG+" 指定分类查询", "指定分类 " + username);
		if (username != null) {
			SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
			List<CateInfo> list = new ArrayList<CateInfo>();
			String sql = "select * from " + TABLE_CATE + " where username="
					+ "'" + username + "'";
			Cursor cursor = db.rawQuery(sql, null);
			LogTest.LogMsg(TAG+" 指定分类查询", "结果集数 " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					LogTest.LogMsg(TAG+"指定分类查询", "" + "111111 循环添加");
					CateInfo cateInfo = new CateInfo();
					cateInfo.setId(cursor.getInt(cursor.getColumnIndex("cateId")));
					cateInfo.setcName(cursor.getString(cursor.getColumnIndex("cName")));
					list.add(cateInfo);
					LogTest.LogMsg(TAG+" 指定分类查询", "" + "111111 循环结束");
				}
				cursor.close();
				return list;
			} else {
				LogTest.LogMsg(TAG+" 指定分类查询", "" + "查询不到");
				cursor.close();
				return null;
			}

		} else {
			LogTest.LogMsg(TAG+" 指定分类查询", "" + "指定分类名都为空 叫我查个屁");
			return null;
		}

	}
	
	
	/**
	 * 修改
	 * @param cName
	 * @param name
	 * @return
	 */
	public int upDateUser(String cName, String name) {
		LogTest.LogMsg(TAG, cName+"");
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "cName" + " = ?";
		String[] whereValue = { cName};
		ContentValues cv = new ContentValues();
		cv.put("cName", name);
		
		int row = db.update(TABLE_CATE, cv, where, whereValue);
		return row;
	}
	

	/**
	 * 删除
	 * @param userId
	 * @return
	 */
	public int deleteUser(int id) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		int row = db.delete(TABLE_CATE, where, whereValue);
		LogTest.LogMsg(TAG, "删除id为 "+id+ " 信息");
		return row;
	}

}

