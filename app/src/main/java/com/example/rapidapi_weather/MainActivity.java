package com.example.rapidapi_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    TextView show;
    Button data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (Button) findViewById(R.id.button);
        show = (TextView) findViewById(R.id.tv);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getdata().execute();
                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
            }
        });


    }
private class getdata extends AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/help/countries?format=json")
                .get()
                .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0cfaff05demsha3a5433660b999bp1067bcjsn07626e033c33")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String myrsult = null;
        try {
            myrsult = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myrsult;
    }
    protected void onPostExecute(String s) {



        show.setText(s);
    }
    }
}


