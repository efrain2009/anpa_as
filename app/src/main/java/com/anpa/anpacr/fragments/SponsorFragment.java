package com.anpa.anpacr.fragments;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SponsorListAdapter;
import com.anpa.anpacr.domain.Sponsor;

public class SponsorFragment extends android.support.v4.app.Fragment{
	
	private SponsorListAdapter sponsorAdapter;
	private GridView lv_sponsor;
	
	OnLoadSponsorListListener onLoadSponsorListListener; //Interface para recibir la lista desde el activity.

    //El activity debe tener esta implementacion.
    public interface OnLoadSponsorListListener {
        public List<Sponsor> loadSponsorList();
    }

	/*
	 * Creacion del fragment
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list_sponsor, container, false);
		/**
		 * Se instancia el interface
		 */
		try {
            onLoadSponsorListListener = (OnLoadSponsorListListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnLoadSponsorListListener");
        }
		
		lv_sponsor = (GridView) view.findViewById(R.id.list_sponsor);
		
		//Carga los datos obtenidos del activity, llamando al interface del activity
		 List<Sponsor> sponsorlist = onLoadSponsorListListener.loadSponsorList();
		
		sponsorAdapter = new SponsorListAdapter(getActivity(), sponsorlist);
		lv_sponsor.setAdapter(sponsorAdapter);
		lv_sponsor.setOnItemClickListener(onclickListSponsor);

		return view;
	}
	
	private OnItemClickListener onclickListSponsor = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Sponsor sponsor = (Sponsor) sponsorAdapter.getItem(position);
		}
	};	
}
