package com.anpa.anpacr.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.InfoExpandListAdapter;
import com.anpa.anpacr.domain.FreqAnswer;
import com.anpa.anpacr.domain.GenericListItem;

public class FreqAnswerFragment extends android.support.v4.app.Fragment{

	private InfoExpandListAdapter freqAnswerNewsListAdapter;
	private ExpandableListView lv_freqAnswerNews;
	
	OnLoadListListenerFreqAnswerNews onLoadListListenerFreqAnswerNews; 	//Interface para recibir la lista desde el activity.

    //El activity debe tener esta implementaci�n.
    public interface OnLoadListListenerFreqAnswerNews {
        public List<FreqAnswer> loadFreqAnswerList();
    }
	
    /*
	 * Creaci�n del fragment
	 */
		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_freq_answers, container, false);
			
			lv_freqAnswerNews = (ExpandableListView) view.findViewById(R.id.list_freqAnswerNews);
			
			//Carga los datos obtenidos del activity, llamando al interface del activity
			 List<FreqAnswer> freqAnswerCastrationlist = onLoadListListenerFreqAnswerNews.loadFreqAnswerList();
			
			 List<GenericListItem> groups = new ArrayList<GenericListItem>();
			 
			 for(FreqAnswer fq :freqAnswerCastrationlist){
				 GenericListItem object = new GenericListItem();
				 String [] respuestas = new String [] {fq.get_srespuesta()};
				 object.set_lArreglo(respuestas);
				 object.set_sTitle(fq.get_spregunta());
				 groups.add(object);
			 }
			 
			//freqAnswerListAdapter = new FreqAnswerListAdapter(getActivity(), freqAnswerCastrationlist);
			freqAnswerNewsListAdapter = new InfoExpandListAdapter(getActivity().getApplicationContext(), groups);
			List<GenericListItem> listGeneric = new ArrayList<GenericListItem>();
			for(FreqAnswer item: freqAnswerCastrationlist){
				GenericListItem generic = new GenericListItem();
				generic.set_sTitle(item.get_spregunta());
				generic.set_lArreglo(new String[]{item.get_srespuesta()});
				listGeneric.add(generic);
			}
			lv_freqAnswerNews.setAdapter(freqAnswerNewsListAdapter);
			
			
			return view;
		}
		
		@Override
		public void onAttach(Activity activity){
			super.onAttach(activity);
			/**
			 * Se instancia el interface
			 */
			try {
				onLoadListListenerFreqAnswerNews = (OnLoadListListenerFreqAnswerNews) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnLoadListListener");
	        }
		}
		
		
	private OnItemClickListener onclickListFreqAnswerList = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FreqAnswer freqAnswer = (FreqAnswer) freqAnswerNewsListAdapter.getChild(1, position);
		}
	};	
}
