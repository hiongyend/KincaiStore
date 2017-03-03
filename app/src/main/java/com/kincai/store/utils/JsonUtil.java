package com.kincai.store.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.kincai.store.Constants;
import com.kincai.store.bean.AreaCityInfo;
import com.kincai.store.bean.AddressInfo;
import com.kincai.store.bean.AreaProvinceInfo;
import com.kincai.store.bean.AreaZoneInfo;
import com.kincai.store.bean.AdvInfo;
import com.kincai.store.bean.CateInfo;
import com.kincai.store.bean.HomeGridInfo;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.SearchItem;
import com.kincai.store.bean.UserInfo;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description JSON转换工具
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:36:40
 *
 */
public class JsonUtil {

	/**
	 * 解析{"data":[{"":""}{"":""}...]}类型
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> listJson(String key,
			String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					Map<String, Object> map = new HashMap<String, Object>();
					Iterator<String> iterator = jsonObject2.keys();
					while (iterator.hasNext()) {
						String json_key = iterator.next();
						Object json_value = jsonObject2.get(json_key);
						if (json_value == null) {
							json_value = "";
						}
						map.put(json_key, json_value);
					}
					list.add(map);
					map = null;
				}
				return list;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;

	}

	/**
	 * 解析用户
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<UserInfo> getList(String key, String jsonString) {

		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					UserInfo userInfo = new UserInfo();
					// userInfo.setId(Integer.parseInt(jsonObject2.getString("id")));
					userInfo.setUserId(jsonObject2.getInt("id"));
					userInfo.setUsername(jsonObject2.getString("username"));
					userInfo.setPassword(jsonObject2.getString("password"));
					userInfo.setEmail(jsonObject2.getString("email"));
					userInfo.setFace(jsonObject2.getString("face"));
					userInfo.setSex(jsonObject2.getString("sex"));
					userInfo.setRegTime(jsonObject2.getString("regTime"));
					userInfo.setActiveFlag(Integer.parseInt(jsonObject2
							.getString("activeFlag")));
					userInfo.setIntegral(jsonObject2.getInt("integral"));
					userInfo.setNickname(jsonObject2.getString("nickname"));
					userInfo.setGrade(jsonObject2.getInt("grade"));
					userInfo.setLoginState(jsonObject2.getString("login_state"));
					userInfo.setDeviceId(jsonObject2.getString("device_id"));
					userInfo.setUniqueId(jsonObject2.getString("unique_id"));
					list.add(userInfo);

				}
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;

	}

	/**
	 * 解析商品
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<ProInfo> getPro(String key, String jsonString) {

		List<ProInfo> list = new ArrayList<ProInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				System.out.println("34444444444444444333");
				
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					ProInfo proInfo = new ProInfo();
					proInfo.setId(jsonObject2.getInt("id"));
					proInfo.setStoreId(jsonObject2.getInt("store_id"));
					proInfo.setpName(jsonObject2.getString("pName"));
					proInfo.setpNum(jsonObject2.getInt("pNum"));
					proInfo.setpSn(jsonObject2.getString("pSn"));
					proInfo.setpDesc(jsonObject2.getString("pDesc"));
					proInfo.setmPrice(jsonObject2.getString("mPrice"));
					proInfo.setiPrice(jsonObject2.getString("iPrice"));
					proInfo.setIsHot(jsonObject2.getInt("isHot"));
					proInfo.setIsShow(jsonObject2.getInt("isShow"));
					proInfo.setcId(jsonObject2.getInt("cId"));
					proInfo.setPubTime(jsonObject2.getString("pubTime"));
					proInfo.setEvaluateNum(jsonObject2.getInt("evaluate_num"));
					proInfo.setSaleNum(jsonObject2.getInt("sale_num"));
					proInfo.setSupportNum(jsonObject2.getInt("support_num"));
					proInfo.setIntegral(jsonObject2.getInt("integral"));
					proInfo.setPaynum(jsonObject2.getInt("pay_num"));
					proInfo.setUrl(jsonObject2.getString("url"));
					list.add(proInfo);
					proInfo = null;

				}
				
				System.out.println(list.size()+"++");
				return list;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解析图片
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<ProImagePathInfo> getImgPath(String key,
			String jsonString) {

		List<ProImagePathInfo> list = new ArrayList<ProImagePathInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					ProImagePathInfo proImagePathInfo = new ProImagePathInfo();
					proImagePathInfo.setId(jsonObject2.getInt("id"));
					proImagePathInfo.setpId(jsonObject2.getInt("pId"));
					proImagePathInfo.setAlbumPath(jsonObject2
							.getString("albumPath"));
					list.add(proImagePathInfo);

				}

			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;

	}

	/**
	 * 解析广告
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<AdvInfo> getIndexAdv(String key, String jsonString) {

		List<AdvInfo> list = new ArrayList<AdvInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					AdvInfo advInfo = new AdvInfo();
					advInfo.setId(jsonObject2.getInt("id"));
					advInfo.setImagePath(jsonObject2.getString("imagePath"));
					advInfo.setAdvUrl(jsonObject2.getString("advUrl"));
					list.add(advInfo);

				}

			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;

	}

	/**
	 * 解析收货地址
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<AddressInfo> getAddress(String key, String jsonString) {

		List<AddressInfo> list = new ArrayList<AddressInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					AddressInfo addressInfo = new AddressInfo();
					// userInfo.setId(Integer.parseInt(jsonObject2.getString("id")));
					addressInfo.setId(jsonObject2.getInt("id"));
					addressInfo.setUserId(jsonObject2.getInt("userId"));
					addressInfo
							.setConsignee(jsonObject2.getString("consignee"));
					addressInfo.setPhoneNum(jsonObject2.getString("phoneNum"));
					addressInfo.setPostalCode(jsonObject2
							.getString("postalCode"));
					addressInfo.setArea(jsonObject2.getString("area"));
					addressInfo.setDetailedAddress(jsonObject2
							.getString("detailedAddress"));
					addressInfo.setAddTime(jsonObject2.getString("addTime"));
					addressInfo.setIsDefault(jsonObject2.getInt("isDefault"));
					list.add(addressInfo);

				}

			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;

	}
	
	/**
	 * 解析分类 cate
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<CateInfo> getCate(String key, String jsonString) {
		
		List<CateInfo> list = new ArrayList<CateInfo>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					CateInfo cateInfo = new CateInfo();
					// userInfo.setId(Integer.parseInt(jsonObject2.getString("id")));
					cateInfo.setId(jsonObject2.getInt("id"));
					cateInfo.setcName(jsonObject2.getString("cName"));
					list.add(cateInfo);
					
				}
				
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;
		
	}

	/**
	 * 得到解析的搜索条目列表
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static List<SearchItem> getSearchItemList(String key,
			String jsonString) {

		List<SearchItem> list = new ArrayList<SearchItem>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray jsonArray = jsonObject.getJSONArray(key);
			if (jsonArray != null) {
				int jsonArrayLength = jsonArray.length();
				for (int i = 0; i < jsonArrayLength; i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					SearchItem searchItem = new SearchItem();
					// userInfo.setId(Integer.parseInt(jsonObject2.getString("id")));
					searchItem.setId(Integer.parseInt(jsonObject2
							.getString("id")));
					searchItem.setItem(jsonObject2.getString("searchItem"));
					list.add(searchItem);

				}
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return list;

	}

	/**
	 * 解析获得message
	 * 
	 * @param key
	 * @param jsonString
	 * @return
	 */
	public static String msgJson(String key, String jsonString) {

		String msg = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			msg = jsonObject.getString(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return msg;
	}
	
	/**
	 * 解析省份
	 * @param jsonString
	 * @return
	 */
	public static List<AreaProvinceInfo> getProvince(String jsonString) {
		
		List<AreaProvinceInfo> provinceInfos = null;
		try {
			JSONArray provinceArray = new JSONArray(jsonString);
			if(null != provinceArray) {
				int proLenght = provinceArray.length();
				provinceInfos = new ArrayList<AreaProvinceInfo>();
				for(int i = 0; i < proLenght; i++) {
					AreaProvinceInfo provinceInfo = new AreaProvinceInfo();
					JSONObject jsonObject = provinceArray.getJSONObject(i);
					provinceInfo.setProvince_id(jsonObject.getInt("province_id"));
					provinceInfo.setProvince_name(jsonObject.getString("province_name"));
					provinceInfos.add(provinceInfo);
					provinceInfo = null;
					
				}
				
			}
			
			return provinceInfos;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	/**
	 * 解析市
	 * @param jsonString
	 * @return
	 */
	public static List<AreaCityInfo> getCity(String jsonString) {
		
		List<AreaCityInfo> cityInfos = null;
		try {
			JSONArray provinceArray = new JSONArray(jsonString);
			if(null != provinceArray) {
				int proLenght = provinceArray.length();
				cityInfos = new ArrayList<AreaCityInfo>();
				for(int i = 0; i < proLenght; i++) {
					AreaCityInfo cityInfo = new AreaCityInfo();
					JSONObject jsonObject = provinceArray.getJSONObject(i);
					cityInfo.setCity_id(jsonObject.getInt("city_id"));
					cityInfo.setProvince_id(jsonObject.getInt("province_id"));
					cityInfo.setCity_name(jsonObject.getString("city_name"));
					cityInfos.add(cityInfo);
					cityInfo = null;
					
				}
				
			}
			
			return cityInfos;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	/**
	 * 解析县/区
	 * @param jsonString
	 * @return
	 */
	public static List<AreaZoneInfo> getZone(String jsonString) {
		
		List<AreaZoneInfo> zoneInfos = null;
		try {
			JSONArray provinceArray = new JSONArray(jsonString);
			if(null != provinceArray) {
				int proLenght = provinceArray.length();
				zoneInfos = new ArrayList<AreaZoneInfo>();
				for(int i = 0; i < proLenght; i++) {
					AreaZoneInfo zoneInfo = new AreaZoneInfo();
					JSONObject jsonObject = provinceArray.getJSONObject(i);
					zoneInfo.setZone_id(jsonObject.getInt("zone_id"));
					zoneInfo.setCity_id(jsonObject.getInt("city_id"));
					zoneInfo.setZone_name(jsonObject.getString("zone_name"));
					zoneInfos.add(zoneInfo);
					zoneInfo = null;
					
				}
				
			}
			
			return zoneInfos;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

	
	/**
	 * 解析首页gridview标签数据
	 * @param jsonString
	 * @return
	 */
	public static List<HomeGridInfo> getGridInfos(String jsonString) {
		
		List<HomeGridInfo> gridInfos = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray data = jsonObject.getJSONArray("data");
				int Lenght = data.length();
				gridInfos = new ArrayList<HomeGridInfo>();
				for(int i = 0; i < Lenght; i++) {
					HomeGridInfo gridInfo = new HomeGridInfo();
					JSONObject jsonObject2 = data.getJSONObject(i);
					gridInfo.setId(jsonObject2.getInt("id"));
					gridInfo.setTitle(jsonObject2.getString("title"));
					gridInfo.setImageUrl(Constants.GRIDTAB_URL+jsonObject2.getString("imageUrl"));
					gridInfos.add(gridInfo);
					gridInfo = null;
					
				}
				
			
			
			return gridInfos;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

	
	/**
	 * 解析科大讯飞语音识别数据
	 * @param json
	 * @return
	 */
	public static String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
				// 如果需要多候选结果，解析数组其他字段
				// for(int j = 0; j < items.length(); j++)
				// {
				// JSONObject obj = items.getJSONObject(j);
				// ret.append(obj.getString("w"));
				// }
			}
			return ret.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * XML转JSON
	 * 
	 * @param xml
	 *            需要解析的XML
	 * @return
	 */
	/*public static String xmlToJson(String xml) {

		try {
			JSONObject jsonObject = XML.toJSONObject(xml);
			return jsonObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}*/
	
	/**
	 * 生成Json
	 * @param object
	 * @return
	 */
	public static String createGsonString(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}

}
