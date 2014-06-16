package com.pomodoro.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.pomodoro.adapters.TaskAdapter;
import com.pomodoro.data.Task;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    TaskAdapter taskAdapter;
    List<Task> taskDataSource;
    AnimateDismissAdapter animateDismissAdapter;

    List<Integer> mSelectedPositions = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView)findViewById(R.id.listView);

        // Load data from database at first time.
        taskDataSource = Task.getAll();
        taskAdapter = new TaskAdapter(this, taskDataSource);
        listView.setAdapter(taskAdapter);

        /*
         Create animation dismissAdapter using baseAdapter.
         It is not necessary to use a global reference here but I have to because I need to use
         the reference in OnselectedMenu function. If I can use this reference to create a function
         here then I only need to save the function. Or, I can set the listener for the menu item,
         if there is a way.
          */

//        animateDismissAdapter = new AnimateDismissAdapter(taskAdapter, new MyOnDismissCallback());
//        animateDismissAdapter.setAbsListView(listView);
//        listView.setAdapter(animateDismissAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);

//                System.out.println("Position " + position + " is selected");
//                CheckBox cb = (CheckBox)view.findViewById(R.id.taskSelectCheckBox);
//                cb.toggle();
//                if(cb.isChecked()) {
//                    mSelectedPositions.add(position);
//                } else {
//                    mSelectedPositions.remove((Integer)position);
//                }

            }
        });

        // Init EditText
        final EditText createNewTaskEditText = (EditText)this.findViewById(R.id.input_task);
        createNewTaskEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Create new Task whose title is input text.
                    String text = v.getText().toString();
                    Task t = new Task(text);

                    /*
                    If I use add() function then the new task will add
                    to the tail of the list, which is wrong because the newest
                    should show up in the front. Therefore, I use insert here.
                     */
                    taskAdapter.addNewItem(t);
                    t.save();
                    // Do the clear stuffs.
                    // Give up focus to the list view.
                    createNewTaskEditText.clearFocus();
                    listView.requestFocus();

                    // Hide the keyboard.
                    hideSoftKeyboard(v);

                    // clean the EditText.
                    v.setText("");
                    return true;
                }
                return false;
            }
        });
        createNewTaskEditText.clearFocus();
        listView.requestFocus();
    }

    private void removeItem(int index) {
        taskDataSource.remove(index);
        this.runOnUiThread(new Runnable() {
            public void run() {
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    private class MyOnDismissCallback implements OnDismissCallback {

        @Override
        public void onDismiss(final AbsListView listView, final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                removeItem(position);
                String info = String.format("Position %d is about to delete.", position);
                System.out.println(info);
            }
        }
    }

    private void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_search) {
            //animateDismissAdapter.animateDismiss(mSelectedPositions);
            //mSelectedPositions.clear();
            taskAdapter.removeItem(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
