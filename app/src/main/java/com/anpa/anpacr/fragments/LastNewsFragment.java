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
import com.anpa.anpacr.activities.DetailNewsActivity;
import com.anpa.anpacr.adapter.NewsListAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.News;

public class LastNewsFragment extends android.support.v4.app.Fragment{
	
	private NewsListAdapter newsAdapter;
	private ListView lv_news;
	
	OnLoadListListener onLoadListListener; //Interface para recibir la lista desde el activity.

    //El activity debe tener esta implementaci�n.
    public interface OnLoadListListener {
        public List<News> loadList();
    }

	/*
	 * Creaci�n del fragment
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list_news, container, false);
		/**
		 * Se instancia el interface
		 */
		try {
            onLoadListListener = (OnLoadListListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnLoadListListener");
        }
		
		lv_news = (ListView) view.findViewById(R.id.list_news);
		
		//Carga los datos obtenidos del activity, llamando al interface del activity
		 List<News> newslist = onLoadListListener.loadList();
		
		newsAdapter = new NewsListAdapter(getActivity(), newslist);
		lv_news.setAdapter(newsAdapter);
		lv_news.setOnItemClickListener(onclickListNews);

		return view;
	}
	
	private OnItemClickListener onclickListNews = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			News news = (News) newsAdapter.getItem(position);
						
			Intent intent = new Intent(getActivity(), DetailNewsActivity.class);
			intent.putExtra(Constants.ID_OBJ_DETAIL_NEWS, news);
			startActivity(intent);
		}
	};	
}
