package com.kincai.store.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 时间转换工具
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:34:12
 *
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

/*	public final static String FORMAT_YEAR = "yyyy";
	public final static String FORMAT_MONTH_DAY = "MM月dd日";

	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
	public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat();*/
//	private static final int YEAR = 365 * 24 * 60 * 60;// 年
//	private static final int MONTH = 30 * 24 * 60 * 60;// 月
//	private static final int DAY = 24 * 60 * 60;// 天
//	private static final int HOUR = 60 * 60;// 小时
//	private static final int MINUTE = 60;// 分钟

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 * 
	 * @param timestamp
	 *            时间戳 单位为毫秒
	 * @return 时间字符串
	 */
/*	public static String getDescriptionTimeFromTimestamp(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		System.out.println("timeGap: " + timeGap);
		String timeStr = null;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}*/

	/**
	 * 获取当前日期的指定格式的字符串
	 * 
	 * @param format
	 *            指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 */
/*	public static String getCurrentTime(String format) {
		if (format == null || format.trim().equals("")) {
			sdf.applyPattern(FORMAT_DATE_TIME);
		} else {
			sdf.applyPattern(format);
		}
		return sdf.format(new Date());
	}*/

	// date类型转换为String类型
	// formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	// data Date类型的时间
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}
	
	/**
	 * 得到年份 2015 的15
	 * @return 后兩位
	 */
	public static String getYearStr2() {
		 Calendar a=Calendar.getInstance();
	     return String.valueOf(a.get(Calendar.YEAR)).substring(2);
	}
	
	/**
	 * 得到年份 2015 
	 * @return 四位
	 */
	public static String getYearStr() {
		 Calendar a=Calendar.getInstance();
	     return String.valueOf(a.get(Calendar.YEAR));
	}

	/**
	 *  long类型转换为String类型
	 * @param currentTime 要转换的long类型的时间
	 * @param formatType 要转换的string类型的时间格式
	 * @return
	 */
	public static String longToString(long currentTime, String formatType) {
		String strTime = "";
		Date date = longToDate(currentTime, formatType);// long类型转成Date类型
		strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}
	
	/**
	 * string类型的时间戳转字符串
	 * @return
	 */
	public static String StringLongToString(String currentTime, String formatType) {
		String strTime = "";
		Date date = longToDate(Long.parseLong(currentTime), formatType);// long类型转成Date类型
		strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	/**
	 * string类型转换为date类型
	 * strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	 * @param strTime 
	 * @param formatType HH时mm分ss秒，
	 * @return strTime的时间格式必须要与formatType的时间格式相同
	 */
	public static Date stringToDate(String strTime, String formatType) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	// long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType) {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType) {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}
	/**
	 * tring类型转换为string类型 转为时间戳字符串
	 * @param strTime 要转换的String类型的时间
	 * @param formatType 时间格式
	 * @return
	 */
	public static String stringToString(String strTime, String formatType) {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return null;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return String.valueOf(currentTime);
		}
	}
	

	/**
	 *  date类型转换为long类型
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	/**
	 * 得到系统当前时间的时间戳
	 * 
	 * @return
	 */
	public static String getCurrentTimeMillis() {
		return String.valueOf(System.currentTimeMillis());
	}
	/**
	 * 返回年-月-日 小时:分钟
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	/**
	 * 返回小时:分钟
	 * @param time
	 * @return
	 */
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
	
	/**
	 * 时间差 xx天xx小时xx分钟
	 * @param TimeMillis
	 * @return
	 */
	public static String getTimeDifference(long TimeMillis) {
		StringBuilder stringBuilder = new StringBuilder();
		int day = (int) (TimeMillis / (86400 * 1000));
		System.out.println("day "+day);
		long hourTime = TimeMillis;
		if(day <= 0) {
			stringBuilder.append("");
		} else {
			stringBuilder.append(String.valueOf(day)).append("天");
			hourTime = TimeMillis - (day * 86400 * 1000);
		}
		int hour = (int) (hourTime / (3600 * 1000 ));
		System.out.println("hour "+hour);
		long minuteTime = hourTime;
		if(hour <= 0) {
			stringBuilder.append(day > 0 ? "0小时" : "");
		} else {
			stringBuilder.append(String.valueOf(hour)).append("小时");
			minuteTime = hourTime - (hour * 3600 * 1000);
		}
		int minute = (int) (minuteTime / (60 * 1000));
		System.out.println("minute "+minute);
		if(minute <= 0) {
			stringBuilder.append("");
		} else {
			stringBuilder.append(String.valueOf(minute)).append("分钟");
		}
		
		return stringBuilder.toString();

	}
	
	public static String getMillis(int day) {
		return String.valueOf(day * 24 * 60 * 60 * 1000);
	}


}
