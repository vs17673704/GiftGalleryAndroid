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
import com.android.nasr.model.ItemModel;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<ItemModel> {

    public CustomListAdapter(@NonNull Context context, ArrayList<ItemModel> data) {
        super(context, R.layout.item_list_template, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ItemModel rowItem = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.item_list_template,parent,false);
        TextView itemId, itemName, itemPrice;
        itemId      =    customView .findViewById(R.id.itemId);
        itemName    =    customView .findViewById(R.id.itemName);
        itemPrice   =    customView .findViewById(R.id.itemPrice);

        itemId.setText(rowItem.getItemid());
        itemName.setText(rowItem.getItemname());
        itemPrice.setText(rowItem.getItemprice());

        return customView;

    }
}
