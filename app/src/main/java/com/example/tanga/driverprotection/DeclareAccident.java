package com.example.tanga.driverprotection;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.rengwuxian.materialedittext.MaterialEditText;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DeclareAccident extends AppCompatActivity  {
    int begin,end,dayofmonth,month,year;
    SessionManager session;
    MaterialEditText speed,adress;
    User u ;
    String day,DARK_SKY_API_KEY,option_list,transformedmonth,transformedday;
    Button confirm ;
    double longitude=10.471516,latitude=36.635198,longitude2=10.471516,latitude2=36.635198;
    private Location location;
    Context context = this;
    private LocationManager locationManager;
    private LocationListener listener;


    double temperature=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_accident);
        u = new User();
        option_list = "exclude=currently,minutely,hourly,alerts&units=si";
        session=new SessionManager(this);
        session.getLogin(u);
        adress = findViewById(R.id.adrfordeclare);
        speed = findViewById(R.id.speedfordeclare);
        begin = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        confirm = findViewById(R.id.confirmaccidentdecclare);
        end = begin +1 ;
        DARK_SKY_API_KEY = "efc5d7378dc028e5969fe0085c7a7866";
        dayofmonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        if(month<10)
        {
            transformedmonth = "0"+String.valueOf(month);
        }
        else transformedmonth = String.valueOf(month);
        if(dayofmonth<10)
        {
            transformedday = "0"+String.valueOf(dayofmonth);
        }
        else transformedday = String.valueOf(dayofmonth);
        year = Calendar.getInstance().get(Calendar.YEAR);

        day = String.valueOf(year)+"-"+transformedmonth+"-"+transformedday+"T00:00:00";
        getWeather();
       /* GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(context);
        if (mGpsLocationTracker.canGetLocation())
        {

           adress.setText("long: "+mGpsLocationTracker.getLongitude()+"lat: "+mGpsLocationTracker.getLatitude());

        }
        else
        {
            mGpsLocationTracker.showSettingsAlert();
        }
*/
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              //  t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                adress.setText(getAdress(location.getLongitude(),location.getLatitude()));
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccident();
                Toast.makeText(DeclareAccident.this,"Thank you for declaring the accident",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeclareAccident.this, Home2Activity.class);
                startActivity(intent);
            }
        });
        animatetaswira();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                animatesecond();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                animateButton();
            }
        }, 1000);


    }
    private void getWeather(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://api.darksky.net/forecast/"+DARK_SKY_API_KEY+"/"+latitude2+","+longitude2+","+day+"?"+option_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // JSONArray dataArray = new JSONArray(response);
                    JSONObject obj = new JSONObject(response);
                    JSONObject daily = obj.getJSONObject("daily");
                    JSONArray data = daily.getJSONArray("data");
                    JSONObject dkhoul = data.getJSONObject(0);
                    String temp = dkhoul.getString("temperatureMax");
                    temperature = Double.valueOf(temp);

                    //  summary.setText(sum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  Log.d("okay msg", "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                MessageList.addAll(Arrays.asList(mGson.fromJson(response, Message[].class)));


                chatBoxAdapter = new ChatRoomAdapter(MessageList, ChatRoomActivity.this);
                myRecylerView.setAdapter(chatBoxAdapter);*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hell no", "Error " + error.getMessage());

            }
        }
        );
        Log.d("nchoufouha",stringRequest.toString());
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    private void addAccident() {
        if(TextUtils.isEmpty(adress.getText().toString())) {
            Toast.makeText(this, "Adress cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(speed.getText().toString())) {
            Toast.makeText(this, "Adress cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest sr = new StringRequest(Request.Method.POST, UrlConst.addAccident, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //     Log.d(" new danger : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("el  accident",jsonObject.toString());
                    //  id_newuser = Integer.parseInt(jsonObject.getString("_id"));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeclareAccident.this, "error someweher in the accident request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("idUser", String.valueOf(u.getId()));
                params2.put("temperature", String.valueOf(temperature));
                params2.put("hour_begin", String.valueOf(begin));
                params2.put("hour_end", String.valueOf(end));
                params2.put("adress", adress.getText().toString());
                params2.put("speed", speed.getText().toString());
                params2.put("longitude", String.valueOf(longitude));
                params2.put("latitude", String.valueOf(latitude));
                params2.put("day", day);


                return new JSONObject(params2).toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }


        };

           /* stringRequest.addMultipartParam("id_user","x-www-form-urlencoded",Integer.toString(id));
            stringRequest.addMultipartParam("id_cause","x-www-form-urlencoded",Integer.toString(c.getId()));
            AppController.getInstance().addToRequestQueue(stringRequest);*/
        Log.d("el requette",sr.toString());
        AppController.getInstance().addToRequestQueue(sr);
    }
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

                //noinspection MissingPermission
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 5, listener);
            }
    public String getAdress(double longitude,double latitude)
    {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        Log.d("haw chetjib :",address + ",city: "+city+ ",country: "+country+" "+state+" "+postalCode+ " "+knownName);
        return state+ ","+country;

    }
    public void animatetaswira(){
        LinearLayout dialog   = (LinearLayout)findViewById(R.id.firstdeclare);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }

    public void animatesecond(){
        LinearLayout dialog   = (LinearLayout) findViewById(R.id.seconddeclare);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }
    public void animateButton(){
        Button dialog   = (Button) findViewById(R.id.confirmaccidentdecclare);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.md_styled_slide_up_normal);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }



}
