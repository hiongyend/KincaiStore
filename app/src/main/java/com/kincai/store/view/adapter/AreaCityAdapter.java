package com.kincai.store.view.adapter;

import java.util.List;

import android.content.Context;

import com.kincai.store.R;
import com.kincai.store.bean.AreaCityInfo;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 市adapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-9 上午10:53:44
 *
 */
public class AreaCityAdapter extends CommonAdapter<AreaCityInfo> {

	public AreaCityAdapter(Context context, List<AreaCityInfo> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, AreaCityInfo cityInfo, int position) {

		viewHolder.setTextViewText(R.id.item_area_tv,
				cityInfo.getCity_name());
	}

	// 实现刷新
	@Override
	public void notifyRefresh(List<AreaCityInfo> list) {

		// TODO Auto-generated method stub
		super.notifyRefresh(list);
	}

}
