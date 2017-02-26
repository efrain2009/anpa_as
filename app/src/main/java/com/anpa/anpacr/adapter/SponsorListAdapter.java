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
import com.anpa.anpacr.domain.Sponsor;

public class SponsorListAdapter extends BaseAdapter{
	
	protected Activity activity;
	protected List<Sponsor> sponsorList;


	public SponsorListAdapter(Activity activity, List<Sponsor> sponsorListFromParse) {
		this.activity = activity;
		this.sponsorList = sponsorListFromParse;
	}


	@Override
	public int getCount() {
		return sponsorList.size();
	}


	@Override
	public Object getItem(int position) {
		return sponsorList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return sponsorList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_sponsor, null);
		}
		
		Sponsor item = sponsorList.get(position);

		TextView txt_descripcion_sponsor = (TextView) view.findViewById(R.id.txt_descripcion_sponsor);
		txt_descripcion_sponsor.setText(item.get_sdescripcion());

		if(item.get_bImagen() != null) {
			ImageView img_sponsor = (ImageView) view.findViewById(R.id.img_sponsor);
			Bitmap bmpImage = BitmapFactory.decodeByteArray(
					item.get_bImagen(), 0, item.get_bImagen().length);
			img_sponsor.setImageBitmap(bmpImage);
		}
		return view;
	}
	
	public void add(Sponsor sponsor){
		sponsorList.add(sponsor);
	}
	
	public void clearAdapter(){
		sponsorList.clear();
	}	
}
