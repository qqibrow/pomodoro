package com.pomodoro.adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.pomodoro.activities.R;
import com.pomodoro.data.Task;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lniu on 5/17/14.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    final static int First_SLOT = 0;

    List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.row, tasks);
        this.tasks = tasks;
        setNotifyOnChange(true);
    }
    @Override
    public int getCount() {
        return tasks.size();
    }

    public void addNewItem(Task t) {
        // Insert actual data into adapter.
        t.save();
        this.insert(t, First_SLOT);
        System.out.println("Added new Item.\n" + t);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(task.getName());

        TextView pomoNum = (TextView) convertView.findViewById(R.id.pomoNum);
        pomoNum.setText("" + task.getFinishedPomo());
        return convertView;
    }

    public void removeItemsInBatch(SparseBooleanArray checkedItemPositions) {
        List<Task> task2remove = new LinkedList<Task>();
        for(int i = 0; i < checkedItemPositions.size(); ++i) {
            task2remove.add(getItem(checkedItemPositions.keyAt(i)));
        }
        for(Task t : task2remove) {
            t.delete();
            super.remove(t);
        }
    }

    public void removeItemsInBatch(final int[] positions) {
        List<Task> task2remove = new LinkedList<Task>();
        for(int pos : positions) {
            task2remove.add(getItem(pos));
        }
        for(Task t : task2remove) {
            t.delete();
            super.remove(t);
        }
    }
}
