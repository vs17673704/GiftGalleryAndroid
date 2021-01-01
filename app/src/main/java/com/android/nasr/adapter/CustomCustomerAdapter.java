package com.android.nasr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.nasr.R;
import com.android.nasr.model.CustomerModel;

import java.util.ArrayList;

public class CustomCustomerAdapter extends ArrayAdapter<CustomerModel> {

    public CustomCustomerAdapter(@NonNull Context context, ArrayList<CustomerModel> data) {
        super(context, R.layout.item_list_template, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CustomerModel rowItem = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.customer_list_template,parent,false);
        TextView customerId, customerName, customerPhone, customerEmail, customerAddress, customerPassword;
        customerId      =    customView .findViewById(R.id.customerId);
        customerName    =    customView .findViewById(R.id.customerName);
        customerPhone   =    customView .findViewById(R.id.customerPhone);
        customerEmail   =    customView .findViewById(R.id.customerEmail);
        customerAddress   =    customView .findViewById(R.id.customerAddress);
        customerPassword   =    customView .findViewById(R.id.customerPassword);

        customerId.setText(rowItem.getCustId());
        customerName.setText(rowItem.getCustName());
        customerPhone.setText(rowItem.getPhone());
        customerEmail.setText(rowItem.getEmail());
        customerPassword.setText(rowItem.getPassword());
        //customerAddress.setText(rowItem.getAddress());

        return customView;

    }
}
