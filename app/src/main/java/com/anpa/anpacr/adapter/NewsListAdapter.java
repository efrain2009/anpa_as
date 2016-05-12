package com.anpa.anpacr.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.News;

public class NewsListAdapter extends BaseAdapter{
	
	protected Activity activity;
	protected List<News> newsList;


	public NewsListAdapter(Activity activity, List<News> newsListFromParse) {
		this.activity = activity;
		this.newsList = newsListFromParse;
	}


	@Override
	public int getCount() {
		return newsList.size();
	}


	@Override
	public Object getItem(int position) {
		return newsList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return newsList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_news, null);
		}
		
		News item = newsList.get(position);
		
		TextView txt_title_news = (TextView) view.findViewById(R.id.txt_title_news);
		txt_title_news.setText(item.get_stitle());
				
		TextView txt_descrip_news = (TextView) view.findViewById(R.id.txt_descrip_news);
		txt_descrip_news.setText(item.get_sdescription());
		
		String txtPreviewDescrip = item.get_sdescription();
		if(txtPreviewDescrip.length() > 30)
			txtPreviewDescrip = txtPreviewDescrip.substring(0,30) + "...";
		txt_descrip_news.setText(txtPreviewDescrip);
		
		if(item.get_bImagen() != null){
			ImageView img_news = (ImageView) view.findViewById(R.id.img_news);
			Bitmap bmpImage = BitmapFactory.decodeByteArray(
				      item.get_bImagen(), 0, item.get_bImagen().length);
			img_news.setImageBitmap(bmpImage);
		}
		
		return view;
	}
	
	public void add(News news){
		newsList.add(news);
	}
	
	public void clearAdapter(){
		newsList.clear();
	}	
}
