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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.nasr.list.CustomerItemList;
import com.android.nasr.list.CustomerOrderList;

public class CustomerDashboard extends AppCompatActivity {


    Button itemListButton, myOrdersButton, myProfile, logouButton;

    TextView custName, custID, custEmail, custPhone;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        getSupportActionBar().setTitle("DASHBOARD");


        sharedPreferences = (CustomerDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Log.e("customerid IS",sharedPreferences.getString("customerid",null));
        Log.e("customername IS",sharedPreferences.getString("customername",null));
        Log.e("address IS",sharedPreferences.getString("address",null));
        Log.e("phone IS",sharedPreferences.getString("phone",null));
        Log.e("email IS",sharedPreferences.getString("email",null));
        Log.e("password IS",sharedPreferences.getString("password",null));
        Log.e("role IS",sharedPreferences.getString("role",null));

        itemListButton  =   findViewById(R.id.itemListButton);
        myOrdersButton  =   findViewById(R.id.myOrdersButton);
        myProfile       =   findViewById(R.id.myProfile);
        logouButton     =   findViewById(R.id.logouButton);
        custID     =   findViewById(R.id.custID);
        custName     =   findViewById(R.id.custName);
        custEmail     =   findViewById(R.id.custEmail);
        custPhone     =   findViewById(R.id.custPhone);

        custID.setText(sharedPreferences.getString("customerid",null));
        custName.setText(sharedPreferences.getString("customername",null));
        custPhone.setText(sharedPreferences.getString("phone",null));
        custEmail.setText(sharedPreferences.getString("email",null));

        itemListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboard.this, CustomerItemList.class));
            }
        });

        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboard.this,CustomerOrderList.class));
            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboard.this, EditCustomerProfile.class));
            }
        });

        logouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = (CustomerDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login","false").commit();
                editor.putString("role","");
                startActivity(new Intent(CustomerDashboard.this,MainActivity.class));
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

    @Override
    protected void onStart() {

        sharedPreferences = (CustomerDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        custID.setText(sharedPreferences.getString("customerid",null));
        custName.setText(sharedPreferences.getString("customername",null));
        custPhone.setText(sharedPreferences.getString("phone",null));
        custEmail.setText(sharedPreferences.getString("email",null));
        super.onStart();
    }

    @Override
    protected void onPostResume() {

        sharedPreferences = (CustomerDashboard.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        custID.setText(sharedPreferences.getString("customerid",null));
        custName.setText(sharedPreferences.getString("customername",null));
        custPhone.setText(sharedPreferences.getString("phone",null));
        custEmail.setText(sharedPreferences.getString("email",null));
        super.onPostResume();
    }
}
