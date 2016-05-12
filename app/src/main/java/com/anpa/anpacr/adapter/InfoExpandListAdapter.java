package com.anpa.anpacr.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.domain.GenericListItem;

/**
 * Adapter para la lista expandible donde se muestran las categorías de gastos,
 * ingresos previstos e ingresos reales.
 * Created by Casa on 03/05/2015.
 */
public class InfoExpandListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<GenericListItem> groups;

    public InfoExpandListAdapter(Context context, List<GenericListItem> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String[] chList = groups.get(groupPosition).get_lArreglo();
        return chList[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        
        LayoutInflater infalInflater = null;
        if (convertView == null) {
            infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item_info, null);
        }
        TextView txtDetail = (TextView) convertView.findViewById(R.id.tv_item_info_description);
        txtDetail.setText(child);
		
            
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String[] chList = groups.get(groupPosition).get_lArreglo();
        return chList.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GenericListItem group = (GenericListItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, null);
            ExpandableListView expandible = (ExpandableListView) parent;
            expandible.expandGroup(groupPosition);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_group_name);
        tv.setText(group.get_sTitle());
        return convertView;
    }

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}
}
