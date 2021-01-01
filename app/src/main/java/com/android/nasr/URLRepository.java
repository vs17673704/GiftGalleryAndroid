package com.android.nasr;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class URLRepository extends AppCompatActivity {

            SharedPreferences sharedPreferences =   (URLRepository.this).getSharedPreferences("userprofile", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            public static final String IP = "http://192.168.43.193:8080/";

            public static final String loginURL = IP +"customers/login/";
            public static final String getCustomerURL = IP +"customers/";
            public static final String getCustomerByIdURL = IP +"customers/";
            public static final String setCustomerURL = IP +"customers/add";
            public static final String updateCustomerURL = IP +"customers/editprofile/";
            public static final String deleteCustomerURL = IP +"customers/delete/";

            public static final String getAllItemsURL = IP +"items/all/";
            public static final String getAddItemsURL = IP +"items/add";
            public static final String getAddItemsByIdURL = IP +"items/";
            public static final String getDeleteItemsByIdURL = IP +"items/delete/";

            public static final String getDeleteOrderByIdURL = IP +"orders/delete/";
            public static final String getAllOrdersURL = IP +"orders/all/";
            public static final String placeOrdersURL = IP +"orders/add/";
            public static final String getCancelOrdersURL = IP +"orders/cancel/";
            public static final String getOrdersByCustomerIdURL = IP +"orders/orderbyid/";


}
