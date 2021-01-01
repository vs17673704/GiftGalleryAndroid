package com.android.nasr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RegisterCustomerProfile extends AppCompatActivity {

    EditText getUserName, getCustomerName, getAddress, getPhoneNumber, getEmail, getPassword;
    ProgressDialog progressDialog;
    Button registerButton;
    boolean connection = true;
    Boolean valid = true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile_set_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("REGISTER");

        getUserName        =   findViewById(R.id.getUserName);
        getCustomerName    =   findViewById(R.id.getCustomerName);
        getAddress         =   findViewById(R.id.getAddress);
        getPhoneNumber     =   findViewById(R.id.getPhoneNumber);
        getEmail           =   findViewById(R.id.getEmail);
        getPassword        =   findViewById(R.id.getPassword);
        registerButton      =   findViewById(R.id.registerButton);
        progressDialog = new ProgressDialog(this);

        final List<EditText> editTextList = new ArrayList<EditText>();
        editTextList.add(getUserName);
        editTextList.add(getCustomerName);
        editTextList.add(getAddress);
        editTextList.add(getPhoneNumber);
        editTextList.add(getEmail);
        editTextList.add(getPassword);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getUserName.getText().toString().matches("")){
                    getUserName.setError("Name Cannot be Empty");
                }
                else if(getCustomerName.getText().toString().matches("")){
                    getCustomerName.setError("Name Cannot be Empty");
                }
                else if(getAddress.getText().toString().matches("")){
                    getAddress.setError("Address Cannot be Empty");
                }
                else if(getPhoneNumber.getText().toString().matches("")){
                    getPhoneNumber.setError("Contact Number Cannot be Empty");
                }
                else if(getEmail.getText().toString().matches("")){
                    getEmail.setError("Email Cannot be Empty");
                }
                else if(getPassword.getText().toString().matches("")){
                    getPassword.setError("Password Cannot be Empty");
                }
                else
                {
                    String credentials = editTextList.get(0).getText().toString();
                    Log.e("ENETERD USERNAME:", credentials);

                    progressDialog.setMessage("Loading");
                    progressDialog.show();

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterCustomerProfile.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getCustomerByIdURL+credentials, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                            progressDialog.dismiss();
//                            progressDialog.hide();

                            getUserName.setError(null);
                            getCustomerName.setError(null);
                            getAddress.setError(null);
                            getPhoneNumber.setError(null);
                            getEmail.setError(null);
                            getPassword.setError(null);

                            Log.w("VOLLEY", response);
                            if (response.equals("{}")){
                                Log.e("GET RESPONSE:",response);
                                setCustomer(editTextList, RegisterCustomerProfile.this);
                            }else{
                                Toast.makeText(RegisterCustomerProfile.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.hide();
                            Toast.makeText(RegisterCustomerProfile.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

                }

            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void setCustomer(List<EditText> editTextList, final Context context)
    {
//        progressDialog.setMessage("Loading");
//        progressDialog.show();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = "http://...";
            JSONObject params = new JSONObject();
            params.put("custId", editTextList.get(0).getText());
            params.put("custname", editTextList.get(1).getText());
            params.put("address",editTextList.get(2).getText());
            params.put("phone", editTextList.get(3).getText());
            params.put("email", editTextList.get(4).getText());
            params.put("role", "Customer");
            params.put("password", editTextList.get(5).getText());
            Log.e("Param Values: ", params.toString());
            final String requestBody = params.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRepository.setCustomerURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    progressDialog.hide();

                    Toast.makeText(RegisterCustomerProfile.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    sharedPreferences = (RegisterCustomerProfile.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("customername",getCustomerName.getText().toString()).commit();
                    editor.putString("address",getAddress.getText().toString()).commit();
                    editor.putString("phone",getPhoneNumber.getText().toString()).commit();
                    editor.putString("email",getEmail.getText().toString()).commit();
                    editor.putString("password",getPassword.getText().toString()).commit();
                    editor.commit();


                    Log.w("VOLLEY", response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.hide();
                    Toast.makeText(RegisterCustomerProfile.this, "Connection Failed",Toast.LENGTH_SHORT).show();
                    connection = false;
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
