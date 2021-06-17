package com.example.tanga.driverprotection;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profilefromchat extends AppCompatActivity {
    public ImageView PicProfile , car;
    public TextView Name, Level, Model, Brand,Plate;
    User userco = new User();
    User user = new User();
    Vehicle v = new Vehicle();

    int id = 0;
int signal = 0 ;

ConstraintLayout cl;
    Context context = this;
    Button Signal, Dashb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilefromchat);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.getLogin(userco);


        id = getIntent().getIntExtra("user", 0);
cl=(ConstraintLayout) findViewById(R.id.constraintprofilefomchat);
        car = (ImageView) findViewById(R.id.car);
        Model = (TextView) findViewById(R.id.Model);
        Signal = (Button) findViewById(R.id.Signal);
        Dashb = (Button) findViewById(R.id.Dashboard);
        requestUser(id);

Dashb.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Profilefromchat.this, PredictionActivity.class);
        startActivity(intent);
    }
});
        Signal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signal==0)
                { addSignal(user.getId());
                cl.setBackgroundResource(R.drawable.bgsignaled);
                signal = 1 ;}
                else if (signal==1)
                {
                    deleteSignal(id,userco.getId());
                    cl.setBackgroundResource(R.drawable.profilefromchatbg);
                    signal = 0 ;
                }
                                      }
                                  }

        );}


    private void requestJsonObject(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConst.getVehicule + String.valueOf(user.getVehicle()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("el karhba", "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                v = mGson.fromJson(response, Vehicle.class);
                Model.setText(v.getModel() +" "+ v.getBrand());
//                Brand.setText(v.getBrand());
                //          Color.setBackgroundColor(v.getColor());
                //  Plate.setText(v.getPlates());
                if (v.getBrand().equals("Renault")) {
                    if (v.getModel().equals("CLIO")) {
                        Drawable myDrawable = getResources().getDrawable(R.drawable.renaultclio1);
                        car.setImageDrawable(myDrawable);


                    } else if (v.getModel().equals("SYMBOLE"))
                    {
                        Drawable myDrawable = getResources().getDrawable(R.drawable.renaultsymbole);
                        car.setImageDrawable(myDrawable);

                    } else if (v.getModel().equals("MEGANE"))
                    {Drawable myDrawable = getResources().getDrawable(R.drawable.renauultmegane);
                        car.setImageDrawable(myDrawable);

                        //   car.setImageResource(R.drawable.renaultmegane);
                    }

                } else if (v.getBrand().equals("Audi"))
                {
                    if (v.getModel().equals("A3"))
                    {
                        Drawable myDrawable = getResources().getDrawable(R.drawable.audiathree);
                        car.setImageDrawable(myDrawable);


                    } else if (v.getModel().equals("A4"))
                    {
                        Drawable myDrawable = getResources().getDrawable(R.drawable.audiafour);
                        car.setImageDrawable(myDrawable);

                    } else if (v.getModel().equals("Q3"))
                    {
                        Drawable myDrawable = getResources().getDrawable(R.drawable.audiqthree);
                        car.setImageDrawable(myDrawable);

                    }


                }
                else if (v.getBrand().equals("Mercedes"))
                {  if (v.getModel().equals("C CLASS"))
                {

                    Drawable myDrawable = getResources().getDrawable(R.drawable.mercedescclass);
                    car.setImageDrawable(myDrawable);
                    Log.d("el model ","c classe");

                }


                else if (v.getModel().equals("G CLASS"))
                {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.mercedesgclass);
                    car.setImageDrawable(myDrawable);
                } else if (v.getModel().equals("GLA"))
                {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.mercedesgla);
                    car.setImageDrawable(myDrawable);
                }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hell no", "Error " + error.getMessage());

            }
        }
        );
        Log.d("nchoufouha",stringRequest.toString());
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void requestUser(int id){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConst.FIND_A_USER + id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                Log.d("el user","Response "+response);
                GsonBuilder builder=new GsonBuilder();
                Gson mGson=builder.create();
                user=mGson.fromJson(response,User.class);
                requestJsonObject();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hell no", "Error " + error.getMessage());

            }
        }
        );
        Log.d("nchoufouha",stringRequest.toString());
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    private void addSignal(int userId) {

        StringRequest sr = new StringRequest(Request.Method.POST, UrlConst.addSignal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" new signal : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                  //  Log.d("el id mta3 signal",jsonObject.getString("_id"));
                    //  id_newuser = Integer.parseInt(jsonObject.getString("_id"));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profilefromchat.this, "error somewhere in the prediction request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("idUser", String.valueOf(userId));
                params2.put("idUserWhoSignaled", String.valueOf(userco.getId()));
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
    private void deleteSignal(int userId,int userwhosignaled) {

        StringRequest sr = new StringRequest(Request.Method.DELETE, UrlConst.deleteSignal+userId+"/"+userwhosignaled, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" new signal : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("el id mta3 signal",jsonObject.getString("_id"));
                    //  id_newuser = Integer.parseInt(jsonObject.getString("_id"));

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profilefromchat.this, "error somewhere in the prediction request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("idUser", String.valueOf(userId));
                params2.put("idUserWhoSignaled", String.valueOf(userwhosignaled));
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




}
