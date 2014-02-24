package com.primary.spell;

import android.os.AsyncTask;
import android.util.Log;
import javax.mail.AuthenticationFailedException;

class SendEmailAsyncTask extends AsyncTask<String, Integer, Boolean>
{
    Mail m = new Mail("PrimarySpell@gmail.com", "5ruq3vcp3");

    public SendEmailAsyncTask(String email,int results,String name)
	{
        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");

        String toArr[] = {email};
        m.setTo(toArr);
        m.setFrom("PrimarySpell@gmail.com");
        m.setSubject("Test Results");
        m.setBody("Exam results of " + name + " = " + results );
    }

    protected Boolean doInBackground(String... params)
	{
        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
        try 
		{
            m.send();
            return true;
        }
		catch (AuthenticationFailedException e) 
		{
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            return false;
        }
		catch (Exception e) 
		{
            e.printStackTrace();
            return false;
        }
    }
}