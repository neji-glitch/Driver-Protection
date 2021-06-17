package com.example.tanga.driverprotection;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class choosemodifActivity extends AppCompatActivity {
    Button pwd, caredit;

    User userco = new User();

    private List<String> provinceList;
    String modelselected;
    String brandselected;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosemodif);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.getLogin(userco);

        pwd = (Button) findViewById(R.id.changepassword);
        caredit = (Button) findViewById(R.id.changecar);
        caredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View editcar = LayoutInflater.from(choosemodifActivity.this)
                        .inflate(R.layout.carchange, null);
                MaterialStyledDialog.Builder mtcar = new MaterialStyledDialog.Builder(choosemodifActivity.this);

                SmartMaterialSpinner spProvince = (SmartMaterialSpinner) editcar.findViewById(R.id.regbrandformodif);
                provinceList = new ArrayList<>();
                provinceList.add("Renault");
                provinceList.add("Mercedes");
                provinceList.add("Audi");

                spProvince.setItems(provinceList);
                List<String> modelList = new ArrayList<>();
                Log.d("item menhom howa", spProvince.getItemAtPosition(1).toString());
                SmartMaterialSpinner model = (SmartMaterialSpinner) editcar.findViewById(R.id.regmodelformofid);
                spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Toast.makeText(choosemodifActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
                        brandselected = provinceList.get(position);
                        if (position == 0) {


                            modelList.clear();
                            modelList.add("MEGANE");
                            modelList.add("CLIO");
                            modelList.add("SYMBOLE");
                            model.setItems(modelList);


                        } else if (position == 1) {
                            modelList.clear();
                            //  Log.d("selected shit"," mercedes")    ;
                            modelList.add("C CLASS");
                            modelList.add("G CLASS");
                            modelList.add("GLA");
                            model.setItems(modelList);

                        } else if (position == 2) {

                            modelList.clear();
                            modelList.add("A3");
                            modelList.add("A4");
                            modelList.add("Q3");
                            model.setItems(modelList);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(choosemodifActivity.this, "Please make a choice", Toast.LENGTH_SHORT).show();
                    }
                });
                model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(choosemodifActivity.this, modelList.get(position), Toast.LENGTH_SHORT).show();
                        modelselected = modelList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(choosemodifActivity.this, "Please make a choice", Toast.LENGTH_SHORT).show();
                    }
                });


                mtcar.setIcon(R.drawable.ic_model)
                        .setTitle("NEW CAR")
                        .setDescription("Fill with your new car's information")
                        .setCustomView(editcar)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }

                        })
                        .setPositiveText("Confirm")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                updateUserVehicle();
                                Toast.makeText(choosemodifActivity.this,"Succesfuly modified !!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(choosemodifActivity.this, Home2Activity.class);
                                startActivity(intent);
                            }

                        }).show();
            }


        });
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View editpwd = LayoutInflater.from(choosemodifActivity.this)
                        .inflate(R.layout.password, null);
                MaterialStyledDialog.Builder mtpwd = new MaterialStyledDialog.Builder(choosemodifActivity.this);
                mtpwd.setIcon(R.drawable.ic_account)
                        .setTitle("EDIT");
                mtpwd.setDescription("Choose what you want to edit")
                        .setCustomView(editpwd)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }

                        })
                        .setPositiveText("Confirm")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText pass1 = (MaterialEditText) editpwd.findViewById(R.id.changethepassword);
                                MaterialEditText pass2 = (MaterialEditText) editpwd.findViewById(R.id.confirmthepassword);
                                if (TextUtils.isEmpty(pass1.getText().toString())) {
                                    Toast.makeText(choosemodifActivity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (!pass1.getText().toString().equals(pass2.getText().toString())) {
                                    Toast.makeText(choosemodifActivity.this, "You didn't retype the same password", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    updateUser(pass1.getText().toString());
                                    Toast.makeText(choosemodifActivity.this,"Succesfuly modified !!",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(choosemodifActivity.this, Home2Activity.class);
                                    startActivity(intent);
                                }

                            }

                        }).show();
            }

        });
    }

    private void updateUser(String newpassword) {

        StringRequest sr = new StringRequest(Request.Method.PUT, UrlConst.updateUser + String.valueOf(userco.getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" updated user : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   /* Log.d("el id mte3ou",jsonObject.getString("_id"));
                    id_newuser = Integer.parseInt(jsonObject.getString("_id"));*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(choosemodifActivity.this, "error someweher in the request", Toast.LENGTH_LONG).show();
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
        Log.d("el requette update", sr.toString());
        AppController.getInstance().addToRequestQueue(sr);
    }

    private void updateUserVehicle() {

        StringRequest sr = new StringRequest(Request.Method.PUT, UrlConst.updateUser + String.valueOf(userco.getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" updated user : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                   /* Log.d("el id mte3ou",jsonObject.getString("_id"));
                    id_newuser = Integer.parseInt(jsonObject.getString("_id"));*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(choosemodifActivity.this, "error someweher in the request", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                if (brandselected.equals("Renault")) {
                    if (modelselected.equals("MEGANE")) {
                        params2.put("vehicle", String.valueOf(12));
                    } else if (modelselected.equals("CLIO")) {
                        params2.put("vehicle", String.valueOf(13));
                    } else if (modelselected.equals("SYMBOLE")) {
                        params2.put("vehicle", String.valueOf(14));
                    }
                } else if (brandselected.equals("Mercedes")) {
                    if (modelselected.equals("C CLASS")) {
                        params2.put("vehicle", String.valueOf(15));
                    } else if (modelselected.equals("G CLASS")) {
                        params2.put("vehicle", String.valueOf(16));
                    } else if (modelselected.equals("GLA")) {
                        params2.put("vehicle", String.valueOf(17));
                    }
                } else if (brandselected.equals("Audi")) {
                    if (modelselected.equals("A3")) {
                        params2.put("vehicle", String.valueOf(18));
                    } else if (modelselected.equals("A4")) {
                        params2.put("vehicle", String.valueOf(19));
                    } else if (modelselected.equals("Q3")) {
                        params2.put("vehicle", String.valueOf(20));
                    }
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
        Log.d("el requette update", sr.toString());
        AppController.getInstance().addToRequestQueue(sr);
    }
}

