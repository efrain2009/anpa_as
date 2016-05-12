package com.anpa.anpacr.fragments;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.activities.DetailTipActivity;
import com.anpa.anpacr.adapter.TipListAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.Tip;

public class LastTipsFragment extends android.support.v4.app.Fragment{
	
	private TipListAdapter tipAdapter;
	private ListView lv_tips;
	
	OnLoadListListener onLoadListListener; 	//Interface para recibir la lista desde el activity.

    //El activity debe tener esta implementacion.
    public interface OnLoadListListener {
        public List<Tip> loadList();
    }
	
    /*
	 * Creacion del fragment
	 */
		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.activity_list_tip, container, false);
			/**
			 * Se instancia el interface
			 */
			try {
	            onLoadListListener = (OnLoadListListener) getActivity();
	        } catch (ClassCastException e) {
	            throw new ClassCastException(getActivity().toString()
	                    + " must implement OnLoadListListener");
	        }
			
			lv_tips = (ListView) view.findViewById(R.id.list_tips);
			
			//Carga los datos obtenidos del activity, llamando al interface del activity
			 List<Tip> tipsList = onLoadListListener.loadList();
			
			tipAdapter = new TipListAdapter(getActivity(), tipsList);
			lv_tips.setAdapter(tipAdapter);
			lv_tips.setOnItemClickListener(onclickListTips);

			return view;
		}
		
	private OnItemClickListener onclickListTips = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Tip tip = (Tip) tipAdapter.getItem(position);
						
			Intent intent = new Intent(getActivity(), DetailTipActivity.class);
			intent.putExtra(Constants.ID_OBJ_DETAIL_TIP, tip);
			startActivity(intent);
		}
	};	
}
