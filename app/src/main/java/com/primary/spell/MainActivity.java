package com.primary.spell;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity
{
    public final String filename = "MyPrefs";
    private SharedPreferences prefData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefData = this.getSharedPreferences(filename, 0);

        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id)
            {
                SharedPreferences.Editor editor = prefData.edit();
                editor.putInt("Class", i);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        };
        ArrayAdapter<CharSequence>  stringArrayAdapter = ArrayAdapter.createFromResource(this,R.array.classlist,android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)  findViewById(R.id.spinner);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(onSpinner);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    public void startExam(View v)
    {
        EditText et = (EditText)  findViewById(R.id.editText);
        if(et.getText().toString().trim().length() == 0)
        {
            et.setError("Name Required");
        }
        else
        {
            SharedPreferences.Editor editor = prefData.edit();
            editor.putString("Name", et.getText().toString());
            editor.commit();

            Intent intent = new Intent(this, ExamActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment
    {
        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}