package com.kincai.store.db;

import com.kincai.store.utils.LogTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description sqliteOpenHelper
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.db
 *
 * @time 2015-6-15 上午10:33:00
 *
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseOpenHelper";
	private static SQLiteDatabase mDb;
	private static DatabaseOpenHelper mHelper;
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "kincai_shop.db";
	/** 用户表*/
	private static final String TABLE_USER = "kincai_user";
	/** 搜索历史表*/
	private static final String TABLE_SEARCH_HISTORY = "kincai_search_history";
	/** 搜索模糊查找*/
	private static final String TABLE_SEARCH_ITEM = "kincai_search_item";
	/** 图片路径表*/
	private static final String TABLE_IMAGE = "kincai_image";
	/** 分类表*/
	private static final String TABLE_CATE = "kincai_cate";
	/** 省份*/
	private static final String TABLE_PROVINCE = "kincai_address_province";
	/** 市*/
	private static final String TABLE_CITY = "kincai_address_city";
	/** 县/区*/
	private static final String TABLE_ZONE = "kincai_address_zone";
	
	
	/**
	 * 单例
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getInstance(Context context) {
		if (mDb == null) {
			synchronized(DatabaseOpenHelper.class){
				if(mDb == null) {
					mDb = getHelper(context).getWritableDatabase();
				}
			}
		}
		return mDb;
	}
	
	public static DatabaseOpenHelper getHelper(Context context) {
		if(mHelper == null) {
			mHelper = new DatabaseOpenHelper(context);
		}
		return mHelper;
	}

	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub+ " userId INTEGER,"
		StringBuffer str1 = new StringBuffer();
		str1.append("create table ");
		str1.append(TABLE_USER);
		str1.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str1.append(" user integer, ");
		str1.append(" username varchar(20), password varchar(32), sex varchar(4), email varchar(50), ");
		str1.append(" face varchar(50), regTime varchar(20), activeFlag integer, ");
		str1.append("integral integer, nickname varchar(30), grade integer, login_state integer, device_id integer, unique_id varchar(50))");
		
		db.execSQL(str1.toString());
		
		/*db.execSQL("create table "
				+ TABLE_USER
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " user integer, "
				+ " username varchar(20), password varchar(32), sex varchar(4), email varchar(50), "
				+ " face varchar(50), regTime varchar(20), activeFlag integer, integral integer, nickname varchar(30), grade integer)");*/
		
		StringBuffer str2 = new StringBuffer();
		str2.append("create table ");
		str2.append(TABLE_SEARCH_HISTORY);
		str2.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str2.append(" content varchar(30))");
		db.execSQL(str2.toString());
		/*db.execSQL("create table "
				+ TABLE_SEARCH_HISTORY
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " content varchar(30))");*/
		
		StringBuffer str3 = new StringBuffer();
		str3.append("create table ");
		str3.append(TABLE_SEARCH_ITEM);
		str3.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str3.append(" item varchar(20))");
		db.execSQL(str3.toString());
		/*db.execSQL("create table "
				+ TABLE_SEARCH_ITEM
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " item varchar(20))");*/
		
		StringBuffer str4 = new StringBuffer();
		str4.append("create table ");
		str4.append(TABLE_IMAGE);
		str4.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str4.append(" pId INTEGER,");
		str4.append(" albumPath varchar(60))");
		db.execSQL(str4.toString());
		
		/*db.execSQL("create table "
				+ TABLE_IMAGE
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " pId INTEGER,"
				+ " albumPath varchar(60))");*/
		
		StringBuffer str5 = new StringBuffer();
		str5.append("create table ");
		str5.append(TABLE_CATE);
		str5.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str5.append(" cateId integer,");
		str5.append(" cName varchar(20))");
		db.execSQL(str5.toString());
		/*db.execSQL("create table "
				+ TABLE_CATE
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " cateId integer,"
				+ " cName varchar(20))");*/
		
		StringBuffer str6 = new StringBuffer();
		str6.append("create table ");
		str6.append(TABLE_PROVINCE);
		str6.append(" (province_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str6.append(" province_name varchar(20))");
		db.execSQL(str6.toString());
		/*db.execSQL("create table "
				+ TABLE_PROVINCE
				+ " (province_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " province_name varchar(20))");*/
		
		StringBuffer str7 = new StringBuffer();
		str7.append("create table ");
		str7.append(TABLE_CITY);
		str7.append(" (city_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str7.append(" city_name varchar(20),");
		str7.append(" province_id integer)");
		db.execSQL(str7.toString());
		/*db.execSQL("create table "
				+ TABLE_CITY
				+ " (city_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " city_name varchar(20),"
				+ " province_id integer)");*/
		
		StringBuffer str8 = new StringBuffer();
		str8.append("create table ");
		str8.append(TABLE_ZONE);
		str8.append(" (zone_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		str8.append(" zone_name varchar(20),");
		str8.append(" city_id integer)");
		db.execSQL(str8.toString());
		/*db.execSQL("create table "
				+ TABLE_ZONE
				+ " (zone_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " zone_name varchar(20),"
				+ " city_id integer)");*/
		
		
		LogTest.LogMsg(TAG, "create table"+"->"+TABLE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if (newVersion > oldVersion) {
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_USER).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_SEARCH_HISTORY).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_SEARCH_ITEM).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_IMAGE).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_CATE).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_PROVINCE).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_CITY).toString());
			db.execSQL(new StringBuffer().append("DROP TABLE IF EXISTS ").append(TABLE_ZONE).toString());
			onCreate(db);
		}
		// TODO Auto-generated method stub
		
	}
}
