package com.android.nasr.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.nasr.CustomerDashboard;
import com.android.nasr.R;
import com.android.nasr.URLRepository;
import com.android.nasr.adapter.CustomListAdapter;
import com.android.nasr.model.ItemModel;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerItemList extends AppCompatActivity {

    TextView orderedItemName, orderPrice, orderStatus, orderDate, custName, custPhone, orderAddress, empty;
    LinearLayout buttonAddRefresh;
    ListView listView;
    EditText saveEditItemName, saveEditItemPrice, saveEditItemId;
    ProgressDialog progressDialog;
    ArrayList<ItemModel> itemModel;
    Button addItemButton, refreshItemButton;
    CustomListAdapter customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SHOP");

        addItemButton = findViewById(R.id.addItemButton);
        empty = findViewById(R.id.empty);
        refreshItemButton = findViewById(R.id.refreshItemButton);
        saveEditItemId = findViewById(R.id.saveEditItemId);
        buttonAddRefresh = findViewById(R.id.buttonAddRefresh);
        final TextView textView = findViewById(R.id.empty);
        listView = findViewById(R.id.listView);
        listView.setEmptyView(empty);
        addItemButton.setText("MY ORDERS");

        progressDialog = new ProgressDialog(this);



        RequestQueue requestQueue = Volley.newRequestQueue(CustomerItemList.this);
        getItems(requestQueue);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemModel itemModels = itemModel.get(position);
                /*Snackbar.make(view, itemModels.getItemid()+"\n"+ itemModels.getItemname()+" API: "+ itemModels.getItemprice(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
                List<String> itemData = new ArrayList<>();
                itemData.add(itemModels.getItemname());
                itemData.add(itemModels.getItemprice());
                itemData.add(itemModels.getItemid());
                optionDilog(itemData);
            }
        });



        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerItemList.this,CustomerOrderList.class));
            }
        });
        refreshItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItems(Volley.newRequestQueue(CustomerItemList.this));
            }
        });




    }

    private void optionDilog(List<String> itemInfo){
        AlertDialog.Builder alert;
        final List<String> itemData = itemInfo;
        Log.e("REACHED","OPTION DILOG");
        alert = new AlertDialog.Builder(CustomerItemList.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_edit_delete_customer,null);

        alert.setMessage("Do you want to orders this product?");

        alert.setCancelable(false);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                orderItem(itemData, CustomerItemList.this);

            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                    getItems(Volley.newRequestQueue(CustomerItemList.this));
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void orderdialog(final List<String> itemData) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(CustomerItemList.this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_summery_template,null);
        orderedItemName = view.findViewById(R.id.orderedItemName);
        orderPrice = view.findViewById(R.id.orderPrice);
        orderStatus = view.findViewById(R.id.orderStatus);
        orderDate = view.findViewById(R.id.orderDate);
        custName = view.findViewById(R.id.custName);
        custPhone = view.findViewById(R.id.custPhone);
        orderAddress = view.findViewById(R.id.orderAddress);

        orderedItemName.setText(itemData.get(0));
        orderPrice.setText(itemData.get(1));
        orderStatus.setText(itemData.get(2));
        orderDate.setText(itemData.get(3));
        custName.setText(itemData.get(4));
        custPhone.setText(itemData.get(5));
        orderAddress.setText(itemData.get(6));

        alert.setView(view);

        alert.setCancelable(false);

        alert.setTitle("ORDER SUMMARY");

        alert.setPositiveButton("CLOSE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });

        alert.setNegativeButton("VIEW ORDERS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CustomerItemList.this, CustomerOrderList.class));
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void getItems(RequestQueue requestQueue) {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        final TextView textView = findViewById(R.id.empty);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getAllItemsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                List<String> itemList = new ArrayList<>();
                itemModel = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        String id = String.valueOf(itemObject.get("itemid"));
                        String name = itemObject.get("itemname").toString();
                        String price = itemObject.get("price").toString();
                        itemModel.add(new ItemModel(id, name, price));
                        itemList.add(itemObject.get("itemname").toString());
                        itemList.add(itemObject.get("price").toString());
                        Log.e("ITEMS:",itemList.toString());
                    }

                    if(itemList.isEmpty())
                        textView.setVisibility(View.VISIBLE);
                    else
                        getlist(CustomerItemList.this, itemModel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(CustomerItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    private void getlist(CustomerItemList itemList, ArrayList<ItemModel> itemModel) {

        customListAdapter = new CustomListAdapter(CustomerItemList.this, itemModel);
        listView.setAdapter(customListAdapter);
    }


    void orderItem(final List<String> itemData, final Context context) {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        final List<String> itemInfo = itemData;
        SharedPreferences sharedPreferences = context.getSharedPreferences("userprofile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String customerid = sharedPreferences.getString("customerid", "");
        String credentials = customerid + "/" + itemInfo.get(2);
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerItemList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRepository.placeOrdersURL+credentials, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                progressDialog.hide();

                Log.w("VOLLEY", response);
                List<String> iteminfo = new ArrayList<>();
                SharedPreferences sharedPreferences = context.getSharedPreferences("userprofile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String customername = sharedPreferences.getString("customername", "");
                String customerphone = sharedPreferences.getString("phone", "");
                String customeraddress = sharedPreferences.getString("address", "");
                Date date = new Date();
                String regex = "dd/MM/yyyy hh:mm a";
                DateFormat df = new SimpleDateFormat(regex);
                String orderdate = df.format(date);

                Log.e("customername",customername);
                Log.e("customerphone",customerphone);
                Log.e("customeraddress",customeraddress);
                Log.e("date",orderdate);
                Log.e("item",itemInfo.get(0));
                Log.e("price",itemInfo.get(1));
                Log.e("itemID",itemInfo.get(2));

                iteminfo.add(itemInfo.get(0));
                iteminfo.add(itemInfo.get(1));
                iteminfo.add("Order Placed");
                iteminfo.add(orderdate);
                iteminfo.add(customername);
                iteminfo.add(customerphone);
                iteminfo.add(customeraddress);
                orderdialog(iteminfo);

                Log.e("ORDER SUMMARY:", itemInfo + "\t" + customername + "\t" + customerphone + "\t" + customeraddress);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(CustomerItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();
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
