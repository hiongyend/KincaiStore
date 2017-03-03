package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.UserInfo;
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
 * @description 用户数据库管理
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-7-24 下午7:49:15
 *
 */
public class UserDao {
	private static final String TABLE_USER = "kincai_user";
	private static final String TAG = "UserDao";
	private Context mContext;

	public UserDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 保存用户信息
	 * 
	 * @param list
	 * @return
	 */
	public long saveUserInfo(List<UserInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		long row = 0;
		for (UserInfo info : list) {

			ContentValues cv = new ContentValues();
			cv.put("user", info.getUserId());
			cv.put("username", info.getUsername());
			cv.put("password", info.getPassword());
			cv.put("sex", info.getSex());
			cv.put("email", info.getEmail());
			cv.put("face", info.getFace());
			cv.put("regTime", info.getRegTime());
			cv.put("activeFlag", info.getActiveFlag());
			cv.put("integral", info.getIntegral());
			cv.put("nickname", info.getNickname());
			cv.put("grade", info.getGrade());
			cv.put("login_state", info.getLoginState());
			cv.put("device_id", info.getDeviceId());
			cv.put("unique_id", info.getUniqueId());
			
			row = db.insert(TABLE_USER, null, cv);

		}

		LogTest.LogMsg("UserDb", "row->" + row);
		return row;

	}

	/**
	 * 得到所有用户
	 * 
	 * @return
	 */
	public List<UserInfo> getUserInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<UserInfo> list = new ArrayList<UserInfo>();
		String sql = "select * from " + TABLE_USER;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				userInfo.setUserId(cursor.getInt(cursor.getColumnIndex("user")));
				userInfo.setUsername(cursor.getString(cursor
						.getColumnIndex("username")));
				userInfo.setPassword(cursor.getString(cursor
						.getColumnIndex("password")));
				userInfo.setSex(cursor.getString(cursor.getColumnIndex("sex")));
				userInfo.setEmail(cursor.getString(cursor
						.getColumnIndex("email")));
				userInfo.setFace(cursor.getString(cursor.getColumnIndex("face")));

				userInfo.setRegTime(cursor.getString(cursor
						.getColumnIndex("regTime")));
				userInfo.setActiveFlag(cursor.getInt(cursor
						.getColumnIndex("activeFlag")));
				userInfo.setIntegral(cursor.getInt(cursor
						.getColumnIndex("integral")));
				userInfo.setNickname(cursor.getString(cursor
						.getColumnIndex("nickname")));
				userInfo.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
				userInfo.setLoginState(cursor.getString(cursor
						.getColumnIndex("login_state")));
				userInfo.setDeviceId(cursor.getString(cursor
						.getColumnIndex("device_id")));
				userInfo.setUniqueId(cursor.getString(cursor
						.getColumnIndex("unique_id")));
				list.add(userInfo);
			}
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 用户查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到指定用户
	 * 
	 * @return
	 */
	public List<UserInfo> getUserInfoOne(String username) {
		LogTest.LogMsg(TAG + " 指定用户查询", "指定用户 " + username);
		if (username != null) {
			SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
			List<UserInfo> list = new ArrayList<UserInfo>();
			String sql = "select * from " + TABLE_USER + " where username="
					+ "'" + username + "'";
			Cursor cursor = db.rawQuery(sql, null);
			LogTest.LogMsg(TAG + " 指定用户查询", "结果集数 " + cursor.getCount());
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					LogTest.LogMsg(TAG + "指定用户查询", "" + "111111 循环添加");
					UserInfo userInfo = new UserInfo();
					userInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					userInfo.setUserId(cursor.getInt(cursor
							.getColumnIndex("user")));
					userInfo.setUsername(cursor.getString(cursor
							.getColumnIndex("username")));
					userInfo.setPassword(cursor.getString(cursor
							.getColumnIndex("password")));
					userInfo.setSex(cursor.getString(cursor
							.getColumnIndex("sex")));
					userInfo.setEmail(cursor.getString(cursor
							.getColumnIndex("email")));
					userInfo.setFace(cursor.getString(cursor
							.getColumnIndex("face")));

					userInfo.setRegTime(cursor.getString(cursor
							.getColumnIndex("regTime")));
					userInfo.setActiveFlag(cursor.getInt(cursor
							.getColumnIndex("activeFlag")));
					userInfo.setIntegral(cursor.getInt(cursor
							.getColumnIndex("integral")));
					userInfo.setNickname(cursor.getString(cursor
							.getColumnIndex("nickname")));
					userInfo.setGrade(cursor.getInt(cursor
							.getColumnIndex("grade")));
					userInfo.setLoginState(cursor.getString(cursor
							.getColumnIndex("login_state")));
					userInfo.setDeviceId(cursor.getString(cursor
							.getColumnIndex("device_id")));
					userInfo.setUniqueId(cursor.getString(cursor
							.getColumnIndex("unique_id")));
					list.add(userInfo);
					LogTest.LogMsg(TAG + " 指定用户查询", "" + "111111 循环结束");
				}
				cursor.close();
				return list;
			} else {
				LogTest.LogMsg(TAG + " 指定用户查询", "" + "查询不到");
				cursor.close();
				return null;
			}

		} else {
			LogTest.LogMsg(TAG + " 指定用户查询", "" + "指定的用户名都为空 叫我查个屁");
			return null;
		}

	}

	/**
	 * 修改用户信息
	 * 
	 * @param userId
	 * @param user
	 *            用户名或者性别或密码
	 */
	public int upDateUser(String userId, String flag, String userInfo) {
		LogTest.LogMsg(TAG, userId + "");
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "user" + " = ?";
		String[] whereValue = { userId };
		ContentValues cv = new ContentValues();
		if ("sex".equals(flag)) {
			cv.put("sex", userInfo);

		} else if ("nickname".equals(flag)) {
			cv.put("nickname", userInfo);

		} else if ("password".equals(flag)) {
			cv.put("password", userInfo);
		}

		LogTest.LogMsg(TAG, "修改用户信息：" + flag);
		int row = db.update(TABLE_USER, cv, where, whereValue);
		return row;
	}
	
	/**
	 * 修改用户单条数据
	 * @param userId
	 * @param userInfo
	 * @return
	 */
	public int upDateOne(String userId, String userInfo) {
		LogTest.LogMsg(TAG, userId + "");
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "user" + " = ?";
		String[] whereValue = { userId };
		ContentValues cv = new ContentValues();
		cv.put("face", userInfo);

		int row = db.update(TABLE_USER, cv, where, whereValue);
		return row;
	}

	/**
	 * 删除用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteUser(int userId) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		String where = "user" + " = ?";
		String[] whereValue = { Integer.toString(userId) };
		int row = db.delete(TABLE_USER, where, whereValue);
		LogTest.LogMsg(TAG, "删除id为 " + userId + " 用户信息");
		return row;
	}

}
