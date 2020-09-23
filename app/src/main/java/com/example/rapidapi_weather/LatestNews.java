package com.example.rapidapi_weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
// Developement of this section is on hold because project is not funded and
// it is my stand alone project further implementation needs funding....
//okay so i guess i will parse the JSON coming from italy and will show it on
// 4 section provided on Main activity under latest Update for now as place holder....... :( THB not happy with this.

 public class LatestNews extends AsyncTask<String,Void,String > {
   private Context context;
   String result;
   Integer Confirm,Active,Death,Recovered;

    @Override
    protected String doInBackground(String... strings) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-data.p.rapidapi.com/report/country/name?date-format=YYYY-MM-DD&format=json&date=2020-04-01&name=Italy")
                .get()
                .addHeader("x-rapidapi-host", "covid-19-data.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "125301d8e4mshb4a7cf457a07801p170ceejsne508dad955b1")
                .build();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    protected void onPostExecute(String s){

        super.onPostExecute(s);
        try {
            JSONArray jsonArray = new JSONArray(result);
                JSONObject jo = (JSONObject) jsonArray.get(0);
                    JSONArray prov = jo.getJSONArray("provinces");
                      JSONObject jo2 = (JSONObject) prov.get(0);
                           Confirm = (Integer)jo2.get("confirmed");
                            Active = (Integer)jo2.get("active");
                            Death = (Integer)jo2.get("deaths");
                            Recovered = (Integer)jo2.get("recovered");

                            MainActivity.Confirm_case.setText(String.valueOf(Confirm));
                            MainActivity.Recovered_case.setText(String.valueOf(Recovered));
                            MainActivity.Death_case.setText(String.valueOf(Death));
                            MainActivity.Active_case.setText(String.valueOf(Active));
                }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
