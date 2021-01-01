package com.android.nasr.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.nasr.R;
import com.android.nasr.URLRepository;
import com.android.nasr.adapter.CustomCustomerAdapter;
import com.android.nasr.model.CustomerModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CustomerList extends AppCompatActivity {

    TextView empty;
    LinearLayout buttonAddRefresh;
    ListView listView;
    EditText customerId, customerName, customerAddress, customerPhone, customerEmail, customerPassword;
    ArrayList<CustomerModel> customerModel;
    Button addItemButton, refreshItemButton;
    CustomCustomerAdapter customCustomerAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("CUSTOMER");

        addItemButton = findViewById(R.id.addItemButton);
        empty = findViewById(R.id.empty);
        empty.setText("No customers found");
        refreshItemButton = findViewById(R.id.refreshItemButton);
        buttonAddRefresh = findViewById(R.id.buttonAddRefresh);
        /*final TextView textView = */findViewById(R.id.empty).setVisibility(View.GONE);
        listView = findViewById(R.id.listView);

        progressDialog = new ProgressDialog(this);



        RequestQueue requestQueue = Volley.newRequestQueue(CustomerList.this);
        getCustomer(requestQueue);



        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CustomerModel customerModels = customerModel.get(position);
                /*Snackbar.make(view, customerModels.getItemid()+"\n"+ customerModels.getItemname()+" API: "+ customerModels.getItemprice(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
                List<String> itemData = new ArrayList<>();
                itemData.add(customerModels.getCustId());
                itemData.add(customerModels.getCustName());
                itemData.add(customerModels.getAddress());
                itemData.add(customerModels.getPhone());
                itemData.add(customerModels.getEmail());
                itemData.add(customerModels.getPassword());
                showdialog("update", itemData);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog("add",null);

            }
        });

        refreshItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCustomer(Volley.newRequestQueue(CustomerList.this));
            }
        });




    }

    private void showdialog(String operation, final List<String> itemData) {
        AlertDialog.Builder alert;
        final String op = operation;
        alert = new AlertDialog.Builder(CustomerList.this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_edit_delete_customer,null);

        customerId = view.findViewById(R.id.customerId);
        customerName = view.findViewById(R.id.customerName);
        customerAddress = view.findViewById(R.id.cutomerAddress);
        customerPhone = view.findViewById(R.id.customerPhone);
        customerEmail = view.findViewById(R.id.customerEmail);
        customerPassword = view.findViewById(R.id.customerPassword);

        if(op.equals("update"))
        {

            customerId.setText(itemData.get(0));
            customerName.setText(itemData.get(1));
            customerAddress.setText(itemData.get(2));
            customerPhone.setText(itemData.get(3));
            customerEmail.setText(itemData.get(4));
            customerPassword.setText(itemData.get(5));
            customerId.setEnabled(false);
        }

        alert.setView(view);
        alert.setTitle("Manage Customer");
        alert.setCancelable(false);

        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String custId = customerId.getText().toString();
                String custName = customerName.getText().toString();
                String custAddress= customerAddress.getText().toString();
                String custPhone = customerPhone.getText().toString();
                String custEmail = customerEmail.getText().toString();
                String custPassword = customerPassword.getText().toString();
                //Toast.makeText(ItemList.this, "Item name: " + itemName + "\nItemprice: "+itemPrice+"\nItem ID: "+itemId, Toast.LENGTH_SHORT).show();

                if(customerId.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerName.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerAddress.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerPhone.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerEmail.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerPassword.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    customerId.setError(null);
//                    customerName.setError(null);
//                    customerAddress.setError(null);
//                    customerPhone.setError(null);
//                    customerEmail.setError(null);
//                    customerPassword.setError(null);

                    List<String> itemData = new ArrayList<>();
                    itemData.add(custId);
                    itemData.add(custName);
                    itemData.add(custAddress);
                    itemData.add(custPhone);
                    itemData.add(custEmail);
                    itemData.add(custPassword);
                    Log.e("Operation:",op);
                    if(op.equals("update"))
                    {
                        setCustomer(itemData, CustomerList.this);
                    }
                    else{
                        checkCustomer(custId,itemData);
                    }
                }

            }
        });

        alert.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(customerId.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerName.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerAddress.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerPhone.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerEmail.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(customerPassword.getText().toString().matches("")){
                    Toast.makeText(CustomerList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    deleteCustomer(itemData.get(0));
                }
            }
        });

        alert.setNeutralButton("DISMISS", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //stuff you want the button to do
                Toast.makeText(CustomerList.this, "DISMISS CLICKED", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void getCustomer(RequestQueue requestQueue) {
        progressDialog.setMessage("Loading");
        progressDialog.show();

        final TextView textView = findViewById(R.id.empty);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getCustomerURL+"/all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                List<String> customerList = new ArrayList<>();
                customerModel = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        String id    = itemObject.get("custId").toString();
                        String name  = itemObject.get("custName").toString();
                        String address = itemObject.get("address").toString();
                        String phone = itemObject.get("phone").toString();
                        String email = itemObject.get("email").toString();
                        String password = itemObject.get("password").toString();
                        customerModel.add(new CustomerModel(id, name, address, phone, email, password));
                        customerList.add(itemObject.get("custId").toString());
                        customerList.add(itemObject.get("custName").toString());
                        customerList.add(itemObject.get("address").toString());
                        customerList.add(itemObject.get("phone").toString());
                        customerList.add(itemObject.get("email").toString());
                        customerList.add(itemObject.get("password").toString());
                    }
                    Log.e("ITEMS:",customerList.toString());
                    //Toast.makeText(CustomerList.this, customerList.toString(), Toast.LENGTH_SHORT).show();
                    if(customerList.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                        getlist(CustomerList.this, customerModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(CustomerList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    private void getlist(CustomerList customerList, ArrayList<CustomerModel> customerModel) {

        customCustomerAdapter = new CustomCustomerAdapter(CustomerList.this, customerModel);
        listView.setAdapter(customCustomerAdapter);
    }

    void setCustomer(List<String> itemData, final Context context)
    {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = "http://...";
            JSONObject params = new JSONObject();
            params.put("custId", itemData.get(0));
            params.put("custname", itemData.get(1));
            params.put("address",itemData.get(2));
            params.put("phone",itemData.get(3));
            params.put("email",itemData.get(4));
            params.put("role","Customer");
            params.put("password",itemData.get(5));
            Log.e("Param Values: ", params.toString());
            final String requestBody = params.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRepository.setCustomerURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    progressDialog.hide();

                    customerId.setError(null);
                    customerName.setError(null);
                    customerAddress.setError(null);
                    customerPhone.setError(null);
                    customerEmail.setError(null);
                    customerPassword.setError(null);

                    Log.w("VOLLEY", response);
                    getCustomer(Volley.newRequestQueue(CustomerList.this));
                    try {
                        Log.e("DATA SENT:",response);
                        if(response.equals("{}"))
                            Toast.makeText(context, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                        else{
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.hide();
                    Toast.makeText(CustomerList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    void checkCustomer(String id, List<String> itemdata){

        progressDialog.setMessage("Loading");
        progressDialog.show();

        final List<String> itemData = itemdata;
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getCustomerURL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                if (response.equals("{}")){
                    Log.e("GET RESPONSE:",response);
                    setCustomer(itemData, CustomerList.this);
                    Toast.makeText(CustomerList.this, "Successful", Toast.LENGTH_SHORT).show();
                }else{
                    showdialog("add",null);
                    Toast.makeText(CustomerList.this, "Item already exists", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(CustomerList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    void deleteCustomer(String id){

        progressDialog.setMessage("Loading");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URLRepository.deleteCustomerURL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                if (response.equals("Deleted")){
                    Log.e("GET RESPONSE:",response);
                    Toast.makeText(CustomerList.this, "Item successfully Deleted", Toast.LENGTH_SHORT).show();
                    getCustomer(Volley.newRequestQueue(CustomerList.this));
                }else{
                    Toast.makeText(CustomerList.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(CustomerList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
