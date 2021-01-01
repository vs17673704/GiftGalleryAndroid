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
import com.android.nasr.model.OrderModel;

import java.util.ArrayList;

public class CustomOrderListAdapter extends ArrayAdapter<OrderModel> {

    public CustomOrderListAdapter(@NonNull Context context, ArrayList<OrderModel> data) {
        super(context, R.layout.item_list_template, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        OrderModel rowItem = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.order_list_template,parent,false);
        TextView orderedItemName, orderId, orderStatus, orderDate, custName;

        orderedItemName     =   customView.findViewById(R.id.orderedItemName);
        orderId             =   customView.findViewById(R.id.orderId);
        orderStatus         =   customView.findViewById(R.id.orderStatus);
        orderDate           =   customView.findViewById(R.id.orderDate);
        custName   =   customView.findViewById(R.id.custName);


        orderedItemName.setText(rowItem.getItem());
        orderStatus.setText(rowItem.getStatus());
        orderId.setText(rowItem.getOrderid());
        orderDate.setText(rowItem.getOrderdate());
        custName.setText(rowItem.getCustName());

        return customView;

    }
}
