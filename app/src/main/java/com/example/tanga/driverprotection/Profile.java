package com.example.tanga.driverprotection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {
    public ImageView PicProfile , car;
    public TextView Name, count, Model, Brand,Plate;
    User userco = new User();
    User user = new User();
    Vehicle v = new Vehicle();

    private List<String> provinceList;
    String modelselected ;
    String brandselected ;
    Context context = this;
    Button edit, Dashb;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.getLogin(userco);

        session = new SessionManager(this);
        car = (ImageView) findViewById(R.id.car);
        Model = (TextView) findViewById(R.id.Model);
        edit = (Button) findViewById(R.id.Edit);
        Dashb = (Button) findViewById(R.id.Dashboard);
        count = (TextView) findViewById(R.id.CrashSignaled);
        requestJsonObject();
        Dashb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, PredictionActivity.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, choosemodifActivity.class);
                startActivity(intent);
            }

        });
        getCount();

    }





    private void requestJsonObject(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConst.getVehicule + String.valueOf(userco.getVehicle()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("okay", "Response " + response);
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
    private void updateUser(String newpassword) {

        StringRequest sr = new StringRequest(Request.Method.PUT, UrlConst.updateUser+String.valueOf(userco.getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" updated user : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   /* Log.d("el id mte3ou",jsonObject.getString("_id"));
                    id_newuser = Integer.parseInt(jsonObject.getString("_id"));*/

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "error someweher in the request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();

                      params2.put("password", newpassword);


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
        Log.d("el requette update",sr.toString());
        AppController.getInstance().addToRequestQueue(sr);
    }

    private void updateUserVehicle() {

        StringRequest sr = new StringRequest(Request.Method.PUT, UrlConst.updateUser+String.valueOf(userco.getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" updated user : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   /* Log.d("el id mte3ou",jsonObject.getString("_id"));
                    id_newuser = Integer.parseInt(jsonObject.getString("_id"));*/

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "error someweher in the request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                if(brandselected.equals("Renault"))
                {
                    if(modelselected.equals("MEGANE"))
                    {  params2.put("vehicle", String.valueOf(12));}
                    else if(modelselected.equals("CLIO"))
                    {  params2.put("vehicle", String.valueOf(13));}
                    else if(modelselected.equals("SYMBOLE"))
                    {  params2.put("vehicle", String.valueOf(14));}
                }
                else if(brandselected.equals("Mercedes"))
                {
                    if(modelselected.equals("C CLASS"))
                    {  params2.put("vehicle", String.valueOf(15));}
                    else if(modelselected.equals("G CLASS"))
                    {  params2.put("vehicle", String.valueOf(16));}
                    else if(modelselected.equals("GLA"))
                    {  params2.put("vehicle", String.valueOf(17));}
                }
                else if(brandselected.equals("Audi"))
                {
                    if(modelselected.equals("A3"))
                    {  params2.put("vehicle", String.valueOf(18));}
                    else if(modelselected.equals("A4"))
                    {  params2.put("vehicle", String.valueOf(19));}
                    else if(modelselected.equals("Q3"))
                    {  params2.put("vehicle", String.valueOf(20));}
                }

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
        Log.d("el requette update",sr.toString());
        AppController.getInstance().addToRequestQueue(sr);
    }


    private void getCount(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,UrlConst.getNbAccidents+userco.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // JSONArray dataArray = new JSONArray(response);
                    JSONObject obj = new JSONObject(response);

                    String nbr = obj.getString("nbr");
                    count.setText(nbr);


                    //  summary.setText(sum);
                } catch (JSONException e) {
                    e.printStackTrace();
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




}
