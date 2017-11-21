package com.anpa.anpacr.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.Tip;

import java.util.List;

public class TipListAdapter extends BaseAdapter{
	
	protected List<Tip> tipList;
	protected Activity activity;


	public TipListAdapter(Activity activity, List<Tip> tipListFromParse) {
		this.activity = activity;
		this.tipList = tipListFromParse;
	}


	@Override
	public int getCount() {
		return tipList.size();
	}


	@Override
	public Object getItem(int position) {
		return tipList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return tipList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_tip, null);
		}
		
		Tip item = tipList.get(position);
		
		TextView txt_title_tip = (TextView) view.findViewById(R.id.txt_title_consejo);
		txt_title_tip.setText(item.get_sConsejo());
		String txtPreviewConsejo = item.get_sConsejo();

		int rate = 0;
		int sum = 5*item.get_i5Estrella() + 4*item.get_i4Estrella() + 3*item.get_i3Estrella() + 2*item.get_i2Estrella() + 1*item.get_i1Estrella();
		if(sum != 0){
			rate = sum/ item.get_iTotalVotos();
		}


		if(txtPreviewConsejo.length() > 30)
			txtPreviewConsejo = txtPreviewConsejo.substring(0,40) + "...";
		txt_title_tip.setText(txtPreviewConsejo);

		TextView txt_autor = (TextView) view.findViewById(R.id.txt_autor);
		txt_autor.setText("Por: ".concat(item.get_sAuthor()));
		
		//Huellas de calificacion
		ImageView img_rating1 = (ImageView)view.findViewById(R.id.rating_star_1);
		ImageView img_rating2 = (ImageView)view.findViewById(R.id.rating_star_2);
		ImageView img_rating3 = (ImageView)view.findViewById(R.id.rating_star_3);
		ImageView img_rating4 = (ImageView)view.findViewById(R.id.rating_star_4);
		ImageView img_rating5 = (ImageView)view.findViewById(R.id.rating_star_5);
		
		switch (rate) {
		case 0:
			img_rating1.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating2.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating3.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating4.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating5.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			break;
		case 1:
			img_rating2.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating3.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating4.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating5.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			break;
		case 2:
			img_rating3.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating4.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating5.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			break;
		case 3:
			img_rating4.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			img_rating5.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			break;
		case 4:
			img_rating5.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_footprint_gray));
			break;
		default:
			break;
		}
		
		return view;
	}
	
	public void add(Tip tip){
		tipList.add(tip);
	}
	
	public void clearAdapter(){
		tipList.clear();
	}	
}
