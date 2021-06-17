package com.example.tanga.driverprotection;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.SpeedView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SpeedControllerActivity extends AppCompatActivity {


    EditText SpeedInput;
    TextView SpeedOutput;
    Button Submit;

    com.android.volley.RequestQueue RequestQueue;
    SharedPreferences prefs;

    public String CURRENTSPEED;
    public String CURRENTLONG;
    public String CURRENTLATI;
    private int Speed;
    private int OldSpeed;
    private Boolean SuddenStopOrSpeedDecrease;
    private int LastSpeedValueBeforeAlert;
    Handler handler = new Handler();
    Runnable runnable;
    int SecondPassed = 0;
    TextToSpeech TTS;
    SpeedView SV;
    Button GoToMaps;
    TextView LongitudeTV, LatitudeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_controller);
        prefs = getSharedPreferences("ConnectedAccount", MODE_PRIVATE);
        RequestQueue = Volley.newRequestQueue(getApplicationContext());
        SuddenStopOrSpeedDecrease = false;
        SV = findViewById(R.id.speedView);
        LongitudeTV = findViewById(R.id.longitude);
        LatitudeTV = findViewById(R.id.latitude);
        GoToMaps = findViewById(R.id.gotomaps);
        GoToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationurl = "https://www.google.com/maps/@"+LongitudeTV.getText().toString()+","+LatitudeTV.getText().toString()+",15z";
                Log.d("LOG", locationurl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationurl));
                startActivity(browserIntent);
            }
        });
        SV.setMaxSpeed(280);
        SV.setMarkColor(Color.parseColor("#FFFFFF"));


        TTS = new TextToSpeech(SpeedControllerActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = TTS.setLanguage(Locale.ENGLISH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("LOGS", "Language  Not Supported");
                    }
                }
            }
        });


        if(Speed == 0)
        {
           // SpeedOutput.setText("0 KM/H");
        }

        Thread T = new Thread()
        {
            @Override
            public void run() {
                while(!isInterrupted()){
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GetSimulationValuesFromDatabase();
                                Log.d("LOGS","-----------------------------");
                                Log.d("LOGS",prefs.getString("speed","nullspeed")+" " +prefs.getString("long", "nulllong")+" "+prefs.getString("lati","nulllati"));
                                Log.d("LOGS","-----------------------------");
                                //Speed = Integer.parseInt(prefs.getString("speed", null));
                                //SpeedOutput.setText(Speed+" KM/H");
                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        T.start();

       /* Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Speed == 0)
                {
                    Speed = Integer.parseInt(SpeedInput.getText().toString());
                }
                else
                {
                    if((Integer.parseInt(SpeedInput.getText().toString())+ 80 ) <= Speed)
                    {
                        Log.d("LOG INFO", "Sudden Stop/Speed Decrease.");
                        SuddenStopOrSpeedDecrease = true;
                        LastSpeedValueBeforeAlert = Speed;

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if(Speed <= LastSpeedValueBeforeAlert && SuddenStopOrSpeedDecrease == true)
                                {

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SpeedControllActivity.this);
                                        builder1.setMessage("Hey! ARE YOU OKAY DUDE?");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        builder1.setNegativeButton(
                                                "No",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();


                                }
                            }
                        },15000);

                    }
                    else
                    {
                        if(SuddenStopOrSpeedDecrease == true)
                        {
                            SuddenStopOrSpeedDecrease = false;
                        }
                    }
                    Speed = Integer.parseInt(SpeedInput.getText().toString());
                }
                SpeedOutput.setText(Speed + "KM/H");
            }
        });


*/
    }

    public void InsertAlertIntoDatabase()
    {
         /*StringRequest req = new StringRequest(Request.Method.POST, "http://192.168.43.32:3001/alerts", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject obj = new JSONObject(response);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d("LOGS", error.toString());
             }
         })
         {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> parameters = new HashMap<String, String>();
                 parameters.put("time","Tawa1");
                 parameters.put("long","50.50");
                 parameters.put("lati","10.10");
                 return parameters;
             }
         };

        ReqQueue.add(Req);*/












        //JsonObjectRequest JOR = new JsonObjectRequest(Request.Method.GET, )
        StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.43.179:3001/alerts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("LOGS",obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("LOGS", error.toString());
            }
        })
        {
            @Override
            public byte[] getBody()  {
                HashMap<String, String> parmas = new HashMap<>();
                int begin = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int end = begin +1 ;
                int dayofmonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                String transformedmonth;
                if(month<10)
                {
                     transformedmonth = "0"+String.valueOf(month);
                }
                else transformedmonth = String.valueOf(month);
                int year = Calendar.getInstance().get(Calendar.YEAR);

                String day = String.valueOf(year)+"-"+transformedmonth+"-"+String.valueOf(dayofmonth)+"";
                parmas.put("time",day);
                parmas.put("long", prefs.getString("long", "25.25"));
                parmas.put("lati", prefs.getString("lati", "14.14"));
                return new JSONObject(parmas).toString().getBytes();

            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

       // Log.d("LOGS : ",sr.toString());
        Log.d("LOGS", "ADDED TO DATABASE");
        RequestQueue.add(sr);


    }

    public void GetSimulationValuesFromDatabase()
    {
        JsonArrayRequest JOR = new JsonArrayRequest(Request.Method.GET, "http://192.168.43.179:3001/sim", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    Log.d("LOGS", response.toString());
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("speed", obj.getString("speed"));
                        editor.putString("long", obj.getString("long"));
                        editor.putString("lati", obj.getString("lati"));
                        editor.apply();
                        LongitudeTV.setText(obj.getString("long"));
                        LatitudeTV.setText(obj.getString("lati"));
                        if(Speed == 0)
                        {
                            Speed = Integer.parseInt(obj.getString("speed"));
                            SV.speedTo(Float.valueOf(Speed));

                        }
                        else
                        {
                            OldSpeed = Speed;
                            Speed = Integer.parseInt(obj.getString("speed"));
                            SV.speedTo(Float.valueOf(Speed));

                            Log.d("LOGS", Speed +"-"+OldSpeed);
                            if((Speed + 80 ) <= OldSpeed && SuddenStopOrSpeedDecrease == false)
                            {
                                Log.d("LOGS", "Sudden Stop/Speed Decrease.");
                                SuddenStopOrSpeedDecrease = true;
                                LastSpeedValueBeforeAlert = Speed;

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SpeedControllerActivity.this);
                                builder1.setMessage("Hey there, are you okay? If you do not respond in 20 seconds, Civil protection will be alerted with your current location.");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                SuddenStopOrSpeedDecrease = false;
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                speak("Hey there, are you okay? If you do not respond in 20 seconds, Civil protection will be alerted with your current location.");

                                /*handler.postDelayed(new Runnable() {
                                    public void run() {
                                        if(Speed <= LastSpeedValueBeforeAlert && SuddenStopOrSpeedDecrease == true)
                                        {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SpeedControllActivity.this);
                                            builder1.setMessage("Hey! ARE YOU OKAY DUDE?");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    }
                                },15000);*/

                            }
                            else
                            {
                               /* if(SuddenStopOrSpeedDecrease == true)
                                {
                                    SuddenStopOrSpeedDecrease = false;
                                }*/
                            }
                            if(SuddenStopOrSpeedDecrease == true)
                            {
                                Log.d("LOGS", "STILL IN DANGER "+ LastSpeedValueBeforeAlert);
                                SecondPassed++;
                                if(SecondPassed >=10)
                                {

                                    speak("Civil Protection has been notified");
                                    InsertAlertIntoDatabase();
                                    SuddenStopOrSpeedDecrease = false;
                                }

                            }
                            //Speed = Integer.parseInt(SpeedInput.getText().toString());
                        }

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
        JOR.setShouldCache(false);
        RequestQueue.add(JOR);
    }

    private void speak(String text)
    {
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(TTS != null)
        {
            TTS.stop();
            TTS.shutdown();
        }
        super.onDestroy();
    }
}
