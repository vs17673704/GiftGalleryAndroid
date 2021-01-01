package com.android.nasr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton, signupButton;
    RelativeLayout loginLayout;
    ProgressDialog progressDialog;
    String pass,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = (MainActivity.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("login","").equals("true")){
                startActivity(new Intent(MainActivity.this,ValidationActivity.class));
                finish();
        }

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        username        = findViewById(R.id.username);
        password        = findViewById(R.id.password);
        loginButton     = findViewById(R.id.loginButton);
        signupButton    = findViewById(R.id.signupButton);
        progressDialog  = new ProgressDialog(this);

        user = username.getText().toString();
        pass = password.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().matches("")){
                    username.setError("Please enter username");
                }
                else if(password.getText().toString().matches("")){
                    password.setError("Please enter password");
                }

                else
                {
                    if(user.equals("nasr")&&pass.equals("13643"))
                    {
                        SharedPreferences sharedPreferences = (MainActivity.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("role","Admin").commit();
                        editor.putString("login","true").commit();
                        dashboard(AdminDashboard.class);
                    }
                    else
                    {
                        progressDialog.setMessage("Loading");
                        progressDialog.show();
                        try {
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                            String credentials = null;
                            credentials = username.getText().toString() + "/";
                            credentials+=password.getText().toString();

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.loginURL + credentials, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    progressDialog.dismiss();
                                    progressDialog.hide();

                                    Log.w("VOLLEY", response);
                                    try {
                                        if(response.equals("{}"))
                                            Toast.makeText(MainActivity.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                                        else{
                                            Log.e("CLASS:", response.getClass().toString());
                                            JSONObject jsonObject = new JSONObject(response);
                                            String id    = jsonObject.get("custId").toString();
                                            String name  = jsonObject.get("custname").toString();
                                            String address = jsonObject.get("address").toString();
                                            String phone = jsonObject.get("phone").toString();
                                            String email = jsonObject.get("email").toString();
                                            String password = jsonObject.get("password").toString();
                                            String role = jsonObject.get("role").toString();
                                            SharedPreferences sharedPreferences = (MainActivity.this).getSharedPreferences("userprofile",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("customerid",id).commit();
                                            editor.putString("customername",name).commit();
                                            editor.putString("address",address).commit();
                                            editor.putString("phone",phone).commit();
                                            editor.putString("email",email).commit();
                                            editor.putString("password",password).commit();
                                            editor.putString("role",role).commit();
                                            editor.putString("login","true").commit();
                                            editor.commit();
                                            dashboard(CustomerDashboard.class);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    progressDialog.hide();
                                    Toast.makeText(MainActivity.this, "Connection Failed",Toast.LENGTH_SHORT).show();

                                    Log.e("VOLLEY", error.toString());
                                }
                            }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                    String responseString = "";
                                    if (response != null) {
                                        responseString = String.valueOf(response.statusCode);
                                        return super.parseNetworkResponse(response);
                                        // can get more details such as response.headers
                                    }
                                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                }

                            };
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    100000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterCustomerProfile.class));
            }
        });

    }

    public void dashboard(Class aClass)
    {
        startActivity(new Intent(MainActivity.this,ValidationActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}
