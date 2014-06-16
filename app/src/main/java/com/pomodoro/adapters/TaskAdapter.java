package com.pomodoro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pomodoro.activities.R;
import com.pomodoro.data.Task;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lniu on 5/17/14.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    List<Boolean> itemChecked; // track which position has been selected.

    final static int First_SLOT = 0;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.row, tasks);

        /*
         Init items to label which task is checked or not.
          */
        itemChecked = new ArrayList<Boolean>(tasks.size());
        for(int i = 0; i < tasks.size(); ++i)
            itemChecked.add(false);
    }

    public void addNewItem(Task t) {
        // Insert actual data into adapter.
        this.insert(t, First_SLOT);

        // Insert data into item checked list.
        this.itemChecked.add(First_SLOT, false);
    }

    public void removeItem(int position) {
        itemChecked.remove(position);
        Task item2remove = this.getItem(position);
        item2remove.delete();
        super.remove(item2remove);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(task.getName());

        CheckBox cb = (CheckBox)convertView.findViewById(R.id.taskSelectCheckBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    itemChecked.set(position, true);
                } else {
                    itemChecked.set(position, false);
                }
            }
        });
        cb.setChecked(itemChecked.get(position));

        return convertView;
    }

}
