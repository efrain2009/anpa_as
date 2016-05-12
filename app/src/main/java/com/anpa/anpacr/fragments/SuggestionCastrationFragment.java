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

public class SuggestionCastrationFragment extends android.support.v4.app.Fragment{
		
		private InfoExpandListAdapter suggestionListAdapter;
		private ExpandableListView lv_suggestion;
		
		OnLoadListListenerSuggestionCastration onLoadListListenerSuggestionCastration; 	//Interface para recibir la lista desde el activity.

	    //El activity debe tener esta implementaci�n.
	    public interface OnLoadListListenerSuggestionCastration {
	        public List<FreqAnswer> loadSuggestionList();
	    }
		
	    /*
		 * Creaci�n del fragment
		 */
			@Override
			public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
				View view = inflater.inflate(R.layout.fragment_suggestion_castration, container, false);
				
				lv_suggestion = (ExpandableListView) view.findViewById(R.id.list_suggestionCastration);
				
				//Carga los datos obtenidos del activity, llamando al interface del activity
				// List<FreqAnswer> suggestionCastrationlist = onLoadListListenerSuggestionCastration.loadSuggestionList();
				
				 List<GenericListItem> groups = new ArrayList<GenericListItem>();
				 
					 GenericListItem object = new GenericListItem();
					 String [] respuestas = new String [] {"El animal debe de estar al menos 12 horas sin haber ingerido alimentos.", "No  utilice desparacitantes externos el día antes de la castración.","Lleve una cobija para proteger a su perro o gato al traslado a su casa."};
					 object.set_lArreglo(respuestas);
					 object.set_sTitle("Antes de la operación");
					 groups.add(object);
				 
					 GenericListItem object1 = new GenericListItem();
					 String [] respuestas1 = new String [] {"Acúeste al animal extendido en un lugar cerrado, tranquilo, oscuro y protegido de corrientes de aire, del frío y la lluvia o el sol y permanezca pendiente de él"};
					 object1.set_lArreglo(respuestas1);
					 object1.set_sTitle("Despúes de la operación");
					 groups.add(object1);
				 
				
				suggestionListAdapter = new InfoExpandListAdapter(getActivity().getApplicationContext(), groups);
		
				lv_suggestion.setAdapter(suggestionListAdapter);
				
				
				return view;
			}
			
			@Override
			public void onAttach(Activity activity){
				super.onAttach(activity);
				/**
				 * Se instancia el interface
				 */
				try {
					onLoadListListenerSuggestionCastration = (OnLoadListListenerSuggestionCastration) activity;
		        } catch (ClassCastException e) {
		            throw new ClassCastException(activity.toString()
		                    + " must implement OnLoadListListener");
		        }
			}
			
			
		private OnItemClickListener onclickListFreqAnswerList = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				FreqAnswer freqAnswer = (FreqAnswer) suggestionListAdapter.getChild(1, position);
			}
		};	
	}
