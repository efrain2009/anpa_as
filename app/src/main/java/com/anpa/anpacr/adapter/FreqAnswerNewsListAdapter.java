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

public class FreqAnswerNewsListAdapter extends BaseAdapter{
	
	protected List<GenericListItem> freqAnswerList;
	protected Activity activity;
	

	public FreqAnswerNewsListAdapter(Activity activity, List<GenericListItem> freqAnswerListFromParse) {
		this.activity = activity;
		this.freqAnswerList = freqAnswerListFromParse;
	}


	@Override
	public int getCount() {
		return freqAnswerList.size();
	}


	@Override
	public Object getItem(int position) {
		return freqAnswerList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return freqAnswerList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_faq_cast, null);
		}
		
		GenericListItem item = freqAnswerList.get(position);
		
		TextView txt_pregunta_freqAnswer = (TextView) view.findViewById(R.id.txt_title_pregunta);
		txt_pregunta_freqAnswer.setText(item.get_sTitle());
		/*
		TextView txt_respuesta_freqAnswer = (TextView) view.findViewById(R.id.txt_title_respuesta);
		txt_respuesta_freqAnswer.setText(item.get_srespuesta());
		*/
		return view;
	}
	
	public void add(GenericListItem freqAnswer){
		freqAnswerList.add(freqAnswer);
	}
	
	public void clearAdapter(){
		freqAnswerList.clear();
	}	
}
