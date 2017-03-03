package com.kincai.store.view.adapter;

import java.util.List;

import android.content.Context;

import com.kincai.store.R;
import com.kincai.store.bean.SearchItem;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 提供搜索条目的listviewadapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:28:33
 *
 */
public class SearchItemListAdapter extends CommonAdapter<SearchItem> {

	public SearchItemListAdapter(Context context, List<SearchItem> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, SearchItem item, int position) {
		
		viewHolder.setTextViewText(R.id.search_item_tv, item.getItem());
	}

}
