package com.networks.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class adminTableManagement extends AppCompatActivity implements OnTableClickListener {

    private final String TAG = "adminTableManagement";

    private FirebaseFirestore db;

    protected ArrayList<TableModel> temp_tables;

    private RecyclerView recyclerView;

    private AdminTableAdapter adapter;

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

                                temp_tables.add(new TableModel(tables.getType(), tables.getPlaces(), tables.getStatus(), document.getId()));
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
        recyclerView = (RecyclerView) findViewById(R.id.tables_list);
        recyclerView.setLayoutManager(manager);
        adapter = new AdminTableAdapter(temp_tables, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTableClick(TableModel table) {

    }

    public void add_new_table_btn(View view) {
        showRadioButtonDialog(view);
    }


    private void showRadioButtonDialog(View view) {
        String[] items = {"Inside", "Outside"};
        final int[] myWhich = new int[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(adminTableManagement.this);
        builder.setTitle(R.string.Choose_Option)
                .setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    myWhich[0] = 0;
                                }
                                else
                                    myWhich[0] = 1;
                            }

                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        showRadioButtonDialog2(myWhich[0]);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.create();
        builder.show();
    }

    private void showRadioButtonDialog2(int pos) {
        String[] items = {"2", "4"};
        final int[] myWhich = new int[1];
        AlertDialog.Builder builder2 = new AlertDialog.Builder(adminTableManagement.this);
        builder2.setTitle(R.string.seats_amount).setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                myWhich[0]=0;
                                break;
                            case 1:
                                myWhich[0]=1;
                                break;
                        }
                    }
                }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                db = FirebaseFirestore.getInstance();
                switch (pos) {
                    case 0:
                        if (myWhich[0] == 0) {
                            inside2Seats();
                            break;
                        }
                        if (myWhich[0] == 1) {
                            inside4Seats();
                            break;
                        }
                    case 1:
                        if (myWhich[0] ==0) {
                            outside2Seats();
                            break;
                        }
                        if (myWhich[0] ==1) {
                            outside4Seats();
                            break;
                        }
                }

            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder2.create();
        builder2.show();


    }

    private void inside2Seats() {
        Map<String, Object> table = new HashMap<>();
        table.put("places", 2);
        table.put("status", true);
        table.put("type", "inside");
        Task<DocumentReference> documentReferenceTask = db.collection("Table")
                .add(table)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Addition Succeed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        clear();
                        DataBaseLoad();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void inside4Seats() {
        Map<String, Object> table = new HashMap<>();
        table.put("places", 4);
        table.put("status", true);
        table.put("type", "inside");
        Task<DocumentReference> documentReferenceTask = db.collection("Table")
                .add(table)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Addition Succeed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        clear();
                        DataBaseLoad();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void outside2Seats() {
        Map<String, Object> table = new HashMap<>();
        table.put("places", 2);
        table.put("status", true);
        table.put("type", "outside");
        Task<DocumentReference> documentReferenceTask = db.collection("Table")
                .add(table)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Addition Succeed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        clear();
                        DataBaseLoad();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void outside4Seats() {
        Map<String, Object> table = new HashMap<>();
        table.put("places", 4);
        table.put("status", true);
        table.put("type", "outside");
        Task<DocumentReference> documentReferenceTask = db.collection("Table")
                .add(table)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Addition Succeed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        clear();
                        DataBaseLoad();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void clear() {
        int size = temp_tables.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                temp_tables.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }
    }
}

