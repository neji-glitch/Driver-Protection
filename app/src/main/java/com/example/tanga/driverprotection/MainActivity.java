package com.example.tanga.driverprotection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {
    TextView txt_create_account;
    MaterialEditText edt_login_email,edt_login_password;
    Button btn_login;
    int id_newuser , id_car=1,vehiculeColor,signalnbr=0 ;
SessionManager session;
    private List<String> provinceList;
    String modelselected ;
    String brandselected ;
    Context context = this;
    CompositeDisposable compositeDisposable  = new CompositeDisposable();
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("users");
        session = new SessionManager(this);
        edt_login_email = (MaterialEditText)findViewById(R.id.edt_email);
        edt_login_password = (MaterialEditText)findViewById(R.id.edt_password);




        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_login_password.getText().toString().equals("pcgovtn") && edt_login_email.getText().toString().equals("pc@gov.tn"))
                {
                    startActivity(new Intent(MainActivity.this, CivilProtectionActivity.class));
                }
                else {
                    loginUser(edt_login_email.getText().toString(),
                            edt_login_password.getText().toString());
                }

            }
        });
        txt_create_account = (TextView)findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View register_layout = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout, null);
                View register_layout2 = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout2, null);
                MaterialStyledDialog mt = new MaterialStyledDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_account)
                        .setTitle("Registration")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }

                        })
                        .setPositiveText("Next")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_email = (MaterialEditText)register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_fname = (MaterialEditText)register_layout.findViewById(R.id.edt_fname);
                                MaterialEditText edt_register_lname = (MaterialEditText)register_layout.findViewById(R.id.edt_lname);
                                MaterialEditText edt_register_password = (MaterialEditText)register_layout.findViewById(R.id.edt_password);

                               if(TextUtils.isEmpty(edt_register_email.getText().toString()))
                                {
                                    Toast.makeText(MainActivity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_fname.getText().toString()))
                                {
                                    Toast.makeText(MainActivity.this, "First Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_lname.getText().toString()))
                                {
                                    Toast.makeText(MainActivity.this, "Last Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(TextUtils.isEmpty(edt_register_password.getText().toString()))
                                {
                                    Toast.makeText(MainActivity.this, "Password cannot be null or emplty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                 /* registerUser(edt_register_email.getText().toString(),
                                  edt_register_name.getText().toString(),
                                  edt_register_email.getText().toString());*/
                                else{
                                    // MainActivity.this.removeDialog();


                                  MaterialStyledDialog.Builder mt =  new MaterialStyledDialog.Builder(MainActivity.this);
                                  mt.setCustomView(register_layout2);
                                   //  spProvince = new SmartMaterialSpinner(register_layout2.getContext());

                                    SmartMaterialSpinner spProvince  = (SmartMaterialSpinner)register_layout2.findViewById(R.id.regbrand);
                                    provinceList = new ArrayList<>();
                                    provinceList.add("Renault");
                                    provinceList.add("Mercedes");
                                    provinceList.add("Audi");

                                      spProvince.setItems(provinceList);
                                    List<String> modelList = new ArrayList<>();
                                    Log.d("item menhom howa",spProvince.getItemAtPosition(1).toString());
                                SmartMaterialSpinner model  = (SmartMaterialSpinner)register_layout2.findViewById(R.id.regmodel);
                                    spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
                                            brandselected = provinceList.get(position);
                                            if (position == 0) {


                                                modelList.clear();
                                                modelList.add("MEGANE");
                                                modelList.add("CLIO");
                                                modelList.add("SYMBOLE");
                                                model.setItems(modelList);


                                            }
                                            else if(position ==1)
                                            {
                                                modelList.clear();
                                                //  Log.d("selected shit"," mercedes")    ;
                                                modelList.add("C CLASS");
                                                modelList.add("G CLASS");
                                                modelList.add("GLA");
                                                model.setItems(modelList);

                                            }
                                            else if(position ==2){

                                                modelList.clear();
                                                modelList.add("A3");
                                                modelList.add("A4");
                                                modelList.add("Q3");
                                                model.setItems(modelList);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    });
                                    model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Toast.makeText(MainActivity.this, modelList.get(position), Toast.LENGTH_SHORT).show();
                                            modelselected = modelList.get(position);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                MaterialEditText edt_register_color = (MaterialEditText)register_layout2.findViewById(R.id.edt_Color);
                                edt_register_color.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ColorPickerDialogBuilder
                                                .with(context)
                                                .setTitle("Choose color")
                                                .initialColor(R.color.myTextColor)
                                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                                .density(12)
                                                .setOnColorSelectedListener(new OnColorSelectedListener() {
                                                    @Override
                                                    public void onColorSelected(int selectedColor) {
                                                      //  Toast.makeText("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                                        Log.d("color selected","onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                                    }
                                                })
                                                .setPositiveButton("ok", new ColorPickerClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                                      //  changeBackgroundColor(selectedColor);
                                                        edt_register_color.setBackgroundColor(selectedColor);
                                                        vehiculeColor = selectedColor;
                                                    }
                                                })
                                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .build()
                                                .show();
                                    }
                                });

                                  //  initModel(spProvince,model);
                                            mt.setIcon(R.drawable.ic_account)
                                            .setTitle("Car information")
                                            .setDescription("Please fill all fields")

                                            .setNegativeText("CANCEL")
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    dialog.dismiss();
                                                }

                                            })
                                            .setPositiveText("Register")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                           // registerVehicule(modelselected,brandselected,String.valueOf(vehiculeColor));
                                                          //  SessionManager session2 = new SessionManager(MainActivity.this);
                                                        //    id_car=session2.getVeichule();
                                                        //    Log.d("idcarinmain ",String.valueOf(id_car));
                                                    registerUser(edt_register_email.getText().toString(),edt_register_fname.getText().toString(),edt_register_lname.getText().toString(),edt_register_password.getText().toString());
                                                                                 /*   Log.d("modelselected",modelselected);
                                                    Log.d("brandselected",brandselected);*/


                                                }
                                            }).show();}


                            }
                        }).show();

            }
        });
        animatetaswira();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                animatetextcreate();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
               animatelokhrin();
            }
        }, 1000);

    }
    private void initModel(SmartMaterialSpinner spProvince,SmartMaterialSpinner model) {


        provinceList = new ArrayList<>();
        if(spProvince.getSelectedItem().toString().equals("Renault"))
        {
        Log.d("selected shit"," renault")    ;
            provinceList.clear();
            provinceList.add("MEGANE");
        provinceList.add("CLIO");
        provinceList.add("SYMBOLE");}
        else  if(spProvince.getSelectedItem().toString().equals("Audi"))
        {Log.d("selected shit"," audi")    ;
        provinceList.clear();
        provinceList.add("A3");
            provinceList.add("A4");
            provinceList.add("Q3");}
            else
        {
            provinceList.clear();
            Log.d("selected shit"," mercedes")    ;
            provinceList.add("C CLASS");
            provinceList.add("G CLASS");
            provinceList.add("GLA");}
        model.setItems(provinceList);
        Log.d("item menhom howa",spProvince.getItemAtPosition(1).toString());
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }
    private void registerUser(String email, String firstName,String lastName, String password) {
     /*   compositeDisposable.add(iMyservice.registerUser(email,name,
                password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText( MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));*/
        StringRequest sr = new StringRequest(Request.Method.POST, UrlConst.addUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" new user : ",response);
                if(!response.equals("email already exists"))
                {try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("el id mte3ou",String.valueOf(jsonObject.getInt("_id")));
                    id_newuser = jsonObject.getInt("_id");
                    updateUser();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }}
                else {
                    Toast.makeText(MainActivity.this, "email already exists", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error someweher in the request", Toast.LENGTH_LONG).show();
                Log.d("el erreur : ",error.getMessage());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("first_name", firstName);
                params2.put("last_name", lastName);
                params2.put("email", email);
                params2.put("password", password);
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

    private void registerVehicule(String model, String brand,String color) {
     /*   compositeDisposable.add(iMyservice.registerUser(email,name,
                password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText( MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));*/
        StringRequest sr = new StringRequest(Request.Method.POST, UrlConst.addVehicule, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" new vehicule : ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("el id mte3ha",jsonObject.getString("_id"));
                 //   id_car = Integer.parseInt(jsonObject.getString("_id"));
                    session.setVehicule(Integer.parseInt(jsonObject.getString("_id")));


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error someweher in the request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("brand", brand);
                params2.put("model", model);
                params2.put("color", color);

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

        AppController.getInstance().addToRequestQueue(sr);

    }
    private void updateUser() {
     /*   compositeDisposable.add(iMyservice.registerUser(email,name,
                password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText( MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));*/
        StringRequest sr = new StringRequest(Request.Method.PUT, UrlConst.updateUser+String.valueOf(id_newuser), new Response.Listener<String>() {
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
                Toast.makeText(MainActivity.this, "error someweher in the request", Toast.LENGTH_LONG).show();
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

    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConst.loginUser+email+"/"+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("okay", "Response " + response);
                if((!response.equals("couldn't find email"))&&(!response.equals("check the password")))
                { GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                User user = new User();
                user = mGson.fromJson(response,User.class);
                session.setLogin(true,user);
                Log.d("esmou: ",user.getFirstName()+"karhabtou "+user.getVehicle());
                getCount(user.getId());
                    Log.d("signalnbrjuste9baltest",""+signalnbr);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            if(signalnbr >=5) { Toast.makeText(MainActivity.this,"your account has been blocked !!",Toast.LENGTH_SHORT).show();}
                            else{ Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);}
                                          }
                    }, 1000);


               }
                else
                {
                //lu = Arrays.asList(mGson.fromJson(response, DonationType[].class));
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();}
               /* if(response != null)
                {
                    User user = new User();
                    user = mGson.fromJson(response,User.class);
                    Toast.makeText(MainActivity.this,"connected Succesfully",Toast.LENGTH_SHORT).show();
                }*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hell no", "Error " + error.getMessage());
               // Toast.makeText(MainActivity.this,"email not found",Toast.LENGTH_SHORT).show();

            }
        }
        );
        Log.d("nchoufouha",stringRequest.toString());
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
       /* compositeDisposable.add(iMyservice.LoginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText( MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                    }
                }));*/
       }
    public void animatetaswira(){
        LinearLayout dialog   = (LinearLayout)findViewById(R.id.taswiramain);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }

    public void animatetextcreate(){
        TextView dialog   = (TextView) findViewById(R.id.txt_create_account);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }
    public void animatelokhrin(){
        LinearLayout dialog   = (LinearLayout)findViewById(R.id.restmain);
        dialog.setVisibility(LinearLayout.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(this, R.anim.md_styled_slide_up_slow);
        animation.setDuration(1000);
        dialog.setAnimation(animation);
        dialog.animate();
        animation.start();
    }
    private void getCount(int idofuser){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,UrlConst.countSignal+idofuser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // JSONArray dataArray = new JSONArray(response);
                    JSONObject obj = new JSONObject(response);
                    signalnbr = obj.getInt("nbr");
                    Log.d("signalnbrfilrequette",""+signalnbr);

                    //   String nbr = obj.getString("nbr");
                  //  count.setText(nbr);


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
