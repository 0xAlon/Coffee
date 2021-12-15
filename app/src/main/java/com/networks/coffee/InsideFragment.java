package com.networks.coffee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class InsideFragment extends Fragment {

    private static final int COLUMNS = 3;

    private ArrayList<TableModel> temp_tables;

    private FirebaseFirestore db;

    public InsideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        temp_tables = new ArrayList<TableModel>();
        db = FirebaseFirestore.getInstance();

        DataBaseLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inside, container, false);
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
                                if (tables.getType().equals("inside")) {
                                    temp_tables.add(new TableModel(tables.getType(), tables.getPlaces(), tables.getStatus()));
                                    adapterLoad();
                                }
                            }
                        } else {
                            Log.d("check", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void adapterLoad() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), COLUMNS);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);
        TablesAdapter adapter = new TablesAdapter(getContext(), temp_tables);
        recyclerView.setAdapter(adapter);
    }
}