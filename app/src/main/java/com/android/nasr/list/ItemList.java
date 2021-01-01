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
import java.util.ArrayList;
import java.util.List;
public class ItemList extends AppCompatActivity {

    TextView empty;
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
        getSupportActionBar().setTitle("ITEMS");


        addItemButton = findViewById(R.id.addItemButton);
        empty = findViewById(R.id.empty);
        refreshItemButton = findViewById(R.id.refreshItemButton);
        saveEditItemId = findViewById(R.id.saveEditItemId);
        buttonAddRefresh = findViewById(R.id.buttonAddRefresh);
        final TextView textView = findViewById(R.id.empty);
        listView = findViewById(R.id.listView);
        listView.setEmptyView(empty);

        progressDialog = new ProgressDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(ItemList.this);
        getItems(requestQueue);

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

                ItemModel itemModels = itemModel.get(position);
                /*Snackbar.make(view, itemModels.getItemid()+"\n"+ itemModels.getItemname()+" API: "+ itemModels.getItemprice(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
                List<String> itemData = new ArrayList<>();
                itemData.add(itemModels.getItemname());
                itemData.add(itemModels.getItemprice());
                itemData.add(itemModels.getItemid());
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
                getItems(Volley.newRequestQueue(ItemList.this));
            }
        });




    }

    private void showdialog(String operation, final List<String> itemData) {
        AlertDialog.Builder alert;
        final String op = operation;
        alert = new AlertDialog.Builder(ItemList.this);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_edit_delete_item,null);

        saveEditItemName = view.findViewById(R.id.saveEditItemName);
        saveEditItemPrice = view.findViewById(R.id.saveEditItemPrice);
        saveEditItemId = view.findViewById(R.id.saveEditItemId);

        if(op.equals("update"))
        {
            saveEditItemName.setText(itemData.get(0));
            saveEditItemPrice.setText(itemData.get(1));
            saveEditItemId.setText(itemData.get(2));
            saveEditItemId.setEnabled(false);
        }

        boolean showDelete = true;
        alert.setView(view);

        alert.setCancelable(false);

        alert.setTitle("MANAGE ITEM");

        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                if(saveEditItemName.getText().toString().matches("")){
                    Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(saveEditItemPrice.getText().toString().matches("")){
                    Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else  if(saveEditItemId.getText().toString().matches("")){
                    Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String itemName = saveEditItemName.getText().toString();
                    String itemPrice = saveEditItemPrice.getText().toString();
                    String itemId = saveEditItemId.getText().toString();
                    //Toast.makeText(ItemList.this, "Item name: " + itemName + "\nItemprice: "+itemPrice+"\nItem ID: "+itemId, Toast.LENGTH_SHORT).show();

                    List<String> itemData = new ArrayList<>();
                    itemData.add(itemId);
                    itemData.add(itemName);
                    itemData.add(itemPrice);
                    Log.e("Operation:",op);
                    if(op.equals("update"))
                    {
                        setItem(itemData,ItemList.this);
                    }
                    else{
                        checkItem(itemId,itemData);
                    }
                }

            }
        });

        if(op.equals("add"))
        {
            showDelete =false;
        }
        if(showDelete){

            alert.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(saveEditItemName.getText().toString().matches("")){
                        Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    else  if(saveEditItemPrice.getText().toString().matches("")){
                        Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    else  if(saveEditItemId.getText().toString().matches("")){
                        Toast.makeText(ItemList.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        deleteItem(itemData.get(2));
                    }
                }
            });
        }

        alert.setNeutralButton("DISMISS", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //stuff you want the button to doToast.makeText(ItemList.this, "DISMISS CLICKED", Toast.LENGTH_SHORT).show();
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

//                    Toast.makeText(ItemList.this, String.valueOf(itemModel.isEmpty()), Toast.LENGTH_SHORT).show();

                    if(itemList.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else
                        getlist(ItemList.this, itemModel);

                } catch (JSONException e) {
                    progressDialog.hide();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(ItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    private void getlist(ItemList itemList, ArrayList<ItemModel> itemModel) {

        customListAdapter = new CustomListAdapter(ItemList.this, itemModel);
        listView.setAdapter(customListAdapter);
    }

    void setItem(List<String> itemData, final Context context)
    {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = "http://...";
            JSONObject params = new JSONObject();
            params.put("itemid", itemData.get(0));
            params.put("itemname", itemData.get(1));
            params.put("price",itemData.get(2));
            Log.e("Param Values: ", params.toString());
            final String requestBody = params.toString();

            String credentials = "varun/sharma";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRepository.getAddItemsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    progressDialog.hide();

                    saveEditItemName.setError(null);
                    saveEditItemId.setError(null);
                    saveEditItemPrice.setError(null);

                    Log.w("VOLLEY", response);
                    getItems(Volley.newRequestQueue(ItemList.this));
                    try {
                        if(response.equals("{}"))
                            Toast.makeText(context, "Incorrect username/password", Toast.LENGTH_SHORT).show();
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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.hide();
                    Toast.makeText(ItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    void checkItem(String id, List<String> itemdata){

        progressDialog.setMessage("Loading");
        progressDialog.show();

        final List<String> itemData = itemdata;
        RequestQueue requestQueue = Volley.newRequestQueue(ItemList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLRepository.getAddItemsByIdURL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                progressDialog.dismiss();
//                progressDialog.hide();

                Log.w("VOLLEY", response);
                if (response.equals("{}")){
                    Log.e("GET RESPONSE:",response);
                    setItem(itemData, ItemList.this);
                    Toast.makeText(ItemList.this, "Successful", Toast.LENGTH_SHORT).show();
                }else{
                    showdialog("add",null);
                    progressDialog.hide();
                    Toast.makeText(ItemList.this, "Item already exists", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(ItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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

    void deleteItem(String id){

        progressDialog.setMessage("Loading");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(ItemList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URLRepository.getDeleteItemsByIdURL+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                progressDialog.dismiss();
//                progressDialog.hide();

                Log.w("VOLLEY", response);
                if (response.equals("Deleted")){
                    Log.e("GET RESPONSE:",response);
                    Toast.makeText(ItemList.this, "Item successfully Deleted", Toast.LENGTH_SHORT).show();
                    getItems(Volley.newRequestQueue(ItemList.this));
                }else{
                    progressDialog.hide();
                    Toast.makeText(ItemList.this, "Cannot be deleted. An order exist against it!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Toast.makeText(ItemList.this, "Connection Failed",Toast.LENGTH_SHORT).show();

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
