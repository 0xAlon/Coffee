package com.networks.coffee;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private List<ItemModel> items = null;
    FirebaseFirestore db;
    private String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private ArrayList<ItemModel> temp_list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        db = FirebaseFirestore.getInstance();

        temp_list = new ArrayList<ItemModel>();
        context = this;


        DataBaseLoadOrderBy("price", Query.Direction.ASCENDING);


        View clickView = rootView.findViewById(R.id.nav);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        View clickDilter = rootView.findViewById(R.id.filter);
        clickDilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, clickDilter);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.filter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (String.valueOf(menuItem.getTitle())) {
                            case "price increase":
                                temp_list = new ArrayList<ItemModel>();
                                DataBaseLoadOrderBy("price", Query.Direction.ASCENDING);
                                break;
                            case "price decrease":
                                temp_list = new ArrayList<ItemModel>();
                                DataBaseLoadOrderBy("price", Query.Direction.DESCENDING);
                                break;
                            case "most popular":
                                temp_list = new ArrayList<ItemModel>();
                                DataBaseLoadwhereEqualTo("today");
                                break;
                            case "drink/dish of the day":
                                temp_list = new ArrayList<ItemModel>();
                                DataBaseLoadwhereEqualTo("popular");
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void adapterLoad() {
        recyclerView = findViewById(R.id.movies_rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewAdapter = new ViewAdapter(temp_list, context);
        recyclerView.setAdapter(viewAdapter);
    }

    private void DataBaseLoadwhereEqualTo(String field) {
        db.collection("Items")
                .whereEqualTo(field, true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("check", document.getId() + " => " + document.getData());
                                ItemModel items = document.toObject(ItemModel.class);
                                temp_list.add(new ItemModel(items.getName(), items.getOverview(), items.getPrice(), items.getUrl()));
                                adapterLoad();
                            }
                        } else {
                            Log.d("check", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void DataBaseLoadOrderBy(String field, Query.Direction query) {
        db.collection("Items")
                .orderBy(field, query)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ItemModel items = document.toObject(ItemModel.class);
                                temp_list.add(new ItemModel(items.getName(), items.getOverview(), items.getPrice(), items.getUrl()));
                                adapterLoad();
                            }
                        } else {
                            Log.d("check", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}