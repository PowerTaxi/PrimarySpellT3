package com.primary.spell;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

public class ResultsActivity extends Activity
{
    private int result;
    private int temp;
    public final String filename = "MyPrefs";
    private SharedPreferences prefData;
    private String [] words;
    private String [] input;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        prefData = this.getSharedPreferences(filename, 0);

        Bundle bundle = getIntent().getExtras();
        words = bundle.getStringArray("Words");
        input = bundle.getStringArray("Input");

        calcResults();

        TextView tv = (TextView) findViewById(R.id.editText);
        TextView tv2 = (TextView) findViewById(R.id.textView);
        tv2.setText(Integer.toString(result));
        tv.setText(Integer.toString(result));
        email = prefData.getString("Email", email);
        name = prefData.getString("Name", name);

    }

    public void levenshtein(String a, String b)
    {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int [] cost = new int [b.length() + 1];

        for (int j = 0; j < cost.length; j++)
        {
            cost[j] = j;
        }

        for (int i = 1; i <= a.length(); i++)
        {
            cost[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++)
            {
                int cj = Math.min(1 + Math.min(cost[j], cost[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = cost[j];
                cost[j] = cj;
            }
        }

        if(cost[b.length()] <= 1)
        {
            result = result + 5;
        }
    }

    public void calcResults()
    {
        temp = prefData.getInt("Support", temp);
        for(int i = 0; i < 20; i++)
        {
            if(input[i].length() > 1 && input[i] != null && temp == 1)
            {
                levenshtein(words[i], input[i]);
            }
            else
            {
                if(input[i].equalsIgnoreCase(words[i]))
                {
                    result = result + 5;
                }
            }
        }
    }

    public void emailOnclick(View v)
    {
        new SendEmailAsyncTask(email,result, name).execute();
    }

    public void feedback()
    {
        

    }
}