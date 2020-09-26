package com.example.rapidapi_weather;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = MainActivity.class.getName();

    public static TextView show,Confirm_case,Active_case,Death_case,Recovered_case;
    String name;
    Double lat,lon;
    int flag;
    public  Spinner spinner;
    String responseStr;
    ArrayList<String> countrylist;
    private RequestQueue mqueue;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private static final float DEFAULT_ZOOM = 5f;


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
            new LatestNews().execute();

            ///DropDown Menu on top
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    String country=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    show.setText(country);
                    flag = countrylist.indexOf(country);
                    Log.d("MYINT", "value: " + flag); //loging index value to get lat log of that city
                    geoLocate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(21, 57);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
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

            // Toast.makeText(MainActivity.this,String.valueOf(lat),Toast.LENGTH_LONG).show();
            spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,countrylist));


            super.onPostExecute(avoid);

        }
    }

    private void geoLocate(){

        String searchString = countrylist.get(flag);
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }
        catch (IOException e){
            Log.d(TAG,"geoLocate: IOException: " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM);
       
        }
    }
    private void moveCamera(LatLng latLng, float zoom){

         if(mMap!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
    }
}
