package com.pomodoro.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pomodoro.adapters.TaskAdapter;
import com.pomodoro.data.Task;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new TaskAdapter(this, new ArrayList<Task>());
        final ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, task.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // Init EditText
        final EditText input_task = (EditText)this.findViewById(R.id.input_task);
        input_task.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Create new Task whose title is input text.
                    String text = v.getText().toString();
                    Task t = new Task(text);
                    adapter.add(t);

                    // Do the clear stuffs.
                    // Give up focus to the list view.
                    input_task.clearFocus();
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
        input_task.clearFocus();
        listView.requestFocus();
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
        return super.onOptionsItemSelected(item);
    }

}
