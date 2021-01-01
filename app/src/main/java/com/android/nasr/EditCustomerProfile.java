package com.android.nasr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class EditCustomerProfile extends AppCompatActivity {

    EditText editCustomerName, editAddress, editPhoneNumber, editEmail, editPassword;
    Button submit;
    TextView setUserName;
    String custid;
    String password;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("EDIT CUSTOMER");

        setUserName         =   findViewById(R.id.setUserName);
        editCustomerName    =   findViewById(R.id.editCustomerName);
        editAddress         =   findViewById(R.id.editAddress);
        editPhoneNumber     =   findViewById(R.id.editPhoneNumber);
        editEmail           =   findViewById(R.id.editEmail);
        editPassword        =   findViewById(R.id.editPassword);
        submit              =   findViewById(R.id.submit);

        progressDialog = new ProgressDialog(this);

        Context context = EditCustomerProfile.this;
//        final SharedPreferences

        sharedPreferences = (EditCustomerProfile.this).getSharedPreferences("userprofile",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        custid = sharedPreferences.getString("customerid",null);
        password = sharedPreferences.getString("password",null);
        setUserName.setText(custid);
        editCustomerName.setText(sharedPreferences.getString("customername",null));
        editPhoneNumber.setText(sharedPreferences.getString("phone",null));
        editEmail.setText(sharedPreferences.getString("email",null));
        editAddress.setText(sharedPreferences.getString("address",null));
        editPassword.setText(sharedPreferences.getString("password",null));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editCustomerName.getText().toString().matches("")){
                    editCustomerName.setError("Name Cannot be Empty");
                }
                else if(editAddress.getText().toString().matches("")){
                    editAddress.setError("Address Cannot be Empty");
                }
                else if(editPhoneNumber.getText().toString().matches("")){
                    editPhoneNumber.setError("Contact Number Cannot be Empty");
                }
                else if(editEmail.getText().toString().matches("")){
                    editEmail.setError("Contact Number Cannot be Empty");
                }
                else if(editPassword.getText().toString().matches("")){
                    editPassword.setError("Password Cannot be Empty");
                }
                else
                {

                    progressDialog.setMessage("Loading");
                    progressDialog.show();

                    if(editPassword.getText().toString().equals(password)) {

                        try {

                            JSONObject params = new JSONObject();
                            params.put("custId", setUserName.getText().toString());
                            params.put("custname", editCustomerName.getText().toString());
                            params.put("address",editAddress.getText().toString());
                            params.put("phone", editPhoneNumber.getText().toString());
                            params.put("email", editEmail.getText().toString());
                            params.put("role", "Customer");
                            params.put("password", editPassword.getText().toString());
                            Log.e("Param Values: ", params.toString());
                            final String requestBody = params.toString();

                            RequestQueue requestQueue = Volley.newRequestQueue(EditCustomerProfile.this);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRepository.setCustomerURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    progressDialog.dismiss();
                                    progressDialog.hide();

                                    editCustomerName.setError(null);
                                    editAddress.setError(null);
                                    editPhoneNumber.setError(null);
                                    editEmail.setError(null);
                                    editPassword.setError(null);

                                    editor = sharedPreferences.edit();
                                    editor.putString("customername",editCustomerName.getText().toString()).commit();
                                    editor.putString("address",editAddress.getText().toString()).commit();
                                    editor.putString("phone",editPhoneNumber.getText().toString()).commit();
                                    editor.putString("email",editEmail.getText().toString()).commit();
                                    editor.putString("password",editPassword.getText().toString()).commit();
                                    editor.commit();

                                    Log.w("VOLLEY", response);
                                    Toast.makeText(EditCustomerProfile.this, "Changes successfully saved", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    progressDialog.hide();
                                    Toast.makeText(EditCustomerProfile.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        Toast.makeText(EditCustomerProfile.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
