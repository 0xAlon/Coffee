package com.networks.coffee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends BaseActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = "Register";

    private TextView password;
    private TextView email;
    private TextView name;
    private TextView age;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.activity_register, frameLayout);

        mAuth = FirebaseAuth.getInstance();

        password = (TextView) view.findViewById(R.id.password);
        email = (TextView) view.findViewById(R.id.mail);
        name = (TextView) view.findViewById(R.id.name);
        age = (TextView) view.findViewById(R.id.age);


        Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

    private void register() {

        String user_email, user_password, user_name, user_age;
        user_email = email.getText().toString();
        user_password = password.getText().toString();
        user_name = name.getText().toString();
        user_age = age.getText().toString();

        if (TextUtils.isEmpty(user_email)) {
            Toast.makeText(this, "Invalid Email Or Password toast", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(user_password)) {
            Toast.makeText(this, "Invalid Email Or Password toast", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(user_name)) {
            Toast.makeText(this, "Invalid Email Or Password toast", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("Uid", user.getUid());
                        userData.put("Email", email.getText().toString());
                        userData.put("Name", name.getText().toString());
                        userData.put("Age", Integer.parseInt(user_age));
                        userData.put("Vip", false);
                        userData.put("UserType", 1);
                        db.collection("Users") // Add a new document with a generated ID
                                .add(userData)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Intent intent = new Intent(this, MainActivity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                    }
                }).addOnFailureListener(Throwable::printStackTrace);
    }
}