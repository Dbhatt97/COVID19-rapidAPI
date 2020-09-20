package com.example.rapidapi_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.JarOutputStream;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

   public TextView show,Confirm_case,Active_case,Death_case,Recovered_case;
    String name;
    public  Spinner spinner;
    String responseStr;
    ArrayList<String> countrylist;
    private RequestQueue mqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView) findViewById(R.id.tv);
        Confirm_case=(TextView)findViewById(R.id.Description_Confirm);
        Active_case=(TextView)findViewById(R.id.Description_active);
        Death_case=(TextView)findViewById(R.id.Description_Death);
        Recovered_case=(TextView)findViewById(R.id.Description_Recovered);

        mqueue = Volley.newRequestQueue(MainActivity.this);

        spinner = findViewById(R.id.countrylist);
        countrylist = new ArrayList<>();


        new getdata().execute();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                String country=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Toast.makeText(MainActivity.this,country,Toast.LENGTH_LONG).show();
                show.setText(country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        }





    private class getdata extends AsyncTask<String, Void, String> {


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
            try {
              responseStr = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseStr;
        }
        protected void onPostExecute(String avoid){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(responseStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = null;
                try {
                    jo = (JSONObject) jsonArray.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    name = (String) jo.get("name");
                    countrylist.add(name);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,countrylist));

            super.onPostExecute(avoid);

        }
    }
}
