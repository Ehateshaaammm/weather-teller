package com.example.mohammadehatesham.wheatherteller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText ;
    TextView resultTextView;
    Button button;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;
            try{
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather :( ", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject part = arr.getJSONObject(i);
                    String main = part.getString("main");
                    String desc = part.getString("description");
                    if(!main.equals("")&& !desc.equals("")){
                        message+=main + " : " + desc + "\r\n";
                    }

                }
                if(!message.equals("")){
                    resultTextView.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not find weather :( ", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather :( ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);
        button = findViewById(R.id.button);

    }

    public void getWeather(View view){
        DownloadTask task = new DownloadTask();
        try{
            task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather :( ", Toast.LENGTH_SHORT).show();
        }

    }

}
