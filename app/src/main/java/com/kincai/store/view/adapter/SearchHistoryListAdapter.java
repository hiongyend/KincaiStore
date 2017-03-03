package com.kincai.store.view.adapter;

import java.util.List;

import android.content.Context;

import com.kincai.store.R;
import com.kincai.store.bean.SearchHistory;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 搜素历史适配器
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:29:13
 *
 */
public class SearchHistoryListAdapter extends CommonAdapter<SearchHistory> {

	public SearchHistoryListAdapter(Context context, List<SearchHistory> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, SearchHistory sHistory, int position) {
		
		viewHolder.setTextViewText(R.id.search_item_tv, sHistory.getContent());
	}

	//实现刷新
	@Override
	public void notifyRefresh(List<SearchHistory> list) {
		
		super.notifyRefresh(list);
	}

	
}
