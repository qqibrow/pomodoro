package com.pomodoro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pomodoro.activities.R;
import com.pomodoro.data.Task;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lniu on 5/17/14.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, R.layout.row, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(task.getName());
        return convertView;
    }

}
