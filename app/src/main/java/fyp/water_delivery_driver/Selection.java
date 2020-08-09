package fyp.water_delivery_driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import fyp.water_delivery_driver.Model.Users;
import fyp.water_delivery_driver.R;

public class Selection extends AppCompatActivity {
    Button signin,register;
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.selection);
        signin=findViewById(R.id.btnSignin);
        prefs= PreferenceManager.getDefaultSharedPreferences(Selection.this);
        if(prefs.getString("user_info",null)!=null){
            Users u=new Gson().fromJson(prefs.getString("user_info",null),Users.class);
            if(u!=null){
                startActivity(new Intent(Selection.this,orders_list_page.class));
                finish();
            }
        }

        //Sign in Button Click Listener
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View loginView= LayoutInflater.from(Selection.this).inflate(R.layout.activity_login,null);
                final MaterialEditText username=loginView.findViewById(R.id.emailtxt);
                final MaterialEditText password=loginView.findViewById(R.id.passwordtxt);
                AlertDialog loginDialog =new AlertDialog.Builder(Selection.this)
                .setTitle("Sign in")
                 .setMessage("Provide Username and Password")
                 .setCancelable(false)
                 .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                            }
                        }).setView(loginView).create();
                loginDialog.show();
                loginDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(username.getText().toString())){
                            username.setError("Enter our Email");
                        }else if(!Utils.isEmailValid(username.getText().toString())){
                            username.setError("Invalid Email Format");
                        }else if(TextUtils.isEmpty(password.getText().toString())){
                            password.setError("Enter Password");
                        }else if(!password.getText().toString().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{6,}$")){
                            password.setError("Password should have 1 upper case 1 lower case 1 special character minimium 6 Characters long");
                        }else{
                            if(Utils.isNetworkAvailable(Selection.this)){
                                Firebase_Operations.SignIn(Selection.this,username.getText().toString(),password.getText().toString());
                            }else {
                                Toast.makeText(Selection.this,"Network not Available",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

}
