package com.example.a15520824.phanmemchuyensau;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuocTuyen on 6/17/2018.
 */

public class ActionAdapter extends ArrayAdapter<ActionModel> {

    private Context context;
    private int resource;
    private List<ActionModel> actionList;


    public ActionAdapter(@NonNull Context context, int resource, ArrayList<ActionModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.actionList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView =  LayoutInflater.from(context).inflate(R.layout.layout_listview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.actionName = convertView.findViewById(R.id.txtName);
            viewHolder.actionKey = convertView.findViewById(R.id.txtKey);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ActionModel action = actionList.get(position);
        viewHolder.actionName.setText(action.getName());
        viewHolder.actionKey.setText(action.getKey());

        return convertView;
    }

    public class ViewHolder{
        TextView actionName, actionKey;
    }
}
