package com.statiticsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.statiticsapp.R;

import java.util.HashMap;
import java.util.List;

public class CalculateExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> listTitles;
    private HashMap<String, List<String>> labelsHashMap;
    private HashMap<String, List<String>> valuesHashMap;

    public CalculateExpandableListAdapter(Context context,
                                          List<String> listTitles,
                                          HashMap<String, List<String>> labelsHashMap,
                                          HashMap<String, List<String>> valuesHashMap) {
        this.mContext = context;
        this.listTitles = listTitles;
        this.labelsHashMap = labelsHashMap;
        this.valuesHashMap = valuesHashMap;

    }

    public void setData(HashMap<String, List<String>> valuesHashMap) {
        this.valuesHashMap = valuesHashMap;
    }

    @Override
    public int getGroupCount() {
        return listTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return labelsHashMap.get(listTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<String> childsOfGroup = valuesHashMap.get(listTitles.get(groupPosition));
        String child = childsOfGroup.get(childPosition);
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.calculate_expandable_list_group, null);
        }

        String title = listTitles.get(groupPosition);
        TextView groupTitle = (TextView) convertView.findViewById(R.id.calculate_expandable_list_group_title);
        groupTitle.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.calculate_expandable_list_item, null);
        }

        TextView itemLabel = (TextView) convertView.findViewById(R.id.calculate_expandable_list_item_label);
        TextView itemValue = (TextView) convertView.findViewById(R.id.calculate_expandable_list_item_value);

        String label = labelsHashMap.get(listTitles.get(groupPosition)).get(childPosition);
        String value = "";
        if(valuesHashMap.get(listTitles.get(groupPosition)).size() > 0)
            value = valuesHashMap.get(listTitles.get(groupPosition)).get(childPosition);

        itemLabel.setText(label);
        itemValue.setText(value);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
