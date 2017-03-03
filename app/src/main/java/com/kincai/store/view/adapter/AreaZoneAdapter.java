package com.kincai.store.view.adapter;

import java.util.List;

import android.content.Context;

import com.kincai.store.R;
import com.kincai.store.bean.AreaZoneInfo;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 县/区adapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-9 上午10:53:44
 *
 */
public class AreaZoneAdapter extends CommonAdapter<AreaZoneInfo> {

	public AreaZoneAdapter(Context context, List<AreaZoneInfo> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, AreaZoneInfo zoneInfo, int position) {

		viewHolder.setTextViewText(R.id.item_area_tv,
				zoneInfo.getZone_name());
	}

	// 实现刷新
	@Override
	public void notifyRefresh(List<AreaZoneInfo> list) {

		// TODO Auto-generated method stub
		super.notifyRefresh(list);
	}

}
