package com.networks.coffee.Resources;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.networks.coffee.Controllers.About;
import com.networks.coffee.Controllers.Login;
import com.networks.coffee.Controllers.MainActivity;
import com.networks.coffee.R;
import com.networks.coffee.Controllers.Register;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected FrameLayout frameLayout;


    private DrawerLayout drawer;
    private String TAG = "BaseActivity";
    private FirebaseAuth mAuth;
    private String userType;
    MenuItem name;
    MenuItem type;
    MenuItem logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //context=this;
        frameLayout = (FrameLayout) findViewById(R.id.container);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        name = menu.findItem(R.id.name);
        type = menu.findItem(R.id.type);
        logout = menu.findItem(R.id.logout);

        initUserName();
        initUserType();
    }

    private void initUserName() {

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            db.collection("Users").whereEqualTo("Uid", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (currentUser.getUid().equals(String.valueOf(document.getData().get("Uid")))) {
                                    name.setTitle(String.valueOf(document.getData().get("Name")));
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } else {
            name.setTitle("Error getting Name");
        }
    }

    private void initUserType() {

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            db.collection("Users").whereEqualTo("Uid", currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                switch (String.valueOf(document.getData().get("UserType"))) {
                                    case "1":
                                        type.setTitle("user");
                                        userType = "1";
                                        break;
                                    case "2":
                                        type.setTitle("barista");
                                        userType = "2";
                                        break;
                                    case "3":
                                        type.setTitle("admin");
                                        userType = "3";
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } else {
            name.setTitle("User not connected");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.register) {
            Intent intent = new Intent(this, Register.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, About.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.git) {
            String url = "https://github.com/0xAlon/Coffee";
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } else if (id == R.id.logout) {
            SingOut();
        }
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SingOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}