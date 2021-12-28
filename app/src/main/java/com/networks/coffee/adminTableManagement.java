package com.networks.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class adminTableManagement extends AppCompatActivity implements OnTableClickListener {

    private String TAG = "adminTableManagement";

    private FirebaseFirestore db;

    private ArrayList<TableModel> temp_tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_table_management);

        temp_tables = new ArrayList<TableModel>();
        db = FirebaseFirestore.getInstance();
        DataBaseLoad();
    }


    private void DataBaseLoad() {
        db.collection("Table")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TableModel tables = document.toObject(TableModel.class);

                                temp_tables.add(new TableModel(tables.getType(), tables.getPlaces(), tables.getStatus()));
                                adapterLoad();

                            }
                        } else {
                            Log.d("check", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void adapterLoad() {
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tables_list);
        recyclerView.setLayoutManager(manager);
        AdminTableAdapter adapter = new AdminTableAdapter(temp_tables, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTableClick(TableModel table) {

    }
}
