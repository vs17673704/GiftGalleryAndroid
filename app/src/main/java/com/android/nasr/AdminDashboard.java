package com.android.nasr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.nasr.list.CustomerItemList;
import com.android.nasr.list.CustomerList;
import com.android.nasr.list.CustomerOrderList;
import com.android.nasr.list.ItemList;
import com.android.nasr.list.OrderList;

public class AdminDashboard extends AppCompatActivity {

    Button itemListAdmin, myOrdersAdmin, adminAllCustomers, logouButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        getSupportActionBar().setTitle("ADMIN PANEL");


        SharedPreferences sharedPreferences = (AdminDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.e("customerid IS",sharedPreferences.getString("customerid",null));
        Log.e("customername IS",sharedPreferences.getString("customername",null));
        Log.e("address IS",sharedPreferences.getString("address",null));
        Log.e("phone IS",sharedPreferences.getString("phone",null));
        Log.e("email IS",sharedPreferences.getString("email",null));
        Log.e("password IS",sharedPreferences.getString("password",null));
        Log.e("role IS",sharedPreferences.getString("role",null));

        itemListAdmin  =   findViewById(R.id.itemListAdmin);
        myOrdersAdmin  =   findViewById(R.id.myOrdersAdmin);
        adminAllCustomers       =   findViewById(R.id.adminAllCustomers);
        logouButton     =   findViewById(R.id.logouButton);

        itemListAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ItemList.class));
            }
        });

        myOrdersAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, OrderList.class));
            }
        });

        adminAllCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, CustomerList.class));
            }
        });

        logouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = (AdminDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login","false").commit();
                startActivity(new Intent(AdminDashboard.this,MainActivity.class));
                finish();
            }
        });

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
