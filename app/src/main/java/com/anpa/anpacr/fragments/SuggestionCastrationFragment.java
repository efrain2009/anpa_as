package com.anpa.anpacr.fragments;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

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
				
				 List<GenericListItem> groups = new ArrayList<GenericListItem>();
				 
					 GenericListItem object = new GenericListItem();
					 String [] respuestas = new String [] {"Llevar al animal en ayunas (que su última comida sea a las 10:00 p.m. del día anterior a la campaña).",
							 "Llevar una cobija para después de operados.",
							 "No bañar a su mascota ni el día de la castración, ni el día antes.",
							 "Gatos trasladarlos en fundas de almohada.",
							 "Se castran animales sanos mayores de 3 meses y si son gatos machos mayores de 5 meses."};
					 object.set_lArreglo(respuestas);
					 object.set_sTitle("Antes de la operación");
					 groups.add(object);
				 
					 GenericListItem object1 = new GenericListItem();

				String [] respuestas1 = new String [] {"Su mascota se entregará saliendo bien de la anestesia total que se le aplicó para la cirugía.",
						"Debe mantenerlo en un lugar seguro dentro de la casa, sobre una cobija, limpio, poco iluminado, sin viento, agua, ni sol directo.",
						"No acostarlo en lugares altos.","Los hilos son absorbibles, con el pasar del tiempo se deshacen, el tiempo puede variar según los organismos.",
						"Cuidar la herida, que no se chupen, ni se golpe, ni se abran la herida. Se recomienda el uso de collar isabelino.",
						"Esperar al menos 5 horas después de la cirugía para darle agua y comida; que sea cuando el animal esté bien despierto.",
						"Reforzar el cuidado post operatorio con analgésico y antibióticos por al menos 4 días.",
						"En caso de consultas o emergencias contactar al médico veterinario que realizó la cirugía, el número está en el manual entregado durante la campaña de " +
						"castración, ó bien, al teléfono de ANPA +506 4000-2672, correo electrónico: castraciones@anpacostarica.org, Facebook: ANPA Costa Rica."};

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
