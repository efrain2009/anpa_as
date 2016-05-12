package com.anpa.anpacr.fragments;

import java.util.List;

import android.app.Activity;
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
import com.anpa.anpacr.activities.DetailCastrationActivity;
import com.anpa.anpacr.adapter.CastrationListAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.Castration;

public class LastCastrationFragment extends android.support.v4.app.Fragment{
	
	private CastrationListAdapter castrationAdapter;
	private ListView lv_castrations;
	
	OnLoadListListener onLoadListListener; 	//Interface para recibir la lista desde el activity.

    //El activity debe tener esta implementaci�n.
    public interface OnLoadListListener {
        public List<Castration> loadList();
    }
	
    /*
	 * Creaci�n del fragment
	 */
		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_list_castrations, container, false);
			
			lv_castrations = (ListView) view.findViewById(R.id.list_castrations);
			
			//Carga los datos obtenidos del activity, llamando al interface del activity
			 List<Castration> castrationlist = onLoadListListener.loadList();
			
			castrationAdapter = new CastrationListAdapter(getActivity(), castrationlist);
			lv_castrations.setAdapter(castrationAdapter);
			lv_castrations.setOnItemClickListener(onclickListCastration);			

			return view;
		}
		
		@Override
		public void onAttach(Activity activity){
			super.onAttach(activity);
			/**
			 * Se instancia el interface
			 */
			try {
	            onLoadListListener = (OnLoadListListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnLoadListListener");
	        }
		}
		
	private OnItemClickListener onclickListCastration = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Castration castration = (Castration) castrationAdapter.getItem(position);
						
			Intent intent = new Intent(getActivity(), DetailCastrationActivity.class);
			intent.putExtra(Constants.ID_OBJ_DETAIL_CASTRATION, castration);
			startActivity(intent);
		}
	};	
}
