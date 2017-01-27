package com.anpa.anpacr.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.Castration;

public class CastrationListAdapter extends BaseAdapter{
	
	protected List<Castration> castrationList;
	protected Activity activity;


	public CastrationListAdapter(Activity activity, List<Castration> castrationListFromParse) {
		this.activity = activity;
		this.castrationList = castrationListFromParse;
	}


	@Override
	public int getCount() {
		return castrationList.size();
	}


	@Override
	public Object getItem(int position) {
		return castrationList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return new Long(0);
		//return castrationList.get(position).get_lId();
	}


	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = contentView;
		
		if(contentView == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_list_item_castration, null);
		}
		
		Castration item = castrationList.get(position);
		
		TextView txt_title_castration = (TextView) view.findViewById(R.id.txt_title_castration);
		txt_title_castration.setText(item.get_snombre());
		String horario = "";

		try {
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date dateInicio = format.parse(item.get_sDateInicio());

			///////////////////////////FORMAT FECHA DE LAS CASTRACION/////////////////////////
			// Converting to String again, using an alternative format fecha
			DateFormat dfFecha = new SimpleDateFormat("dd/MM/yyyy");
			String startDate = dfFecha.format(dateInicio);

			///////////////////////////FORMAT HORA DE LAS CASTRACION/////////////////////////
			// Converting to String again, using an alternative format hora
			SimpleDateFormat dthora = new SimpleDateFormat("hh:mm a");
			String startHora = dthora.format(dateInicio);
			horario = startDate + " - " +startHora;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		TextView txt_date_inicio = (TextView) view.findViewById(R.id.txt_format_date_inicio);
		txt_date_inicio.setText(horario);
		
		return view;
	}
	
	public void add(Castration castration){
		castrationList.add(castration);
	}
	
	public void clearAdapter(){
		castrationList.clear();
	}	
}
