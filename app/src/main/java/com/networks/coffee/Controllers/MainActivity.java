package com.networks.coffee.Controllers;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.networks.coffee.Adapters.ViewAdapter;
import com.networks.coffee.Interfaces.OnItemClickListener;
import com.networks.coffee.Model.ItemDescription;
import com.networks.coffee.Model.ItemModel;
import com.networks.coffee.R;
import com.networks.coffee.Resources.BaseActivity;
import com.networks.coffee.Resources.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends BaseActivity implements OnItemClickListener {

    private String TAG = "MainActivity";

    public String getUserType() {
        return userType;
    }

    private String userType = "0";//not connected
    private int age;


    private List<ItemModel> items = null;
    private ArrayList<ItemModel> temp_list;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;

    private Context context;

    private ImageButton admin_new;

    private TextView total;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        //close all fragments
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        total = rootView.findViewById(R.id.total);
        admin_new = rootView.findViewById(R.id.admin_new);

        context = this;

        db = FirebaseFirestore.getInstance();

        temp_list = new ArrayList<ItemModel>();

        checkUserType();

        DataBaseLoad();

        View clickView = rootView.findViewById(R.id.nav);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        View clickFilter = rootView.findViewById(R.id.filter);
        clickFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, clickFilter);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.filter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (String.valueOf(menuItem.getTitle())) {
                            case "price increase":
                                viewAdapter.swapData();
                                viewAdapter.notifyDataSetChanged();
                                Collections.sort(viewAdapter.getItems(), new Comparator<ItemModel>() {
                                    @Override
                                    public int compare(ItemModel lhs, ItemModel rhs) {
                                        return lhs.getPrice().compareTo(rhs.getPrice());
                                    }
                                });
                                viewAdapter.notifyDataSetChanged();
                                break;
                            case "price decrease":
                                viewAdapter.swapData();
                                viewAdapter.notifyDataSetChanged();
                                Collections.sort(viewAdapter.getItems(), new Comparator<ItemModel>() {
                                    @Override
                                    public int compare(ItemModel lhs, ItemModel rhs) {
                                        return rhs.getPrice().compareTo(lhs.getPrice());
                                    }
                                });
                                viewAdapter.notifyDataSetChanged();
                                break;
                            case "most popular":
                                viewAdapter.swapData();
                                viewAdapter.getFilter(1).filter("true");
                                break;
                            case "drink/dish of the day":
                                viewAdapter.swapData();
                                viewAdapter.getFilter(2).filter("true");
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Admin functionality
        View clickAdd = rootView.findViewById(R.id.admin_new);
        clickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("3")) {
                    Intent intent = new Intent(view.getContext(), AddNewItem.class);
                    startActivity(intent);
                }
                if (userType.equals("2")) {
                    Toast.makeText(context, "Discount available",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void checkUserType() {
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
                                this.age = Integer.parseInt(document.getData().get("Age").toString());
                                switch (String.valueOf(document.getData().get("UserType"))) {
                                    case "1":
                                        userType = "1"; // connected user
                                        break;
                                    case "2":
                                        userType = "2"; // barista
                                        ((ImageButton) admin_new).setImageResource(R.drawable.baseline_person_24);
                                        admin_new.setVisibility(View.VISIBLE);
                                        break;
                                    case "3":
                                        userType = "3"; // admin
                                        admin_new.setVisibility(View.VISIBLE);
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
            // User not connected
        }
    }

    private void adapterLoad() {
        recyclerView = findViewById(R.id.movies_rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        viewAdapter = new ViewAdapter(temp_list, context, this);
        recyclerView.setAdapter(viewAdapter);
    }

    private void DataBaseLoad() {
        db.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemModel items = document.toObject(ItemModel.class);
                                if (age < 18 && items.getAlcoholic().equals(true)) {
                                    //no add alcoholic drink
                                } else {
                                    temp_list.add(new ItemModel(items.getName(), items.getOverview(), items.getPrice(), items.getUrl(), items.getCount(), items.getAlcoholic(), items.getToday(), items.getPopular(), document.getId()));
                                    adapterLoad();
                                }
                            }
                        } else {
                            Log.d("check", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(ItemModel item) {
        checkUserType();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, new ItemDescription(fragmentManager, item, userType))
                .addToBackStack("main")
                .commit();
    }

    public void onClickBtn(View view) {
        Intent intent = new Intent(this, tableManagement.class);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }
}