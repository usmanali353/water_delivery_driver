package fyp.water_delivery_driver;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import fyp.water_delivery_driver.Model.Users;
import fyp.water_delivery_driver.R;

public class orders_list_page extends AppCompatActivity implements Listener {
  RecyclerView orders_list;
  SharedPreferences prefs;
    EasyWayLocation easyWayLocation;
    NavigationView nv;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    String driverId;
    Users u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        driverId=prefs.getString("user_id","");
        orders_list=findViewById(R.id.orders_list);
        drawerLayout=findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                orders_list_page.this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        u=new Gson().fromJson(prefs.getString("user_info",null), Users.class);
        Log.e("cnic",new Gson().toJson(u));
//        Log.e("cnic",u.getCnic());
        nv=findViewById(R.id.nav_view);
        View header = nv.inflateHeaderView(R.layout.navbarheader);
        TextView name=header.findViewById(R.id.name);
        TextView email=header.findViewById(R.id.email);
        ImageView img=header.findViewById(R.id.letterimg);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(u.getName().substring(0, 1), color1);
        img.setImageDrawable(drawable);
        name.setText(u.getName());
        email.setText(u.getEmail());
        nv.inflateMenu(R.menu.menu_main);
        orders_list.setLayoutManager(new LinearLayoutManager(this));
        Firebase_Operations.getOrders(this,orders_list,u);
       LocationRequest request = new LocationRequest();
        request.setInterval(20000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
         actionBarDrawerToggle.syncState();
         nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 if(item.getItemId()==R.id.action_profile){
                     drawerLayout.closeDrawer(Gravity.LEFT);
                     View v=LayoutInflater.from(orders_list_page.this).inflate(R.layout.driver_profile,null);
                     MaterialEditText name=v.findViewById(R.id.nametxt);
                     MaterialEditText email=v.findViewById(R.id.emailtxt);
                     MaterialEditText cnic=v.findViewById(R.id.cnic);
                     MaterialEditText phone=v.findViewById(R.id.phone);
                     MaterialEditText vehicle=v.findViewById(R.id.vehicle);
                     name.setEnabled(false);
                     email.setEnabled(false);
                     cnic.setEnabled(false);
                     phone.setEnabled(false);
                     vehicle.setEnabled(false);
                     name.setText(u.getName());
                     email.setText(u.getEmail());
                     cnic.setText(u.getCnic());
                     phone.setText(u.getPhone());
                     vehicle.setText(u.getVehicleNo());
                     AlertDialog driverProfileDialog=new AlertDialog.Builder(orders_list_page.this)
                             .setTitle("Driver Profile")
                             .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             }).setView(v).create();
                       driverProfileDialog.show();

                 }else if(item.getItemId()==R.id.action_logout){
                     drawerLayout.closeDrawer(Gravity.LEFT);
                     prefs.edit().remove("user_info").apply();
                     prefs.edit().remove("location").apply();
                     FirebaseAuth.getInstance().signOut();
                     startActivity(new Intent(orders_list_page.this,Selection.class));
                     finish();
                 }else if(item.getItemId()==R.id.action_share_location){
                     drawerLayout.closeDrawer(Gravity.LEFT);
                     View v= LayoutInflater.from(orders_list_page.this).inflate(R.layout.share_location_toggle,null);
                     ToggleButton btn=v.findViewById(R.id.location_switch);
                     btn.setChecked(prefs.getBoolean("location",false));
                     AlertDialog shareLocation=new AlertDialog.Builder(orders_list_page.this)
                             .setTitle("Share Location")
                             .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     if(btn.isChecked()){
                                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                             if (ContextCompat.checkSelfPermission(orders_list_page.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(orders_list_page.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                                                 prefs.edit().putBoolean("location",true).apply();
                                                 Toast.makeText(orders_list_page.this,"Location Update Turned On",Toast.LENGTH_LONG).show();
                                                 easyWayLocation.startLocation();
                                             }else{
                                                 requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION }, 1000);
                                             }
                                         }
                                     }else{
                                         Toast.makeText(orders_list_page.this,"Location Updates Turned Off",Toast.LENGTH_LONG).show();
                                         prefs.edit().putBoolean("location",false).apply();
                                         easyWayLocation.endUpdates();
                                     }
                                 }
                             }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                 }
                             }).setView(v).create();
                     shareLocation.show();



                 }
                 return true;
             }
         });
        easyWayLocation = new EasyWayLocation(this,request, false,this);
    }
    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        if(location!=null) {
            Log.e("location",String.valueOf(location.getLatitude()+","+location.getLongitude()));
            Map<String, Object> updateLocation = new HashMap<>();
            updateLocation.put("driverLat", location.getLatitude());
            updateLocation.put("driverLng", location.getLongitude());
            Firebase_Operations.ShareLocation(orders_list_page.this, updateLocation,driverId);
        }
    }

    @Override
    public void locationCancelled() {

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefs.edit().putBoolean("location",false).apply();
        easyWayLocation.endUpdates();
        Toast.makeText(orders_list_page.this,"Application Closed",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prefs.edit().putBoolean("location",true).apply();
                    easyWayLocation.startLocation();
                } else {
                    Toast.makeText(orders_list_page.this,"We need acess to location to Share with admin",Toast.LENGTH_LONG).show();
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
        }
    }
}
