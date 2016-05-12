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

public class FreqAnswerListAdapter extends BaseAdapter{
	
	protected List<GenericListItem> freqAnswerNewsList;
	protected Activity activity;
	

	public FreqAnswerListAdapter(Activity activity, List<GenericListItem> freqAnswerNewsListFromParse) {
		this.activity = activity;
		this.freqAnswerNewsList = freqAnswerNewsListFromParse;
	}


	@Override
	public int getCount() {
		return freqAnswerNewsList.size();
	}


	@Override
	public Object getItem(int position) {
		return freqAnswerNewsList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return freqAnswerNewsList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_faq_news, null);
		}
		
		GenericListItem item = freqAnswerNewsList.get(position);
		
		TextView txt_pregunta_freqAnswer = (TextView) view.findViewById(R.id.txt_title_pregunta);
		txt_pregunta_freqAnswer.setText(item.get_sTitle());
		/*
		TextView txt_respuesta_freqAnswer = (TextView) view.findViewById(R.id.txt_title_respuesta);
		txt_respuesta_freqAnswer.setText(item.get_srespuesta());
		*/
		return view;
	}
	
	public void add(GenericListItem freqAnswer){
		freqAnswerNewsList.add(freqAnswer);
	}
	
	public void clearAdapter(){
		freqAnswerNewsList.clear();
	}	
}
