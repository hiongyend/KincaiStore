package com.kincai.store.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 解析assets目录下的地区xml数据 pull和dom解析
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.utils
 * 
 * @time 2015-7-9 上午8:42:56
 * 
 */
public class GetAssetsFiles {
	private static final String TAG = "GetAssetsFiles";

	/**
	 * 使用pull解析
	 * @param context
	 * @return
	 */
	public static List<Map<String, Object>> getAddressStrForPull(Context context) {
		List<Map<String, Object>> maps = null;
		Map<String, Object> map = null;
		try {
			InputStream is = context.getAssets().open("province_city_zone_data/province_city_zone.xml");
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(is, "utf-8");
			int event = xmlParser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {// 不等于文档末尾
				LogTest.LogMsg(TAG, "event " + event);
				switch (event) {
				case XmlPullParser.START_TAG:// 开始
					if ("map".equals(xmlParser.getName())) {
						maps = new ArrayList<Map<String, Object>>();
						map = new HashMap<String, Object>();
					} else if ("zone".equals(xmlParser.getName())) {
						map.put("zone", xmlParser.nextText());
					} else if ("province".equals(xmlParser.getName())) {
						map.put("province", xmlParser.nextText());
					} else if ("city".equals(xmlParser.getName())) {
						map.put("city", xmlParser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:// 结束
					if ("map".equals(xmlParser.getName())) {
						maps.add(map);
					}

				}
				// 解析下一个节点
				event = xmlParser.next();
			}
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;

	}
	
	/**
	 * 使用dom解析
	 * @param context
	 * @return
	 */
	public static List<Map<String, Object>> getAddressStrForDom(Context context) {
		
		
		try {
			List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			InputStream is = context.getAssets().open("province_city_zone_data/province_city_zone.xml");
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element element = document.getDocumentElement();//得到的是整个xml
			
			map.put("zone", element.getElementsByTagName("zone").item(0).getTextContent());//得到的是根节点西zone节点的内容
			map.put("province", element.getElementsByTagName("province").item(0).getTextContent());
			map.put("city", element.getElementsByTagName("city").item(0).getTextContent());
			maps.add(map);
			return maps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
		
	}
}
