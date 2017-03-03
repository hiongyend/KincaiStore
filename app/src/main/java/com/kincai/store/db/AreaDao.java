package com.kincai.store.db;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.bean.AreaCityInfo;
import com.kincai.store.bean.AreaProvinceInfo;
import com.kincai.store.bean.AreaZoneInfo;
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
 * @description 地区选择
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.db
 * 
 * @time 2015-7-8 下午2:38:15
 * 
 */
public class AreaDao {

	/** 省份 */
	private static final String TABLE_PROVINCE = "kincai_address_province";
	/** 市 */
	private static final String TABLE_CITY = "kincai_address_city";
	/** 县/区 */
	private static final String TABLE_ZONE = "kincai_address_zone";
	private static final String TAG = "AddressDao";

	private Context mContext;

	public AreaDao(Context context) {
		this.mContext = context;

	}

	/**
	 * 保存省份
	 * 
	 * @param list
	 * @return
	 */
	public long saveProviceInfo(List<AreaProvinceInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		db.beginTransaction();
		long row = 0;
		try {
			for (AreaProvinceInfo info : list) {

				ContentValues cv = new ContentValues();
				cv.put("province_id", info.getProvince_id());
				cv.put("province_name", info.getProvince_name());

				row = db.insert(TABLE_PROVINCE, null, cv);

			}
			
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.endTransaction();
		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 保存城市
	 * 
	 * @param list
	 * @return
	 */
	public long saveCityInfo(List<AreaCityInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		db.beginTransaction();
		long row = 0;
		try {
			for (AreaCityInfo info : list) {

				ContentValues cv = new ContentValues();
				cv.put("city_id", info.getCity_id());
				cv.put("city_name", info.getCity_name());
				cv.put("province_id", info.getProvince_id());
				row = db.insert(TABLE_CITY, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.endTransaction();
		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 保存县/区
	 * 
	 * @param list
	 * @return
	 */
	public long saveZoneInfo(List<AreaZoneInfo> list) {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		db.beginTransaction();
		long row = 0;
		try {
			for (AreaZoneInfo info : list) {

				ContentValues cv = new ContentValues();
				cv.put("zone_id", info.getZone_id());
				cv.put("zone_name", info.getZone_name());
				cv.put("city_id", info.getCity_id());

				row = db.insert(TABLE_ZONE, null, cv);

			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.endTransaction();
		LogTest.LogMsg(TAG, "row->" + row);
		return row;

	}

	/**
	 * 得到省份
	 * 
	 * @return
	 */
	public List<AreaProvinceInfo> getProvinceInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<AreaProvinceInfo> list = new ArrayList<AreaProvinceInfo>();
		String sql = "select * from " + TABLE_PROVINCE;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AreaProvinceInfo provinceInfo = new AreaProvinceInfo();
				provinceInfo.setProvince_id(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				provinceInfo.setProvince_name(cursor.getString(cursor
						.getColumnIndex("province_name")));
				list.add(provinceInfo);
			}

			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到所有市
	 * 
	 * @return
	 */
	public List<AreaCityInfo> getCityInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<AreaCityInfo> list = new ArrayList<AreaCityInfo>();
		String sql = "select * from " + TABLE_CITY;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AreaCityInfo cityInfo = new AreaCityInfo();
				cityInfo.setCity_id(cursor.getInt(cursor
						.getColumnIndex("city_id")));
				cityInfo.setCity_name(cursor.getString(cursor
						.getColumnIndex("city_name")));
				cityInfo.setProvince_id(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				list.add(cityInfo);
			}

			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到所有县/区
	 * 
	 * @return
	 */
	public List<AreaZoneInfo> getZoneInfo() {
		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<AreaZoneInfo> list = new ArrayList<AreaZoneInfo>();
		String sql = "select * from " + TABLE_ZONE;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				AreaZoneInfo zoneInfo = new AreaZoneInfo();
				zoneInfo.setZone_id(cursor.getInt(cursor
						.getColumnIndex("zone_id")));
				zoneInfo.setZone_name(cursor.getString(cursor
						.getColumnIndex("zone_name")));
				zoneInfo.setCity_id(cursor.getInt(cursor
						.getColumnIndex("city_id")));
				list.add(zoneInfo);
			}

			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}

	/**
	 * 得到指定市
	 * 
	 * @return
	 */
	public List<AreaCityInfo> getCityInfo(int provinceId) {
		LogTest.LogMsg(TAG + " 指定查询", "指定 " + provinceId);

		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<AreaCityInfo> list = new ArrayList<AreaCityInfo>();
		String sql = "select * from " + TABLE_CITY + " where province_id="
				+ provinceId;
		Cursor cursor = db.rawQuery(sql, null);
		LogTest.LogMsg(TAG + " 指定查询", "结果集数 " + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				LogTest.LogMsg(TAG + "指定查询", "" + "111111 循环添加");
				AreaCityInfo cityInfo = new AreaCityInfo();
				cityInfo.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
				cityInfo.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
				cityInfo.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
				list.add(cityInfo);
				cityInfo = null;
				LogTest.LogMsg(TAG + " 指定查询", "" + "111111 循环结束");
			}
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 指定查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}
	
	/**
	 * 得到指定县/区
	 * 
	 * @return
	 */
	public List<AreaZoneInfo> getZoneInfo(int cityId) {
		LogTest.LogMsg(TAG + " 指定查询", "指定 " + cityId);

		SQLiteDatabase db = DatabaseOpenHelper.getInstance(mContext);
		List<AreaZoneInfo> list = new ArrayList<AreaZoneInfo>();
		String sql = "select * from " + TABLE_ZONE + " where city_id="
				+ cityId;
		Cursor cursor = db.rawQuery(sql, null);
		LogTest.LogMsg(TAG + " 指定查询", "结果集数 " + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				LogTest.LogMsg(TAG + "指定查询", "" + "111111 循环添加");
				AreaZoneInfo zoneInfo = new AreaZoneInfo();
				zoneInfo.setZone_id(cursor.getInt(cursor.getColumnIndex("zone_id")));
				zoneInfo.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
				zoneInfo.setZone_name(cursor.getString(cursor.getColumnIndex("zone_name")));
				list.add(zoneInfo);
				zoneInfo = null;
				LogTest.LogMsg(TAG + " 指定查询", "" + "111111 循环结束");
			}
			cursor.close();
			return list;
		} else {
			LogTest.LogMsg(TAG + " 指定查询", "" + "查询不到");
			cursor.close();
			return null;
		}
	}


}
