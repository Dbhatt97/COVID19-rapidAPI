package com.example.rapidapi_weather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LatestNews extends AsyncTask<String,Void,String > {
    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/report/country/name?date-format=YYYY-MM-DD&format=json&date=2020-04-01&name=Italy")
                .get()
                .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0cfaff05demsha3a5433660b999bp1067bcjsn07626e033c33")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().toString();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        

    }
}
