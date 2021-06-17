package com.example.tanga.driverprotection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CivilProtectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_protection);
        ListView Accident = findViewById(R.id.carcrashs);
        RequestQueue ReqQueue = Volley.newRequestQueue(getApplicationContext());
        ArrayList<Accidents> Data = new ArrayList<>();
       // Data = FetchDataFromWebService(ReqQueue);
        Log.d("LOG","--------------------------------------------------------------------");
        Data = FetchDataFromWebService(ReqQueue);
        Log.d("LOG", Data.toString());
        Log.d("LOG","--------------------------------------------------------------------");
        CivilProtectionAdapter pAdapter = new CivilProtectionAdapter(getApplicationContext(),Data);
        Accident.setAdapter(pAdapter);
       // Accident.invalidateViews();

    }

    private ArrayList<Accidents> FetchDataFromWebService(RequestQueue RequestQueue)
    {
        final ArrayList<Accidents> PL = new ArrayList<>();
        String url ="http://192.168.43.32:3001/alerts";
        Log.d("LOG", url);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("LOG", response + "");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject Object = response.getJSONObject(i);
                        Log.d("LOG", Object.getString("time"));
                        Accidents P = new Accidents();
                        P.setCrashTime(Object.getString("time"));
                        P.setLongitude(Object.getString("long"));
                        P.setLatitude(Object.getString("lati"));
                        Log.d("LOG", P.toString());
                        PL.add(P);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setShouldCache(false);
        Log.d("LOG","CacheFalse");
        RequestQueue.add(jsonObjectRequest);
        Log.d("LOG","request added");
        return PL;
    }

}
