package com.primary.spell;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends Activity
{
    public final String filename = "MyPrefs";
    private SharedPreferences prefData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingaction);
        prefData = this.getSharedPreferences(filename, 0);
    }

    public void buttonOnClickConfirm(View view)
    {
        EditText et = (EditText)  findViewById(R.id.editText);
		
        if(et.getText().toString().trim().length() == 0)
        {
            et.setError("Email Required");
        }
        else
        {
            SharedPreferences.Editor editor = prefData.edit();
            editor.putString("Email", et.getText().toString());
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
	
    public void buttonOnClickCancel(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toggleClick(View view)
    {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor supportEdit = prefData.edit();

        if(on)
        {
            supportEdit.putInt("Support", 1);
            supportEdit.commit();
        }
        else
        {
            supportEdit.putInt("Support", 0);
            supportEdit.commit();
        }
    }
}