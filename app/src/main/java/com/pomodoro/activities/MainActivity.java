package com.pomodoro.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.pomodoro.adapters.TaskAdapter;
import com.pomodoro.data.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    TaskAdapter taskAdapter;
    AnimateDismissAdapter animateDismissAdapter;

    public final static int REQUEST_CODE = 21;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.REQUEST_CODE) {
            Task returnTask = data.getParcelableExtra(AlarmActivity.POS);
            taskAdapter.updateRelativeTask(returnTask);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView)findViewById(R.id.listView);

        // Load data from database at first time.

        // Create data adapter and set it to ListView.
        taskAdapter = new TaskAdapter(this, Task.getAll());
        listView.setAdapter(taskAdapter);
        listView.setEmptyView(findViewById(android.R.id.empty));

        // Set Click to delete function.
        setLongClickToDelete(listView);
        /*
         Create animation dismissAdapter using baseAdapter.
         It is not necessary to use a global reference here but I have to because I need to use
         the reference in OnselectedMenu function. If I can use this reference to create a function
         here then I only need to save the function. Or, I can set the listener for the menu item,
         if there is a way.
          */

        animateDismissAdapter = new AnimateDismissAdapter(taskAdapter, new OnDismissCallback() {
            @Override
            public void onDismiss(final AbsListView listView, final int[] reverseSortedPositions) {
                taskAdapter.removeItemsInBatch(reverseSortedPositions);
            }
        });

        animateDismissAdapter.setAbsListView(listView);
        listView.setAdapter(animateDismissAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                String info = String.format("task %s is select.", task.getName());
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra(AlarmActivity.POS, task);
                startActivityForResult(intent, MainActivity.REQUEST_CODE);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskAdapter.saveAll();
    }

    private void setLongClickToDelete(final ListView listView) {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                setSubtitle(mode);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

                        //taskAdapter.removeItemsInBatch(checkedItemPositions);
                        final List<Integer> list = new LinkedList<Integer>();
                        for(int i = 0; i < checkedItemPositions.size(); ++i) {
                            list.add(checkedItemPositions.keyAt(i));
                        }
                        String title = "Delete Selected Item";
                        String info;
                        if(checkedItemPositions.size() == 1) {
                            String taskTitle =
                                    taskAdapter.getItem(checkedItemPositions.keyAt(0)).getName();
                            info = String.format("Are you sure you want to delete item with " +
                                    "the title \"%s\"", taskTitle);
                        } else if(checkedItemPositions.size() > 1) {
                            info = String.format("Are you sure you want " +
                                    "to delete these %d items", list.size());
                        } else {
                            info = "error";
                        }
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(title)
                                .setMessage(info)
                                .setPositiveButton(android.R.string.yes,
                                        new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        animateDismissAdapter.animateDismiss(list);
                                    }
                                })
                                .setNegativeButton(android.R.string.no,
                                        new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, menu);
                mode.setTitle("Select Items");
                setSubtitle(mode);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }

            private void setSubtitle(ActionMode mode) {
                final int checkedCount = listView.getCheckedItemCount();
                switch (checkedCount) {
                    case 0:
                        mode.setSubtitle(null);
                        break;
                    case 1:
                        mode.setSubtitle("One item selected");
                        break;
                    default:
                        mode.setSubtitle("" + checkedCount + " items selected");
                        break;
                }
            }
        });
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

        }
        return super.onOptionsItemSelected(item);
    }

}
