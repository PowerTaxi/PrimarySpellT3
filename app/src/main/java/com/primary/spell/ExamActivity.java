package com.primary.spell;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Prediction;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Gesture;
import java.util.ArrayList;

public class ExamActivity extends Activity implements OnClickListener, OnInitListener, OnGesturePerformedListener
{
    private TextToSpeech text2speech;
    private ProgressBar progressBar;
    private GestureLibrary customGest;
    private SharedPreferences prefData;
    private final String filename = "MyPrefs";
    private String [] words = new String[20];
    private String [] input = new String[20];
    private int num;
    private int count = 0;
    private int temp = 0;
    private String location;
    private HashSet<String> wordSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        prefData = this.getSharedPreferences(filename, 0);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        readFile();
        customGest = GestureLibraries.fromRawResource(this, R.raw.gestures);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        if (!customGest.load())
        {
            finish();
        }

        GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        overlay.addOnGesturePerformedListener(this);
        text2speech = new TextToSpeech(this, this);
        findViewById(R.id.imageButton3).setOnClickListener(this);
    }

    @Override
    public void onInit(int code)
    {
        if (code == TextToSpeech.SUCCESS)
        {
            text2speech.setLanguage(Locale.getDefault());
        }
        else
        {
            text2speech = null;
            Toast.makeText(this, "Failed to initialise TTS engine.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view)
    {
        if (text2speech != null)
        {
            String text = words[0];
            if (text != null && count <= 19)
            {
                if (!text2speech.isSpeaking())
                {
                    text2speech.speak(input[temp], TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            else
            {
                text2speech.speak("something went wrong", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
    {
        ArrayList<Prediction> predictions = customGest.recognize(gesture);

        if (predictions.size() > 0)
        {
            Prediction prediction = predictions.get(0);

            if (prediction.score > 1.0)
            {
                Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
                if(input[temp] != null)
                {
                    input[temp] = input[temp] + prediction.toString();
                }
                else
                {
                    input[temp] = prediction.toString();
                }
            }
        }
    }

    public void nextButton(View view)
    {
        if(count <= 19)
        {
            if(input[temp] == "" || input[temp] == null)
            {
                input[temp] = ".";
            }
        }
        count++;
        temp++;
        progressBar.setProgress((count * 5));
        if (text2speech != null)
        {
            String text = words[0];
            if (text != null && count <= 19)
            {
                if (!text2speech.isSpeaking())
                {
                    text2speech.speak(words[count], TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            else
            {
                progressBar.setProgress(100);
                text2speech.speak("congratulations, you have completed the test", TextToSpeech.QUEUE_FLUSH, null);
                resultsPage();
            }
        }
    }

    public void resultsPage()
    {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("Words", words);
        intent.putExtra("Input", input);
        startActivity(intent);
    }

    public void deleteButton(View view)
    {
        if(input[temp].length() != 0)
        {
            input[temp] = input[temp].substring(0, input[temp].length() - 1);
        }
    }

    public void readFile()
    {
        BufferedReader bufferedRead;
        num = prefData.getInt("Class", num);
        try
        {
            switch (num)
            {
                case 0:
                    location = "juniorInfants.txt";
                    break;
                case 1:
                    location = "seniorInfants.txt";
                    break;
                case 2:
                    location = "firstClass.txt";
                    break;
                case 3:
                    location = "secondClass.txt";
                    break;
                case 4:
                    location = "thirdClass.txt";
                    break;
                case 5:
                    location = "fourthClass.txt";
                    break;
            }

            String line;
            bufferedRead = new BufferedReader(new InputStreamReader(getAssets().open(location)));

            while((line = bufferedRead.readLine()) != null)
            {
                wordSet.add(line);
            }

            bufferedRead.close();
            setToArray();
        }
        catch(IOException e){e.printStackTrace();}
    }

    public void setToArray()
    {
        int i = 0;
        for(String string : wordSet)
        {
            if(i < 20)
            {
                words[i] = string;
                i++;
            }
            else
            {
                break;
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        if (text2speech != null)
        {
            text2speech.stop();
            text2speech.shutdown();
        }
        super.onDestroy();
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
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