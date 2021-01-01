package com.android.nasr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = (ValidationActivity.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getString("login","").equals("true")&&sharedPreferences.getString("role","").equals("Admin"))
        {
            dashboard(AdminDashboard.class);
        }
        else if(sharedPreferences.getString("login","").equals("true"))
        {
            dashboard(CustomerDashboard.class);
        }
        else
            dashboard(MainActivity.class);
    }

    public void dashboard(Class aClass)
    {
        startActivity(new Intent(ValidationActivity.this, aClass));
        finish();
    }

}
