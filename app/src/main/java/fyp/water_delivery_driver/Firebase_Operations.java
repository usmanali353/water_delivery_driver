package fyp.water_delivery_driver;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import fyp.water_delivery_driver.Adapters.orders_list_adapter;
import fyp.water_delivery_driver.Model.Orders;
import fyp.water_delivery_driver.Model.Users;

public class Firebase_Operations {
    public static void SignIn(final Context context, String email, String password){
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Authenticating User....");
        pd.show();
       FirebaseFirestore.getInstance().collection("Driver").whereEqualTo("email",email).whereEqualTo("password",password).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               pd.dismiss();
               if(queryDocumentSnapshots.getDocuments().size()>0){
                   Toast.makeText(context,"Login Sucess",Toast.LENGTH_LONG).show();
                   prefs.edit().putString("user_info",new Gson().toJson(queryDocumentSnapshots.getDocuments().get(0).toObject(Users.class))).apply();
                   prefs.edit().putString("user_id",queryDocumentSnapshots.getDocuments().get(0).getId()).apply();
                   context.startActivity(new Intent(context,orders_list_page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                   ((AppCompatActivity)context).finish();
               }else {
                   Toast.makeText(context,"Login Failed",Toast.LENGTH_LONG).show();
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               pd.dismiss();
               Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
           }
       });
    }
    public static void getOrders(Context context,RecyclerView orders_list,Users u){
        ArrayList<Orders> orders=new ArrayList<>();
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Fetching your Orders...");
        pd.show();
        FirebaseFirestore.getInstance().collection("Orders").whereEqualTo("assignTo",u.getCnic()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                   for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                       orders.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Orders.class));
                   }
                   orders_list.setAdapter(new orders_list_adapter(orders,context));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void ShareLocation(Context context, Map<String,Object> map,String driverId){
        FirebaseFirestore.getInstance().collection("Driver").document(driverId).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
