package fyp.water_delivery_driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fyp.water_delivery_driver.Model.Orders;
import fyp.water_delivery_driver.R;

public class order_details extends AppCompatActivity {
Orders order;
SharedPreferences prefs;
TextView customerName,phone,email,address,orderedItems;
Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        customerName=findViewById(R.id.customerName);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        orderedItems=findViewById(R.id.items);
        confirmBtn=findViewById(R.id.confirm_button);
        order=new Gson().fromJson(getIntent().getStringExtra("orderData"),Orders.class);
        if(order!=null){
            customerName.setText(order.getCustomerName());
            phone.setText(order.getPhone());
            email.setText(order.getEmail());
            address.setText(order.getAddress());
            StringBuilder sb=new StringBuilder();
            if(order.getOrderedItems()!=null) {
                for (int i = 0; i < order.getOrderedItems().size(); i++) {
                    sb.append(order.getOrderedItems().get(i).getName() + " " + order.getOrderedItems().get(i).getWeight() + " " + "x" + order.getOrderedItems().get(i).getQuantity());
                    sb.append("\n");
                }
                orderedItems.setText(sb.toString());
            }

        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+order.getDeliveryLatitude()+","+order.getDeliveryLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
    }

}
