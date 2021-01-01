package com.android.nasr.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.nasr.R;
import com.android.nasr.URLRepository;
import com.android.nasr.adapter.CustomOrderListAdapter;
import com.android.nasr.model.OrderModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderList extends AppCompatActivity {

    TextView empty;
    ListView listView;
    ArrayList<OrderModel> orderModel;
    CustomOrderListAdapter customOrderListAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ORDERS");

        empty = findViewById(R.id.emptyOrder);
        final TextView textView = findViewById(R.id.empty);
        listView = findViewById(R.id.orderListView);
        listView.setEmptyView(empty);

        progressDialog = new ProgressDialog(this);


        RequestQueue requestQueue = Volley.newRequestQueue(OrderList.this);
        getOrder(requestQueue);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderModel orderModels = orderModel.get(position);


                showdialog(orderModels.getOrderid());
            }
        });
    }

    private void showdialog(final String orderid) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(OrderList.this);

        alert.setCancelable(false);

        alert.setMessage("You can only cancel or delete this order");

        alert.setPositiveButton("CANCEL ORDER", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                final String orderId = orderid;
                    setOrder(orderId,OrderList.this);
            }
        });
        alert.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOrder(orderid);
            }
        });

        alert.setNeutralButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void showdialogCustomer(final String orderid) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(OrderList.this);

        alert.setCancelable(false);

        alert.setMessage("");

        alert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                final String orderId = orderid;
                setOrder(orderId,OrderList.this);
            }
        });
        alert.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void getOrder(RequestQueue requestQueue) {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getAllOrdersURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                //Log.e("VOLLEY", response);
                List<String> orderList = new ArrayList<>();
                orderModel = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("TAG:",jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        String orderId = String.valueOf(itemObject.get("orderid"));
                        String date = itemObject.get("orderdate").toString();
                        String status = itemObject.get("status").toString();
                        String customerName = itemObject.get("custName").toString();
                        String itemName = itemObject.get("item").toString();
                        orderModel.add(new OrderModel(orderId, date, status, customerName, itemName));
                        orderList.add(itemObject.get("orderid").toString());
                        orderList.add(itemObject.get("orderdate").toString());
                        orderList.add(itemObject.get("status").toString());
                        orderList.add(itemObject.get("custName").toString());
                        orderList.add(itemObject.get("item").toString());
                    }
                    Log.e("ITEMS:", Arrays.asList(orderModel).toString());

                    if(orderList.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                        getlist(OrderList.this, orderModel);
                } catch (JSONException e) {
                    progressDialog.hide();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(OrderList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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


    private void getlist(OrderList itemList, ArrayList<OrderModel> orderModel) {

        customOrderListAdapter = new CustomOrderListAdapter(OrderList.this, orderModel);
        listView.setAdapter(customOrderListAdapter);
    }

    void setOrder(String orderid, final Context context)
    {

        progressDialog.setMessage("Loading");
        progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLRepository.getCancelOrdersURL+orderid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    progressDialog.hide();

                    Log.w("VOLLEY", response);
                    getOrder(Volley.newRequestQueue(OrderList.this));
                    try {
                        if (response.equals("Updated")){
                            Toast.makeText(context, "CANCELLED", Toast.LENGTH_SHORT).show();
                        getOrder(Volley.newRequestQueue(OrderList.this));
                    }
                        else{
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                        }
                    } catch (JSONException e) {
                        progressDialog.hide();
                        e.printStackTrace();
                    }
                }
            },  new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.hide();
                    Toast.makeText(OrderList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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
            Volley.newRequestQueue(OrderList.this).add(stringRequest);
    }

    void deleteOrder(String id){

        progressDialog.setMessage("Loading");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(OrderList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URLRepository.getDeleteOrderByIdURL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                if (response.equals("Deleted")){
                    Log.e("GET RESPONSE:",response);
                    Toast.makeText(OrderList.this, "Item successfully Deleted", Toast.LENGTH_SHORT).show();
                    getOrder(Volley.newRequestQueue(OrderList.this));
                }else{
                    Toast.makeText(OrderList.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(OrderList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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
