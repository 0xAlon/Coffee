package com.networks.coffee.Controllers;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.networks.coffee.R;
import com.networks.coffee.Resources.BaseActivity;

public class Login extends BaseActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = "Login";

    TextView password;
    TextView mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        View view = getLayoutInflater().inflate(R.layout.activity_login, frameLayout);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        password = (TextView) view.findViewById(R.id.password);
        mail = (TextView) view.findViewById(R.id.mail);


        Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        View clickView = view.findViewById(R.id.nav);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    void login() {
        String user_email, user_password;
        user_email = mail.getText().toString();
        user_password = password.getText().toString();


        if (TextUtils.isEmpty(user_email)) {
            Toast.makeText(this, "Invalid Email Or Password toast", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(user_password)) {
            Toast.makeText(this, "Invalid Email Or Password toast", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}