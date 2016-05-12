package com.anpa.anpacr.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.GenericListItem;

public class SuggestionListAdapter extends BaseAdapter{
	
	protected List<GenericListItem> suggestionList;
	protected Activity activity;
	

	public SuggestionListAdapter(Activity activity, List<GenericListItem> suggestionListFromParse) {
		this.activity = activity;
		this.suggestionList = suggestionListFromParse;
	}


	@Override
	public int getCount() {
		return suggestionList.size();
	}


	@Override
	public Object getItem(int position) {
		return suggestionList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return suggestionList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_suggestion, null);
		}
		
		GenericListItem item = suggestionList.get(position);
		
		TextView txt_title_suggestion = (TextView) view.findViewById(R.id.txt_title_suggestion);
		txt_title_suggestion.setText(item.get_sTitle());
		return view;
	}
	
	public void add(GenericListItem suggestion){
		suggestionList.add(suggestion);
	}
	
	public void clearAdapter(){
		suggestionList.clear();
	}	
}
